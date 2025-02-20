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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.KeepalivePacketData;
import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IHwBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.service.carrier.CarrierIdentifier;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.Rlog;
import android.telephony.data.DataProfile;

import com.android.internal.telephony.BaseCommands;
import com.android.internal.telephony.ClientWakelockTracker;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.cdma.CdmaSmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import vendor.huawei.hardware.radio.ims.V1_0.IRadioIms;
import vendor.huawei.hardware.radio.ims.V1_0.RILCURSMUPDATEFILE;
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCallModify;
import vendor.huawei.hardware.radio.ims.V1_0.RILImsDial;
import vendor.huawei.hardware.radio.ims.V1_0.RILImsModifyEndCause;
import vendor.huawei.hardware.radio.ims.V1_0.RILUICCAUTH;
import vendor.huawei.hardware.radio.ims.V1_0.RadioError;
import vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo;


public final class ImsRIL extends BaseCommands implements CommandsInterface {
    private static final int DEFAULT_ACK_WAKE_LOCK_TIMEOUT_MS = 200;
    private static final int DEFAULT_BLOCKING_MESSAGE_RESPONSE_TIMEOUT_MS = 2000;
    private static final int DEFAULT_WAKE_LOCK_TIMEOUT = 60000;
    static final String LOG_TAG = "RILJ_IMS";
    static final String RILJ_ACK_WAKELOCK_NAME = "RILJ_ACK_WL";

    protected final RegistrantList mCallModifyRegistrants;
    protected RegistrantList mCallStateRegistrants;
    protected RegistrantList mHandoverStatusRegistrants;

    public RegistrantList mHoldingToneRegistrants;
    protected RegistrantList mImsCSRedialRegistrations;
    protected RegistrantList mImsDMCNRegistrants;
    protected RegistrantList mImsNetworkStateChangedRegistrants;
    protected RegistrantList mImsRegModeRegistrants;
    protected RegistrantList mLtePDCPInfoRegistrants;
    protected RegistrantList mLteRRCInfoRegistrants;
    protected RegistrantList mModifyCallRegistrants;
    protected final RegistrantList mModifyCallResultRegistrants;
    protected RegistrantList mMtStatusReportRegistrants;

    protected RegistrantList mRingbackToneRegistrants;
    protected RegistrantList mSpeechInfoRegistrants;
    protected RegistrantList mSrvStatusRegistrations;
    protected Registrant mSsnRegistrant;
    AtomicBoolean mTestingEmergencyCall;
    protected RegistrantList mVoWiFiRegErrReportRegistrations;
    protected RegistrantList mVoicePrivacyOffRegistrants;
    protected RegistrantList mVoicePrivacyOnRegistrants;
    protected RegistrantList mVtFlowInfoReportRegistrants;

    final PowerManager.WakeLock mAckWakeLock;
    final int mAckWakeLockTimeout;
    private final ImsRilHandler imsRilHandler;
    private final ClientWakelockTracker mClientWakelockTracker;
    volatile int mAckWlSequenceNum;
    final PowerManager.WakeLock mWakeLock;

    protected WorkSource mActiveWakelockWorkSource;
    int mWakeLockCount;
    final int mWakeLockTimeout;
    volatile int mWlSequenceNum;

    int mRequestMessagesPending;
    int mRequestMessagesWaiting;
    private Context mContext;
    final Integer mPhoneId;

    static final String[] SOCKET_NAME_IMSRIL = {"rildi", "rildi2", "rildi3"};
    volatile IRadioIms mRadioProxy;
    final AtomicLong mRadioProxyCookie;
    final RadioProxyDeathRecipient mRadioProxyDeathRecipient;
    HwImsRadioResponse mRadioResponse;
    HwImsRadioIndication mRadioIndication;
    ArrayList<ImsRILRequest> mRequestsList;




    public void registerForHandoverStatusChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mHandoverStatusRegistrants.add(r);
    }

    public void unregisterForHandoverStatusChanged(Handler h) {
        this.mHandoverStatusRegistrants.remove(h);
    }

    public void registerForSrvStatusUpdate(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSrvStatusRegistrations.add(r);
    }

    public void unregisterForSrvStatusUpdate(Handler h) {
        this.mSrvStatusRegistrations.remove(h);
    }

    public void registerForUnsolLTE_PDCPInfo(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mLtePDCPInfoRegistrants.add(r);
    }

    public void unregisterForUnsolLTE_PDCPInfo(Handler h) {
        this.mLtePDCPInfoRegistrants.remove(h);
    }

    public void registerForUnsolLTE_RRCInfo(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mLteRRCInfoRegistrants.add(r);
    }

    public void unregisterForUnsolLTE_RRCInfo(Handler h) {
        this.mLteRRCInfoRegistrants.remove(h);
    }

    public void registerForImsCSRedial(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mImsCSRedialRegistrations.add(r);
    }

    public void unregisterForImsCSRedial(Handler h) {
        this.mImsCSRedialRegistrations.remove(h);
    }

    public void registerForImsDMCN(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mImsDMCNRegistrants.add(r);
    }

    public void unregisterForImsDMCN(Handler h) {
        this.mImsDMCNRegistrants.remove(h);
    }

    public void registerForVtFlowInfo(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mVtFlowInfoReportRegistrants.add(r);
    }

    public void unregisterForVtFlowInfo(Handler h) {
        this.mVtFlowInfoReportRegistrants.remove(h);
    }

    public void registerForUnsolSpeechInfo(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSpeechInfoRegistrants.add(r);
    }

    public void unregisterForUnsolSpeechInfo(Handler h) {
        this.mSpeechInfoRegistrants.remove(h);
    }

    public void registerForMtStatusReport(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mMtStatusReportRegistrants.add(r);
    }

    public void unregisterForMtStatusReport(Handler h) {
        this.mMtStatusReportRegistrants.remove(h);
    }

    public void registerForCallStateChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mCallStateRegistrants.add(r);
    }

    public void unregisterForCallStateChanged(Handler h) {
        this.mCallStateRegistrants.remove(h);
    }

    public void registerForImsNetworkStateChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mImsNetworkStateChangedRegistrants.add(r);
    }

    public void unregisterForImsNetworkStateChanged(Handler h) {
        this.mImsNetworkStateChangedRegistrants.remove(h);
    }

    public void registerForRingbackTone(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mRingbackToneRegistrants.add(r);
    }

    public void unregisterForRingbackTone(Handler h) {
        this.mRingbackToneRegistrants.remove(h);
    }

    public void registerForImsRegMode(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mImsRegModeRegistrants.add(r);
    }



    public class ImsRilHandler extends Handler {
        ImsRilHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i != 2) {
                if (i == 6) {
                    ImsRIL imsRIL = ImsRIL.this;
                    imsRIL.logd("handleMessage: EVENT_RADIO_PROXY_DEAD cookie = " + msg.obj + " mRadioProxyCookie = " + ImsRIL.this.mRadioProxyCookie.get());
                    if (((Long) msg.obj).longValue() == ImsRIL.this.mRadioProxyCookie.get()) {
                        ImsRIL.this.resetProxyAndRequestList();
                        ImsRIL.this.getRadioProxy(null);
                        return;
                    }
                    return;
                }
                return;
            }
        }

    }

    public void resetProxyAndRequestList() {
        this.mRadioProxy = null;
        ImsRILRequest.resetSerial();
        clearRequestList(1, false);
    }

    private void clearRequestList(int error, boolean loggable) {
        synchronized (this.mRequestsList) {
            int count = this.mRequestsList.size();
            for (int i = 0; i < count; i++) {
                ImsRILRequest rr = this.mRequestsList.get(i);
                rr.onError(error, null);
                decrementWakeLock(rr);
                rr.release();
            }
            this.mRequestsList.clear();
        }
    }


    private void decrementWakeLock(ImsRILRequest rr) {
        synchronized (rr) {
            switch (rr.mWakeLockType) {
                case -1:
                    break;
                case 0:
                    synchronized (this.mWakeLock) {
                        this.mClientWakelockTracker.stopTracking(rr.mClientId, rr.mRequest, rr.mSerial, this.mWakeLockCount > 1 ? this.mWakeLockCount - 1 : 0);
                        String clientId = getWorkSourceClientId(rr.mWorkSource);
                        if (!this.mClientWakelockTracker.isClientActive(clientId) && this.mActiveWakelockWorkSource != null) {
                            this.mActiveWakelockWorkSource.remove(rr.mWorkSource);
                            // TODO Iceows
                            //if (this.mActiveWakelockWorkSource.size() == 0) {
                            //    this.mActiveWakelockWorkSource = null;
                            //}
                            this.mWakeLock.setWorkSource(this.mActiveWakelockWorkSource);
                        }
                        if (this.mWakeLockCount > 1) {
                            this.mWakeLockCount--;
                        } else {
                            this.mWakeLockCount = 0;
                            this.mWakeLock.release();
                        }
                    }
                    break;
                case 1:
                    break;
                default:
                    logw("Decrementing Invalid Wakelock type " + rr.mWakeLockType);
                    break;
            }
            rr.mWakeLockType = -1;
        }
    }

    private String getWorkSourceClientId(WorkSource workSource) {
        if (workSource != null) {
            // TODO Iceows
            //return String.valueOf(workSource.get(0)) + ":" + workSource.getName(0);
        }
        return null;
    }

    private void handleRadioProxyExceptionForRR(String caller, Exception e, ImsRILRequest rr) {
        logd(caller + ": " + e);
        resetProxyAndRequestList();
    }
    public final class RadioProxyDeathRecipient implements IHwBinder.DeathRecipient {
        RadioProxyDeathRecipient() {

        }

        @Override // android.os.IHwBinder.DeathRecipient
        public void serviceDied(long cookie) {
            ImsRIL.this.imsRilHandler.sendMessageDelayed(ImsRIL.this.imsRilHandler.obtainMessage(6, Long.valueOf(cookie)), 3000L);
        }
    }

    public IRadioIms getRadioProxy(Message result) {
        log("Iceows - getRadioProxy");

        if (this.mRadioProxy != null) {
            return this.mRadioProxy;
        }
        try {
            try {
                IRadioIms huaweiRadio1_0 = IRadioIms.getService(SOCKET_NAME_IMSRIL[this.mPhoneId == null ? 0 : this.mPhoneId.intValue()]);
                this.mRadioProxy = huaweiRadio1_0;
            } catch (RemoteException | RuntimeException e) {
                logd("getRadioProxy: huaweiradioProxy got V1_0 exception = " + e);
            }
            if (this.mRadioProxy != null) {
                this.mRadioProxy.linkToDeath(this.mRadioProxyDeathRecipient, this.mRadioProxyCookie.incrementAndGet());
                this.mRadioProxy.setResponseFunctionsHuawei(this.mRadioResponse, this.mRadioIndication);
            }
        } catch (RemoteException | RuntimeException e2) {
            this.mRadioProxy = null;
            log("setResponseFunctions occurs exception = " + e2);
        }
        
        if (this.mRadioProxy == null) {
            if (result != null) {
                AsyncResult.forMessage(result, (Object) null, CommandException.fromRilErrno(1));
                result.sendToTarget();
            }
            this.imsRilHandler.sendMessageDelayed(this.imsRilHandler.obtainMessage(6, Long.valueOf(this.mRadioProxyCookie.get())), 3000L);
        }
        return this.mRadioProxy;
    }

    public static String requestToString(int request) {
        switch (request) {
            case 12:
                return "HW_IMS_HANGUP";
            case 13:
                return "HW_IMS_HANGUP_WAITING_OR_BACKGROUND";
            case 14:
                return "HW_IMS_HANGUP_FOREGROUND_RESUME_BACKGROUND";
            case 15:
                return "HW_IMS_SWITCH_WAITING_OR_HOLDING_AND_ACTIVE";
            case 16:
                return "HW_IMS_CONFERENCE";
            case 17:
                return "HW_IMS_UDUB";
            default:
                return "<unknown request> " + request;
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    public ImsRIL(Context context, Integer instanceId) {
        super(context);
        this.mWlSequenceNum = 0;
        this.mAckWlSequenceNum = 0;
        this.mClientWakelockTracker = new ClientWakelockTracker();
        this.mRequestsList = new ArrayList<>();
        this.mModifyCallRegistrants = new RegistrantList();
        this.mTestingEmergencyCall = new AtomicBoolean(false);
        this.mHandoverStatusRegistrants = new RegistrantList();
        this.mSrvStatusRegistrations = new RegistrantList();
        this.mImsCSRedialRegistrations = new RegistrantList();
        this.mImsDMCNRegistrants = new RegistrantList();
        this.mVoWiFiRegErrReportRegistrations = new RegistrantList();
        this.mLtePDCPInfoRegistrants = new RegistrantList();
        this.mLteRRCInfoRegistrants = new RegistrantList();
        this.mVtFlowInfoReportRegistrants = new RegistrantList();
        this.mSpeechInfoRegistrants = new RegistrantList();
        this.mMtStatusReportRegistrants = new RegistrantList();
        this.mCallStateRegistrants = new RegistrantList();
        this.mImsNetworkStateChangedRegistrants = new RegistrantList();
        this.mRingbackToneRegistrants = new RegistrantList();
        this.mImsRegModeRegistrants = new RegistrantList();
        this.mRadioProxy = null;
        this.mRadioProxyCookie = new AtomicLong(0L);
        this.mVoicePrivacyOnRegistrants = new RegistrantList();
        this.mVoicePrivacyOffRegistrants = new RegistrantList();
        this.mCallModifyRegistrants = new RegistrantList();
        this.mModifyCallResultRegistrants = new RegistrantList();
        this.mHoldingToneRegistrants = new RegistrantList();

        this.mContext = context;
        this.mPhoneId = instanceId;
        
        this.mRadioResponse = new HwImsRadioResponse(0);
        this.mRadioIndication = new HwImsRadioIndication(0);
        this.mRadioResponse.ImsRadioResponse(this);
        this.mRadioIndication.ImsRadioIndication(this);

        this.imsRilHandler = new ImsRilHandler();
        this.mRadioProxyDeathRecipient = new RadioProxyDeathRecipient();

        // getRadioProxy init
        getRadioProxy(null);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(1, LOG_TAG);
        this.mWakeLock.setReferenceCounted(false);
        this.mAckWakeLock = pm.newWakeLock(1, RILJ_ACK_WAKELOCK_NAME);
        this.mAckWakeLock.setReferenceCounted(false);
        this.mWakeLockTimeout = SystemProperties.getInt("ro.ril.wake_lock_timeout", (int) DEFAULT_WAKE_LOCK_TIMEOUT);
        this.mAckWakeLockTimeout = SystemProperties.getInt("ro.ril.wake_lock_timeout", (int) DEFAULT_ACK_WAKE_LOCK_TIMEOUT_MS);
        this.mRequestMessagesPending = 0;
        this.mRequestMessagesWaiting = 0;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
    }

    public void log(String msg) {
        Rlog.d("RILJ_IMS[" + this.mPhoneId + "]", msg);
    }

    public void logd(String msg) {
        Rlog.d("RILJ_IMS[" + this.mPhoneId + "]", msg);
    }

    public void logv(String msg) {
        Rlog.v("RILJ_IMS[" + this.mPhoneId + "]", msg);
    }

    public void logw(String msg) {
        Rlog.w("RILJ_IMS[" + this.mPhoneId + "]", msg);
    }
    
    @Override
    public void getImsRegistrationState(Message message) {

    }

    @Override
    public void setSuppServiceNotifications(boolean b, Message message) {

    }

    @Override
    public void supplyIccPin(String s, Message message) {

    }

    @Override
    public void supplyIccPinForApp(String s, String s1, Message message) {

    }

    @Override
    public void supplyIccPuk(String s, String s1, Message message) {

    }

    @Override
    public void supplyIccPukForApp(String s, String s1, String s2, Message message) {

    }

    @Override
    public void supplyIccPin2(String s, Message message) {

    }

    @Override
    public void supplyIccPin2ForApp(String s, String s1, Message message) {

    }

    @Override
    public void supplyIccPuk2(String s, String s1, Message message) {

    }

    @Override
    public void supplyIccPuk2ForApp(String s, String s1, String s2, Message message) {

    }

    @Override
    public void changeIccPin(String s, String s1, Message message) {

    }

    @Override
    public void changeIccPinForApp(String s, String s1, String s2, Message message) {

    }

    @Override
    public void changeIccPin2(String s, String s1, Message message) {

    }

    @Override
    public void changeIccPin2ForApp(String s, String s1, String s2, Message message) {

    }

    @Override
    public void changeBarringPassword(String s, String s1, String s2, Message message) {

    }

    @Override
    public void supplyNetworkDepersonalization(String s, Message message) {

    }

    @Override
    public void getCurrentCalls(Message message) {

    }

    @Override
    public void getPDPContextList(Message message) {

    }

    @Override
    public void getDataCallList(Message message) {

    }

    @Override
    public void dial(String s, int i, Message message) {

    }

    @Override
    public void dial(String s, int i, UUSInfo uusInfo, Message message) {

    }

    @Override
    public void getIMSI(Message message) {

    }

    @Override
    public void getIMSIForApp(String s, Message message) {

    }

    @Override
    public void getIMEI(Message message) {

    }

    @Override
    public void getIMEISV(Message message) {

    }

    @Override
    public void hangupConnection(int i, Message message) {

    }

    @Override
    public void hangupWaitingOrBackground(Message message) {

    }

    @Override
    public void hangupForegroundResumeBackground(Message message) {

    }

    @Override
    public void switchWaitingOrHoldingAndActive(Message message) {

    }

    @Override
    public void conference(Message message) {

    }

    @Override
    public void setPreferredVoicePrivacy(boolean b, Message message) {

    }

    @Override
    public void getPreferredVoicePrivacy(Message message) {

    }

    @Override
    public void separateConnection(int i, Message message) {

    }

    @Override
    public void acceptCall(Message message) {

    }

    @Override
    public void rejectCall(Message message) {

    }

    @Override
    public void explicitCallTransfer(Message message) {

    }

    @Override
    public void getLastCallFailCause(Message message) {

    }

    @Override
    public void getLastPdpFailCause(Message message) {

    }

    @Override
    public void getLastDataCallFailCause(Message message) {

    }

    @Override
    public void setMute(boolean b, Message message) {

    }

    @Override
    public void getMute(Message message) {

    }

    @Override
    public void getSignalStrength(Message message) {

    }

    @Override
    public void getVoiceRegistrationState(Message message) {

    }

    @Override
    public void getDataRegistrationState(Message message) {

    }

    @Override
    public void getOperator(Message message) {

    }

    @Override
    public void sendDtmf(char c, Message message) {

    }

    @Override
    public void startDtmf(char c, Message message) {

    }

    @Override
    public void stopDtmf(Message message) {

    }

    @Override
    public void sendBurstDtmf(String s, int i, int i1, Message message) {

    }

    @Override
    public void sendSMS(String s, String s1, Message message) {

    }

    @Override
    public void sendSMSExpectMore(String s, String s1, Message message) {

    }

    @Override
    public void sendCdmaSms(byte[] bytes, Message message) {

    }

    @Override
    public void sendImsGsmSms(String s, String s1, int i, int i1, Message message) {

    }

    @Override
    public void sendImsCdmaSms(byte[] bytes, int i, int i1, Message message) {

    }

    @Override
    public void deleteSmsOnSim(int i, Message message) {

    }

    @Override
    public void deleteSmsOnRuim(int i, Message message) {

    }

    @Override
    public void writeSmsToSim(int i, String s, String s1, Message message) {

    }

    @Override
    public void writeSmsToRuim(int i, String s, Message message) {

    }

    @Override
    public void setRadioPower(boolean b, Message message) {

    }

    @Override
    public void acknowledgeLastIncomingGsmSms(boolean b, int i, Message message) {

    }

    @Override
    public void acknowledgeLastIncomingCdmaSms(boolean b, int i, Message message) {

    }

    @Override
    public void acknowledgeIncomingGsmSmsWithPdu(boolean b, String s, Message message) {

    }

    @Override
    public void iccIO(int i, int i1, String s, int i2, int i3, int i4, String s1, String s2, Message message) {

    }

    @Override
    public void iccIOForApp(int i, int i1, String s, int i2, int i3, int i4, String s1, String s2, String s3, Message message) {

    }

    @Override
    public void queryCLIP(Message message) {

    }

    @Override
    public void getCLIR(Message message) {

    }

    @Override
    public void setCLIR(int i, Message message) {

    }

    @Override
    public void queryCallWaiting(int i, Message message) {

    }

    @Override
    public void setCallWaiting(boolean b, int i, Message message) {

    }

    @Override
    public void setCallForward(int i, int i1, int i2, String s, int i3, Message message) {

    }

    @Override
    public void queryCallForwardStatus(int i, int i1, String s, Message message) {

    }

    @Override
    public void setNetworkSelectionModeAutomatic(Message message) {

    }

    @Override
    public void setNetworkSelectionModeManual(String s, Message message) {

    }

    @Override
    public void getNetworkSelectionMode(Message message) {

    }

    @Override
    public void getAvailableNetworks(Message message) {

    }

    @Override
    public void startNetworkScan(NetworkScanRequest networkScanRequest, Message message) {

    }

    @Override
    public void stopNetworkScan(Message message) {

    }

    @Override
    public void getBasebandVersion(Message message) {

    }

    @Override
    public void queryFacilityLock(String s, String s1, int i, Message message) {

    }

    @Override
    public void queryFacilityLockForApp(String s, String s1, int i, String s2, Message message) {

    }

    @Override
    public void setFacilityLock(String s, boolean b, String s1, int i, Message message) {

    }

    @Override
    public void setFacilityLockForApp(String s, boolean b, String s1, int i, String s2, Message message) {

    }

    @Override
    public void sendUSSD(String s, Message message) {

    }

    @Override
    public void cancelPendingUssd(Message message) {

    }

    @Override
    public void resetRadio(Message message) {

    }

    @Override
    public void setBandMode(int i, Message message) {

    }

    @Override
    public void queryAvailableBandMode(Message message) {

    }

    @Override
    public void setPreferredNetworkType(int i, Message message) {

    }

    @Override
    public void getPreferredNetworkType(Message message) {

    }

    @Override
    public void setLocationUpdates(boolean b, Message message) {

    }

    @Override
    public void getSmscAddress(Message message) {

    }

    @Override
    public void setSmscAddress(String s, Message message) {

    }

    @Override
    public void reportSmsMemoryStatus(boolean b, Message message) {

    }

    @Override
    public void reportStkServiceIsRunning(Message message) {

    }

    @Override
    public void invokeOemRilRequestRaw(byte[] bytes, Message message) {

    }

    @Override
    public void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo imsiEncryptionInfo, Message message) {

    }

    @Override
    public void invokeOemRilRequestStrings(String[] strings, Message message) {

    }

    @Override
    public void sendTerminalResponse(String s, Message message) {

    }

    @Override
    public void sendEnvelope(String s, Message message) {

    }

    @Override
    public void sendEnvelopeWithStatus(String s, Message message) {

    }

    @Override
    public void handleCallSetupRequestFromSim(boolean b, Message message) {

    }

    @Override
    public void setGsmBroadcastActivation(boolean b, Message message) {

    }

    @Override
    public void setGsmBroadcastConfig(SmsBroadcastConfigInfo[] smsBroadcastConfigInfos, Message message) {

    }

    @Override
    public void getGsmBroadcastConfig(Message message) {

    }

    @Override
    public void getDeviceIdentity(Message message) {

    }

    @Override
    public void getCDMASubscription(Message message) {

    }

    @Override
    public void sendCDMAFeatureCode(String s, Message message) {

    }

    @Override
    public void setPhoneType(int i) {

    }

    @Override
    public void queryCdmaRoamingPreference(Message message) {

    }

    @Override
    public void setCdmaRoamingPreference(int i, Message message) {

    }

    @Override
    public void setCdmaSubscriptionSource(int i, Message message) {

    }

    @Override
    public void getCdmaSubscriptionSource(Message message) {

    }

    @Override
    public void setTTYMode(int i, Message message) {

    }

    @Override
    public void queryTTYMode(Message message) {

    }

    @Override
    public void setupDataCall(int i, DataProfile dataProfile, boolean b, boolean b1, int i1, LinkProperties linkProperties, Message message) {

    }

    @Override
    public void deactivateDataCall(int i, int i1, Message message) {

    }

    @Override
    public void setCdmaBroadcastActivation(boolean b, Message message) {

    }

    @Override
    public void setCdmaBroadcastConfig(CdmaSmsBroadcastConfigInfo[] cdmaSmsBroadcastConfigInfos, Message message) {

    }

    @Override
    public void getCdmaBroadcastConfig(Message message) {

    }

    @Override
    public void exitEmergencyCallbackMode(Message message) {

    }

    @Override
    public void getIccCardStatus(Message message) {

    }

    @Override
    public void getIccSlotsStatus(Message message) {

    }

    @Override
    public void setLogicalToPhysicalSlotMapping(int[] ints, Message message) {

    }

    @Override
    public void requestIccSimAuthentication(int i, String s, String s1, Message message) {

    }

    @Override
    public void getVoiceRadioTechnology(Message message) {

    }

    @Override
    public void setInitialAttachApn(DataProfile dataProfile, boolean b, Message message) {

    }

    @Override
    public void setDataProfile(DataProfile[] dataProfiles, boolean b, Message message) {

    }

    @Override
    public void iccOpenLogicalChannel(String s, int i, Message message) {

    }

    @Override
    public void iccCloseLogicalChannel(int i, Message message) {

    }

    @Override
    public void iccTransmitApduLogicalChannel(int i, int i1, int i2, int i3, int i4, int i5, String s, Message message) {

    }

    @Override
    public void iccTransmitApduBasicChannel(int i, int i1, int i2, int i3, int i4, String s, Message message) {

    }

    @Override
    public void nvReadItem(int i, Message message) {

    }

    @Override
    public void nvWriteItem(int i, String s, Message message) {

    }

    @Override
    public void nvWriteCdmaPrl(byte[] bytes, Message message) {

    }

    @Override
    public void nvResetConfig(int i, Message message) {

    }

    @Override
    public void getHardwareConfig(Message message) {

    }

    @Override
    public void getModemActivityInfo(Message message) {

    }

    @Override
    public void setAllowedCarriers(List<CarrierIdentifier> list, Message message) {

    }

    @Override
    public void getAllowedCarriers(Message message) {

    }

    @Override
    public void sendDeviceState(int i, boolean b, Message message) {

    }

    @Override
    public void setUnsolResponseFilter(int i, Message message) {

    }

    @Override
    public void setSignalStrengthReportingCriteria(int i, int i1, int[] ints, int i2, Message message) {

    }

    @Override
    public void setLinkCapacityReportingCriteria(int i, int i1, int i2, int[] ints, int[] ints1, int i3, Message message) {

    }

    @Override
    public void setSimCardPower(int i, Message message) {

    }

    @Override
    public void startNattKeepalive(int i, KeepalivePacketData keepalivePacketData, int i1, Message message) {

    }

    @Override
    public void stopNattKeepalive(int i, Message message) {

    }
}
