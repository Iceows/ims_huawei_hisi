package com.huawei.ims;

import android.telephony.PhoneNumberUtils;
import android.telephony.ims.ImsReasonInfo;
import com.android.internal.telephony.ATParseEx;
import com.android.internal.telephony.DriverCall;
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCall;
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCallEx;
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCallV1_2;

public class DriverImsCall extends DriverCall {
    static final String LOG_TAG = "DRIVERCALL-IMS";
    public static final String SEPARATOR_TAG = ",";
    public ImsReasonInfo callFailCause;
    public ImsCallProfiles imsCallProfile;
    public int peerVideoSupport;
    public int radioTechFromRilImsCall;
    public String redirectNumber;
    public int redirectNumberPresentation;
    public int redirectNumberToa;
    public State state;

    public enum State {
        ACTIVE,
        HOLDING,
        DIALING,
        ALERTING,
        INCOMING,
        WAITING,
        END
    }

    public DriverImsCall() {
        this.radioTechFromRilImsCall = -1;
        this.redirectNumber = null;
        this.redirectNumberToa = 0;
        this.redirectNumberPresentation = 1;
        this.imsCallProfile = new ImsCallProfiles();
    }

    public DriverImsCall(DriverImsCall driverImsCall) {
        this.radioTechFromRilImsCall = -1;
        this.redirectNumber = null;
        this.redirectNumberToa = 0;
        this.redirectNumberPresentation = 1;
        this.imsCallProfile = new ImsCallProfiles(driverImsCall.imsCallProfile);
        this.callFailCause = new ImsReasonInfo();
        this.state = driverImsCall.state;
        this.index = driverImsCall.index;
        this.number = driverImsCall.number;
        this.isMT = driverImsCall.isMT;
        this.TOA = driverImsCall.TOA;
        this.isMpty = driverImsCall.isMpty;
        this.als = driverImsCall.als;
        this.isVoice = driverImsCall.isVoice;
        this.isVoicePrivacy = driverImsCall.isVoicePrivacy;
        this.numberPresentation = driverImsCall.numberPresentation;
        this.name = driverImsCall.name;
        this.namePresentation = driverImsCall.namePresentation;
        this.peerVideoSupport = driverImsCall.peerVideoSupport;
        this.radioTechFromRilImsCall = driverImsCall.radioTechFromRilImsCall;
        this.redirectNumberToa = driverImsCall.redirectNumberToa;
        this.redirectNumber = driverImsCall.redirectNumber;
        this.redirectNumberPresentation = driverImsCall.redirectNumberPresentation;
    }

    public DriverImsCall(RILImsCall rILImsCall) {
        this.radioTechFromRilImsCall = -1;
        this.redirectNumber = null;
        this.redirectNumberToa = 0;
        this.redirectNumberPresentation = 1;
        if (rILImsCall != null) {
            this.imsCallProfile = new ImsCallProfiles(rILImsCall.callDetails);
            this.callFailCause = new ImsReasonInfo();
            this.state = stateFromCall(rILImsCall.state);
            this.index = rILImsCall.index;
            this.TOA = rILImsCall.toa;
            this.number = PhoneNumberUtils.stringFromStringAndTOA(rILImsCall.number, rILImsCall.toa);
            this.isMT = rILImsCall.isMT != 0;
            this.isMpty = rILImsCall.isMpty != 0;
            this.als = rILImsCall.als;
            this.isVoice = rILImsCall.isVoice != 0;
            this.isVoicePrivacy = rILImsCall.isVoicePrivacy != 0;
            this.numberPresentation = presentationFromCLIP(rILImsCall.numberPresentation);
            this.name = rILImsCall.name;
            this.namePresentation = presentationFromCLIP(rILImsCall.namePresentation);
            this.peerVideoSupport = rILImsCall.peerVideoSupport;
        }
    }

    public DriverImsCall(RILImsCallEx rILImsCallEx) {
        this.radioTechFromRilImsCall = -1;
        this.redirectNumber = null;
        this.redirectNumberToa = 0;
        this.redirectNumberPresentation = 1;
        if (rILImsCallEx != null) {
            this.imsCallProfile = new ImsCallProfiles(rILImsCallEx.callDetails);
            this.callFailCause = new ImsReasonInfo();
            this.state = stateFromCall(rILImsCallEx.state);
            this.index = rILImsCallEx.index;
            this.TOA = rILImsCallEx.toa;
            this.number = PhoneNumberUtils.stringFromStringAndTOA(rILImsCallEx.number, rILImsCallEx.toa);
            this.isMT = rILImsCallEx.isMT != 0;
            this.isMpty = rILImsCallEx.isMpty != 0;
            this.als = rILImsCallEx.als;
            this.isVoice = rILImsCallEx.isVoice != 0;
            this.isVoicePrivacy = rILImsCallEx.isVoicePrivacy != 0;
            this.numberPresentation = presentationFromCLIP(rILImsCallEx.numberPresentation);
            this.name = rILImsCallEx.name;
            this.namePresentation = presentationFromCLIP(rILImsCallEx.namePresentation);
            this.peerVideoSupport = rILImsCallEx.peerVideoSupport;
            this.radioTechFromRilImsCall = rILImsCallEx.imsDomain;
        }
    }

    public DriverImsCall(RILImsCallV1_2 rILImsCallV1_2) {
        this.radioTechFromRilImsCall = -1;
        this.redirectNumber = null;
        this.redirectNumberToa = 0;
        this.redirectNumberPresentation = 1;
        if (rILImsCallV1_2 != null) {
            this.imsCallProfile = new ImsCallProfiles(rILImsCallV1_2.callDetails);
            this.callFailCause = new ImsReasonInfo();
            this.state = stateFromCall(rILImsCallV1_2.state);
            this.index = rILImsCallV1_2.index;
            this.TOA = rILImsCallV1_2.toa;
            this.number = PhoneNumberUtils.stringFromStringAndTOA(rILImsCallV1_2.number, rILImsCallV1_2.toa);
            this.isMT = rILImsCallV1_2.isMT != 0;
            this.isMpty = rILImsCallV1_2.isMpty != 0;
            this.als = rILImsCallV1_2.als;
            this.isVoice = rILImsCallV1_2.isVoice != 0;
            this.isVoicePrivacy = rILImsCallV1_2.isVoicePrivacy != 0;
            this.numberPresentation = presentationFromCLIP(rILImsCallV1_2.numberPresentation);
            this.name = rILImsCallV1_2.name;
            this.namePresentation = presentationFromCLIP(rILImsCallV1_2.namePresentation);
            this.peerVideoSupport = rILImsCallV1_2.peerVideoSupport;
            this.radioTechFromRilImsCall = rILImsCallV1_2.imsDomain;
            this.redirectNumberToa = rILImsCallV1_2.redirectNumberToa;
            this.redirectNumber = PhoneNumberUtils.stringFromStringAndTOA(rILImsCallV1_2.redirectNumber, rILImsCallV1_2.redirectNumberToa);
            this.redirectNumberPresentation = presentationFromCLIP(rILImsCallV1_2.redirectNumberPresentation);
        }
    }

    private boolean isChanged(DriverImsCall update) {
        boolean hasChanged = false;
        State state = this.state;
        State state2 = update.state;
        if (state != state2) {
            this.state = state2;
            hasChanged = true;
        }
        if (this.index != update.index) {
            this.index = update.index;
            hasChanged = true;
        }
        if ((this.number == null && update.number != null) || (this.number != null && !this.number.equals(update.number))) {
            this.number = update.number;
            hasChanged = true;
        }
        if (this.isMT != update.isMT) {
            this.isMT = update.isMT;
            hasChanged = true;
        }
        if (this.isMpty != update.isMpty) {
            this.isMpty = update.isMpty;
            hasChanged = true;
        }
        int i = this.radioTechFromRilImsCall;
        int i2 = update.radioTechFromRilImsCall;
        if (i != i2) {
            this.radioTechFromRilImsCall = i2;
            return true;
        }
        return hasChanged;
    }

    public static State stateFromCall(int i) throws ATParseEx {
        switch (i) {
            case 0:
                return State.ACTIVE;
            case 1:
                return State.HOLDING;
            case 2:
                return State.DIALING;
            case 3:
                return State.ALERTING;
            case 4:
                return State.INCOMING;
            case 5:
                return State.WAITING;
            case 6:
                return State.END;
            default:
                throw new ATParseEx("illegal call state " + i);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=");
        sb.append(this.index);
        sb.append(this.SEPARATOR_TAG);
        sb.append(this.state);
        sb.append(",toa=");
        sb.append(this.TOA);
        sb.append(this.SEPARATOR_TAG);
        sb.append(this.isMpty ? "conf" : "norm");
        sb.append(this.SEPARATOR_TAG);
        sb.append(this.isMT ? "mt" : "mo");
        sb.append(this.SEPARATOR_TAG);
        sb.append(this.als);
        sb.append(this.SEPARATOR_TAG);
        sb.append(this.isVoice ? "voc" : "nonvoc");
        sb.append(this.SEPARATOR_TAG);
        sb.append(this.isVoicePrivacy ? "evp" : "noevp");
        sb.append(",peerVideoSupport=");
        sb.append(this.peerVideoSupport);
        sb.append(",radioTechFromRilImsCall=");
        sb.append(this.radioTechFromRilImsCall);
        sb.append(",,cli=");
        sb.append(this.numberPresentation);
        sb.append(",,");
        sb.append(this.namePresentation);
        sb.append("Ims Call Profile =");
        sb.append(this.imsCallProfile);
        sb.append(",redirectNumber = ");
        sb.append(HiddenPrivacyInfo.putMosaic(this.redirectNumber, 0));
        sb.append(",redirectNumberToa = ");
        sb.append(this.redirectNumberToa);
        sb.append(",redirectNumberPresentation");
        sb.append(this.redirectNumberPresentation);
        return sb.toString();
    }

    public boolean update(DriverImsCall driverImsCall) {
        if (driverImsCall == null) {
            return false;
        }
        boolean isChanged = isChanged(driverImsCall);
        if (driverImsCall.callFailCause != null) {
            if (this.callFailCause == null) {
                this.callFailCause = new ImsReasonInfo(driverImsCall.callFailCause.mCode, driverImsCall.callFailCause.mExtraCode, driverImsCall.callFailCause.mExtraMessage);
            } else {
                if (this.callFailCause.mCode != driverImsCall.callFailCause.mCode) {
                    this.callFailCause.mCode = driverImsCall.callFailCause.mCode;
                }
                if (this.callFailCause.mExtraCode != driverImsCall.callFailCause.mExtraCode) {
                    this.callFailCause.mExtraCode = driverImsCall.callFailCause.mExtraCode;
                }
                this.callFailCause.mExtraMessage = driverImsCall.callFailCause.mExtraMessage;
            }
        }
        boolean z = isChanged;
        if (this.imsCallProfile.update(driverImsCall.imsCallProfile)) {
            z = isChanged;
            if (!isChanged) {
                z = true;
            }
        }
        return z;
    }
}
