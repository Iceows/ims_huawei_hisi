package vendor.huawei.hardware.hisiradio.V1_1;

import android.hidl.base.V1_0.DebugInfo;
import android.hidl.base.V1_0.IBase;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.RemoteException;
import com.android.internal.telephony.uicc.AnswerToReset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import vendor.huawei.hardware.hisiradio.V1_0.CsgNetworkInfo;
import vendor.huawei.hardware.hisiradio.V1_0.RILUICCAUTH;

/* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
public interface IHisiRadio extends vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio {
    public static final String kInterfaceName = "vendor.huawei.hardware.hisiradio@1.1::IHisiRadio";

    @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase, android.os.IHwInterface
    IHwBinder asBinder();

    void getAttachedApnSettings(int i) throws RemoteException;

    @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
    ArrayList<byte[]> getHashChain() throws RemoteException;

    @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
    String interfaceDescriptor() throws RemoteException;

    @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws RemoteException;

    @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
    void ping() throws RemoteException;

    @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws RemoteException;

    void setHwPreferredNetworkType_1_1(int i, int i2) throws RemoteException;

    void setTemperatureControl(int i, int i2, int i3) throws RemoteException;

    void setUlfreqEnable(int i, int i2) throws RemoteException;

    @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static IHisiRadio asInterface(IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof IHisiRadio)) {
            return (IHisiRadio) iface;
        }
        IHisiRadio proxy = new Proxy(binder);
        try {
            Iterator<String> it = proxy.interfaceChain().iterator();
            while (it.hasNext()) {
                String descriptor = it.next();
                if (descriptor.equals(kInterfaceName)) {
                    return proxy;
                }
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    static IHisiRadio castFrom(IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static IHisiRadio getService(String serviceName, boolean retry) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static IHisiRadio getService(boolean retry) throws RemoteException {
        return getService("default", retry);
    }

    static IHisiRadio getService(String serviceName) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName));
    }

    static IHisiRadio getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
    public static final class Proxy implements IHisiRadio {
        private IHwBinder mRemote;

        public Proxy(IHwBinder remote) {
            this.mRemote = (IHwBinder) Objects.requireNonNull(remote);
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase, android.os.IHwInterface
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException e) {
                return "[class or subclass of vendor.huawei.hardware.hisiradio@1.1::IHisiRadio]@Proxy";
            }
        }

        public final boolean equals(Object other) {
            return HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setResponseFunctionsHuawei(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadioResponse radioResponse, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadioIndication radioIndication) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeStrongBinder(radioResponse == null ? null : radioResponse.asBinder());
            _hidl_request.writeStrongBinder(radioIndication != null ? radioIndication.asBinder() : null);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void responseAcknowledgement() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getSimHotplugState(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getCardType(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getIccid(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setSimSlot(int serial, int modem1ToSlot, int modem2ToSlot, int modem3ToSlot) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(modem1ToSlot);
            _hidl_request.writeInt32(modem2ToSlot);
            _hidl_request.writeInt32(modem3ToSlot);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getSimSlot(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setActiveModemMode(int serial, int mode) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(mode);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setVoicecallBackGroundState(int serial, int state) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(state);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getVoicecallBackGroundState(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getVoicePreferStatus(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(11, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void rejectCallWithReason(int serial, int callIndex, int cause) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(callIndex);
            _hidl_request.writeInt32(cause);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(12, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getChannelInfo(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(13, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setLteReleaseVersion(int serial, int version) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(version);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(14, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getLteReleaseVersion(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(15, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setImsDomain(int serial, int domain) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(domain);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(16, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void vowifiToImsaMsg(int serial, ArrayList<Byte> msg) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt8Vector(msg);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(17, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void uiccAuth(int serial, RILUICCAUTH uiccAuthParam) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            uiccAuthParam.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(18, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void clearDsFlowInfo(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(19, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getLocationInfo(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(20, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setNetworkRatAndSrvDomain(int serial, int at_rat, int srv_domain) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(at_rat);
            _hidl_request.writeInt32(srv_domain);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(21, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setVsimSimState(int serial, int index, int enable, int slot) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(index);
            _hidl_request.writeInt32(enable);
            _hidl_request.writeInt32(slot);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(22, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getVsimSimState(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(23, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getSystemInfoEx(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(24, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getPlmnInfo(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(25, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setIsmcoex(int serial, String p_lte_freq) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeString(p_lte_freq);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(26, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getRatCombinePrio(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(27, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getDsFlowInfo(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(28, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getDeviceVersion(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(29, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setSciChgCfg(int serial, int slot1, int slot2, int slot3) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(slot1);
            _hidl_request.writeInt32(slot2);
            _hidl_request.writeInt32(slot3);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(30, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void vsimCheckCard(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(31, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setDsFlowNvWriteConfigPara(int serial, int index1, int index2) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(index1);
            _hidl_request.writeInt32(index2);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(32, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setUeOperationMode(int serial, int index1) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(index1);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(33, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void vsimPower(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(34, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void queryServiceCellBand(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(35, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setTime(int serial, String date, String strtime, String zone) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeString(date);
            _hidl_request.writeString(strtime);
            _hidl_request.writeString(zone);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(36, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void vsimBasebandVersion(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(37, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getLwclash(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(38, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setNickName(int serial, String nickName) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeString(nickName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(39, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getNickName(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(40, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void imsRegister(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(41, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setApDsFlowReportConfig(int serial, int enable_rpt, int threshold, int total_threshold, int oper) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(enable_rpt);
            _hidl_request.writeInt32(threshold);
            _hidl_request.writeInt32(total_threshold);
            _hidl_request.writeInt32(oper);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(42, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void notifyCellularCommParaReady(int serial, int paratype, int pathtype) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(paratype);
            _hidl_request.writeInt32(pathtype);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(43, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void sendPseudocellCellInfo(int serial, int infoType, int lac, int cid, int radiotech, String plmn) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(infoType);
            _hidl_request.writeInt32(lac);
            _hidl_request.writeInt32(cid);
            _hidl_request.writeInt32(radiotech);
            _hidl_request.writeString(plmn);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(44, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void notifyCellularCloudParaReady(int serial, int paratype, int pathtype) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(paratype);
            _hidl_request.writeInt32(pathtype);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(45, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getAvailableCsgIds(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(46, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void manualSelectionCsgId(int serial, CsgNetworkInfo csgInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            csgInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(47, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void sendLaaCmd(int serial, int cmd, String reserved) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(cmd);
            _hidl_request.writeString(reserved);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(48, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getLaaDetailedState(int serial, String reserved) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeString(reserved);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(49, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setupDataCallEmergency(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(50, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void deactivateDataCallEmergency(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(51, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getCellInfoListOtdoa(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(52, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setDmRcsConfig(int serial, int rcsCapability, int devConfig) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(rcsCapability);
            _hidl_request.writeInt32(devConfig);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(53, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setRcsSwitch(int serial, int switchState) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(switchState);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(54, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getRcsSwitchState(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(55, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void informModemTetherStatusToChangeGRO(int serial, int tetherConnectStatus, String rmnetName) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(tetherConnectStatus);
            _hidl_request.writeString(rmnetName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(56, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setMobileDataEnable(int serial, int status) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(status);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(57, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setRoamingDataEnable(int serial, int status) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(status);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(58, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getCurrentCallsV1_2(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(59, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getAvailableCsgIds_1_1(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(60, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setReattachRequiredUnsol(int serial, boolean onOff) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeBool(onOff);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(61, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setDeepNoDisturbState(int serial, int status) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(status);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(62, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getNumOfRecPseBaseStation(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(63, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getImsDomain(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(64, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void setDmPcscf(int serial, String pcscf) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeString(pcscf);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(65, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void sendSimChgTypeInfo(int serial, int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(66, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getCardTrayInfo(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(67, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getNvcfgMatchedResult(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(68, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
        public void getCapOfRecPseBaseStation(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(69, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio
        public void getAttachedApnSettings(int serial) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(70, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio
        public void setUlfreqEnable(int serial, int mode) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(mode);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(71, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio
        public void setHwPreferredNetworkType_1_1(int serial, int nwType) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(nwType);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(72, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio
        public void setTemperatureControl(int serial, int status, int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IHisiRadio.kInterfaceName);
            _hidl_request.writeInt32(serial);
            _hidl_request.writeInt32(status);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(73, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public ArrayList<String> interfaceChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256067662, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                ArrayList<String> _hidl_out_descriptors = _hidl_reply.readStringVector();
                return _hidl_out_descriptors;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public String interfaceDescriptor() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256136003, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                String _hidl_out_descriptor = _hidl_reply.readString();
                return _hidl_out_descriptor;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public ArrayList<byte[]> getHashChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                int _hidl_index_0 = 0;
                this.mRemote.transact(256398152, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                ArrayList<byte[]> _hidl_out_hashchain = new ArrayList<>();
                HwBlob _hidl_blob = _hidl_reply.readBuffer(16L);
                int _hidl_vec_size = _hidl_blob.getInt32(8L);
                HwBlob childBlob = _hidl_reply.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), 0L, true);
                _hidl_out_hashchain.clear();
                while (true) {
                    int _hidl_index_02 = _hidl_index_0;
                    if (_hidl_index_02 >= _hidl_vec_size) {
                        return _hidl_out_hashchain;
                    }
                    byte[] _hidl_vec_element = new byte[32];
                    long _hidl_array_offset_1 = _hidl_index_02 * 32;
                    childBlob.copyToInt8Array(_hidl_array_offset_1, _hidl_vec_element, 32);
                    _hidl_out_hashchain.add(_hidl_vec_element);
                    _hidl_index_0 = _hidl_index_02 + 1;
                }
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public void setHALInstrumentation() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256462420, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) throws RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public void ping() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256921159, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public DebugInfo getDebugInfo() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257049926, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                DebugInfo _hidl_out_info = new DebugInfo();
                _hidl_out_info.readFromParcel(_hidl_reply);
                return _hidl_out_info;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public void notifySyspropsChanged() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257120595, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    /* loaded from: C:\GitHub\iceows\ims_hi6250_volte\telephony-common\telephony-common-jar\classes.dex */
    public static abstract class Stub extends HwBinder implements IHisiRadio {
        private int _hidl_index_0;

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase, android.os.IHwInterface
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IHisiRadio.kInterfaceName, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName, IBase.kInterfaceName));
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public final String interfaceDescriptor() {
            return IHisiRadio.kInterfaceName;
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{-77, 61, 38, -78, 42, 68, 56, 108, -97, -65, -56, -71, 82, AnswerToReset.DIRECT_CONVENTION, -63, -112, -106, -83, -78, -95, -39, -49, 42, 94, 4, -98, 42, -101, -24, 31, 15, 99}, new byte[]{38, -77, -84, AnswerToReset.EUICC_SUPPORTED, -5, -32, 72, -48, -104, 95, -21, 81, -122, -104, -77, 43, -19, -124, -92, -83, -82, 44, -4, -18, 83, -54, 26, -14, 27, 27, 103, -25}, new byte[]{-67, -38, -74, 24, 77, 122, 52, 109, -90, -96, 125, -64, AnswerToReset.EUICC_SUPPORTED, -116, -15, -102, 105, 111, 76, -86, 54, 17, -59, 31, 46, 20, 86, 90, 20, -76, 15, -39}));
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.os.IHwBinder, android.hardware.radio.V1_0.IRadio, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public final DebugInfo getDebugInfo() {
            DebugInfo info = new DebugInfo();
            info.pid = HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // vendor.huawei.hardware.hisiradio.V1_1.IHisiRadio, vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        @Override // android.os.IHwBinder, android.hardware.radio.V1_0.IRadio, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) {
            return true;
        }

        @Override // android.os.IHwBinder
        public IHwInterface queryLocalInterface(String descriptor) {
            if (IHisiRadio.kInterfaceName.equals(descriptor)) {
                return this;
            }
            return null;
        }

        public void registerAsService(String serviceName) throws RemoteException {
            registerService(serviceName);
        }

        public String toString() {
            return interfaceDescriptor() + "@Stub";
        }

        @Override // android.os.HwBinder
        public void onTransact(int _hidl_code, HwParcel _hidl_request, HwParcel _hidl_reply, int _hidl_flags) throws RemoteException {
            boolean _hidl_is_oneway;
            switch (_hidl_code) {
                case 1:
                    _hidl_is_oneway = (_hidl_flags & 1) != 0;
                    if (_hidl_is_oneway) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    vendor.huawei.hardware.hisiradio.V1_0.IHisiRadioResponse radioResponse = vendor.huawei.hardware.hisiradio.V1_0.IHisiRadioResponse.asInterface(_hidl_request.readStrongBinder());
                    vendor.huawei.hardware.hisiradio.V1_0.IHisiRadioIndication radioIndication = vendor.huawei.hardware.hisiradio.V1_0.IHisiRadioIndication.asInterface(_hidl_request.readStrongBinder());
                    setResponseFunctionsHuawei(radioResponse, radioIndication);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 2:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        responseAcknowledgement();
                        return;
                    }
                case 3:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial = _hidl_request.readInt32();
                        getSimHotplugState(serial);
                        return;
                    }
                case 4:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial2 = _hidl_request.readInt32();
                        getCardType(serial2);
                        return;
                    }
                case 5:
                    int modem2ToSlot = _hidl_flags & 1;
                    _hidl_index_0 = modem2ToSlot != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial3 = _hidl_request.readInt32();
                        getIccid(serial3);
                        return;
                    }
                case 6:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial4 = _hidl_request.readInt32();
                    int modem1ToSlot = _hidl_request.readInt32();
                    int modem2ToSlot2 = _hidl_request.readInt32();
                    int modem3ToSlot = _hidl_request.readInt32();
                    setSimSlot(serial4, modem1ToSlot, modem2ToSlot2, modem3ToSlot);
                    return;
                case 7:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial5 = _hidl_request.readInt32();
                        getSimSlot(serial5);
                        return;
                    }
                case 8:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial6 = _hidl_request.readInt32();
                        int mode = _hidl_request.readInt32();
                        setActiveModemMode(serial6, mode);
                        return;
                    }
                case 9:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial7 = _hidl_request.readInt32();
                        int state = _hidl_request.readInt32();
                        setVoicecallBackGroundState(serial7, state);
                        return;
                    }
                case 10:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial8 = _hidl_request.readInt32();
                        getVoicecallBackGroundState(serial8);
                        return;
                    }
                case 11:
                    int cause = _hidl_flags & 1;
                    _hidl_index_0 = cause != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial9 = _hidl_request.readInt32();
                        getVoicePreferStatus(serial9);
                        return;
                    }
                case 12:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial10 = _hidl_request.readInt32();
                    int callIndex = _hidl_request.readInt32();
                    int cause2 = _hidl_request.readInt32();
                    rejectCallWithReason(serial10, callIndex, cause2);
                    return;
                case 13:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial11 = _hidl_request.readInt32();
                        getChannelInfo(serial11);
                        return;
                    }
                case 14:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial12 = _hidl_request.readInt32();
                        int version = _hidl_request.readInt32();
                        setLteReleaseVersion(serial12, version);
                        return;
                    }
                case 15:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial13 = _hidl_request.readInt32();
                        getLteReleaseVersion(serial13);
                        return;
                    }
                case 16:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial14 = _hidl_request.readInt32();
                        int domain = _hidl_request.readInt32();
                        setImsDomain(serial14, domain);
                        return;
                    }
                case 17:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial15 = _hidl_request.readInt32();
                        ArrayList<Byte> msg = _hidl_request.readInt8Vector();
                        vowifiToImsaMsg(serial15, msg);
                        return;
                    }
                case 18:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial16 = _hidl_request.readInt32();
                    RILUICCAUTH uiccAuthParam = new RILUICCAUTH();
                    uiccAuthParam.readFromParcel(_hidl_request);
                    uiccAuth(serial16, uiccAuthParam);
                    return;
                case 19:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial17 = _hidl_request.readInt32();
                        clearDsFlowInfo(serial17);
                        return;
                    }
                case 20:
                    int srv_domain = _hidl_flags & 1;
                    _hidl_index_0 = srv_domain != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial18 = _hidl_request.readInt32();
                        getLocationInfo(serial18);
                        return;
                    }
                case 21:
                    int enable = _hidl_flags & 1;
                    _hidl_index_0 = enable != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial19 = _hidl_request.readInt32();
                    int at_rat = _hidl_request.readInt32();
                    int srv_domain2 = _hidl_request.readInt32();
                    setNetworkRatAndSrvDomain(serial19, at_rat, srv_domain2);
                    return;
                case 22:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial20 = _hidl_request.readInt32();
                    int index = _hidl_request.readInt32();
                    int enable2 = _hidl_request.readInt32();
                    int slot = _hidl_request.readInt32();
                    setVsimSimState(serial20, index, enable2, slot);
                    return;
                case 23:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial21 = _hidl_request.readInt32();
                        getVsimSimState(serial21);
                        return;
                    }
                case 24:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial22 = _hidl_request.readInt32();
                        getSystemInfoEx(serial22);
                        return;
                    }
                case 25:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial23 = _hidl_request.readInt32();
                        getPlmnInfo(serial23);
                        return;
                    }
                case 26:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial24 = _hidl_request.readInt32();
                        String p_lte_freq = _hidl_request.readString();
                        setIsmcoex(serial24, p_lte_freq);
                        return;
                    }
                case 27:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial25 = _hidl_request.readInt32();
                        getRatCombinePrio(serial25);
                        return;
                    }
                case 28:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial26 = _hidl_request.readInt32();
                        getDsFlowInfo(serial26);
                        return;
                    }
                case 29:
                    int slot2 = _hidl_flags & 1;
                    _hidl_index_0 = slot2 != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial27 = _hidl_request.readInt32();
                        getDeviceVersion(serial27);
                        return;
                    }
                case 30:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial28 = _hidl_request.readInt32();
                    int slot1 = _hidl_request.readInt32();
                    int slot22 = _hidl_request.readInt32();
                    int slot3 = _hidl_request.readInt32();
                    setSciChgCfg(serial28, slot1, slot22, slot3);
                    return;
                case 31:
                    int index2 = _hidl_flags & 1;
                    _hidl_index_0 = index2 != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial29 = _hidl_request.readInt32();
                        vsimCheckCard(serial29);
                        return;
                    }
                case 32:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial30 = _hidl_request.readInt32();
                    int index1 = _hidl_request.readInt32();
                    int index22 = _hidl_request.readInt32();
                    setDsFlowNvWriteConfigPara(serial30, index1, index22);
                    return;
                case 33:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial31 = _hidl_request.readInt32();
                        int index12 = _hidl_request.readInt32();
                        setUeOperationMode(serial31, index12);
                        return;
                    }
                case 34:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial32 = _hidl_request.readInt32();
                        vsimPower(serial32);
                        return;
                    }
                case 35:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial33 = _hidl_request.readInt32();
                        queryServiceCellBand(serial33);
                        return;
                    }
                case 36:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial34 = _hidl_request.readInt32();
                    String date = _hidl_request.readString();
                    String strtime = _hidl_request.readString();
                    String zone = _hidl_request.readString();
                    setTime(serial34, date, strtime, zone);
                    return;
                case 37:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial35 = _hidl_request.readInt32();
                        vsimBasebandVersion(serial35);
                        return;
                    }
                case 38:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial36 = _hidl_request.readInt32();
                        getLwclash(serial36);
                        return;
                    }
                case 39:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial37 = _hidl_request.readInt32();
                        String nickName = _hidl_request.readString();
                        setNickName(serial37, nickName);
                        return;
                    }
                case 40:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial38 = _hidl_request.readInt32();
                        getNickName(serial38);
                        return;
                    }
                case 41:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial39 = _hidl_request.readInt32();
                        imsRegister(serial39);
                        return;
                    }
                case 42:
                    int pathtype = _hidl_flags & 1;
                    _hidl_index_0 = pathtype != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial40 = _hidl_request.readInt32();
                    int enable_rpt = _hidl_request.readInt32();
                    int threshold = _hidl_request.readInt32();
                    int total_threshold = _hidl_request.readInt32();
                    int oper = _hidl_request.readInt32();
                    setApDsFlowReportConfig(serial40, enable_rpt, threshold, total_threshold, oper);
                    return;
                case 43:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial41 = _hidl_request.readInt32();
                    int paratype = _hidl_request.readInt32();
                    int pathtype2 = _hidl_request.readInt32();
                    notifyCellularCommParaReady(serial41, paratype, pathtype2);
                    return;
                case 44:
                    int pathtype3 = _hidl_flags & 1;
                    _hidl_index_0 = pathtype3 != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial42 = _hidl_request.readInt32();
                    int infoType = _hidl_request.readInt32();
                    int lac = _hidl_request.readInt32();
                    int cid = _hidl_request.readInt32();
                    int radiotech = _hidl_request.readInt32();
                    String plmn = _hidl_request.readString();
                    sendPseudocellCellInfo(serial42, infoType, lac, cid, radiotech, plmn);
                    return;
                case 45:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial43 = _hidl_request.readInt32();
                    int paratype2 = _hidl_request.readInt32();
                    int pathtype4 = _hidl_request.readInt32();
                    notifyCellularCloudParaReady(serial43, paratype2, pathtype4);
                    return;
                case 46:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial44 = _hidl_request.readInt32();
                        getAvailableCsgIds(serial44);
                        return;
                    }
                case 47:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial45 = _hidl_request.readInt32();
                    CsgNetworkInfo csgInfo = new CsgNetworkInfo();
                    csgInfo.readFromParcel(_hidl_request);
                    manualSelectionCsgId(serial45, csgInfo);
                    return;
                case 48:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial46 = _hidl_request.readInt32();
                    int cmd = _hidl_request.readInt32();
                    String reserved = _hidl_request.readString();
                    sendLaaCmd(serial46, cmd, reserved);
                    return;
                case 49:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial47 = _hidl_request.readInt32();
                        String reserved2 = _hidl_request.readString();
                        getLaaDetailedState(serial47, reserved2);
                        return;
                    }
                case 50:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial48 = _hidl_request.readInt32();
                        setupDataCallEmergency(serial48);
                        return;
                    }
                case 51:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial49 = _hidl_request.readInt32();
                        deactivateDataCallEmergency(serial49);
                        return;
                    }
                case 52:
                    int devConfig = _hidl_flags & 1;
                    _hidl_index_0 = devConfig != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial50 = _hidl_request.readInt32();
                        getCellInfoListOtdoa(serial50);
                        return;
                    }
                case 53:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial51 = _hidl_request.readInt32();
                    int rcsCapability = _hidl_request.readInt32();
                    int devConfig2 = _hidl_request.readInt32();
                    setDmRcsConfig(serial51, rcsCapability, devConfig2);
                    return;
                case 54:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial52 = _hidl_request.readInt32();
                        int switchState = _hidl_request.readInt32();
                        setRcsSwitch(serial52, switchState);
                        return;
                    }
                case 55:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial53 = _hidl_request.readInt32();
                        getRcsSwitchState(serial53);
                        return;
                    }
                case 56:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                    int serial54 = _hidl_request.readInt32();
                    int tetherConnectStatus = _hidl_request.readInt32();
                    String rmnetName = _hidl_request.readString();
                    informModemTetherStatusToChangeGRO(serial54, tetherConnectStatus, rmnetName);
                    return;
                case 57:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial55 = _hidl_request.readInt32();
                        int status = _hidl_request.readInt32();
                        setMobileDataEnable(serial55, status);
                        return;
                    }
                case 58:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial56 = _hidl_request.readInt32();
                        int status2 = _hidl_request.readInt32();
                        setRoamingDataEnable(serial56, status2);
                        return;
                    }
                case 59:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial57 = _hidl_request.readInt32();
                        getCurrentCallsV1_2(serial57);
                        return;
                    }
                case 60:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial58 = _hidl_request.readInt32();
                        getAvailableCsgIds_1_1(serial58);
                        return;
                    }
                case 61:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial59 = _hidl_request.readInt32();
                        boolean onOff = _hidl_request.readBool();
                        setReattachRequiredUnsol(serial59, onOff);
                        return;
                    }
                case 62:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial60 = _hidl_request.readInt32();
                        int status3 = _hidl_request.readInt32();
                        setDeepNoDisturbState(serial60, status3);
                        return;
                    }
                case 63:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial61 = _hidl_request.readInt32();
                        getNumOfRecPseBaseStation(serial61);
                        return;
                    }
                case 64:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial62 = _hidl_request.readInt32();
                        getImsDomain(serial62);
                        return;
                    }
                case 65:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial63 = _hidl_request.readInt32();
                        String pcscf = _hidl_request.readString();
                        setDmPcscf(serial63, pcscf);
                        return;
                    }
                case 66:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial64 = _hidl_request.readInt32();
                        int type = _hidl_request.readInt32();
                        sendSimChgTypeInfo(serial64, type);
                        return;
                    }
                case 67:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial65 = _hidl_request.readInt32();
                        getCardTrayInfo(serial65);
                        return;
                    }
                case 68:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial66 = _hidl_request.readInt32();
                        getNvcfgMatchedResult(serial66);
                        return;
                    }
                case 69:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio.kInterfaceName);
                        int serial67 = _hidl_request.readInt32();
                        getCapOfRecPseBaseStation(serial67);
                        return;
                    }
                case 70:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(IHisiRadio.kInterfaceName);
                        int serial68 = _hidl_request.readInt32();
                        getAttachedApnSettings(serial68);
                        return;
                    }
                case 71:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(IHisiRadio.kInterfaceName);
                        int serial69 = _hidl_request.readInt32();
                        int mode2 = _hidl_request.readInt32();
                        setUlfreqEnable(serial69, mode2);
                        return;
                    }
                case 72:
                    int type2 = _hidl_flags & 1;
                    _hidl_index_0 = type2 != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    } else {
                        _hidl_request.enforceInterface(IHisiRadio.kInterfaceName);
                        int serial70 = _hidl_request.readInt32();
                        int nwType = _hidl_request.readInt32();
                        setHwPreferredNetworkType_1_1(serial70, nwType);
                        return;
                    }
                case 73:
                    _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                    if (_hidl_index_0 != 1) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(IHisiRadio.kInterfaceName);
                    int serial71 = _hidl_request.readInt32();
                    int status4 = _hidl_request.readInt32();
                    int type3 = _hidl_request.readInt32();
                    setTemperatureControl(serial71, status4, type3);
                    return;
                default:
                    switch (_hidl_code) {
                        case 256067662:
                            _hidl_is_oneway = (_hidl_flags & 1) != 0;
                            if (_hidl_is_oneway) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            ArrayList<String> _hidl_out_descriptors = interfaceChain();
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeStringVector(_hidl_out_descriptors);
                            _hidl_reply.send();
                            return;
                        case 256131655:
                            _hidl_is_oneway = (_hidl_flags & 1) != 0;
                            if (_hidl_is_oneway) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            } else {
                                _hidl_request.enforceInterface(IBase.kInterfaceName);
                                _hidl_reply.writeStatus(0);
                                _hidl_reply.send();
                                return;
                            }
                        case 256136003:
                            _hidl_is_oneway = (_hidl_flags & 1) != 0;
                            if (_hidl_is_oneway) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            String _hidl_out_descriptor = interfaceDescriptor();
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeString(_hidl_out_descriptor);
                            _hidl_reply.send();
                            return;
                        case 256398152:
                            _hidl_is_oneway = (_hidl_flags & 1) != 0;
                            if (_hidl_is_oneway) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            ArrayList<byte[]> _hidl_out_hashchain = getHashChain();
                            _hidl_reply.writeStatus(0);
                            HwBlob _hidl_blob = new HwBlob(16);
                            int _hidl_vec_size = _hidl_out_hashchain.size();
                            _hidl_blob.putInt32(8L, _hidl_vec_size);
                            _hidl_blob.putBool(12L, false);
                            HwBlob childBlob = new HwBlob(_hidl_vec_size * 32);
                            while (_hidl_index_0 < _hidl_vec_size) {
                                long _hidl_array_offset_1 = _hidl_index_0 * 32;
                                childBlob.putInt8Array(_hidl_array_offset_1, _hidl_out_hashchain.get(_hidl_index_0));
                                _hidl_index_0++;
                            }
                            _hidl_blob.putBlob(0L, childBlob);
                            _hidl_reply.writeBuffer(_hidl_blob);
                            _hidl_reply.send();
                            return;
                        case 256462420:
                            _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                            if (_hidl_index_0 != 1) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            } else {
                                _hidl_request.enforceInterface(IBase.kInterfaceName);
                                setHALInstrumentation();
                                return;
                            }
                        case 256660548:
                            _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                            if (_hidl_index_0 != 0) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            return;
                        case 256921159:
                            _hidl_is_oneway = (_hidl_flags & 1) != 0;
                            if (_hidl_is_oneway) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            } else {
                                _hidl_request.enforceInterface(IBase.kInterfaceName);
                                ping();
                                _hidl_reply.writeStatus(0);
                                _hidl_reply.send();
                                return;
                            }
                        case 257049926:
                            _hidl_is_oneway = (_hidl_flags & 1) != 0;
                            if (_hidl_is_oneway) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            DebugInfo _hidl_out_info = getDebugInfo();
                            _hidl_reply.writeStatus(0);
                            _hidl_out_info.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                            return;
                        case 257120595:
                            _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                            if (_hidl_index_0 != 1) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            } else {
                                _hidl_request.enforceInterface(IBase.kInterfaceName);
                                notifySyspropsChanged();
                                return;
                            }
                        case 257250372:
                            _hidl_index_0 = (_hidl_flags & 1) != 0 ? 1 : 0;
                            if (_hidl_index_0 != 0) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            return;
                        default:
                            return;
                    }
            }
        }
    }
}
