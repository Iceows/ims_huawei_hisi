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


import android.os.RemoteException
import android.telephony.Rlog
import android.util.Log
import vendor.huawei.hardware.radio.ims.V1_0.IRadioImsIndication
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCallModify
import vendor.huawei.hardware.radio.ims.V1_0.RILImsHandover
import vendor.huawei.hardware.radio.ims.V1_0.RILImsModifyEndCause
import vendor.huawei.hardware.radio.ims.V1_0.RILImsMtStatusReport
import vendor.huawei.hardware.radio.ims.V1_0.RILImsRegErrReport
import vendor.huawei.hardware.radio.ims.V1_0.RILImsSrvstatusList
import vendor.huawei.hardware.radio.ims.V1_0.RILImsSuppSvcNotification
import vendor.huawei.hardware.radio.ims.V1_0.RILUnsolMsgPayload
import vendor.huawei.hardware.radio.ims.V1_0.RILVtFlowInfoReport
import vendor.huawei.hardware.radio.ims.V1_0.RilConstS32.*


/*
    package vendor.huawei.hardware.radio.ims.V1_0;
    void UnsolMsg(int i, int i2, RILUnsolMsgPayload rILUnsolMsgPayload) throws RemoteException;
    void imsCallModifyEndCauseInd(int i, RILImsModifyEndCause rILImsModifyEndCause) throws RemoteException;
    void imsCallModifyInd(int i, RILImsCallModify rILImsCallModify) throws RemoteException;
    void imsCallMtStatusInd(int i, RILImsMtStatusReport rILImsMtStatusReport) throws RemoteException;
    void imsHandoverInd(int i, RILImsHandover rILImsHandover) throws RemoteException;
    void imsNetworkStateChanged(int i) throws RemoteException;
    void imsRegErrRptInd(int i, RILImsRegErrReport rILImsRegErrReport) throws RemoteException;
    void imsSrvStatusInd(int i, RILImsSrvstatusList rILImsSrvstatusList) throws RemoteException;
    void imsSuppSrvInd(int i, RILImsSuppSvcNotification rILImsSuppSvcNotification) throws RemoteException;
    void vtFlowInfoReport(int i, RILVtFlowInfoReport rILVtFlowInfoReport) throws RemoteException;
 */


class HwImsRadioIndication internal constructor(private val mSlotId: Int) : IRadioImsIndication.Stub() {

    private val LOG_TAG = "HwImsRadioIndication"



    override fun UnsolMsg(indicationType: Int, msgId: Int, rilUnsolMsgPayload: RILUnsolMsgPayload) {
        Log.d(LOG_TAG, "indicationType = $indicationType, msgId = $msgId, msgPayload = $rilUnsolMsgPayload")

        // Huawei RilConstS32.java on package ,vendor.huawei.hardware.radio.ims.V1_0;
        when (msgId) {
            RIL_UNSOL_HW_IMS_RESPONSE_CALL_STATE_CHANGED -> imsCallStateChanged(indicationType)
            RIL_UNSOL_HW_IMS_CALL_RING -> imsCallRing(indicationType)
            RIL_UNSOL_HW_IMS_RINGBACK_TONE -> imsRingBackTone(indicationType)
            RIL_UNSOL_HW_IMS_VOICE_BAND_INFO -> imsVoiceBandInfo(indicationType)
            RIL_UNSOL_HW_IMS_HOLD_TONE_IND -> imsCallHeldChange(indicationType)
            RIL_UNSOL_HW_IMS_CS_REDIAL_NOTIFY -> imsCSRedialNotify(indicationType)

            else -> Log.w(LOG_TAG, "Unknown msg type :$msgId")
        }
    }

    private fun imsCSRedialNotify(indicationType: Int) {
        Rlog.d(LOG_TAG, "imsCSRedialNotify, indication type : " + indicationType)
    }

    private fun imsRingBackTone(indicationType: Int) {
        Rlog.d(LOG_TAG, "imsRingBackTone, indication type : " + indicationType)
    }

    private fun imsVoiceBandInfo(indicationType: Int) {
        Rlog.d(LOG_TAG, "imsVoiceBandInfo, indication type : " + indicationType)

        /*
        Rlog.d("ImsRadioIndication", "imsVoiceBandInfo =" + var1.toString() + ",bandInfo = " + var2)
        this.mRil.processIndication(var1)
        val var3: ImsRIL = this.mRil
        val var4: IntArray = ImsRIL.arrayListToPrimitiveArray(var2)
        this.mRil.unsljLog(3019)
        if (this.mRil.mSpeechInfoRegistrants != null && var4 != null) {
            this.mRil.mSpeechInfoRegistrants.notifyRegistrants(
                AsyncResult(
                    null as Any?,
                    var4,
                    null as Throwable?
                )
            )
        }
        */

    }

    private fun imsCallRing(indicationType: Int) {
        Rlog.d(LOG_TAG, "imsCallRing indication type : " + indicationType)
    }


    private fun imsCallStateChanged(indicationType: Int) {
        Rlog.d(LOG_TAG, "imsCallStateChanged")
        if (indicationType > 1) { // 1 is the normal one, 0 happens sometimes, 0 seems to mean "call terminated"
            // Weird...
            Rlog.w(LOG_TAG, "unknown indicationType $indicationType")
        }
        try {
            RilHolder.getImsRadio(mSlotId)!!.getCurrentImsCalls(RilHolder.getNextSerial())
        } catch (e: RemoteException) {
            Rlog.e(LOG_TAG, "Error getting current calls", e)
        }

    }

    private fun imsCallHeldChange(indicationType: Int) {
        imsCallStateChanged(indicationType)
        // We can probably optimise this somehow but I don't know how. CallSession checks the status
        // If its held, it will send the correct notifications.
    }


    override fun imsCallModifyEndCauseInd(type: Int, cause: RILImsModifyEndCause) {
        // Huawei
        Rlog.d(LOG_TAG, "imsCallModifyEndCauseInd" + type)
    }

    override fun imsCallModifyInd(type: Int, modify: RILImsCallModify) {
        try {
            RilHolder.getImsRadio(mSlotId)!!.getCurrentImsCalls(RilHolder.getNextSerial())
        } catch (e: RemoteException) {
            Rlog.e(LOG_TAG, "Error getting current calls for handover", e)
        }
        // Huawei
    }

    override fun imsCallMtStatusInd(type: Int, imsCallMtStatus: RILImsMtStatusReport) {
        // TODO: MT status indications - Missed incoming call notifications
        Rlog.d(LOG_TAG, "Received MT status indication: $type/$imsCallMtStatus")
        // Huawei
    }

    override fun imsHandoverInd(indicationType: Int, imsHandover: RILImsHandover) {
        // Huawei
    }

    override fun imsSrvStatusInd(type: Int, imsSrvStatus: RILImsSrvstatusList) {
        // Huawei
    }

    override fun imsSuppSrvInd(type: Int, idsSuppSacNotification: RILImsSuppSvcNotification) {
        // Huawei
    }


    override fun vtFlowInfoReport(indicationType: Int, rilVtFlowInfoReport: RILVtFlowInfoReport) {
        // Huawei not needed
    }


    // END OF HUAWEI METHODS



    override fun imsNetworkStateChanged(i: Int) {
        // AOSP IMS
    }

    override fun imsRegErrRptInd(p0: Int, p1: RILImsRegErrReport?) {
        TODO("Not yet implemented")
    }


}
