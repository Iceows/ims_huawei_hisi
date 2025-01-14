package vendor.huawei.hardware.hisiradio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
public final class RILVsimContentResponse {
    public int cardCap;
    public int cardType;
    public int index;
    public String imsi = new String();
    public String hplmn = new String();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != RILVsimContentResponse.class) {
            return false;
        }
        RILVsimContentResponse other = (RILVsimContentResponse) otherObject;
        if (this.index == other.index && this.cardCap == other.cardCap && this.cardType == other.cardType && HidlSupport.deepEquals(this.imsi, other.imsi) && HidlSupport.deepEquals(this.hplmn, other.hplmn)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.index))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.cardCap))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.cardType))), Integer.valueOf(HidlSupport.deepHashCode(this.imsi)), Integer.valueOf(HidlSupport.deepHashCode(this.hplmn)));
    }

    public final String toString() {
        return "{.index = " + this.index + ", .cardCap = " + this.cardCap + ", .cardType = " + this.cardType + ", .imsi = " + this.imsi + ", .hplmn = " + this.hplmn + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(48L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<RILVsimContentResponse> readVectorFromParcel(HwParcel parcel) {
        ArrayList<RILVsimContentResponse> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 48, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            RILVsimContentResponse _hidl_vec_element = new RILVsimContentResponse();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 48);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.index = _hidl_blob.getInt32(_hidl_offset + 0);
        this.cardCap = _hidl_blob.getInt32(_hidl_offset + 4);
        this.cardType = _hidl_blob.getInt32(_hidl_offset + 8);
        this.imsi = _hidl_blob.getString(_hidl_offset + 16);
        parcel.readEmbeddedBuffer(this.imsi.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 16 + 0, false);
        this.hplmn = _hidl_blob.getString(_hidl_offset + 32);
        parcel.readEmbeddedBuffer(this.hplmn.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 32 + 0, false);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(48);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<RILVsimContentResponse> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 48);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 48);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.index);
        _hidl_blob.putInt32(4 + _hidl_offset, this.cardCap);
        _hidl_blob.putInt32(8 + _hidl_offset, this.cardType);
        _hidl_blob.putString(16 + _hidl_offset, this.imsi);
        _hidl_blob.putString(32 + _hidl_offset, this.hplmn);
    }
}
