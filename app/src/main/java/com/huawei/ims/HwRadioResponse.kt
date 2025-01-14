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

import android.util.Log
import vendor.huawei.hardware.radio.V2_0.IRadioResponse
import vendor.huawei.hardware.radio.V2_0.IccIoResultEx
import vendor.huawei.hardware.radio.V2_0.RILPreferredPLMNSelector
import vendor.huawei.hardware.radio.V2_0.RadioResponseInfo
import vendor.huawei.hardware.radio.V2_0.RspMsgPayload


class HwRadioResponse internal constructor(private val mSlotId: Int) : IRadioResponse.Stub() {
    private val LOG_TAG = "HwRadioResponse"

    override fun RspMsg(
        radioResponseInfo: RadioResponseInfo?,
        msgType: Int,
        rspMsgPayload: RspMsgPayload?
    ) {

        Log.i(LOG_TAG, "rspmsg radioresponseinfo = $radioResponseInfo,msgtype=$msgType")
        Log.i(LOG_TAG, "serial " + radioResponseInfo)
        Log.i(LOG_TAG, "slotID=" + mSlotId)

        // Huawei
        radioResponseInfo?.let { RilHolder.triggerCB(it.serial, radioResponseInfo, rspMsgPayload) }

    }


    override fun getPolListResponse(
        radioResponseInfo: RadioResponseInfo?,
        rILPreferredPLMNSelector: RILPreferredPLMNSelector?
    ) {
        TODO("Not yet implemented")
    }

    override fun getSimMatchedFileFromRilCacheResponse(
        radioResponseInfo: RadioResponseInfo?,
        iccIoResultEx: IccIoResultEx?
    ) {
        TODO("Not yet implemented")
    }


}
