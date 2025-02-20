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

import android.os.AsyncResult;
import android.os.Message;
import android.os.Parcel;
import android.os.WorkSource;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandException;


public class ImsRILRequest {
    static final String LOG_TAG = "RILJ_IMS";
    private static final int MAX_POOL_SIZE = 4;
    String mClientId;
    ImsRILRequest mNext;
    int mRequest;
    Message mResult;
    int mSerial;
    int mWakeLockType;
    WorkSource mWorkSource;
    Parcel mp;
    static int sNextSerial = 0;
    static Object sSerialMonitor = new Object();
    private static Object sPoolSync = new Object();
    private static ImsRILRequest sPool = null;
    private static int sPoolSize = 0;

    public static ImsRILRequest obtain(int request, Message result) {
        ImsRILRequest rr = null;
        synchronized (sPoolSync) {
            if (sPool != null) {
                rr = sPool;
                sPool = rr.mNext;
                rr.mNext = null;
                sPoolSize--;
            }
        }
        if (rr == null) {
            rr = new ImsRILRequest();
        }
        ImsRILRequest rr2 = rr;
        synchronized (sSerialMonitor) {
            int i = sNextSerial;
            sNextSerial = i + 1;
            rr2.mSerial = i;
        }
        rr2.mRequest = request;
        rr2.mResult = result;
        rr2.mp = Parcel.obtain();
        rr2.mWakeLockType = -1;
        rr2.mWorkSource = null;
        if (result != null && result.getTarget() == null) {
            throw new NullPointerException("Message target must not be null");
        }
        rr2.mp.writeInt(request);
        rr2.mp.writeInt(rr2.mSerial);
        return rr2;
    }

    public static ImsRILRequest obtain(int request, Message result, WorkSource workSource) {
        ImsRILRequest rr = obtain(request, result);
        if (workSource != null) {
            rr.mWorkSource = workSource;
            // TODO Iceows - String.valueOf(workSource.get(0)) + ":" + workSource.getName(0);
            rr.mClientId = "0123";
        } else {
            Rlog.e(LOG_TAG, "null workSource " + request);
        }
        return rr;
    }

    public void release() {
        synchronized (sPoolSync) {
            if (sPoolSize < 4) {
                this.mNext = sPool;
                sPool = this;
                sPoolSize++;
                this.mResult = null;
            }
        }
    }

    private ImsRILRequest() {
    }

    public static void resetSerial() {
        synchronized (sSerialMonitor) {
            sNextSerial = 0;
        }
    }

    public String serialString() {
        StringBuilder sb = new StringBuilder(8);
        String sn = Integer.toString(this.mSerial);
        sb.append('[');
        int s = sn.length();
        for (int i = 0; i < 4 - s; i++) {
            sb.append('0');
        }
        sb.append(sn);
        sb.append(']');
        return sb.toString();
    }

    public void onError(int error, Object ret) {
        CommandException ex = CommandException.fromRilErrno(error);
        Rlog.d(LOG_TAG, serialString() + "< " + ImsRIL.requestToString(this.mRequest) + " error: " + ex);
        if (this.mResult != null) {
            AsyncResult.forMessage(this.mResult, ret, ex);
            this.mResult.sendToTarget();
        }
        if (this.mp != null) {
            this.mp.recycle();
            this.mp = null;
        }
    }
}
