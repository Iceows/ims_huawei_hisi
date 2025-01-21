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

import android.annotation.SuppressLint
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
import com.android.internal.telephony.DriverCall
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCall
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCallDomain
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCallType
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCallV1_2
import vendor.huawei.hardware.radio.ims.V1_0.RILImsDial
import java.util.concurrent.ConcurrentHashMap

class HwImsCallSession
/* For outgoing (MO) calls */ (private val mSlotId: Int, profile: ImsCallProfile) : ImsCallSessionImplBase() {
    private var mbV1_2Call = false
    private val mProfile: ImsCallProfile
    private val mLocalProfile: ImsCallProfile
    private val mRemoteProfile: ImsCallProfile
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
        this.mProfile = ImsCallProfile(SERVICE_TYPE_NORMAL, profile.callType)
        this.mLocalProfile = ImsCallProfile(SERVICE_TYPE_NORMAL, profile.callType)
        this.mRemoteProfile = ImsCallProfile(SERVICE_TYPE_NORMAL, profile.callType)
        this.mState = State.IDLE

        this.radioTechFromRilImsCall = -1
        this.redirectNumber = null
        this.redirectNumberToa = 0
        this.redirectNumberPresentation = 1
    }

    // For incoming (MT) calls
    constructor(slotId: Int, profile: ImsCallProfile, dc: DriverImsCall) : this(slotId, profile) {
        updateCall(dc)
        calls[dc.index] = this
        mbV1_2Call = false
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


    fun updateCallV1_2(call: RILImsCallV1_2) {
        val lastState = mState

        Log.e(tag, "ERROR must be implement function updateCallV1_2")
    }

    @SuppressLint("MissingPermission")
    fun updateCall(call: DriverImsCall) {
        val lastState = mState
        when (call.state) {
            DriverImsCall.State.ACTIVE -> {
                if (driverImsCall == null) {
                    Rlog.e(tag, "Phantom call!")
                    mState = State.ESTABLISHED
                    listener?.callSessionInitiated(mProfile)
                } else if (driverImsCall!!.state ==  DriverImsCall.State.DIALING ||  driverImsCall!!.state == DriverImsCall.State.ALERTING ||  driverImsCall!!.state == DriverImsCall.State.INCOMING ||   driverImsCall!!.state == DriverImsCall.State.WAITING  ) {
                        mState = State.ESTABLISHED
                    extractImsCallProfileIntoCallProfile(call)
                        listener?.callSessionInitiated(mProfile)
                } else if ((driverImsCall!!.state == DriverImsCall.State.HOLDING && !confInProgress) || confInProgress ) {
                    Rlog.d(tag, "Call being resumed.")
                    listener?.callSessionResumed(mProfile)
                } else {
                    Rlog.e(tag, "Call resumed skipped, conf status = " + confInProgress)
                }
            }

            DriverImsCall.State.HOLDING -> {
                Rlog.i(tag, "Holding ")
                listener?.callSessionHeld(mProfile)
            }

            DriverImsCall.State.DIALING -> {
                Rlog.i(tag, "Dialing ")
                listener?.callSessionProgressing(ImsStreamMediaProfile())
            }

            DriverImsCall.State.ALERTING
            -> {
                mState = State.NEGOTIATING
                if (driverImsCall == null) {
                    Rlog.e(tag, "Alerting an incoming call wtf?")
                } else {
                    extractImsCallProfileIntoCallProfile(call)
                    listener?.callSessionProgressing(ImsStreamMediaProfile())
                }
            }
            DriverImsCall.State.INCOMING,  DriverImsCall.State.WAITING
            -> {
                extractImsCallProfileIntoCallProfile(call)
            }
            DriverImsCall.State.END
            -> die(ImsReasonInfo())
        }

/*
        val subId = HwImsService.instance!!.subscriptionManager
                .getActiveSubscriptionInfoForSimSlotIndex(mSlotId).subscriptionId
        val telephonyManager = HwImsService.instance!!.telephonyManager.createForSubscriptionId(subId)
*/

        if (lastState == mState /*state unchanged*/ && call.state !=  DriverImsCall.State.END && call != driverImsCall) {
            listener?.callSessionUpdated(mProfile)
        }
        driverImsCall = call
    }

    private fun extractImsCallProfileIntoCallProfile(dcUpdate: DriverImsCall)
    {
        if (dcUpdate == null) {
            Rlog.e(tag, "Null dcUpdate in extractImsCallProfileIntoCallProfile");
            return;
        }
        updateImsCallProfile(dcUpdate);
    }
    private fun updateImsCallProfile(dc: DriverImsCall) {
        Rlog.d(tag, "enter in updateImsCallProfile")
        if (dc == null) {
            Rlog.e(tag,"updateImsCallProfile called with dc null")
            return
        }

        mProfile.setCallExtra("oi", dc.number)
        mProfile.setCallExtra("cna", dc.name)
        mProfile.setCallExtraInt("oir", presentationToOIR(dc.numberPresentation))
        mProfile.setCallExtraInt("cnap", presentationToOIR(dc.namePresentation))
        mProfile.setCallExtraInt("remote_vt_capability", dc.peerVideoSupport)

        // Normallement dans DriverImsCall
        val ratTypeFromModem = radioTechFromRilImsCall
        if (ratTypeFromModem != -1) {
            mProfile.setCallExtra(
                "CallRadioTech",
                getRadioTechFromDriverCall(ratTypeFromModem)
            )
        } else {
            //val hwTelephonyManager: HwTelephonyManager = HwTelephonyManager.getDefault()
            //if (hwTelephonyManager != null) {
                //val default4GSlotId: Int = hwTelephonyManager.getDefault4GSlotId()
                //val imsRegDomain: Int = hwTelephonyManager.getImsDomain(default4GSlotId)
            val imsRegDomain=0;
            mProfile.setCallExtra(
                "CallRadioTech",
                getRadioTechFromDriverCall(imsRegDomain)
            )
        }

        redirectNumberToa = redirectNumberToa
        redirectNumber = PhoneNumberUtils.stringFromStringAndTOA(redirectNumber, redirectNumberToa)
        redirectNumberPresentation = DriverCall.presentationFromCLIP(redirectNumberPresentation)

        mProfile.setCallExtra("redirect_number", redirectNumber)
        mProfile.setCallExtraInt(
            "redirect_number_presentation",
            presentationToOIR(redirectNumberPresentation)
        )


        /*
        val i = dc.imsCallProfile.call_type
        if (i == 10) {
            mProfile.mCallType = 1
            mProfile.mMediaProfile.mVideoDirection = -1
            return
        }
        when (i) {
            0 -> {
                mProfile.mCallType = 2
                mProfile.mMediaProfile.mVideoDirection = -1
                return
            }

            1 -> {
                mProfile.mCallType = 5
                mProfile.mMediaProfile.mVideoDirection = 2
                return
            }

            2 -> {
                mProfile.mCallType = 6
                mProfile.mMediaProfile.mVideoDirection = 1
                return
            }

            3 -> {
                mProfile.mCallType = 4
                mProfile.mMediaProfile.mVideoDirection = 3
                return
            }

            4 -> {
                mProfile.mMediaProfile.mVideoDirection = 0
                return
            }

            else -> return
        }

         */
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
        return mProfile
    }

    override fun getRemoteCallProfile(): ImsCallProfile {
        return mRemoteProfile
    }

    override fun getLocalCallProfile(): ImsCallProfile {
        return mLocalProfile;
    }

    override fun getProperty(name: String?): String {
        return mProfile.getCallExtra(name)
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
            CALL_TYPE_VOICE_N_VIDEO, CALL_TYPE_VOICE -> RILImsCallType.CALL_TYPE_VOICE
            CALL_TYPE_VIDEO_N_VOICE, CALL_TYPE_VT -> RILImsCallType.CALL_TYPE_VT
            CALL_TYPE_VT_TX -> RILImsCallType.CALL_TYPE_VT_TX
            CALL_TYPE_VT_RX -> RILImsCallType.CALL_TYPE_VT_RX
            CALL_TYPE_VT_NODIR -> RILImsCallType.CALL_TYPE_VT_NODIR
            CALL_TYPE_VS -> throw RuntimeException("NI VS!!")
            CALL_TYPE_VS_TX -> RILImsCallType.CALL_TYPE_CS_VS_TX
            CALL_TYPE_VS_RX -> RILImsCallType.CALL_TYPE_CS_VS_RX
            else -> throw RuntimeException("Unknown callType $callType")
        }
    }

    override fun start(callee: String, profile: ImsCallProfile?) {
        Log.d(tag, "calling " + Rlog.pii(tag, callee))
        mCallee = callee
        val callInfo = RILImsDial()
        callInfo.address = callee
        callInfo.clir = profile!!.getCallExtraInt(EXTRA_OIR) // Huawei do this so it **must** be right... Oh wait...
        val extras = profile.mCallExtras.getBundle("OemCallExtras")
        if (extras != null) {
            Rlog.e(tag, "NI reading oemcallextras, it is $extras")
        }
        val callType: Int
        try {
            callType = convertAospCallType(profile.callType)
        } catch (e: RuntimeException) {
            listener?.callSessionInitiatedFailed(ImsReasonInfo(ImsReasonInfo.CODE_LOCAL_INTERNAL_ERROR, ImsReasonInfo.CODE_UNSPECIFIED, e.message))
            throw e
        }

        callInfo.callDetails.callType = callType
        if (HwImsService.instance!!.getConfig(mSlotId)!!.getConfigInt(ImsConfig.ConfigConstants.VLT_SETTING_ENABLED) == ImsConfig.FeatureValueConstants.ON) {
            callInfo.callDetails.callDomain = RILImsCallDomain.CALL_DOMAIN_AUTOMATIC
        } else {
            callInfo.callDetails.callDomain = RILImsCallDomain.CALL_DOMAIN_CS
        }

        try {
            Rlog.d(tag, "adding to awaiting id from ril")
            awaitingIdFromRIL[mCallee] = this // Do it sooner rather than later so that this call is not seen as a phantom
            RilHolder.getImsRadio(mSlotId)!!.imsDial(RilHolder.callback({ radioResponseInfo, _ ->
                if (radioResponseInfo.error == 0) {
                    Rlog.d(tag, "successfully placed call")
                    mInCall = true
                    mState = State.ESTABLISHED
                    listener?.callSessionInitiated(profile)
                } else {
                    Rlog.e(tag, "call failed")
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

    }

    override fun startConference(members: Array<String>?, profile: ImsCallProfile?) {
        // This method is to initiate the conference call, not to add all the members.
        start(members!![0], profile)
        //TODO is this right?
    }

    override fun accept(callType: Int, profile: ImsStreamMediaProfile?) {
        mState = State.ESTABLISHING
        try {
            RilHolder.getImsRadio(mSlotId)!!.acceptImsCall(RilHolder.callback({ radioResponseInfo, _ ->
                if (radioResponseInfo.error != 0) {
                    listener?.callSessionInitiatedFailed(ImsReasonInfo())
                    Rlog.e(tag, "error accepting ims call")
                } else {
                    listener?.callSessionInitiated(mProfile)
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
    }

    override fun reject(reason: Int) {
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
                    listener?.callSessionHeld(mProfile)
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
                    listener?.callSessionResumed(mProfile)
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
        listener?.callSessionMergeComplete(HwImsCallSession(mSlotId, mProfile, call))
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
