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
      ImsCallProfiles class was not part of android repos before Android 9 version (los16)
      I found it in the huawei ims but especially also in the msm8996 ims
      https://github.com/bcyj/android_tools_leeco_msm8996/blob/master/telephony-apps/ims/src/org/codeaurora/ims/CallDetails.java
      https://github.com/SivanLiu/HwFrameWorkSource/blob/master/Mate20_9_0_0/src/main/java/android/telephony/ims/ImsCallProfile.java
      https://github.com/LineageOS/android_frameworks_base/blob/lineage-16.0/telephony/java/android/telephony/ims/ImsCallProfile.java

      It seems that this is part of a set of classes published by codeaurora

      This classe use ImsServiceState
 */

package com.huawei.ims;

import java.util.Arrays;
import java.util.Map;
import vendor.huawei.hardware.radio.ims.V1_0.RILImsCallDetails;



public class HwImsCallDetails {

    /*
     * Type of the call based on the media type and the direction of the media.
     */

    public static final int CALL_DOMAIN_UNKNOWN = 11; /*
     * Phone.CALL_DOMAIN_UNKNOWN
     * ; Unknown domain. Sent
     * by RIL when modem has
     * not yet selected a
     * domain for a call
     */

    public static final int CALL_DOMAIN_CS = 1; /*
     * Phone.CALL_DOMAIN_CS; Circuit
     * switched domain
     */
    public static final int CALL_DOMAIN_PS = 2; /*
     * Phone.CALL_DOMAIN_PS; Packet
     * switched domain
     */
    public static final int CALL_DOMAIN_AUTOMATIC = 3; /*
     * Phone.
     * CALL_DOMAIN_AUTOMATIC;
     * Automatic domain. Sent
     * by Android to indicate
     * that the domain for a
     * new call should be
     * selected by modem
     */
    public static final int CALL_DOMAIN_NOT_SET = 4; /*
     * Phone.CALL_DOMAIN_NOT_SET
     * ; Init value used
     * internally by telephony
     * until domain is set
     */

    public static final int CALL_RESTRICT_CAUSE_NONE = 0; /*
     * Default cause, not
     * restricted
     */
    public static final int CALL_RESTRICT_CAUSE_RAT = 1; /*
     * Service not
     * supported by RAT
     */
    public static final int CALL_RESTRICT_CAUSE_DISABLED = 2; /*
     * Service
     * disabled
     */

    public static final int CALL_TYPE_VOICE = 0; /*
     * Phone.CALL_TYPE_VOICE /*
     * Voice call-audio in both
     * directions
     */

    public static final int CALL_TYPE_VT_TX = 1; /*
     * Phone.CALL_TYPE_VT_TX; PS
     * Video telephony call: one
     * way TX video, two way audio
     */

    public static final int CALL_TYPE_VT_RX = 2; /*
     * Phone.CALL_TYPE_VT_RX Video
     * telephony call: one way RX
     * video, two way audio
     */

    public static final int CALL_TYPE_VT = 3; /*
     * Phone.CALL_TYPE_VT; Video
     * telephony call: two way video,
     * two way audio
     */

    public static final int CALL_TYPE_VT_NODIR = 4; /*
     * Phone.CALL_TYPE_VT_NODIR;
     * Video telephony call: no
     * direction, two way audio,
     * intermediate state in a
     * video call till video
     * link is setup
     */

    public static final int CALL_TYPE_SMS = 5; /*
     * Phone.CALL_TYPE_SMS;SMS Type
     */

    public static final int CALL_TYPE_VT_PAUSE = 6; /*
     * Indicates that video is paused;
     * This is an internal call type.
     * The type is used by TeleService and
     * InCallUI only. See CALL_TYPE_VT_RESUME
     */

    public static final int CALL_TYPE_VT_RESUME = 7; /*
     * This is an internal call
     * type. VT_RESUME call
     * type is used to send
     * unpause request to
     * TeleService.
     */

    public static final int CALL_TYPE_UNKNOWN = 10; /*
     * Phone.CALL_TYPE_UNKNOWN;
     * Unknown Call type, may be
     * used for answering call
     * with same call type as
     * incoming call. This is
     * only for telephony, not
     * meant to be passed to RIL
     */


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


    public int call_type;
    public int call_domain;
    public int callsubstate = 0;
    public int callMediaId = MEDIA_ID_UNKNOWN;
    public String[] extras;
    private int mVideoPauseState = VIDEO_PAUSE_STATE_RESUMED;

    public HwImsServiceState[] localAbility;
    public HwImsServiceState[] peerAbility;


    public HwImsCallDetails() {
        this.call_type = CALL_TYPE_UNKNOWN;
        this.call_domain = CALL_DOMAIN_NOT_SET;
        this.extras = null;
    }

    public HwImsCallDetails(int callType, int callDomain, String[] extraparams) {
        call_type = callType;
        call_domain = callDomain;
        extras = extraparams;
    }


    public HwImsCallDetails(HwImsCallDetails imsCallProfiles) {
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

    public HwImsCallDetails(RILImsCallDetails rILImsCallDetails) {
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

    /**
     * Convenience method, returns true if media id is valid, false otherwise.
     */
    public boolean hasMediaIdValid() {
        return callMediaId != MEDIA_ID_UNKNOWN && callMediaId >= 0;
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

    public void setVideoPauseState(int videoPauseState) {
        switch (videoPauseState) {
            case VIDEO_PAUSE_STATE_RESUMED:
            case VIDEO_PAUSE_STATE_PAUSED:
                mVideoPauseState = videoPauseState;
        }
    }

    /**
     * @return string representation.
     */
    @Override
    public String toString() {
        HwImsServiceState[] imsServiceStateArr;
        HwImsServiceState.StatusForAccessTech[] statusForAccessTechArr;
        HwImsServiceState[] imsServiceStateArr2;
        HwImsServiceState.StatusForAccessTech[] statusForAccessTechArr2;
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
            for (HwImsServiceState srv : this.localAbility) {
                if (srv != null) {
                    buf.append("isValid = ");
                    buf.append(srv.isValid);
                    buf.append(" type = ");
                    buf.append(srv.type);
                    buf.append(" state = ");
                    buf.append(srv.state);
                    if (srv.accessTechStatus != null) {
                        for (HwImsServiceState.StatusForAccessTech at : srv.accessTechStatus) {
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
            for (HwImsServiceState srv2 : this.peerAbility) {
                if (srv2 != null) {
                    buf2.append("isValid = ");
                    buf2.append(srv2.isValid);
                    buf2.append(" type = ");
                    buf2.append(srv2.type);
                    buf2.append(" state = ");
                    buf2.append(srv2.state);
                    if (srv2.accessTechStatus != null) {
                        for (HwImsServiceState.StatusForAccessTech at2 : srv2.accessTechStatus) {
                            buf2.append(" accTechStatus ");
                            buf2.append(at2);
                        }
                    }
                }
            }
            peerSrvAbility = buf2.toString();
        }
        return " " + this.call_type
                + " " + this.call_domain
                + " " + extrasResult
                + " callSubState "
                + this.callsubstate
                + " videoPauseState"
                + this.mVideoPauseState
                + " mediaId" + this.callMediaId
                + " Local Ability " + localSrvAbility
                + " Peer Ability " + peerSrvAbility;
    }

    public boolean update(HwImsCallDetails imsCallProfiles) {
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
