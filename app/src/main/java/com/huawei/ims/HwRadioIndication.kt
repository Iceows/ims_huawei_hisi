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

import android.telephony.Rlog
import android.util.Log
import vendor.huawei.hardware.radio.V2_0.IRadioIndication
import vendor.huawei.hardware.radio.V2_0.RILUnsolMsgPayload
import vendor.huawei.hardware.radio.V2_0.RilConstS32.*


class HwRadioIndication internal constructor(private val mSlotId: Int) : IRadioIndication.Stub() {
    private val LOG_TAG = "HwRadioIndication"
    override fun UnsolMsg(indicationType: Int, msgId: Int, rILUnsolMsgPayload: RILUnsolMsgPayload?) {

        Log.d(LOG_TAG, "indicationType = $indicationType, msgId = $msgId, msgPayload = $rILUnsolMsgPayload")

        // Huawei RilConstS32.java on package vendor.huawei.hardware.radio.V2_0;
        when (msgId) {

            //RIL_UNSOL_HW_IMSA_VOWIFI_MSG
            RIL_UNSOL_HW_RESIDENT_NETWORK_CHANGED -> hwResidentRadioChanged(indicationType);
            RIL_UNSOL_HW_CS_CHANNEL_INFO_IND-> hwCsChannelInfo(indicationType);
            RIL_UNSOL_HW_ECCNUM-> hwEccNum(indicationType);
            RIL_UNSOL_HW_EXIST_NETWORK_INFO -> hwExistNetWorkInfo(indicationType);
            else -> Log.w(LOG_TAG, "Unknown msg type :$msgId")
        }

    }

    private fun hwExistNetWorkInfo(indicationType: Int) {
        Rlog.d(LOG_TAG, "hwExistNetWorkInfo indication type : " + indicationType)
    }
    private fun hwResidentRadioChanged(indicationType: Int) {
        Rlog.d(LOG_TAG, "hwResidentRadioChanged indication type : " + indicationType)
    }

    private fun hwCsChannelInfo(indicationType: Int) {
        Rlog.d(LOG_TAG, "hwCsChannelInfo indication type : " + indicationType)
    }
    private fun hwEccNum(indicationType: Int) {
        Rlog.d(LOG_TAG, "hwEccNum indication type : " + indicationType)
    }

}