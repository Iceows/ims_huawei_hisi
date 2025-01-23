/*
 * This file is part of HwIms
 * Copyright (C) 2025 Penn Mackintosh and Raphael Mounier
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

/*
      ImsServiceState class was not part of android repos before Android 9 version (los16)
      I found it in the huawei ims but especially also in the msm8996 ims
      https://github.com/bcyj/android_tools_leeco_msm8996/blob/master/telephony-apps/ims/src/org/codeaurora/ims/CallServiceState.java

      It seems that this is part of a set of classes published by codeaurora
 */

package com.huawei.ims;

import android.os.Parcel;


public class ImsServiceState {


    public static final int CALL_TYPE_VOICE = 0;

    public static final int CALL_TYPE_VT_TX = 1;
    public static final int CALL_TYPE_VT_RX = 2;
    public static final int CALL_TYPE_VT = 3;
    public static final int CALL_TYPE_VT_NODIR = 4;
    public static final int CALL_TYPE_CS_VS_TX = 5;
    public static final int CALL_TYPE_CS_VS_RX = 6;
    public static final int CALL_TYPE_PS_VS_TX = 7;
    public static final int CALL_TYPE_PS_VS_RX = 8;
    public static final int CALL_TYPE_UNKNOWN = 9;
    public static final int CALL_TYPE_SMS = 10;
    public static final int CALL_TYPE_UT = 11;

    public static final int RADIO_TECH_1xRTT = 6;
    public static final int RADIO_TECH_ANY = -1;
    public static final int RADIO_TECH_EDGE = 2;
    public static final int RADIO_TECH_EHRPD = 13;
    public static final int RADIO_TECH_EVDO_0 = 7;
    public static final int RADIO_TECH_EVDO_A = 8;
    public static final int RADIO_TECH_EVDO_B = 12;
    public static final int RADIO_TECH_GPRS = 1;
    public static final int RADIO_TECH_GSM = 16;
    public static final int RADIO_TECH_HSDPA = 9;
    public static final int RADIO_TECH_HSPA = 11;
    public static final int RADIO_TECH_HSPAP = 15;
    public static final int RADIO_TECH_HSUPA = 10;
    public static final int RADIO_TECH_IS95A = 4;
    public static final int RADIO_TECH_IS95B = 5;
    public static final int RADIO_TECH_IWLAN = 19;
    public static final int RADIO_TECH_LTE = 14;
    public static final int RADIO_TECH_TD_SCDMA = 17;
    public static final int RADIO_TECH_UMTS = 3;
    public static final int RADIO_TECH_UNKNOWN = 0;
    public static final int RADIO_TECH_WIFI = 18;
    public static final int STATE_DISABLED = 0;
    public static final int STATE_ENABLED = 2;
    public static final int STATE_NOT_SUPPORTED = 3;
    public static final int STATE_PARTIALLY_ENABLED = 1;
    public static final int STATE_SOS_PENDING = 8;
    public StatusForAccessTech[] accessTechStatus;
    public boolean isValid;
    public int state;
    public int type;
    public byte[] userdata;

    /* loaded from: ImsServiceState$StatusForAccessTech.class */
    public static class StatusForAccessTech {
        public int networkMode;
        public int registered;
        public int restrictCause;
        public int state;

        public StatusForAccessTech() {
        }

        public StatusForAccessTech(Parcel parcel) {
            if (parcel != null) {
                this.networkMode = parcel.readInt();
                this.state = parcel.readInt();
                this.restrictCause = parcel.readInt();
                this.registered = parcel.readInt();
            }
        }

        public String toString() {
            return " mode = " + this.networkMode + " state = " + this.state + " restrictCause = " + this.restrictCause + " registered = " + this.registered;
        }
    }

    public ImsServiceState() {
        this.isValid = false;
    }

    public ImsServiceState(Parcel parcel) {
        boolean z = false;
        this.isValid = false;
        if (parcel != null) {
            this.isValid = parcel.readInt() != 0 ? true : z;
            this.type = parcel.readInt();
            this.state = parcel.readInt();
            this.userdata = parcel.createByteArray();
        }
    }
}
