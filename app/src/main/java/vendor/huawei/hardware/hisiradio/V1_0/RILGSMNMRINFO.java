package vendor.huawei.hardware.hisiradio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
public final class RILGSMNMRINFO {
    public int arfcn;
    public int bsic;
    public int rxlev;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != RILGSMNMRINFO.class) {
            return false;
        }
        RILGSMNMRINFO other = (RILGSMNMRINFO) otherObject;
        if (this.arfcn == other.arfcn && this.bsic == other.bsic && this.rxlev == other.rxlev) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.arfcn))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.bsic))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.rxlev))));
    }

    public final String toString() {
        return "{.arfcn = " + this.arfcn + ", .bsic = " + this.bsic + ", .rxlev = " + this.rxlev + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(12L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<RILGSMNMRINFO> readVectorFromParcel(HwParcel parcel) {
        ArrayList<RILGSMNMRINFO> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 12, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            RILGSMNMRINFO _hidl_vec_element = new RILGSMNMRINFO();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 12);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.arfcn = _hidl_blob.getInt32(0 + _hidl_offset);
        this.bsic = _hidl_blob.getInt32(4 + _hidl_offset);
        this.rxlev = _hidl_blob.getInt32(8 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(12);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<RILGSMNMRINFO> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 12);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 12);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.arfcn);
        _hidl_blob.putInt32(4 + _hidl_offset, this.bsic);
        _hidl_blob.putInt32(8 + _hidl_offset, this.rxlev);
    }
}
