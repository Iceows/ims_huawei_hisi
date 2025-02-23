/*
 * This file is part of HwIms
 * Copyright (C) 2019,2025 Penn Mackintosh and Raphael Mounier
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.huawei.ims

import android.os.Message
import android.os.RemoteException
import android.telephony.PhoneNumberUtils
import android.telephony.Rlog
import android.telephony.ims.ImsCallProfile
import android.telephony.ims.ImsCallProfile.*
import android.telephony.ims.ImsCallSessionListener
import android.telephony.ims.ImsReasonInfo
import android.telephony.ims.ImsStreamMediaProfile
import android.telephony.ims.stub.ImsCallSessionImplBase
import android.util.Log
import com.android.ims.ImsConfig
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCallDomain
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCallType
import vendor.huawei.hardware.radio.ims.V1_0.RILImsDial
import java.util.concurrent.ConcurrentHashMap


// ImsCallSessionImplBase have a trackingBug = 170729553 , Android 11 max


class HwImsCallSession
/* For outgoing (MO) calls */ (private val mSlotId: Int, profile: ImsCallProfile) : ImsCallSessionImplBase() {

    private var mCallId = 0
    private var mbV1_2Call = false
    private val mCallProfile: ImsCallProfile
    private val mLocalProfile: ImsCallProfile
    private val mRemoteProfile: ImsCallProfile

    private val mStreamMediaProfile : ImsStreamMediaProfile

    private var listener: ImsCallSessionListener? = null

    var driverImsCall: DriverImsCall? = null
    private var mInCall = false

    private val mCallIdLock = Object()
    private var confInProgress = false
    private var mState: Int = 0

    var mCallee: String = ""
    private val mCount: Int

    private var radioTechFromRilImsCall = 0
    private var redirectNumber: String? = null
    private var redirectNumberPresentation = 0
    private var redirectNumberToa = 0

    init {
        this.mCount = sCount++

        this.mCallProfile = ImsCallProfile(SERVICE_TYPE_NORMAL, profile.callType)
        this.mLocalProfile = ImsCallProfile(SERVICE_TYPE_NORMAL, profile.callType)
        this.mRemoteProfile = ImsCallProfile(SERVICE_TYPE_NORMAL, profile.callType)
        this.mState = State.IDLE

        this.radioTechFromRilImsCall = -1
        this.redirectNumber = null
        this.redirectNumberToa = 0
        this.redirectNumberPresentation = 1

        this.mStreamMediaProfile = ImsStreamMediaProfile()

    }

    // For incoming (MT) calls
    constructor(slotId: Int, profile: ImsCallProfile, dc: DriverImsCall) : this(slotId, profile) {
        Log.d(tag, "constructor")
        updateCall(dc)
        calls[dc.index] = this
        mbV1_2Call = false
        mCallId = dc.index
    }

    fun addIdFromRIL(dc: DriverImsCall) {
        synchronized(sCallsLock) {
            var worked = awaitingIdFromRIL.remove("+" + dc.number, this)
            if (!worked)
                worked = awaitingIdFromRIL.remove(dc.number, this)
            if (worked) {
                synchronized(mCallIdLock) {
                    updateCall(dc)
                    calls[dc.index] = this
                    mCallIdLock.notify()
                }
            }
        }
    }

    private fun hwOirToOir(oir: Int): Int {
        return when (oir) {
            OIR_BEHAVIOUR_TYPE_DEFAULT -> OIR_PRESENTATION_NOT_RESTRICTED
            OIR_BEHAVIOUR_TYPE_NOT_RESTRICTED -> OIR_PRESENTATION_NOT_RESTRICTED
            OIR_BEHAVIOUR_TYPE_NOT_SUBSCRIBED -> OIR_PRESENTATION_PAYPHONE
            OIR_BEHAVIOUR_TYPE_RESTRICTED -> OIR_PRESENTATION_RESTRICTED
            else -> OIR_PRESENTATION_UNKNOWN
        }
    }

    fun updateCall(dcUpdate: DriverImsCall) {
        val lastState = mState

        when (dcUpdate.state) {
            DriverImsCall.State.ACTIVE -> {
                Rlog.i(tag, "Active ")
                if (driverImsCall == null) {
                    Rlog.e(tag, "Phantom call!")
                    mCallId = dcUpdate.index
                    mState = State.ESTABLISHED
                    listener?.callSessionInitiated(mCallProfile)
                } else if (driverImsCall!!.state ==  DriverImsCall.State.DIALING ||  driverImsCall!!.state == DriverImsCall.State.ALERTING ||  driverImsCall!!.state == DriverImsCall.State.INCOMING ||   driverImsCall!!.state == DriverImsCall.State.WAITING  ) {
                    mState = State.ESTABLISHED
                    driverImsCall!!.state=DriverImsCall.State.ACTIVE;
                    extractImsCallProfileIntoCallProfile(dcUpdate)
                    listener?.callSessionInitiated(mCallProfile)

                /*} else if (this.mDc.state == DriverImsCall.State.DIALING || this.mDc.state == DriverImsCall.State.ALERTING || this.mDc.state == DriverImsCall.State.INCOMING || this.mDc.state == DriverImsCall.State.WAITING) {
                    this.mState = 4;
                    this.mDc.state = DriverImsCall.State.ACTIVE;
                    extractImsCallProfileIntoCallProfile(dcUpdate);
                    if (ImsCallProviderUtils.isVilteEnhancementSupported()) {
                        notifyCallSessionStarted();
                    }
                    this.mListenerProxy.callSessionStarted(this.mCallProfile);
                 */

                } else if ((driverImsCall!!.state == DriverImsCall.State.HOLDING && !confInProgress) || confInProgress ) {
                    Rlog.d(tag, "Call being resumed.")
                    listener?.callSessionResumed(mCallProfile)
                } else {
                    Rlog.e(tag, "Call resumed skipped, conf status = " + confInProgress)
                }
            }

            DriverImsCall.State.HOLDING -> {
                Rlog.i(tag, "Holding ")
                listener?.callSessionHeld(mCallProfile)
            }

            DriverImsCall.State.DIALING -> {
                Rlog.i(tag, "Dialing ")
                // Strange Dialing can call several time - on LOS 20, crash the second time !!
                if (driverImsCall!=null) {
                    if (driverImsCall!!.state == DriverImsCall.State.DIALING) {
                        Rlog.i(tag, "Old state is already dialing ")
                    }
                }
                else {
                    listener?.callSessionProgressing(mStreamMediaProfile)
                }
            }

            DriverImsCall.State.ALERTING -> {
                Rlog.i(tag, "Alerting ")
                mState = State.NEGOTIATING
                if (driverImsCall == null) {
                    Rlog.e(tag, "Alerting an incoming call wtf?")
                } else {
                    extractImsCallProfileIntoCallProfile(dcUpdate)
                    listener?.callSessionProgressing(ImsStreamMediaProfile())
                }
            }

            DriverImsCall.State.INCOMING, DriverImsCall.State.WAITING -> {
                extractImsCallProfileIntoCallProfile(dcUpdate)
            }

            DriverImsCall.State.END -> {
                die(ImsReasonInfo())
            }
        }

/*
        val subId = HwImsService.instance!!.subscriptionManager
                .getActiveSubscriptionInfoForSimSlotIndex(mSlotId).subscriptionId
        val telephonyManager = HwImsService.instance!!.telephonyManager.createForSubscriptionId(subId)
*/

        if (lastState == mState /*state unchanged*/ && dcUpdate.state !=  DriverImsCall.State.END && dcUpdate != driverImsCall) {
            listener?.callSessionUpdated(mCallProfile)
        }
        driverImsCall = dcUpdate
    }

    private fun extractImsCallProfileIntoCallProfile(dcUpdate: DriverImsCall)
    {
        if (dcUpdate == null) {
            Rlog.e(tag, "Null dcUpdate in extractImsCallProfileIntoCallProfile")
            return
        }
        updateImsCallProfile(dcUpdate)
    }
    private fun updateImsCallProfile(dc: DriverImsCall) {
        Log.d(tag, "enter in updateImsCallProfile")
        if (dc == null) {
            Rlog.e(tag,"updateImsCallProfile called with dc null")
            return
        }

        mCallProfile.setCallExtra("oi", dc.number)
        mCallProfile.setCallExtra("cna", dc.name)
        mCallProfile.setCallExtraInt("oir", presentationToOIR(dc.numberPresentation))
        mCallProfile.setCallExtraInt("cnap", presentationToOIR(dc.namePresentation))
        mCallProfile.setCallExtraInt("remote_vt_capability", dc.peerVideoSupport)

        // Normallement dans DriverImsCall
        val ratTypeFromModem = dc.radioTechFromRilImsCall
        if (ratTypeFromModem != -1) {
            mCallProfile.setCallExtra("CallRadioTech", getRadioTechFromDriverCall(ratTypeFromModem))
        } else {
            //val hwTelephonyManager: HwTelephonyManager = HwTelephonyManager.getDefault()
            //if (hwTelephonyManager != null) {
                //val default4GSlotId: Int = hwTelephonyManager.getDefault4GSlotId()
                //val imsRegDomain: Int = hwTelephonyManager.getImsDomain(default4GSlotId)
            //}
            val imsRegDomain=0;
            mCallProfile.setCallExtra(
                "CallRadioTech",
                getRadioTechFromDriverCall(imsRegDomain)
            )
        }

        redirectNumberToa = redirectNumberToa
        redirectNumber = PhoneNumberUtils.stringFromStringAndTOA(redirectNumber, redirectNumberToa)

        mCallProfile.setCallExtra("redirect_number", redirectNumber)
        mCallProfile.setCallExtraInt(
            "redirect_number_presentation",
            presentationToOIR(redirectNumberPresentation)
        )

        // Translate Huawei IMS call type
        when (dc.imsCallProfiles.call_type) {
            HwImsCallDetails.CALL_TYPE_UNKNOWN -> {
                mCallProfile.mCallType = CALL_TYPE_VOICE_N_VIDEO
                mCallProfile.mMediaProfile.mVideoDirection = ImsStreamMediaProfile.DIRECTION_INVALID
            }

            HwImsCallDetails.CALL_TYPE_VOICE -> {
                mCallProfile.mCallType = CALL_TYPE_VOICE
                mCallProfile.mMediaProfile.mVideoDirection = ImsStreamMediaProfile.DIRECTION_INVALID
            }

            HwImsCallDetails.CALL_TYPE_VT -> {
                mCallProfile.mCallType = CALL_TYPE_VT
                mCallProfile.mMediaProfile.mVideoDirection =
                    ImsStreamMediaProfile.DIRECTION_SEND_RECEIVE
            }

            HwImsCallDetails.CALL_TYPE_VT_TX -> {
                mCallProfile.mCallType = CALL_TYPE_VT_TX
                mCallProfile.mMediaProfile.mVideoDirection = ImsStreamMediaProfile.DIRECTION_SEND
            }

            HwImsCallDetails.CALL_TYPE_VT_RX -> {
                mCallProfile.mCallType = CALL_TYPE_VT_RX
                mCallProfile.mMediaProfile.mVideoDirection = ImsStreamMediaProfile.DIRECTION_RECEIVE
            }

            HwImsCallDetails.CALL_TYPE_VT_NODIR ->                 // Not propagating VT_NODIR call type to UI
                mCallProfile.mMediaProfile.mVideoDirection =
                    ImsStreamMediaProfile.DIRECTION_INACTIVE
        }

    }

    private fun getRadioTechFromDriverCall(imsDomain: Int): String? {
        var radioTech = 0
        when (imsDomain) {
            0 -> radioTech = 14
            1 -> radioTech = 18
        }
        Rlog.d(tag,"getRadioTechFromDriverCall $radioTech ,imsDomain $imsDomain")
        return Integer.toString(radioTech)
    }

    private fun die(reason: ImsReasonInfo) {
        if (driverImsCall != null)
            calls.remove(driverImsCall!!.index)
        awaitingIdFromRIL.remove(mCallee)
        mState = State.TERMINATED
        listener?.callSessionTerminated(reason)
    }

    fun notifyEnded() {
        die(ImsReasonInfo())
    }

    override fun setListener(listener: ImsCallSessionListener?) {
        this.listener = listener
    }

    override fun getCallId(): String {
        return "slot" + mSlotId + "id" + if (driverImsCall == null) "unknown!" + Integer.toString(mCount) else driverImsCall!!.index
    }

    override fun getCallProfile(): ImsCallProfile {
        return mCallProfile
    }

    override fun getRemoteCallProfile(): ImsCallProfile {
        return mRemoteProfile
    }

    override fun getLocalCallProfile(): ImsCallProfile {
        return mLocalProfile;
    }

    override fun getProperty(name: String?): String {
        return mCallProfile.getCallExtra(name)
    }

    override fun getState(): Int {
        return mState
    }

    override fun isInCall(): Boolean {
        return mInCall
    }

    override fun setMute(muted: Boolean) {
        try {
            val serial = RilHolder.prepareBlock(mSlotId)
            RilHolder.getImsRadio(mSlotId)!!.setMute(serial, muted)
            if (RilHolder.blockUntilComplete(serial).error != 0) {
                Rlog.e(tag, "Failed to setMute! " + RilHolder.blockUntilComplete(serial))
            }
        } catch (e: RemoteException) {
            Rlog.e(tag, "Error sending setMute request!", e)
        }

    }


    private fun convertAospCallType(callType: Int): Int {
        return when (callType) {
            CALL_TYPE_VOICE_N_VIDEO-> RILImsCallType.CALL_TYPE_SMS
            CALL_TYPE_VOICE -> RILImsCallType.CALL_TYPE_VOICE
            CALL_TYPE_VIDEO_N_VOICE -> RILImsCallType.CALL_TYPE_VT
            CALL_TYPE_VT -> RILImsCallType.CALL_TYPE_VT
            CALL_TYPE_VT_TX -> RILImsCallType.CALL_TYPE_VT_TX
            CALL_TYPE_VT_RX -> RILImsCallType.CALL_TYPE_VT_RX
            CALL_TYPE_VT_NODIR -> RILImsCallType.CALL_TYPE_VT_NODIR
            CALL_TYPE_VS -> throw RuntimeException("NI VS!!")
            CALL_TYPE_VS_TX -> RILImsCallType.CALL_TYPE_CS_VS_TX
            CALL_TYPE_VS_RX -> RILImsCallType.CALL_TYPE_CS_VS_RX
            else -> throw RuntimeException("Unknown callType $callType")
        }
    }

    /*
    fun extractProfileExtrasIntoImsCallProfile(profile: ImsCallProfile?, details: ImsCallProfiles ) {
        val callExtras = profile!!.mCallExtras.getBundle("OemCallExtras")
        if (callExtras != null) {
            val extras = arrayOfNulls<String>(callExtras.size())
            var i = 0
            for (key in callExtras.keySet()) {
                val value =
                    if (callExtras[key] == null) HwImsConfigImpl.NULL_STRING_VALUE else callExtras[key].toString()
                val extraString = "$key=$value"
                var extraStringForOutput = extraString
                if (key == com.huawei.ims.HwImsCallSessionImpl.NUMBERMARKINFO_NUMBER || key == com.huawei.ims.HwImsCallSessionImpl.MEXTI_SEARCH_NUMBER || key == com.huawei.ims.HwImsCallSessionImpl.MEXTI_BACKUP_NUMBER) {
                    val value2 = HiddenPrivacyInfo.putMosaic(callExtras[key].toString(), 0)
                    extraStringForOutput = "$key=$value2"
                }
                Rlog.d(tag,"Packing extra string: $extraStringForOutput")
                extras[i] = extraString
                i++
            }
            details.setExtras(extras)
            return
        }
        Rlog.d(tag,"No extras in ImsCallProfile to map into ImsCallProfiles.")
    }
*/

/*
        mCallProfile.mCallType = profile.mCallType
        mCallProfile.mMediaProfile = profile.mMediaProfile
        mState = 1
        mCallee = callee
        val clir: Int = profile.getCallExtraInt("oir")
        val details = ImsCallProfiles(mapCallTypeFromProfile(profile.mCallType), 3, null)
        extractProfileExtrasIntoImsCallProfile(profile, details)
        this.mCi.dial(callee, clir, details, this.mHandler.obtainMessage(1))
*/

    override fun start(callee: String, profile: ImsCallProfile?) {
        Log.d(tag, "IMS start - Start calling " + Rlog.pii(tag, callee))

        mState = 1
        mCallee = callee

        if (profile != null) {
            mCallProfile.mCallType = profile.mCallType
            mCallProfile.mMediaProfile = profile.mMediaProfile
        }

        val callInfo = RILImsDial()
        Log.d(tag, "IMS start - IMS After dialing");

        callInfo.address = callee

       // extractProfileExtrasIntoImsCallProfile
        callInfo.clir = profile!!.getCallExtraInt(EXTRA_OIR) // Huawei do this so it **must** be right... Oh wait...
        val extras = profile.mCallExtras.getBundle("OemCallExtras")
        if (extras != null) {
            Rlog.i(tag, "NI reading oemcallextras, it is $extras")
        }
        val callType: Int
        try {
            callType = convertAospCallType(profile.callType)
        } catch (e: RuntimeException) {
            listener?.callSessionInitiatedFailed(ImsReasonInfo(ImsReasonInfo.CODE_LOCAL_INTERNAL_ERROR, ImsReasonInfo.CODE_UNSPECIFIED, e.message))
            throw e
        }


        callInfo.callDetails.callType = callType
        callInfo.callDetails.callDomain = 3
        if (HwImsService.instance!!.getConfig(mSlotId)!!.getConfigInt(ImsConfig.ConfigConstants.VLT_SETTING_ENABLED) == ImsConfig.FeatureValueConstants.ON) {
            callInfo.callDetails.callDomain = RILImsCallDomain.CALL_DOMAIN_AUTOMATIC
        } else {
            callInfo.callDetails.callDomain = RILImsCallDomain.CALL_DOMAIN_CS
        }

        try {
            Log.d(tag, "IMS start - Adding to awaiting id from ril")
            awaitingIdFromRIL[mCallee] = this // Do it sooner rather than later so that this call is not seen as a phantom
            RilHolder.getImsRadio(mSlotId)!!.imsDial(RilHolder.callback({ radioResponseInfo, _ ->
                if (radioResponseInfo.error == 0) {
                    Log.d(tag, "successfully placed call")
                    mInCall = true
                    mState = State.ESTABLISHED
                    listener?.callSessionInitiated(profile)
                } else {
                    Log.e(tag, "call failed")
                    mState = State.TERMINATED
                    awaitingIdFromRIL.remove(callee, this)
                    listener?.callSessionInitiatedFailed(ImsReasonInfo())
                }
            }, mSlotId), callInfo)
        } catch (e: RemoteException) {
            listener?.callSessionInitiatedFailed(ImsReasonInfo())
            awaitingIdFromRIL.remove(callee, this)
            Rlog.e(tag, "Sending imsDial failed with exception", e)
        }

        Log.d(tag, "IMS start - Ending")
    }

    override fun startConference(members: Array<String>?, profile: ImsCallProfile?) {
        Rlog.d(tag, "startConference...")

        // This method is to initiate the conference call, not to add all the members.
        start(members!![0], profile)
        //TODO is this right?
    }

    override fun accept(callType: Int, profile: ImsStreamMediaProfile?) {
        Rlog.d(tag, "accept...")

        mState = State.ESTABLISHING
        try {
            RilHolder.getImsRadio(mSlotId)!!.acceptImsCall(RilHolder.callback({ radioResponseInfo, _ ->
                if (radioResponseInfo.error != 0) {
                    listener?.callSessionInitiatedFailed(ImsReasonInfo())
                    Rlog.e(tag, "error accepting ims call")
                } else {
                    listener?.callSessionInitiated(mCallProfile)
                    mInCall = true
                }
            }, mSlotId), convertAospCallType(callType))
        } catch (e: RemoteException) {
            listener?.callSessionInitiatedFailed(ImsReasonInfo())
            Rlog.e(tag, "failed to accept ims call")
        }

    }

    override fun deflect(destination: String?) {
        // Huawei shim this, we can do the same.
        Rlog.d(tag, "deflect...")
    }

    override fun reject(reason: Int) {
        Rlog.d(tag, "reject...")
        /*
        try {
            getRilCallId();
            RilHolder.INSTANCE.getImsRadio(mSlotId).rejectCallWithReason(RilHolder.callback((radioResponseInfo, rspMsgPayload) -> {
                if (radioResponseInfo.error == 0) {
                    Rlog.d(tag, "Rejected incoming call.");
                } else {
                    Rlog.e(tag, "Failed to reject incoming call!");
                }
            }, mSlotId), rilImsCall.index, reason);
        } catch (RemoteException e) {
            //and here too
            Rlog.e(tag, "Error listing ims calls!");
        }
        */
        // The above doesn't work. So, we do it the huawei way, which is to hangup the call. Reeee.
        mState = State.TERMINATING
        try {
            getRilCallId()
            RilHolder.getImsRadio(mSlotId)!!.hangup(RilHolder.callback({ radioResponseInfo, _ ->
                Rlog.d(tag, "got cb for hangup!")
                if (radioResponseInfo.error != 0) {
                    mState = State.INVALID
                    Rlog.e(tag, "Error hanging up!")
                } else {
                    mState = State.TERMINATED
                    die(ImsReasonInfo())

                }
            }, mSlotId), driverImsCall!!.index)
            // TODO FIXME: Radio doesn't reply to hangup() so we assume it worked.
            mState = State.TERMINATED
            die(ImsReasonInfo())
        } catch (e: RemoteException) {
            Rlog.e(tag, "error hanging up", e)
        }

    }

    private fun getRilCallId() {
        synchronized(mCallIdLock) {
            while (driverImsCall == null) {
                try {
                    mCallIdLock.wait()
                } catch (ignored: InterruptedException) {
                }

            }
        }
    }

    override fun terminate(reason: Int) {
        Rlog.d(tag, "terminate...")
        mState = State.TERMINATING
        try {
            getRilCallId()
            Rlog.d(tag, "terminating call...")
            RilHolder.getImsRadio(mSlotId)!!.hangup(RilHolder.callback({ radioResponseInfo, _ ->
                Rlog.d(tag, "got cb for hangup!")
                if (radioResponseInfo.error != 0) {
                    mState = State.INVALID
                    Rlog.e(tag, "Error hanging up!")
                } else {
                    mState = State.TERMINATED
                    die(ImsReasonInfo())
                }
            }, mSlotId), driverImsCall!!.index)
            // TODO FIXME: Radio doesn't reply to hangup() so we assume it worked.
            mState = State.TERMINATED
            die(ImsReasonInfo())
        } catch (e: RemoteException) {
            Rlog.e(tag, "error hanging up", e)
        }

    }

    override fun hold(profile: ImsStreamMediaProfile?) {
        try {
            RilHolder.getImsRadio(mSlotId)!!.switchWaitingOrHoldingAndActive(RilHolder.callback({ radioResponseInfo, _ ->
                if (radioResponseInfo.error == 0) {
                    listener?.callSessionHeld(mCallProfile)
                } else {
                    listener?.callSessionHoldFailed(ImsReasonInfo())
                }
            }, mSlotId))
        } catch (e: RemoteException) {
            Rlog.e(tag, "Error holding", e)
        }

    }

    override fun resume(profile: ImsStreamMediaProfile?) {
        try {
            RilHolder.getImsRadio(mSlotId)!!.switchWaitingOrHoldingAndActive(RilHolder.callback({ radioResponseInfo, _ ->
                if (radioResponseInfo.error == 0) {
                    listener?.callSessionResumed(mCallProfile)
                } else {
                    Rlog.e(tag, "failed to resume")
                    listener?.callSessionResumeFailed(ImsReasonInfo())
                }
            }, mSlotId))
        } catch (e: RemoteException) {
            listener?.callSessionResumeFailed(ImsReasonInfo())
            Rlog.e(tag, "failed to resume", e)
        }

    }

    override fun merge() {
        try {
            RilHolder.getImsRadio(mSlotId)!!.conference(RilHolder.callback({ radioResponseInfo, _ ->
                if (radioResponseInfo.error == 0) {
                    // Do nothing, notifyConfDone will be called by the RadioResponse code (triggered by RadioIndication)
                } else {
                    listener?.callSessionMergeFailed(ImsReasonInfo())
                }
            }, mSlotId))
        } catch (e: RemoteException) {
            listener?.callSessionMergeFailed(ImsReasonInfo())
            Rlog.e(tag, "failed to request conference", e)
        }
    }

    fun notifyConfDone(call: DriverImsCall) {
        Rlog.i(tag, "notifyConfDone")
        listener?.callSessionMergeComplete(HwImsCallSession(mSlotId, mCallProfile, call))
    }


    override fun update(callType: Int, profile: ImsStreamMediaProfile?) {
        Rlog.e(tag, "Please implement update call")
    }

    override fun extendToConference(participants: Array<String>?) {
        // Huawei shim this, so do we.
    }

    override fun inviteParticipants(participants: Array<String>?) {
        // Huawei shim this, so do we.
    }

    override fun removeParticipants(participants: Array<String>?) {
        // Huawei shim this, so do we.
    }

    override fun sendDtmf(c: Char, m: Message?) {
        try {
            RilHolder.getImsRadio(mSlotId)!!.sendDtmf(RilHolder.callback({ radioResponseInfo, _ ->
                if (radioResponseInfo.error != 0) {
                    Rlog.e(tag, "send DTMF error!")
                    //TODO we need to reply don't we? Respond with an error to DTMF
                } else {
                    Rlog.d(tag, "sent dtmf ok!")
                    if (m!!.replyTo != null) {
                        try {
                            m.replyTo.send(m)
                        } catch (e: RemoteException) {
                            Rlog.e(tag, "failed to reply to DTMF!", e)
                        }

                    }
                }
            }, mSlotId), Character.toString(c))
        } catch (e: RemoteException) {
            Rlog.e(tag, "failed to send DTMF!", e)
        }

    }

    override fun startDtmf(c: Char) {
        try {
            RilHolder.getImsRadio(mSlotId)!!.startDtmf(RilHolder.callback({ radioResponseInfo, _ ->
                if (radioResponseInfo.error != 0) {
                    Rlog.e(tag, "DTMF error!")
                } else {
                    Rlog.d(tag, "start dtmf ok!")
                }
            }, mSlotId), Character.toString(c))
        } catch (e: RemoteException) {
            Rlog.e(tag, "failed to start DTMF!", e)
        }

    }

    override fun stopDtmf() {
        try {
            RilHolder.getImsRadio(mSlotId)!!.stopDtmf(RilHolder.callback({ radioResponseInfo, _ ->
                if (radioResponseInfo.error != 0) {
                    Rlog.e(tag, "stop DTMF error!")
                } else {
                    Rlog.d(tag, "stopped dtmf ok!")
                }
            }, mSlotId))
        } catch (e: RemoteException) {
            Rlog.e(tag, "failed to stop DTMF!", e)
        }

    }

    //TODO USSD

    //TODO Video Calling

    override fun isMultiparty(): Boolean {
        if (driverImsCall!=null)
            return driverImsCall!!.isMpty
        else
            return false
    }

    companion object {
        private const val OIR_BEHAVIOUR_TYPE_DEFAULT = 0
        private const val OIR_BEHAVIOUR_TYPE_NOT_RESTRICTED = 1
        private const val OIR_BEHAVIOUR_TYPE_RESTRICTED = 2
        private const val OIR_BEHAVIOUR_TYPE_NOT_SUBSCRIBED = 3


        private const val tag = "HwImsCallSession"
        val awaitingIdFromRIL = ConcurrentHashMap<String, HwImsCallSession>()
        val calls = ConcurrentHashMap<Int, HwImsCallSession>()

        private var sCount = 0

        val sCallsLock = Object()
    }

    //TODO RealTimeText
}
