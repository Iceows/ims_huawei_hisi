package vendor.huawei.hardware.hisiradio.V1_1;

import android.hardware.radio.V1_0.CdmaSignalStrength;
import android.hardware.radio.V1_0.EvdoSignalStrength;
import android.hardware.radio.V1_0.LteSignalStrength;
import android.hardware.radio.V1_2.WcdmaSignalStrength;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;
import vendor.huawei.hardware.hisiradio.V1_0.GsmSignalStrength;

/* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
public final class HwSignalStrength_1_1 {
    public int lteband;
    public final GsmSignalStrength gsm = new GsmSignalStrength();
    public final WcdmaSignalStrength wcdma = new WcdmaSignalStrength();
    public final CdmaSignalStrength cdma = new CdmaSignalStrength();
    public final EvdoSignalStrength evdo = new EvdoSignalStrength();
    public final LteSignalStrength lte = new LteSignalStrength();
    public final NrSignalStrength nr = new NrSignalStrength();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != HwSignalStrength_1_1.class) {
            return false;
        }
        HwSignalStrength_1_1 other = (HwSignalStrength_1_1) otherObject;
        if (HidlSupport.deepEquals(this.gsm, other.gsm) && HidlSupport.deepEquals(this.wcdma, other.wcdma) && HidlSupport.deepEquals(this.cdma, other.cdma) && HidlSupport.deepEquals(this.evdo, other.evdo) && HidlSupport.deepEquals(this.lte, other.lte) && this.lteband == other.lteband && HidlSupport.deepEquals(this.nr, other.nr)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.gsm)), Integer.valueOf(HidlSupport.deepHashCode(this.wcdma)), Integer.valueOf(HidlSupport.deepHashCode(this.cdma)), Integer.valueOf(HidlSupport.deepHashCode(this.evdo)), Integer.valueOf(HidlSupport.deepHashCode(this.lte)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.lteband))), Integer.valueOf(HidlSupport.deepHashCode(this.nr)));
    }

    public final String toString() {
        return "{.gsm = " + this.gsm + ", .wcdma = " + this.wcdma + ", .cdma = " + this.cdma + ", .evdo = " + this.evdo + ", .lte = " + this.lte + ", .lteband = " + this.lteband + ", .nr = " + this.nr + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(96L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<HwSignalStrength_1_1> readVectorFromParcel(HwParcel parcel) {
        ArrayList<HwSignalStrength_1_1> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 96, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            HwSignalStrength_1_1 _hidl_vec_element = new HwSignalStrength_1_1();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 96);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.gsm.readEmbeddedFromParcel(parcel, _hidl_blob, 0 + _hidl_offset);
        this.wcdma.readEmbeddedFromParcel(parcel, _hidl_blob, 12 + _hidl_offset);
        this.cdma.readEmbeddedFromParcel(parcel, _hidl_blob, 28 + _hidl_offset);
        this.evdo.readEmbeddedFromParcel(parcel, _hidl_blob, 36 + _hidl_offset);
        this.lte.readEmbeddedFromParcel(parcel, _hidl_blob, 48 + _hidl_offset);
        this.lteband = _hidl_blob.getInt32(72 + _hidl_offset);
        this.nr.readEmbeddedFromParcel(parcel, _hidl_blob, 76 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(96);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<HwSignalStrength_1_1> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 96);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 96);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        this.gsm.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
        this.wcdma.writeEmbeddedToBlob(_hidl_blob, 12 + _hidl_offset);
        this.cdma.writeEmbeddedToBlob(_hidl_blob, 28 + _hidl_offset);
        this.evdo.writeEmbeddedToBlob(_hidl_blob, 36 + _hidl_offset);
        this.lte.writeEmbeddedToBlob(_hidl_blob, 48 + _hidl_offset);
        _hidl_blob.putInt32(72 + _hidl_offset, this.lteband);
        this.nr.writeEmbeddedToBlob(_hidl_blob, 76 + _hidl_offset);
    }
}
