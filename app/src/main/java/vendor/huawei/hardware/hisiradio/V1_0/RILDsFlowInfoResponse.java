package vendor.huawei.hardware.hisiradio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
public final class RILDsFlowInfoResponse {
    public String lastDsTime = new String();
    public String lastTxFlow = new String();
    public String lastRxFlow = new String();
    public String totalDsTime = new String();
    public String totalTxFlow = new String();
    public String totalRxFlow = new String();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != RILDsFlowInfoResponse.class) {
            return false;
        }
        RILDsFlowInfoResponse other = (RILDsFlowInfoResponse) otherObject;
        if (HidlSupport.deepEquals(this.lastDsTime, other.lastDsTime) && HidlSupport.deepEquals(this.lastTxFlow, other.lastTxFlow) && HidlSupport.deepEquals(this.lastRxFlow, other.lastRxFlow) && HidlSupport.deepEquals(this.totalDsTime, other.totalDsTime) && HidlSupport.deepEquals(this.totalTxFlow, other.totalTxFlow) && HidlSupport.deepEquals(this.totalRxFlow, other.totalRxFlow)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.lastDsTime)), Integer.valueOf(HidlSupport.deepHashCode(this.lastTxFlow)), Integer.valueOf(HidlSupport.deepHashCode(this.lastRxFlow)), Integer.valueOf(HidlSupport.deepHashCode(this.totalDsTime)), Integer.valueOf(HidlSupport.deepHashCode(this.totalTxFlow)), Integer.valueOf(HidlSupport.deepHashCode(this.totalRxFlow)));
    }

    public final String toString() {
        return "{.lastDsTime = " + this.lastDsTime + ", .lastTxFlow = " + this.lastTxFlow + ", .lastRxFlow = " + this.lastRxFlow + ", .totalDsTime = " + this.totalDsTime + ", .totalTxFlow = " + this.totalTxFlow + ", .totalRxFlow = " + this.totalRxFlow + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(96L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<RILDsFlowInfoResponse> readVectorFromParcel(HwParcel parcel) {
        ArrayList<RILDsFlowInfoResponse> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 96, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            RILDsFlowInfoResponse _hidl_vec_element = new RILDsFlowInfoResponse();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 96);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.lastDsTime = _hidl_blob.getString(_hidl_offset + 0);
        parcel.readEmbeddedBuffer(this.lastDsTime.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 0 + 0, false);
        this.lastTxFlow = _hidl_blob.getString(_hidl_offset + 16);
        parcel.readEmbeddedBuffer(this.lastTxFlow.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 16 + 0, false);
        this.lastRxFlow = _hidl_blob.getString(_hidl_offset + 32);
        parcel.readEmbeddedBuffer(this.lastRxFlow.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 32 + 0, false);
        this.totalDsTime = _hidl_blob.getString(_hidl_offset + 48);
        parcel.readEmbeddedBuffer(this.totalDsTime.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 48 + 0, false);
        this.totalTxFlow = _hidl_blob.getString(_hidl_offset + 64);
        parcel.readEmbeddedBuffer(this.totalTxFlow.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 64 + 0, false);
        this.totalRxFlow = _hidl_blob.getString(_hidl_offset + 80);
        parcel.readEmbeddedBuffer(this.totalRxFlow.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 80 + 0, false);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(96);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<RILDsFlowInfoResponse> _hidl_vec) {
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
        _hidl_blob.putString(0 + _hidl_offset, this.lastDsTime);
        _hidl_blob.putString(16 + _hidl_offset, this.lastTxFlow);
        _hidl_blob.putString(32 + _hidl_offset, this.lastRxFlow);
        _hidl_blob.putString(48 + _hidl_offset, this.totalDsTime);
        _hidl_blob.putString(64 + _hidl_offset, this.totalTxFlow);
        _hidl_blob.putString(80 + _hidl_offset, this.totalRxFlow);
    }
}
