package com.huawei.ims;

import java.util.Arrays;
import java.util.Map;
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCallDetails;

/* loaded from: ImsCallProfiles.class */
public class ImsCallProfiles {
    public static final int CALL_DOMAIN_AUTOMATIC = 3;
    public static final int CALL_DOMAIN_CS = 1;
    public static final int CALL_DOMAIN_NOT_SET = 4;
    public static final int CALL_DOMAIN_PS = 2;
    public static final int CALL_DOMAIN_UNKNOWN = 11;
    public static final int CALL_RESTRICT_CAUSE_DISABLED = 2;
    public static final int CALL_RESTRICT_CAUSE_NONE = 0;
    public static final int CALL_RESTRICT_CAUSE_RAT = 1;
    public static final int CALL_TYPE_SMS = 5;
    public static final int CALL_TYPE_UNKNOWN = 10;
    public static final int CALL_TYPE_VOICE = 0;
    public static final int CALL_TYPE_VT = 3;
    public static final int CALL_TYPE_VT_NODIR = 4;
    public static final int CALL_TYPE_VT_PAUSE = 6;
    public static final int CALL_TYPE_VT_RESUME = 7;
    public static final int CALL_TYPE_VT_RX = 2;
    public static final int CALL_TYPE_VT_TX = 1;
    public static final int CALL_TYPE_VT_UPGRADE_CANCEL = 8;
    public static final String EXTRAS_CODEC = "Codec";
    public static final String EXTRAS_HANDOVER_INFORMATION = "handoverInfo";
    public static final String EXTRAS_IS_CONFERENCE_URI = "isConferenceUri";
    public static final String EXTRAS_PARENT_CALL_ID = "parentCallId";
    private static final String EXTRA_KEY_BACKUP_NUMBER = "nexti_backup_number";
    private static final String EXTRA_KEY_SEARCH_NUMBER = "nexti_search_number";
    public static final int EXTRA_TYPE_LTE_TO_IWLAN_HO_FAIL = 1;
    public static final int MEDIA_ID_UNKNOWN = -1;
    private static final String NUMBERMARKINFO_NUMBER = "numbermarkinfo_number";
    public static final int VIDEO_PAUSE_STATE_PAUSED = 1;
    public static final int VIDEO_PAUSE_STATE_RESUMED = 2;
    public static final String NULL_STRING_VALUE = "";
    public int callMediaId;
    public int call_domain;
    public int call_type;
    public int callsubstate;
    public String[] extras;
    public ImsServiceState[] localAbility;
    private int mVideoPauseState;
    public ImsServiceState[] peerAbility;

    public ImsCallProfiles() {
        this.callsubstate = 0;
        this.callMediaId = -1;
        this.mVideoPauseState = 2;
        this.call_type = 10;
        this.call_domain = 4;
        this.extras = null;
    }

    public ImsCallProfiles(int i, int i2, String[] strArr) {
        this.callsubstate = 0;
        this.callMediaId = -1;
        this.mVideoPauseState = 2;
        this.call_type = i;
        this.call_domain = i2;
        this.extras = strArr;
    }

    public ImsCallProfiles(ImsCallProfiles imsCallProfiles) {
        this.callsubstate = 0;
        this.callMediaId = -1;
        this.mVideoPauseState = 2;
        if (imsCallProfiles != null) {
            this.call_type = imsCallProfiles.call_type;
            this.call_domain = imsCallProfiles.call_domain;
            this.callsubstate = imsCallProfiles.callsubstate;
            this.callMediaId = imsCallProfiles.callMediaId;
            this.extras = imsCallProfiles.extras;
            this.localAbility = imsCallProfiles.localAbility;
            this.peerAbility = imsCallProfiles.peerAbility;
        }
    }

    public ImsCallProfiles(RILImsCallDetails rILImsCallDetails) {
        this.callsubstate = 0;
        this.callMediaId = -1;
        this.mVideoPauseState = 2;
        if (rILImsCallDetails != null) {
            this.call_type = rILImsCallDetails.callType;
            this.call_domain = rILImsCallDetails.callDomain;
        }
    }

    private void addExtra(String str) {
        if (this.extras != null) {
            this.extras = (String[]) Arrays.copyOf(this.extras, this.extras.length + 1);
            this.extras[this.extras.length - 1] = str;
        }
    }

    public static String[] getExtrasFromMap(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        String[] strArr = new String[map.size()];
        for (Map.Entry<String, String> entry : map.entrySet()) {
            strArr[0] = NULL_STRING_VALUE + entry.getKey() + "=" + entry.getValue();
        }
        return strArr;
    }

    public String getValueForKeyFromExtras(String[] strArr, String str) {
        for (int i = 0; strArr != null && i < strArr.length; i++) {
            if (strArr[i] != null) {
                String[] split = strArr[i].split("=");
                if (split.length == 2 && split[0].equals(str)) {
                    return split[1];
                }
            }
        }
        return null;
    }

    public int getVideoPauseState() {
        return this.mVideoPauseState;
    }

    public boolean hasMediaIdValid() {
        return this.callMediaId != -1 && this.callMediaId >= 0;
    }

    public void setExtras(String[] strArr) {
        this.extras = strArr;
    }

    public void setExtrasFromMap(Map<String, String> map) {
        this.extras = getExtrasFromMap(map);
    }

    public String[] setValueForKeyInExtras(String[] strArr, String str, String str2) {
        if (strArr != null) {
            for (int i = 0; i < strArr.length; i++) {
                if (strArr[i] != null) {
                    String[] split = strArr[i].split("=");
                    if (split.length == 2 && split[0].equals(str)) {
                        split[1] = str2;
                    }
                }
            }
        }
        return strArr;
    }

    public void setVideoPauseState(int i) {
        switch (i) {
            case 1:
            case 2:
                this.mVideoPauseState = i;
                return;
            default:
                return;
        }
    }


    public java.lang.String toString() {
            ImsServiceState[] imsServiceStateArr;
            ImsServiceState.StatusForAccessTech[] statusForAccessTechArr;
            ImsServiceState[] imsServiceStateArr2;
            ImsServiceState.StatusForAccessTech[] statusForAccessTechArr2;
            String[] strArr;
            String extrasResult = NULL_STRING_VALUE;
            String localSrvAbility = NULL_STRING_VALUE;
            String peerSrvAbility = NULL_STRING_VALUE;
            StringBuffer stringBuffer = new StringBuffer();

            if (this.extras != null) {
                for (String s : this.extras) {
                    if (s != null) {
                        if (s.contains(NUMBERMARKINFO_NUMBER) || s.contains(EXTRA_KEY_BACKUP_NUMBER) || s.contains(EXTRA_KEY_SEARCH_NUMBER)) {
                            String before = s.substring(0, s.indexOf("=") + 1);
                            String after = s.substring(s.indexOf("=") + 1, s.length());
                            s = before + HiddenPrivacyInfo.putMosaic(after, 0);
                        }
                        stringBuffer.append(s);
                    }
                }
                extrasResult = stringBuffer.toString();
            }
            if (this.localAbility != null) {
                StringBuffer buf = new StringBuffer(NULL_STRING_VALUE);
                for (ImsServiceState srv : this.localAbility) {
                    if (srv != null) {
                        buf.append("isValid = ");
                        buf.append(srv.isValid);
                        buf.append(" type = ");
                        buf.append(srv.type);
                        buf.append(" state = ");
                        buf.append(srv.state);
                        if (srv.accessTechStatus != null) {
                            for (ImsServiceState.StatusForAccessTech at : srv.accessTechStatus) {
                                buf.append(" accTechStatus ");
                                buf.append(at);
                            }
                        }
                    }
                }
                localSrvAbility = buf.toString();
            }
            if (this.peerAbility != null) {
                StringBuffer buf2 = new StringBuffer(NULL_STRING_VALUE);
                for (ImsServiceState srv2 : this.peerAbility) {
                    if (srv2 != null) {
                        buf2.append("isValid = ");
                        buf2.append(srv2.isValid);
                        buf2.append(" type = ");
                        buf2.append(srv2.type);
                        buf2.append(" state = ");
                        buf2.append(srv2.state);
                        if (srv2.accessTechStatus != null) {
                            for (ImsServiceState.StatusForAccessTech at2 : srv2.accessTechStatus) {
                                buf2.append(" accTechStatus ");
                                buf2.append(at2);
                            }
                        }
                    }
                }
                peerSrvAbility = buf2.toString();
            }
            return " " + this.call_type + " " + this.call_domain + " " + extrasResult + " callSubState " + this.callsubstate + " videoPauseState" + this.mVideoPauseState + " mediaId" + this.callMediaId + " Local Ability " + localSrvAbility + " Peer Ability " + peerSrvAbility;
    }

    public boolean update(ImsCallProfiles imsCallProfiles) {
        boolean z;
        boolean z2 = false;
        if (imsCallProfiles == null) {
            return false;
        }
        if (this.call_type != imsCallProfiles.call_type) {
            this.call_type = imsCallProfiles.call_type;
            z2 = true;
        }
        if (this.call_domain != imsCallProfiles.call_domain) {
            this.call_domain = imsCallProfiles.call_domain;
            z2 = true;
        }
        if (this.callsubstate != imsCallProfiles.callsubstate) {
            this.callsubstate = imsCallProfiles.callsubstate;
            z2 = true;
        }
        this.localAbility = imsCallProfiles.localAbility;
        this.peerAbility = imsCallProfiles.peerAbility;
        int i = 0;
        while (true) {
            z = z2;
            if (imsCallProfiles.extras == null || i >= imsCallProfiles.extras.length) {
                break;
            }
            String[] split = imsCallProfiles.extras[i].split("=");
            z2 = z;
            if (split.length == 2) {
                String valueForKeyFromExtras = getValueForKeyFromExtras(this.extras, split[0]);
                if (valueForKeyFromExtras != null) {
                    z2 = z;
                    if (!valueForKeyFromExtras.equals(split[1])) {
                        this.extras = setValueForKeyInExtras(this.extras, split[0], split[1]);
                        z2 = true;
                    }
                } else {
                    z2 = true;
                    addExtra(imsCallProfiles.extras[i]);
                }
            }
            i++;
        }
        setVideoPauseState(imsCallProfiles.getVideoPauseState());
        return z;
    }
}
