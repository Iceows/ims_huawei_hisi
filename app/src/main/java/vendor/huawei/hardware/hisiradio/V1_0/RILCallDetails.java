package vendor.huawei.hardware.hisiradio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
public final class RILCallDetails {
    public int callDomain;
    public int callType;
    public byte extras;
    public int extrasLength;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != RILCallDetails.class) {
            return false;
        }
        RILCallDetails other = (RILCallDetails) otherObject;
        if (this.callType == other.callType && this.callDomain == other.callDomain && this.extrasLength == other.extrasLength && this.extras == other.extras) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.callType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.callDomain))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.extrasLength))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.extras))));
    }

    public final String toString() {
        return "{.callType = " + RILCallType.toString(this.callType) + ", .callDomain = " + RILCallDomain.toString(this.callDomain) + ", .extrasLength = " + this.extrasLength + ", .extras = " + ((int) this.extras) + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(16L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<RILCallDetails> readVectorFromParcel(HwParcel parcel) {
        ArrayList<RILCallDetails> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 16, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            RILCallDetails _hidl_vec_element = new RILCallDetails();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 16);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.callType = _hidl_blob.getInt32(0 + _hidl_offset);
        this.callDomain = _hidl_blob.getInt32(4 + _hidl_offset);
        this.extrasLength = _hidl_blob.getInt32(8 + _hidl_offset);
        this.extras = _hidl_blob.getInt8(12 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(16);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<RILCallDetails> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 16);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 16);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.callType);
        _hidl_blob.putInt32(4 + _hidl_offset, this.callDomain);
        _hidl_blob.putInt32(8 + _hidl_offset, this.extrasLength);
        _hidl_blob.putInt8(12 + _hidl_offset, this.extras);
    }
}
