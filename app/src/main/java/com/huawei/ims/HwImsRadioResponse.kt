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


import android.os.Bundle
import android.telephony.Rlog
import android.telephony.ims.ImsCallProfile
import android.util.Log
import vendor.huawei.hardware.radio.ims.V1_0.*
import java.util.*


/*
package vendor.huawei.hardware.radio.ims.V1_0;
void RspMsg(RadioResponseInfo radioResponseInfo, int i, RspMsgPayload rspMsgPayload) throws RemoteException;
void conferenceResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void explicitCallTransferResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void getCurrentImsCallsResponse(RadioResponseInfo radioResponseInfo, ArrayList<RILImsCall> arrayList) throws RemoteException;
void getCurrentImsCallsResponseV1_2(RadioResponseInfo radioResponseInfo, ArrayList<RILImsCallV1_2> arrayList) throws RemoteException;
void getCurrentImsCallsWithImsDomainResponse(RadioResponseInfo radioResponseInfo, ArrayList<RILImsCallEx> arrayList) throws RemoteException;
void getImsRegistrationStateResponse(RadioResponseInfo radioResponseInfo, boolean z, int i) throws RemoteException;
void getLastCallFailCauseResponse(RadioResponseInfo radioResponseInfo, LastCallFailCauseInfo lastCallFailCauseInfo) throws RemoteException;
void hangupConnectionResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void hangupForegroundResumeBackgroundResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void hangupWaitingOrBackgroundResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void rejectCallResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void sendDtmfResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void setClirResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void setImsRegErrReportResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void setMuteResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void startDtmfResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void stopDtmfResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void switchWaitingOrHoldingAndActiveResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;
void uiccAuthResponse(RadioResponseInfo radioResponseInfo, RILUICCAUTHRESPONSE riluiccauthresponse) throws RemoteException;

*/

class HwImsRadioResponse internal constructor(private val mSlotId: Int) : IRadioImsResponse.Stub() {
    private val LOG_TAG = "HwImsRadioResponse"

    /**
     * Key to retrieve the call ID from an incoming call intent.
     * @see .open
     */
    val EXTRA_CALL_ID = "android:imsCallID"

    /**
     * Part of the ACTION_IMS_SERVICE_UP or _DOWN intents.
     * A long value; the phone ID corresponding to the IMS service coming up or down.
     * Internal use only.
     * @hide
     */
    val EXTRA_PHONE_ID = "android:phone_id"

    /**
     * Part of the ACTION_IMS_INCOMING_CALL intents.
     * An boolean value; Flag to indicate that the incoming call is a normal call or call for USSD.
     * The value "true" indicates that the incoming call is for USSD.
     * Internal use only.
     * @hide
     */
    val EXTRA_USSD = "android:ussd"

    /**
     * Part of the ACTION_IMS_INCOMING_CALL intents.
     * An integer value; service identifier obtained from [ImsManager.open].
     * Internal use only.
     * @hide
     */
    val EXTRA_SERVICE_ID = "android:imsServiceId"

    /**
     * Part of the ACTION_IMS_INCOMING_CALL intents.
     * A boolean value; Flag to indicate whether the call is an unknown
     * dialing call. Such calls are originated by sending commands (like
     * AT commands) directly to modem without Android involvement.
     * Even though they are not incoming calls, they are propagated
     * to Phone app using same ACTION_IMS_INCOMING_CALL intent.
     * Internal use only.
     * @hide
     */
    val EXTRA_IS_UNKNOWN_CALL = "android:isUnknown"
    override fun RspMsg(
        radioResponseInfo: RadioResponseInfo?,
        msgType: Int,
        rspMsgPayload: RspMsgPayload?
    ) {
        Log.i(LOG_TAG, "rspmsg radioresponseinfo = $radioResponseInfo,msgtype=$msgType")
        Log.i(LOG_TAG, "serial " + radioResponseInfo)
        Log.i(LOG_TAG, "type=" + RespCode.getName(msgType))
        Log.i(LOG_TAG, "slotID=" + mSlotId)

        //  C:\GitHub\iceows\ims_hi6250_volte\app\src\main\java\com\huawei\ims\ImsRadioResponse.java
        when (msgType) {

            RilConstS32.RIL_REQUEST_HW_IMS_DIAL ->  Log.i(LOG_TAG, "RIL_REQUEST_HW_IMS_DIAL")             /* 579 */
            RilConstS32.RIL_REQUEST_HW_IMS_SEND_USSD -> Log.i(LOG_TAG, "RIL_REQUEST_HW_IMS_SEND_USSD")    /* 588 */
            RilConstS32.RIL_REQUEST_HW_IMS_ANSWER -> Log.i(LOG_TAG, "RIL_REQUEST_HW_IMS_ANSWER")          /* 590 */
            RilConstS32.RIL_REQUEST_HW_GET_IMS_SWITCH -> Log.i(LOG_TAG, "RIL_REQUEST_HW_GET_IMS_SWITCH")  /* 651 */
            RilConstS32.RIL_REQUEST_HW_SET_IMS_SWITCH -> Log.i(LOG_TAG, "RIL_REQUEST_HW_SET_IMS_SWITCH")  /* 650 */
            RilConstS32.RIL_REQUEST_HW_IMS_REGISTER -> Log.i(LOG_TAG, "RIL_REQUEST_HW_IMS_REGISTER")      /* 686 */

            else -> Log.w(LOG_TAG, "Unknown msg type :$msgType")
        }

        // Huawei
        radioResponseInfo?.let { RilHolder.triggerImsCB(it.serial, radioResponseInfo, rspMsgPayload) }

    }

    override fun conferenceResponse(p0: RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun explicitCallTransferResponse(p0: RadioResponseInfo?) {
        TODO("Not yet implemented")
    }


    // Huawei
    override fun getCurrentImsCallsResponse(radioResponseInfo: RadioResponseInfo?, calls: ArrayList<RILImsCall>)
    {
        Log.i(LOG_TAG, "getCurrentImsCallsResponse on slotID " + mSlotId)
        synchronized(HwImsCallSession.sCallsLock) {
            val num: Int = calls.size
            val dcCalls = ArrayList<DriverImsCall>(num)

            Log.i(LOG_TAG, "ArrayList size = " + num)

            for (i in 0 until num) {
                val dc = DriverImsCall(calls[i])

                Log.i(LOG_TAG, "calls list contains " + redactCall(dc))
                // RIL sometimes gives us the leading +, so first try with one, and if its null, try again without the +.
                var session = HwImsCallSession.awaitingIdFromRIL["+" + dc.number]
                if (session == null)
                    session = HwImsCallSession.awaitingIdFromRIL[dc.number]
                if (session != null) {
                    Rlog.d(LOG_TAG, "giving call id from ril.")
                    session.addIdFromRIL(dc)
                }
                session = HwImsCallSession.calls[dc.index]
                if (session == null) {
                    if (dc.isMT ) {
                        Log.d(LOG_TAG, "Notifying MmTelFeature incoming call! " + redactCall(dc))
                        // An incoming call that we have never seen before, tell the framework.
                    } else {
                        Log.e(LOG_TAG, "Phantom Call!!!! " + redactCall(dc))
                        HwImsCallSession.calls.forEach { s, hwImsCallSession -> Rlog.d(LOG_TAG, "Phantom debugging got call in static calls " + redactCall(hwImsCallSession.driverImsCall!!) + " with number " + s) }
                        HwImsCallSession.awaitingIdFromRIL.forEach { s, hwImsCallSession -> Rlog.d(LOG_TAG, "Phantom debugging got call in static awaiting " + hwImsCallSession.mCallee + " with number " + s) }
                    }
                    val extras = Bundle()
                    val callSession = HwImsCallSession(mSlotId, ImsCallProfile(), dc)
                    extras.putInt(EXTRA_PHONE_ID, mSlotId)
                    extras.putString(EXTRA_CALL_ID, callSession.callId)
                    extras.putBoolean(EXTRA_IS_UNKNOWN_CALL, dc.isMT) // A new outgoing call should never happen. Someone is playing with AT commands or talking to the modem.
                    Log.i(LOG_TAG, "createMmTelFeature" )
                    HwImsService.instance!!.createMmTelFeature(mSlotId)!!.notifyIncomingCall(callSession, extras)
                } else {
                    // Existing call, update it's data.
                    session.updateCall(dc)
                }
                if (dc.isMpty  && dc.state == DriverImsCall.State.DIALING) { // Dialing & Multiparty
                    // It is a new conference call being added.
                    for (confSession in HwImsCallSession.calls.values) {
                        if (confSession.isMultiparty) {
                            Rlog.d(LOG_TAG, "adding call " + dc.index + " to conference " + confSession.callId)
                            confSession.notifyConfDone(dc)
                            break
                        }
                    }
                }
                //calls.add(call.index)
                dcCalls.add(dc)
            }
            dcCalls.sort()


            /*
            for ((_, value) in HwImsCallSession.calls) {
                Rlog.d(LOG_TAG, "calls : " + value.driverImsCall)
                if (!calls.contains(value.driverImsCall!!.index)) {
                    Rlog.d(LOG_TAG, "notifying dead call " + redactCall(value.driverImsCall!!))

                    try {
                        Rlog.d(LOG_TAG, "notifying dead call " + redactCall(value.driverImsCall!!))
                        value.notifyEnded()
                    } catch (e: RuntimeException) {
                        Rlog.e(LOG_TAG, "error notifying dead call!", e)
                    }

                }
            }

             */
        }
    }

    private fun redactCall(call: DriverImsCall): String {
        return "{.state = " + call.state + ", .index = " + call.index + ", .toa = " + call.TOA + ", .isMpty = " + call.isMpty + ", .isMT = " + call.isMT + ", .als = " + call.als + ", .isVoice = " + call.isVoice + ", .isVoicePrivacy = " + call.isVoicePrivacy + ", .number = " + Rlog.pii(LOG_TAG, call.number) + ", .numberPresentation = " + call.numberPresentation + ", .name = " + Rlog.pii(LOG_TAG, call.name) + ", .namePresentation = " + call.namePresentation + ", .callDetails = " + call.imsCallProfile.toString() +  ", .peerVideoSupport = " + call.peerVideoSupport + "}"
    }

    // https://github.com/LineageOS/android_frameworks_opt_telephony/blob/lineage-16.0/src/java/com/android/internal/telephony/RadioResponse.java
    override fun getCurrentImsCallsResponseV1_2(
        radioResponseInfo: RadioResponseInfo?,
        arrayList: ArrayList<RILImsCallV1_2>
    ) {
        // Huawei
        Log.i(LOG_TAG, "getCurrentImsCallsResponse V1.2 on slotID " + mSlotId)
     }

    override fun getCurrentImsCallsWithImsDomainResponse(
        p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?,
        p1: ArrayList<RILImsCallEx>?
    ) {
        TODO("Not yet implemented")
    }

    override fun getImsRegistrationStateResponse(
        p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?,
        p1: Boolean,
        p2: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getLastCallFailCauseResponse(
        p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?,
        p1: LastCallFailCauseInfo?
    ) {
        TODO("Not yet implemented")
    }

    override fun hangupConnectionResponse(radioResponseInfo: RadioResponseInfo) {
        RspMsg(radioResponseInfo, -1, null)
    }

    override fun hangupForegroundResumeBackgroundResponse(radioResponseInfo: RadioResponseInfo) {
        RspMsg(radioResponseInfo, -1, null)
    }

    override fun hangupWaitingOrBackgroundResponse(radioResponseInfo: RadioResponseInfo) {
        RspMsg(radioResponseInfo, -1, null)
    }

    override fun rejectCallResponse(radioResponseInfo: RadioResponseInfo) {
        RspMsg(radioResponseInfo, -1, null)
    }


    override fun sendDtmfResponse(p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun setClirResponse(p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    // vendor.huawei.hardware.radio.ims.V1_0.IRadioImsResponse
    override fun setImsRegErrReportResponse(responseInfo: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?) {
        responseVoid(responseInfo)
    }

    override fun setMuteResponse(responseInfo: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun startDtmfResponse(responseInfo: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun stopDtmfResponse(p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun switchWaitingOrHoldingAndActiveResponse(p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun uiccAuthResponse(
        p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?,
        p1: RILUICCAUTHRESPONSE?
    ) {
        TODO("Not yet implemented")
    }

    private fun responseVoid(radioResponseInfo: RadioResponseInfo?) {

        radioResponseInfo?.let { RilHolder.triggerImsCB(it.serial, radioResponseInfo,null) }

    }


    /*
RilConstS32.RIL_REQUEST_HW_IMS_DIAL ->  Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_DIAL")            579 - 0x243
RilConstS32.RIL_REQUEST_HW_IMS_SEND_USSD -> Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_SEND_USSD")   588
RilConstS32.RIL_REQUEST_HW_IMS_ANSWER -> Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_ANSWER")         590
RilConstS32.RIL_REQUEST_HW_GET_IMS_SWITCH -> Log.w(LOG_TAG, "RIL_REQUEST_HW_GET_IMS_SWITCH") 651
RilConstS32.RIL_REQUEST_HW_SET_IMS_SWITCH -> Log.w(LOG_TAG, "RIL_REQUEST_HW_SET_IMS_SWITCH") 650
RilConstS32.RIL_REQUEST_HW_IMS_REGISTER -> Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_REGISTER")     686
*/
    enum class RespCode(var value: Int) {

        RIL_REQUEST_HW_IMS_DIAL(579),RIL_REQUEST_HW_IMS_SEND_USSD(588),
        RIL_REQUEST_HW_IMS_ANSWER(590), RIL_REQUEST_HW_GET_IMS_SWITCH(650),
        RIL_REQUEST_HW_SET_IMS_SWITCH(651), RIL_REQUEST_HW_IMS_REGISTER(686);

        companion object {

            fun getName(code: Int): String {
                val x = Arrays.stream(RespCode.values()).filter { resp_code -> resp_code.value == code }.findAny()
                return if (x.isPresent) {
                    x.get().name
                } else {
                    Integer.toString(code)
                }
            }
        }

    }

}
