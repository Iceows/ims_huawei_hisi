/*
 * This file is part of HwIms
 * Copyright (C) 2019-2025 Penn Mackintosh and Raphael Mounier
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

package com.huawei.ims;


import static vendor.huawei.hardware.radio.ims.V1_0.RilConstS32.*;

import android.telephony.Rlog;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import kotlin.NotImplementedError;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vendor.huawei.hardware.radio.ims.V1_0.IRadioImsResponse;
import vendor.huawei.hardware.radio.ims.V1_0.LastCallFailCauseInfo;
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCall;
import vendor.huawei.hardware.radio.ims.V1_0.RILUICCAUTHRESPONSE;
import vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo;
import vendor.huawei.hardware.radio.ims.V1_0.RspMsgPayload;


public final class HwImsRadioResponseV2 extends IRadioImsResponse.Stub {
        private final String LOG_TAG;
        private final int mSlotId;

    public HwImsRadioResponseV2(int mSlotId) {
        this.mSlotId = mSlotId;
        this.LOG_TAG = "HwImsRadioResponseV2";
    }
    public void RspMsg(@Nullable RadioResponseInfo radioResponseInfo, int msgType, @Nullable RspMsgPayload rspMsgPayload) {
        Log.i(this.LOG_TAG, "rspmsg radioresponseinfo = " + radioResponseInfo + ",msgtype=" + msgType);
        Log.i(this.LOG_TAG, "serial " + radioResponseInfo);
        Log.i(this.LOG_TAG, "type=" + msgType);
        Log.i(this.LOG_TAG, "slotID=" + this.mSlotId);
        switch (msgType) {
            case RIL_REQUEST_HW_IMS_DIAL:
                Log.i(this.LOG_TAG, "RIL_REQUEST_HW_IMS_DIAL");
                break;
            case RIL_REQUEST_HW_IMS_SEND_USSD:
                Log.i(this.LOG_TAG, "RIL_REQUEST_HW_IMS_SEND_USSD");
                break;
            case RIL_REQUEST_HW_IMS_ANSWER:
                Log.i(this.LOG_TAG, "RIL_REQUEST_HW_IMS_ANSWER");
                break;
            case RIL_REQUEST_HW_SET_IMS_SWITCH:
                Log.i(this.LOG_TAG, "RIL_REQUEST_HW_SET_IMS_SWITCH");
                break;
            case RIL_REQUEST_HW_GET_IMS_SWITCH:
                Log.i(this.LOG_TAG, "RIL_REQUEST_HW_GET_IMS_SWITCH");
                break;
            case RIL_REQUEST_HW_IMS_REGISTER:
                Log.i(this.LOG_TAG, "RIL_REQUEST_HW_IMS_REGISTER");
                break;
            default:
                Log.w(this.LOG_TAG, "Unknown msg type :" + msgType);
        }

        if (radioResponseInfo != null) {
            RilHolder.INSTANCE.triggerImsCB(radioResponseInfo.serial, radioResponseInfo, rspMsgPayload);
        }

    }

    public void conferenceResponse(@Nullable RadioResponseInfo p0) {
        String var2 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var2);
    }

    public void explicitCallTransferResponse(@Nullable RadioResponseInfo p0) {
        String var2 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var2);
    }

    public void getCurrentImsCallsResponse(@Nullable RadioResponseInfo radioResponseInfo, @NotNull ArrayList calls) {
        Intrinsics.checkNotNullParameter(calls, "calls");
        Object ret = null;
        Log.i(this.LOG_TAG, "getCurrentImsCallsResponse on slotID " + this.mSlotId);
        Object var4 = HwImsCallSession.Companion.getSCallsLock();
        synchronized(var4) {

            int num = calls.size();
            ArrayList dcCalls = new ArrayList(num);
            Log.i(this.LOG_TAG, "ArrayList size = " + num);

            for(int i = 0 ; i < num; ++i) {
                DriverImsCall driverImsCall = new DriverImsCall((RILImsCall)calls.get(i));
                dcCalls.add(driverImsCall);
                if (driverImsCall.isVoicePrivacy) {
                    Log.i(this.LOG_TAG, "InCall VoicePrivacy is enabled");
                } else {
                    Log.i(this.LOG_TAG, "InCall VoicePrivacy is disabled");
                }
            }

            CollectionsKt.sort((List)dcCalls);
            Unit var13 = Unit.INSTANCE;
        }
    }

    private final String redactCall(DriverImsCall call) {
        return "{.state = " + call.state + ", .index = " + call.index + ", .toa = " + call.TOA + ", .isMpty = " + call.isMpty + ", .isMT = " + call.isMT + ", .als = " + call.als + ", .isVoice = " + call.isVoice + ", .isVoicePrivacy = " + call.isVoicePrivacy + ", .number = " + Rlog.pii(this.LOG_TAG, call.number) + ", .numberPresentation = " + call.numberPresentation + ", .name = " + Rlog.pii(this.LOG_TAG, call.name) + ", .namePresentation = " + call.namePresentation + ", .callProfiles = " + call.imsCallProfiles.toString() + ", .peerVideoSupport = " + call.peerVideoSupport + "}";
    }

    public void getCurrentImsCallsResponseV1_2(@Nullable RadioResponseInfo radioResponseInfo, @NotNull ArrayList arrayList) {
        Log.i(this.LOG_TAG, "getCurrentImsCallsResponse V1.2 on slotID " + this.mSlotId);
    }

    public void getCurrentImsCallsWithImsDomainResponse(@Nullable RadioResponseInfo p0, @Nullable ArrayList p1) {
        String var3 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var3);
    }

    public void getImsRegistrationStateResponse(@Nullable RadioResponseInfo p0, boolean p1, int p2) {
        String var4 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var4);
    }

    public void getLastCallFailCauseResponse(@Nullable RadioResponseInfo p0, @Nullable LastCallFailCauseInfo p1) {
        String var3 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var3);
    }

    public void hangupConnectionResponse(@NotNull RadioResponseInfo radioResponseInfo) {
        this.RspMsg(radioResponseInfo, -1, (RspMsgPayload)null);
    }

    public void hangupForegroundResumeBackgroundResponse(@NotNull RadioResponseInfo radioResponseInfo) {
        this.RspMsg(radioResponseInfo, -1, (RspMsgPayload)null);
    }

    public void hangupWaitingOrBackgroundResponse(@NotNull RadioResponseInfo radioResponseInfo) {
        this.RspMsg(radioResponseInfo, -1, (RspMsgPayload)null);
    }

    public void rejectCallResponse(@NotNull RadioResponseInfo radioResponseInfo) {
        this.RspMsg(radioResponseInfo, -1, (RspMsgPayload)null);
    }

    public void sendDtmfResponse(@Nullable RadioResponseInfo p0) {
        String var2 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var2);
    }

    public void setClirResponse(@Nullable RadioResponseInfo p0) {
        String var2 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var2);
    }

    public void setImsRegErrReportResponse(@Nullable RadioResponseInfo responseInfo) {
        this.responseVoid(responseInfo);
    }

    public void setMuteResponse(@Nullable RadioResponseInfo responseInfo) {
        String var2 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var2);
    }

    public void startDtmfResponse(@Nullable RadioResponseInfo responseInfo) {
        String var2 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var2);
    }

    public void stopDtmfResponse(@Nullable RadioResponseInfo p0) {
        String var2 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var2);
    }

    public void switchWaitingOrHoldingAndActiveResponse(@Nullable RadioResponseInfo p0) {
        String var2 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var2);
    }

    public void uiccAuthResponse(@Nullable RadioResponseInfo p0, @Nullable RILUICCAUTHRESPONSE p1) {
        String var3 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var3);
    }

    private void responseVoid(RadioResponseInfo radioResponseInfo) {
        if (radioResponseInfo != null) {
            RilHolder.INSTANCE.triggerImsCB(radioResponseInfo.serial, radioResponseInfo, (RspMsgPayload) null);
        }
    }

}
