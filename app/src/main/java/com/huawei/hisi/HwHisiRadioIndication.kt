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

package com.huawei.hisi

import android.telephony.Rlog
import android.util.Log
import vendor.huawei.hardware.hisiradio.V1_0.RILAPDsFlowInfoReport
import vendor.huawei.hardware.hisiradio.V1_0.RILUnsolMsgPayload
import vendor.huawei.hardware.hisiradio.V1_0.RILVsimOtaSmsResponse
import vendor.huawei.hardware.hisiradio.V1_0.RilSysInfor
import vendor.huawei.hardware.hisiradio.V1_1.HwSignalStrength_1_1
import vendor.huawei.hardware.hisiradio.V1_1.IHisiRadioIndication
import vendor.huawei.hardware.hisiradio.V1_1.RilConstS32.*

class HwHisiRadioIndication (private val mSlotId: Int) : IHisiRadioIndication.Stub() {
    private val LOG_TAG = "HwHisiRadioIndication"
/*
    Line 4968: 01-14 06:22:13.795  1713  1824 D HwHisiRadioIndication: indicationType = 1, msgId = 2071, msgPayload = {.nData = 2, .nDatas = [0, 0], .strData = , .strDatas = []}
    Line 5286: 01-14 06:22:14.524  1713  1824 D HwHisiRadioIndication: indicationType = 1, msgId = 2078, msgPayload = {.nData = 2, .nDatas = [0, 0], .strData = , .strDatas = []}
    Line 5674: 01-14 06:22:15.187  1713  1824 D HwHisiRadioIndication: indicationType = 1, msgId = 2030, msgPayload = {.nData = 2, .nDatas = [1, 0], .strData = , .strDatas = []}
    Line 6102: 01-14 06:23:10.240  1713  1824 D HwHisiRadioIndication: indicationType = 0, msgId = 2077, msgPayload = {.nData = 16, .nDatas = [0, -133526144, 2147483647, 255, 0, 0, 32767, -1, 32767, -1, -112, -7, -12, -1, 2147483647, 7], .strData = , .strDatas = []}
    */



    override fun UnsolMsg(indicationType: Int, msgId: Int, rilUnsolMsgPayload: RILUnsolMsgPayload) {
        Log.d(LOG_TAG, "indicationType = $indicationType, msgId = $msgId, msgPayload = $rilUnsolMsgPayload")

        when (msgId) {
            RIL_UNSOL_HW_PLMN_SEARCH_INFO_IND -> hisiPLMNSearchInfo(indicationType)
            RIL_UNSOL_HW_LIMIT_PDP_ACT_IND -> hisiLimitPDP(indicationType)
            RIL_UNSOL_HW_SIGNAL_STRENGTH -> hisiSignalStrength(indicationType)
            RIL_UNSOL_HW_RESTRAT_RILD_NV_MATCH -> hisiRestratRild(indicationType)
            RIL_UNSOL_HW_NETWORK_REJECT_CASE  -> hisiRejectCase(indicationType)

            else -> Log.w(LOG_TAG, "Unknown msg type :$msgId")
        }

    }

    private fun hisiRejectCase(indicationType: Int) {
        Rlog.d(LOG_TAG, "hisiRejectCase, indication type : " + indicationType)
    }

    private fun hisiPLMNSearchInfo(indicationType: Int) {
        Rlog.d(LOG_TAG, "hisiPLMNSearchInfo, indication type : " + indicationType)
    }
    private fun hisiRestratRild(indicationType: Int) {
        Rlog.d(LOG_TAG, "hisiRestratRild, indication type : " + indicationType)
    }
    private fun hisiLimitPDP(indicationType: Int) {
        Rlog.d(LOG_TAG, "hisiLimitPDP, indication type : " + indicationType)
    }
    private fun hisiSignalStrength(indicationType: Int) {
        Rlog.d(LOG_TAG, "hisiSignalStrength, indication type : " + indicationType)
    }

    override fun apDsFlowInfoReport(i: Int, rILAPDsFlowInfoReport: RILAPDsFlowInfoReport?) {
        TODO("Not yet implemented")
    }

    override fun dsFlowInfoReport(i: Int, rILAPDsFlowInfoReport: RILAPDsFlowInfoReport?) {
        TODO("Not yet implemented")
    }

    override fun imsaToVowifiMsg(i: Int, arrayList: ArrayList<Byte>?) {
        TODO("Not yet implemented")
    }

    override fun recPseBaseStationReport(i: Int, i2: Int) {
        TODO("Not yet implemented")
    }

    override fun simMatchRestartRildInd(i: Int, i2: Int) {
        TODO("Not yet implemented")
    }

    override fun sysInforInd(i: Int, rilSysInfor: RilSysInfor?) {
        TODO("Not yet implemented")
    }

    override fun vsimOtaSmsReport(i: Int, rILVsimOtaSmsResponse: RILVsimOtaSmsResponse?) {
        TODO("Not yet implemented")
    }

    override fun currentHwSignalStrength_1_1(i: Int, hwSignalStrength_1_1: HwSignalStrength_1_1?) {
        TODO("Not yet implemented")
    }

    override fun updateUlfreqRPT(i: Int, i2: Int, i3: Int, i4: Int) {
        TODO("Not yet implemented")
    }

}