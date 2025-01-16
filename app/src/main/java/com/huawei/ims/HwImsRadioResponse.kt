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
import com.android.ims.ImsManager
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

            RilConstS32.RIL_REQUEST_HW_IMS_DIAL ->  Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_DIAL")             /* 579 */
            RilConstS32.RIL_REQUEST_HW_IMS_SEND_USSD -> Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_SEND_USSD")    /* 588 */
            RilConstS32.RIL_REQUEST_HW_IMS_ANSWER -> Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_ANSWER")          /* 590 */
            RilConstS32.RIL_REQUEST_HW_GET_IMS_SWITCH -> Log.w(LOG_TAG, "RIL_REQUEST_HW_GET_IMS_SWITCH")  /* 651 */
            RilConstS32.RIL_REQUEST_HW_SET_IMS_SWITCH -> Log.w(LOG_TAG, "RIL_REQUEST_HW_SET_IMS_SWITCH")  /* 650 */
            RilConstS32.RIL_REQUEST_HW_IMS_REGISTER -> Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_REGISTER")      /* 686 */

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

    private fun getCurrentImsCallsResponseV2(
        responseInfo: RadioResponseInfo,
        calls: ArrayList<RILImsCall>
    ) {
        Rlog.d(LOG_TAG, "getCurrentImsCallsResponse:responseInfo =$responseInfo")
        responseCurrentImsCallsV2(responseInfo, calls)
    }

    private fun responseCurrentImsCallsV2(
        responseInfo: RadioResponseInfo,
        calls: ArrayList<RILImsCall>
    ) {
        /*
        val rr: ImsRILRequest = this.mRil.processResponse(responseInfo)
        if (rr != null) {
            var ret: Any? = null
            if (responseInfo.error === 0) {
                val num = calls.size
                val dcCalls: ArrayList<DriverImsCall> = ArrayList<DriverImsCall>(num)
                for (i in 0 until num) {
                    val dc = DriverImsCall(calls[i])
                    if (!this.mRil.isSupportCnap()) {
                        dc.namePresentation = 2
                        this.mRil.logd("isSupportCnap : false")
                    }
                    dcCalls.add(dc)
                    if (dc.isVoicePrivacy) {
                        this.mRil.mVoicePrivacyOnRegistrants.notifyRegistrants()
                        this.mRil.logd("InCall VoicePrivacy is enabled")
                    } else {
                        this.mRil.mVoicePrivacyOffRegistrants.notifyRegistrants()
                        this.mRil.logd("InCall VoicePrivacy is disabled")
                    }
                }
                Collections.sort(dcCalls)
                ret = dcCalls
                sendMessageResponse(rr.mResult, ret)
            }
            this.mRil.processResponseDone(rr, responseInfo, ret)
        }
        */

    }


    override fun getCurrentImsCallsResponse(radioResponseInfo: RadioResponseInfo?, arrayList: ArrayList<RILImsCall>)
    {
        // Huawei
        Log.i(LOG_TAG, "getCurrentImsCallsResponse on slotID " + mSlotId)
        synchronized(HwImsCallSession.sCallsLock) {
            val calls = ArrayList<Int>(arrayList.size)
            for (call in arrayList) {
                Log.i(LOG_TAG, "calls list contains " + redactCall(call))
                // RIL sometimes gives us the leading +, so first try with one, and if its null, try again without the +.
                var session = HwImsCallSession.awaitingIdFromRIL["+" + call.number]
                if (session == null)
                    session = HwImsCallSession.awaitingIdFromRIL[call.number]
                if (session != null) {
                    Rlog.d(LOG_TAG, "giving call id from ril.")
                    session.addIdFromRIL(call)
                }
                session = HwImsCallSession.calls[call.index]
                if (session == null) {
                    if (call.isMT > 0) {
                        Log.d(LOG_TAG, "Notifying MmTelFeature incoming call! " + redactCall(call))
                        // An incoming call that we have never seen before, tell the framework.
                    } else {
                        Log.e(LOG_TAG, "Phantom Call!!!! " + redactCall(call))
                        HwImsCallSession.calls.forEach { s, hwImsCallSession -> Rlog.d(LOG_TAG, "Phantom debugging got call in static calls " + redactCall(hwImsCallSession.rilImsCall!!) + " with number " + s) }
                        HwImsCallSession.awaitingIdFromRIL.forEach { s, hwImsCallSession -> Rlog.d(LOG_TAG, "Phantom debugging got call in static awaiting " + hwImsCallSession.mCallee + " with number " + s) }
                        // Someone has been talking to AT... naughty.
                    }
                    val extras = Bundle()
                    val callSession = HwImsCallSession(mSlotId, ImsCallProfile(), call)
                    extras.putInt(ImsManager.EXTRA_PHONE_ID, mSlotId)
                    extras.putString(ImsManager.EXTRA_CALL_ID, callSession.callId)
                    extras.putBoolean(ImsManager.EXTRA_IS_UNKNOWN_CALL, call.isMT.toInt() == 0) // A new outgoing call should never happen. Someone is playing with AT commands or talking to the modem.
                    Log.i(LOG_TAG, "createMmTelFeature" )
                    HwImsService.instance!!.createMmTelFeature(mSlotId)!!.notifyIncomingCall(callSession, extras)
                } else {
                    // Existing call, update it's data.
                    session.updateCall(call)
                }
                if (call.isMpty > 0 && call.state == 2) { // Dialing & Multiparty
                    // It is a new conference call being added.
                    for (confSession in HwImsCallSession.calls.values) {
                        if (confSession.isMultiparty) {
                            Rlog.d(LOG_TAG, "adding call " + call.index + " to conference " + confSession.callId)
                            confSession.notifyConfDone(call)
                            break
                        }
                    }
                }
                calls.add(call.index)
            }
            for ((_, value) in HwImsCallSession.calls) {
                Rlog.d(LOG_TAG, "calls : " + value.rilImsCall)
                if (!calls.contains(value.rilImsCall!!.index)) {
                    Rlog.d(LOG_TAG, "notifying dead call " + redactCall(value.rilImsCall!!))

                    try {
                        Rlog.d(LOG_TAG, "notifying dead call " + redactCall(value.rilImsCall!!))
                        value.notifyEnded()
                    } catch (e: RuntimeException) {
                        Rlog.e(LOG_TAG, "error notifying dead call!", e)
                    }

                }
            }
        }
    }

    private fun redactCall(call: RILImsCall): String {
        return "{.state = " + call.state + ", .index = " + call.index + ", .toa = " + call.toa + ", .isMpty = " + call.isMpty + ", .isMT = " + call.isMT + ", .als = " + call.als + ", .isVoice = " + call.isVoice + ", .isVoicePrivacy = " + call.isVoicePrivacy + ", .number = " + Rlog.pii(LOG_TAG, call.number) + ", .numberPresentation = " + call.numberPresentation + ", .name = " + Rlog.pii(LOG_TAG, call.name) + ", .namePresentation = " + call.namePresentation + ", .callDetails = " + call.callDetails.toString() + ", .isEConference = " + call.isECOnference + ", .peerVideoSupport = " + call.peerVideoSupport + "}"
    }
    override fun getCurrentImsCallsResponseV1_2(
        p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?,
        p1: ArrayList<RILImsCallV1_2>?
    ) {
        TODO("Not yet implemented")
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

    override fun setImsRegErrReportResponse(p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun setMuteResponse(p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun startDtmfResponse(p0: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo?) {
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


    enum class RespCode(var value: Int) {
        /*
        RilConstS32.RIL_REQUEST_HW_IMS_DIAL ->  Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_DIAL")            579 - 0x243
        RilConstS32.RIL_REQUEST_HW_IMS_SEND_USSD -> Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_SEND_USSD")   588
        RilConstS32.RIL_REQUEST_HW_IMS_ANSWER -> Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_ANSWER")         590
        RilConstS32.RIL_REQUEST_HW_GET_IMS_SWITCH -> Log.w(LOG_TAG, "RIL_REQUEST_HW_GET_IMS_SWITCH") 651
        RilConstS32.RIL_REQUEST_HW_SET_IMS_SWITCH -> Log.w(LOG_TAG, "RIL_REQUEST_HW_SET_IMS_SWITCH") 650
        RilConstS32.RIL_REQUEST_HW_IMS_REGISTER -> Log.w(LOG_TAG, "RIL_REQUEST_HW_IMS_REGISTER")     686
        */
        IMS_DIAL_RESPONSE(0xdc), SET_IMS_CALL_WAITING_RESPONSE(0x100),
        GET_LTE_INFO_RESPONSE(0x136), ACCEPT_IMS_CALL_RESPONSE(0xe7),
        SET_DMPCSCF_RESPONSE(0x13c), SET_DMDYN_RESPONSE(0x13d),
        SET_DMTIMER_RESPONSE(0x13e), SET_DMSMS_RESPONSE(0x13f),
        GET_DMPCSCF_RESPONSE(0x140), GET_DMTIMER_RESPONSE(0x141),
        GET_DMDYN_RESPONSE(0x142), GET_DMSMS_RESPONSE(0x143),
        GET_DMUSER_RESPONSE(0x144), WIFI_EMERGENCY_AID(0x151),
        SEND_BATTERY_STATUS_RESPONSE(0x147), MODIFY_IMS_CALL_INITIATE_RESPONSE(0x133),
        MODIFY_IMS_CALL_CONFIRM_RESPONSE(0x114), GET_IMS_IMPU_RESPONSE(0xf6),
        SET_IMS_VT_CAPABILITY_RESPONSE(0x150), IMS_LAST_CALL_FAIL_REASON_INFO_RESPONSE(0x14f),
        SWITCH_WAITING_OR_HOLDING_AND_ACTIVE_FOR_IMS_RESPONSE(0x156),
        PASS1(0xe3), PASS2(0x35), PASS3(0x36);

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
