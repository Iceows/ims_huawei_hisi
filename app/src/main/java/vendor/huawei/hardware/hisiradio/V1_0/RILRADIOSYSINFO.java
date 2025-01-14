package vendor.huawei.hardware.hisiradio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
public final class RILRADIOSYSINFO {
    public int lockState;
    public int roamStatus;
    public int simState;
    public int srvDomain;
    public int srvStatus;
    public int sysMode;
    public int sysSubmode;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != RILRADIOSYSINFO.class) {
            return false;
        }
        RILRADIOSYSINFO other = (RILRADIOSYSINFO) otherObject;
        if (this.sysSubmode == other.sysSubmode && this.srvStatus == other.srvStatus && this.srvDomain == other.srvDomain && this.roamStatus == other.roamStatus && this.sysMode == other.sysMode && this.simState == other.simState && this.lockState == other.lockState) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.sysSubmode))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.srvStatus))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.srvDomain))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.roamStatus))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.sysMode))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.simState))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.lockState))));
    }

    public final String toString() {
        return "{.sysSubmode = " + this.sysSubmode + ", .srvStatus = " + this.srvStatus + ", .srvDomain = " + this.srvDomain + ", .roamStatus = " + this.roamStatus + ", .sysMode = " + this.sysMode + ", .simState = " + this.simState + ", .lockState = " + this.lockState + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(28L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<RILRADIOSYSINFO> readVectorFromParcel(HwParcel parcel) {
        ArrayList<RILRADIOSYSINFO> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 28, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            RILRADIOSYSINFO _hidl_vec_element = new RILRADIOSYSINFO();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 28);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.sysSubmode = _hidl_blob.getInt32(0 + _hidl_offset);
        this.srvStatus = _hidl_blob.getInt32(4 + _hidl_offset);
        this.srvDomain = _hidl_blob.getInt32(8 + _hidl_offset);
        this.roamStatus = _hidl_blob.getInt32(12 + _hidl_offset);
        this.sysMode = _hidl_blob.getInt32(16 + _hidl_offset);
        this.simState = _hidl_blob.getInt32(20 + _hidl_offset);
        this.lockState = _hidl_blob.getInt32(24 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(28);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<RILRADIOSYSINFO> _hidl_vec) {
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
        _hidl_blob.putInt32(0 + _hidl_offset, this.sysSubmode);
        _hidl_blob.putInt32(4 + _hidl_offset, this.srvStatus);
        _hidl_blob.putInt32(8 + _hidl_offset, this.srvDomain);
        _hidl_blob.putInt32(12 + _hidl_offset, this.roamStatus);
        _hidl_blob.putInt32(16 + _hidl_offset, this.sysMode);
        _hidl_blob.putInt32(20 + _hidl_offset, this.simState);
        _hidl_blob.putInt32(24 + _hidl_offset, this.lockState);
    }
}
