package vendor.huawei.hardware.hisiradio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
public final class RILDataProfileResponse {
    public int authType;
    public int cid;
    public String protocol = new String();
    public String apn = new String();
    public String user = new String();
    public String passwd = new String();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != RILDataProfileResponse.class) {
            return false;
        }
        RILDataProfileResponse other = (RILDataProfileResponse) otherObject;
        if (HidlSupport.deepEquals(this.protocol, other.protocol) && HidlSupport.deepEquals(this.apn, other.apn) && this.authType == other.authType && HidlSupport.deepEquals(this.user, other.user) && HidlSupport.deepEquals(this.passwd, other.passwd) && this.cid == other.cid) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.protocol)), Integer.valueOf(HidlSupport.deepHashCode(this.apn)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.authType))), Integer.valueOf(HidlSupport.deepHashCode(this.user)), Integer.valueOf(HidlSupport.deepHashCode(this.passwd)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.cid))));
    }

    public final String toString() {
        return "{.protocol = " + this.protocol + ", .apn = " + this.apn + ", .authType = " + this.authType + ", .user = " + this.user + ", .passwd = " + this.passwd + ", .cid = " + this.cid + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(80L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<RILDataProfileResponse> readVectorFromParcel(HwParcel parcel) {
        ArrayList<RILDataProfileResponse> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 80, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            RILDataProfileResponse _hidl_vec_element = new RILDataProfileResponse();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 80);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.protocol = _hidl_blob.getString(_hidl_offset + 0);
        parcel.readEmbeddedBuffer(this.protocol.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 0 + 0, false);
        this.apn = _hidl_blob.getString(_hidl_offset + 16);
        parcel.readEmbeddedBuffer(this.apn.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 16 + 0, false);
        this.authType = _hidl_blob.getInt32(_hidl_offset + 32);
        this.user = _hidl_blob.getString(_hidl_offset + 40);
        parcel.readEmbeddedBuffer(this.user.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 40 + 0, false);
        this.passwd = _hidl_blob.getString(_hidl_offset + 56);
        parcel.readEmbeddedBuffer(this.passwd.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 56 + 0, false);
        this.cid = _hidl_blob.getInt32(_hidl_offset + 72);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(80);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<RILDataProfileResponse> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 80);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 80);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putString(0 + _hidl_offset, this.protocol);
        _hidl_blob.putString(16 + _hidl_offset, this.apn);
        _hidl_blob.putInt32(32 + _hidl_offset, this.authType);
        _hidl_blob.putString(40 + _hidl_offset, this.user);
        _hidl_blob.putString(56 + _hidl_offset, this.passwd);
        _hidl_blob.putInt32(72 + _hidl_offset, this.cid);
    }
}
