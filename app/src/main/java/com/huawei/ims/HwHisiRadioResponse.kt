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
import vendor.huawei.hardware.hisiradio.V1_0.CellInfo
import vendor.huawei.hardware.hisiradio.V1_0.CsgNetworkInfo
import vendor.huawei.hardware.hisiradio.V1_0.CsgNetworkInfo_1_1
import vendor.huawei.hardware.hisiradio.V1_0.HwCall_V1_2
import vendor.huawei.hardware.hisiradio.V1_0.RILDeviceVersionResponse
import vendor.huawei.hardware.hisiradio.V1_0.RILDsFlowInfoResponse
import vendor.huawei.hardware.hisiradio.V1_0.RILRADIOSYSINFO
import vendor.huawei.hardware.hisiradio.V1_0.RILUICCAUTHRESPONSE
import vendor.huawei.hardware.hisiradio.V1_0.RadioResponseInfo
import vendor.huawei.hardware.hisiradio.V1_0.RspMsgPayload
import vendor.huawei.hardware.hisiradio.V1_0.SetupDataCallResult
import vendor.huawei.hardware.hisiradio.V1_1.HwDataRegStateResult_1_1
import vendor.huawei.hardware.hisiradio.V1_1.HwSignalStrength_1_1
import vendor.huawei.hardware.hisiradio.V1_1.HwVoiceRegStateResult_1_1
import vendor.huawei.hardware.hisiradio.V1_1.IHisiRadioResponse
import vendor.huawei.hardware.hisiradio.V1_1.LteAttachInfo
import java.util.ArrayList
import java.util.Arrays


class HwHisiRadioResponse internal constructor(private val mSlotId: Int) : IHisiRadioResponse.Stub() {
    private val LOG_TAG = "HwHisiRadioResponse"

    override fun RspMsg(
        radioResponseInfo: vendor.huawei.hardware.hisiradio.V1_0.RadioResponseInfo?,
        msgType: Int,
        rspMsgPayload: RspMsgPayload?
    ) {
        Log.i(LOG_TAG, "rspmsg radioresponseinfo = $radioResponseInfo,msgtype=$msgType")
        Log.i(LOG_TAG, "serial " + radioResponseInfo)
        Log.i(LOG_TAG, "type=" + RespCode.getName(msgType))
        Log.i(LOG_TAG, "slotID=" + mSlotId)

        // Huawei
    radioResponseInfo?.let { RilHolder.triggerHisiCB(it.serial, radioResponseInfo, rspMsgPayload) }

    }


    override fun deactivateDataCallEmergencyResponse(radioResponseInfo: RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun getAvailableCsgIdsResponse(
        radioResponseInfo: RadioResponseInfo?,
        arrayList: ArrayList<CsgNetworkInfo>?
    ) {
        TODO("Not yet implemented")
    }

    override fun getAvailableCsgIdsResponse_1_1(
        radioResponseInfo: RadioResponseInfo?,
        arrayList: ArrayList<CsgNetworkInfo_1_1>?
    ) {
        TODO("Not yet implemented")
    }

    override fun getCapOfRecPseBaseStationResponse(radioResponseInfo: RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun getCardTrayInfoResponse(
        radioResponseInfo: RadioResponseInfo?,
        arrayList: ArrayList<Byte>?
    ) {
        TODO("Not yet implemented")
    }

    override fun getCellInfoListOtdoaResponse(
        radioResponseInfo: RadioResponseInfo?,
        arrayList: ArrayList<CellInfo>?
    ) {
        TODO("Not yet implemented")
    }

    override fun getCurrentCallsResponseHwV1_2(
        radioResponseInfo: RadioResponseInfo?,
        arrayList: ArrayList<HwCall_V1_2>?
    ) {
        TODO("Not yet implemented")
    }

    override fun getDeviceVersionResponse(
        radioResponseInfo: RadioResponseInfo?,
        rILDeviceVersionResponse: RILDeviceVersionResponse?
    ) {
        TODO("Not yet implemented")
    }

    override fun getDsFlowInfoResponse(
        radioResponseInfo: RadioResponseInfo?,
        rILDsFlowInfoResponse: RILDsFlowInfoResponse?
    ) {
        TODO("Not yet implemented")
    }

    override fun getNvcfgMatchedResultResponse(
        radioResponseInfo: RadioResponseInfo?,
        str: String?
    ) {
        TODO("Not yet implemented")
    }

    override fun getSystemInfoExResponse(
        radioResponseInfo: RadioResponseInfo?,
        rilradiosysinfo: RILRADIOSYSINFO?
    ) {
        TODO("Not yet implemented")
    }

    override fun manualSelectionCsgIdResponse(radioResponseInfo: RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun sendSimChgTypeInfoResponse(radioResponseInfo: RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun setupDataCallEmergencyResponse(
        radioResponseInfo: RadioResponseInfo?,
        setupDataCallResult: SetupDataCallResult?
    ) {
        TODO("Not yet implemented")
    }

    override fun uiccAuthResponse(
        radioResponseInfo: RadioResponseInfo?,
        riluiccauthresponse: RILUICCAUTHRESPONSE?
    ) {
        TODO("Not yet implemented")
    }

    override fun getDataRegistrationStateResponse_1_1(
        radioResponseInfo: RadioResponseInfo?,
        hwDataRegStateResult_1_1: HwDataRegStateResult_1_1?
    ) {
        TODO("Not yet implemented")
    }

    override fun getHwPreferredNetworkTypeResponse_1_1(
        radioResponseInfo: RadioResponseInfo?,
        i: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getHwSignalStrengthResponse_1_1(
        radioResponseInfo: RadioResponseInfo?,
        hwSignalStrength_1_1: HwSignalStrength_1_1?
    ) {
        TODO("Not yet implemented")
    }

    override fun getLteAttachInfoResponse(
        radioResponseInfo: RadioResponseInfo?,
        lteAttachInfo: LteAttachInfo?
    ) {
        TODO("Not yet implemented")
    }

    override fun getVoiceRegistrationStateResponse_1_1(
        radioResponseInfo: RadioResponseInfo?,
        hwVoiceRegStateResult_1_1: HwVoiceRegStateResult_1_1?
    ) {
        TODO("Not yet implemented")
    }

    override fun setTemperatureControlResponse(radioResponseInfo: RadioResponseInfo?) {
        TODO("Not yet implemented")
    }

    override fun setUlfreqEnableResponse(radioResponseInfo: RadioResponseInfo?) {
        TODO("Not yet implemented")
    }


    enum class RespCode(var value: Int) {
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
                val x = Arrays.stream(HwImsRadioResponse.RespCode.values())
                    .filter { resp_code -> resp_code.value == code }.findAny()
                return if (x.isPresent) {
                    x.get().name
                } else {
                    Integer.toString(code)
                }
            }
        }
    }
}