package vendor.huawei.hardware.hisiradio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
public final class HwCall_V1_2 {
    public final Call call = new Call();
    public String redirectNumber = new String();
    public int redirectNumberPresentation;
    public int redirectNumberToa;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != HwCall_V1_2.class) {
            return false;
        }
        HwCall_V1_2 other = (HwCall_V1_2) otherObject;
        if (HidlSupport.deepEquals(this.call, other.call) && HidlSupport.deepEquals(this.redirectNumber, other.redirectNumber) && this.redirectNumberToa == other.redirectNumberToa && this.redirectNumberPresentation == other.redirectNumberPresentation) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.call)), Integer.valueOf(HidlSupport.deepHashCode(this.redirectNumber)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.redirectNumberToa))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.redirectNumberPresentation))));
    }

    public final String toString() {
        return "{.call = " + this.call + ", .redirectNumber = " + this.redirectNumber + ", .redirectNumberToa = " + this.redirectNumberToa + ", .redirectNumberPresentation = " + CallPresentation.toString(this.redirectNumberPresentation) + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(112L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<HwCall_V1_2> readVectorFromParcel(HwParcel parcel) {
        ArrayList<HwCall_V1_2> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 112, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            HwCall_V1_2 _hidl_vec_element = new HwCall_V1_2();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 112);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.call.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
        this.redirectNumber = _hidl_blob.getString(_hidl_offset + 88);
        parcel.readEmbeddedBuffer(this.redirectNumber.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 88 + 0, false);
        this.redirectNumberToa = _hidl_blob.getInt32(_hidl_offset + 104);
        this.redirectNumberPresentation = _hidl_blob.getInt32(_hidl_offset + 108);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(112);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<HwCall_V1_2> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 112);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 112);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        this.call.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
        _hidl_blob.putString(88 + _hidl_offset, this.redirectNumber);
        _hidl_blob.putInt32(104 + _hidl_offset, this.redirectNumberToa);
        _hidl_blob.putInt32(108 + _hidl_offset, this.redirectNumberPresentation);
    }
}
