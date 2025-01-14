package vendor.huawei.hardware.radio.V2_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
public final class RILPreferredPLMNSelector {
    public int fmt;
    public int gsmAcT;
    public int gsmCompactAcT;
    public int idx;
    public int lteACT;
    public int plmn;
    public int utranAcT;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != RILPreferredPLMNSelector.class) {
            return false;
        }
        RILPreferredPLMNSelector other = (RILPreferredPLMNSelector) otherObject;
        if (this.idx == other.idx && this.fmt == other.fmt && this.plmn == other.plmn && this.gsmAcT == other.gsmAcT && this.gsmCompactAcT == other.gsmCompactAcT && this.utranAcT == other.utranAcT && this.lteACT == other.lteACT) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.idx))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.fmt))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.plmn))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.gsmAcT))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.gsmCompactAcT))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.utranAcT))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.lteACT))));
    }

    public final String toString() {
        return "{.idx = " + this.idx + ", .fmt = " + this.fmt + ", .plmn = " + this.plmn + ", .gsmAcT = " + this.gsmAcT + ", .gsmCompactAcT = " + this.gsmCompactAcT + ", .utranAcT = " + this.utranAcT + ", .lteACT = " + this.lteACT + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(28L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<RILPreferredPLMNSelector> readVectorFromParcel(HwParcel parcel) {
        ArrayList<RILPreferredPLMNSelector> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 28, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            RILPreferredPLMNSelector _hidl_vec_element = new RILPreferredPLMNSelector();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 28);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.idx = _hidl_blob.getInt32(0 + _hidl_offset);
        this.fmt = _hidl_blob.getInt32(4 + _hidl_offset);
        this.plmn = _hidl_blob.getInt32(8 + _hidl_offset);
        this.gsmAcT = _hidl_blob.getInt32(12 + _hidl_offset);
        this.gsmCompactAcT = _hidl_blob.getInt32(16 + _hidl_offset);
        this.utranAcT = _hidl_blob.getInt32(20 + _hidl_offset);
        this.lteACT = _hidl_blob.getInt32(24 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(28);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<RILPreferredPLMNSelector> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 28);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 28);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.idx);
        _hidl_blob.putInt32(4 + _hidl_offset, this.fmt);
        _hidl_blob.putInt32(8 + _hidl_offset, this.plmn);
        _hidl_blob.putInt32(12 + _hidl_offset, this.gsmAcT);
        _hidl_blob.putInt32(16 + _hidl_offset, this.gsmCompactAcT);
        _hidl_blob.putInt32(20 + _hidl_offset, this.utranAcT);
        _hidl_blob.putInt32(24 + _hidl_offset, this.lteACT);
    }
}
