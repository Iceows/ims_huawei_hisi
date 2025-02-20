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
import android.os.AsyncResult
import android.os.Handler
import android.os.Looper
import android.os.Message
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


    private var mDc: DriverImsCall? = null

    private var mCallId: Int
    private var mAcceptPending = false
    public var mHandler: Handler
    public var mCi: ImsRIL? = null

    private var mbV1_2Call = false
    private val mProfile: ImsCallProfile
    private val mLocalProfile: ImsCallProfile
    private val mRemoteProfile: ImsCallProfile

    private val mStreamMediaProfile : ImsStreamMediaProfile

    private var listener: ImsCallSessionListener? = null
    private var mListenerProxy: ImsCallSessionListenerProxy? = null

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

        this.mLocalProfile = ImsCallProfile(SERVICE_TYPE_NORMAL, profile.callType)
        this.mRemoteProfile = ImsCallProfile(SERVICE_TYPE_NORMAL, profile.callType)
        this.mProfile = ImsCallProfile()
        this.mListenerProxy = ImsCallSessionListenerProxy()
        
        this.mState = State.IDLE
        this.mHandler = HwImsCallSessionImplHandler()

        this.mAcceptPending = false
        this.radioTechFromRilImsCall = -1
        this.redirectNumber = null
        this.redirectNumberToa = 0
        this.redirectNumberPresentation = 1

        mCallId = 0
        this.mStreamMediaProfile = ImsStreamMediaProfile()

        setListener(listener)
        mListenerProxy!!.mListener = listener
    }


    // For incoming (MT) calls
    constructor(slotId: Int, profile: ImsCallProfile, dc: DriverImsCall) : this(slotId, profile) {

        calls[dc.index] = this
        mbV1_2Call = false

        mCi = RilHolder.getImsRadio(mSlotId)
        setListener(listener)

        mDc = DriverImsCall(dc)
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

    class HwImsCallSessionImplHandler : Handler(Looper.getMainLooper()) {
        // android.os.Handler
        override fun handleMessage(msg: Message) {
            val causeCode: Int
            Rlog.d(tag, "Message received: what = " + msg.what)
            when (msg.what) {
                1 -> {
                    var ar: AsyncResult? = msg.obj as AsyncResult
                    if (ar != null && ar.exception != null) {
                        Rlog.d(tag, "Dial error: " + ar.exception);
                         return
                    }
                }
                2 -> {
                    var ar: AsyncResult? = msg.obj as AsyncResult
                    if (ar != null && ar.exception != null) {
                        Rlog.d(tag, "Accept error: " + ar.exception);
                        return
                    }
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


    @SuppressLint("MissingPermission")
    //DriverImsCall dcUpdate
    fun updateCall(call: DriverImsCall) {
        val lastState = mState
        when (call.state) {
            DriverImsCall.State.ACTIVE -> {
                if (driverImsCall == null) {
                    Rlog.e(tag, "Phantom call!")
                    mState = State.ESTABLISHED
                    mCallId = call.index
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
            Rlog.e(tag, "Null dcUpdate in extractImsCallProfileIntoCallProfile")
            return
        }
        updateImsCallProfile(dcUpdate)
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
        val ratTypeFromModem = dc.radioTechFromRilImsCall
        if (ratTypeFromModem != -1) {
            mProfile.setCallExtra("CallRadioTech", getRadioTechFromDriverCall(ratTypeFromModem))
        } else {
            //val hwTelephonyManager: HwTelephonyManager = HwTelephonyManager.getDefault()
            //if (hwTelephonyManager != null) {
                //val default4GSlotId: Int = hwTelephonyManager.getDefault4GSlotId()
                //val imsRegDomain: Int = hwTelephonyManager.getImsDomain(default4GSlotId)
            //}
            val imsRegDomain=0;
            mProfile.setCallExtra(
                "CallRadioTech",
                getRadioTechFromDriverCall(imsRegDomain)
            )
        }

        redirectNumberToa = redirectNumberToa
        redirectNumber = PhoneNumberUtils.stringFromStringAndTOA(redirectNumber, redirectNumberToa)

        mProfile.setCallExtra("redirect_number", redirectNumber)
        mProfile.setCallExtraInt(
            "redirect_number_presentation",
            presentationToOIR(redirectNumberPresentation)
        )

        // Translate Huawei IMS call type
        when (dc.imsCallProfiles.call_type) {
            HwImsCallDetails.CALL_TYPE_UNKNOWN -> {
                mProfile.mCallType = CALL_TYPE_VOICE_N_VIDEO
                mProfile.mMediaProfile.mVideoDirection = ImsStreamMediaProfile.DIRECTION_INVALID
            }

            HwImsCallDetails.CALL_TYPE_VOICE -> {
                mProfile.mCallType = CALL_TYPE_VOICE
                mProfile.mMediaProfile.mVideoDirection = ImsStreamMediaProfile.DIRECTION_INVALID
            }

            HwImsCallDetails.CALL_TYPE_VT -> {
                mProfile.mCallType = CALL_TYPE_VT
                mProfile.mMediaProfile.mVideoDirection =
                    ImsStreamMediaProfile.DIRECTION_SEND_RECEIVE
            }

            HwImsCallDetails.CALL_TYPE_VT_TX -> {
                mProfile.mCallType = CALL_TYPE_VT_TX
                mProfile.mMediaProfile.mVideoDirection = ImsStreamMediaProfile.DIRECTION_SEND
            }

            HwImsCallDetails.CALL_TYPE_VT_RX -> {
                mProfile.mCallType = CALL_TYPE_VT_RX
                mProfile.mMediaProfile.mVideoDirection = ImsStreamMediaProfile.DIRECTION_RECEIVE
            }

            HwImsCallDetails.CALL_TYPE_VT_NODIR ->                 // Not propagating VT_NODIR call type to UI
                mProfile.mMediaProfile.mVideoDirection =
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
        if (isSessionValid()) {
            mCi!!.setMute(muted, mHandler.obtainMessage(13))
        }
    }

    fun isSessionValid(): Boolean {
        val isValid = mState != -1
        if (!isValid) {
            Rlog.e(tag, "Session is closed")
        }
        return isValid
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

        /*
        if (isSessionValid()) {
            HwTelephonyFactory.getHwChrServiceManager()
                .reportCallException("HwIms", mCi.getCHRReportPhoneId(), 0, "ImsRIL")
            this.mCallProfile.mCallType = profile.mCallType
            this.mCallProfile.mMediaProfile = profile.mMediaProfile
            mState = 1
            mCallee = callee
            val clir = profile.getCallExtraInt("oir")
            val details = ImsCallProfiles(mapCallTypeFromProfile(profile.mCallType), 3, null)
            extractProfileExtrasIntoImsCallProfile(profile, details)
            mCi!!.dial(callee, clir, details, mHandler.obtainMessage(1))
        }
         */
    }

    override fun startConference(members: Array<String>?, profile: ImsCallProfile?) {
        // This method is to initiate the conference call, not to add all the members.
        start(members!![0], profile)
        //TODO is this right?
    }

    override fun accept(callType: Int, profile: ImsStreamMediaProfile?) {
        Log.d(tag, "accept : " );
        if (isSessionValid()) {
            if (mAcceptPending) {
                Rlog.d(
                    tag,
                    "this call is being accepted..."
                )
                return
            }
            mAcceptPending = true
           //mCi!!.acceptCall(mHandler.obtainMessage(2), mapCallTypeFromProfile(callType))
        }
    }
    
    override fun deflect(destination: String?) {
        // Huawei shim this, we can do the same.
    }

    override fun reject(reason: Int) {

        if (isSessionValid()) {
            Rlog.d(tag, "reject $reason")
            /*
            val cause: Int = ImsCallProviderUtils.getImsCallRejectCause(reason)
            Rlog.d(log, "reject cause$cause")
            if (cause != -1) {
                mCi.rejectImsCallForCause(mCallId, cause, mHandler.obtainMessage(7))
            } else {
                mCi!!.hangupConnection(mCallId, mHandler.obtainMessage(3))
            }
            triggerRilRecoveryDelayed()*/
        }
        mState = State.TERMINATING
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
        if (isSessionValid()) {
            Rlog.d(tag, "terminate $reason")
            mCi!!.hangupConnection(this.mCallId, mHandler.obtainMessage(3))
            //triggerRilRecoveryDelayed()
        }
    }
    override fun hold(profile: ImsStreamMediaProfile?) {
        if (isSessionValid()) {
            Rlog.d(tag, "hold requested.")
            mCi!!.switchWaitingOrHoldingAndActive(this.mHandler.obtainMessage(4))
        }
    }

    override fun resume(profile: ImsStreamMediaProfile?) {
        if (isSessionValid()) {
            Rlog.d(tag, "resume requested.")
            mCi!!.switchWaitingOrHoldingAndActive(mHandler.obtainMessage(5))
        }
    }

    override fun merge() {

        mCi!!.conference(mHandler.obtainMessage(6))
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


    override fun sendDtmf(c: Char, result: Message?) {
        if (isSessionValid()) {
            mCi!!.sendDtmf(c, result)
        }
    }

    override fun startDtmf(c: Char) {
        if (isSessionValid()) {
            mCi!!.startDtmf(c, null)
        }
    }


    override fun stopDtmf() {
        if (isSessionValid()) {
            mCi!!.stopDtmf(null)
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
