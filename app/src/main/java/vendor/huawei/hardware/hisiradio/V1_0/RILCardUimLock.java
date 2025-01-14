package vendor.huawei.hardware.hisiradio.V1_0;

import java.util.ArrayList;

/* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
public final class RILCardUimLock {
    public static final int RIL_CARD_UIM_LOCKED = 2;
    public static final int RIL_CARD_UIM_UNKNOWN_LOCK = 0;
    public static final int RIL_CARD_UIM_UNLOCKED = 1;

    public static final String toString(int o) {
        if (o == 0) {
            return "RIL_CARD_UIM_UNKNOWN_LOCK";
        }
        if (o == 1) {
            return "RIL_CARD_UIM_UNLOCKED";
        }
        if (o == 2) {
            return "RIL_CARD_UIM_LOCKED";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("RIL_CARD_UIM_UNKNOWN_LOCK");
        if ((o & 1) == 1) {
            list.add("RIL_CARD_UIM_UNLOCKED");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("RIL_CARD_UIM_LOCKED");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
