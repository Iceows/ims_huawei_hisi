/*
 * This file is part of HwIms
 * Copyright (C) 2019,2025 Penn Mackintosh and Raphael Mounier
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.huawei.ims;

import android.os.RemoteException;
import android.annotation.NonNull;

import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.feature.CapabilityChangeRequest;
import android.telephony.ims.feature.MmTelFeature;
import android.telephony.ims.stub.ImsCallSessionImplBase;
import android.telephony.ims.stub.ImsRegistrationImplBase;
import android.util.Log;
import android.util.SparseArray;

import com.android.ims.ImsException;

// This file has to remain Java because changeEnabledCapabilities is abstract in MmTelFeature and
// it exposes a protected subclass of MmTelFeature which Kotlin blocks from compilation.
// TODO find a way to refactor to Kt

public class HwMmTelFeature extends MmTelFeature {

    private MmTelCapabilities mMmtelCapabilities = new MmTelFeature.MmTelCapabilities();
    //private RegistrantList mCapabilitiesChangedRegistrants = new RegistrantList();

    private static final HwMmTelFeature[] instances = {null, null, null};
    private final String LOG_TAG = "HwImsMmTelFeatureImpl";
    // Enabled Capabilities - not status
    private final SparseArray<MmTelCapabilities> mEnabledCapabilities = new SparseArray<>();

    private final int mSlotId;
    private boolean mbImsRegister;
    public TelephonyManager telephonyManager;

    private HwMmTelFeature(int slotId) { // Use getInstance(slotId)

        Log.d(LOG_TAG, "HwMmTelFeature::constructor");
        mSlotId = slotId;
        mbImsRegister = false;
        mEnabledCapabilities.append(ImsRegistrationImplBase.REGISTRATION_TECH_LTE,
                new MmTelCapabilities(MmTelCapabilities.CAPABILITY_TYPE_VOICE));
        // TODO: check if Mapcon is installed.
        // TODO : test VoWIFI
        // TODO : Iceows : remove voWifi support - need mapcon
        //mEnabledCapabilities.append(ImsRegistrationImplBase.REGISTRATION_TECH_IWLAN,
        //        new MmTelCapabilities(MmTelCapabilities.CAPABILITY_TYPE_VOICE));

        setFeatureState(STATE_READY);
    }

    public static HwMmTelFeature getInstance(int slotId) {
        if (instances[slotId] == null) {
            instances[slotId] = new HwMmTelFeature(slotId);
        }
        return instances[slotId];
    }


    @Override
    public boolean queryCapabilityConfiguration(int capability, int radioTech) {
        return mEnabledCapabilities.get(radioTech).isCapable(capability);
    }

    @Override
    public void changeEnabledCapabilities(CapabilityChangeRequest request,
                                          CapabilityCallbackProxy c) {

        Log.d(LOG_TAG, "changeEnabledCapabilities enable");
        for (CapabilityChangeRequest.CapabilityPair pair : request.getCapabilitiesToEnable()) {
            Log.d(LOG_TAG,"Enable for RadioTech (LTE/0-IWAN/1) : " + pair.getRadioTech());
            mEnabledCapabilities.get(pair.getRadioTech()).addCapabilities(pair.getCapability());
            if (pair.getRadioTech() == ImsRegistrationImplBase.REGISTRATION_TECH_IWLAN && pair.getCapability() == MmTelCapabilities.CAPABILITY_TYPE_VOICE)
                MapconController.Companion.getInstance().turnVowifiOn(mSlotId);
        }
        Log.d(LOG_TAG, "changeEnabledCapabilities disable");
        for (CapabilityChangeRequest.CapabilityPair pair : request.getCapabilitiesToDisable()) {
            Log.d(LOG_TAG,"Disable for RadioTech (LTE/0-IWAN/1) : " + pair.getRadioTech());
            mEnabledCapabilities.get(pair.getRadioTech()).removeCapabilities(pair.getCapability());
            if (pair.getRadioTech() == ImsRegistrationImplBase.REGISTRATION_TECH_IWLAN && pair.getCapability() == MmTelCapabilities.CAPABILITY_TYPE_VOICE)
                MapconController.Companion.getInstance().turnVowifiOff(mSlotId);
        }
    }

    int getImsSwitch() {
        int serial = RilHolder.INSTANCE.prepareBlock(mSlotId);
        try {
            RilHolder.INSTANCE.getRadio(mSlotId).getImsSwitch(serial);
            RilHolder.INSTANCE.blockUntilComplete(serial);
        } catch (RemoteException e) {
            Rlog.e(LOG_TAG, "Failed to getImsSwitch!", e);
        }
        return 1;
    }

    private void registerImsInner() {
        try {
            Log.d(LOG_TAG, "registerImsInner");
            int serial = RilHolder.INSTANCE.hisicallback(
                    (radioResponseInfo, rspMsgPayload) -> {
                        if (radioResponseInfo.error != 0) {
                            Log.e(LOG_TAG, "radiorespinfo gives error " + radioResponseInfo.error);
                            HwImsService.Companion.getInstance().getRegistration(mSlotId).onDeregistered(new ImsReasonInfo(ImsReasonInfo.CODE_UNSPECIFIED, radioResponseInfo.error, radioResponseInfo.toString() + rspMsgPayload.toString()));
                        } else {
                            mMmtelCapabilities.addCapabilities(MmTelCapabilities.CAPABILITY_TYPE_VOICE);
                            notifyCapabilitiesStatusChanged(mMmtelCapabilities);
                            HwImsService.Companion.getInstance().getRegistration(mSlotId).notifyRegistered(HwImsRegistration.REGISTRATION_TECH_LTE);
                        }
                        return null;
                    }, mSlotId);
            RilHolder.INSTANCE.getHisiRadio(mSlotId).imsRegister(serial);
        } catch (RemoteException e) {
            HwImsService.Companion.getInstance().getRegistration(mSlotId).notifyDeregistered(new ImsReasonInfo(), ImsRegistrationImplBase.REGISTRATION_TECH_LTE);
            Log.e(LOG_TAG, "error registering ims", e);
        }
    }

    public void registerImsAndSwitch() {
        Log.d(LOG_TAG, "registerIms");
        HwImsService.Companion.getInstance().getRegistration(mSlotId).notifyRegistering(HwImsRegistration.REGISTRATION_TECH_LTE);
        try {
            int serial=RilHolder.INSTANCE.callback((radioResponseInfo, rspMsgPayload) -> {
                if (radioResponseInfo.error != 0) {
                    HwImsService.Companion.getInstance().getRegistration(mSlotId).notifyDeregistered(new ImsReasonInfo(), ImsRegistrationImplBase.REGISTRATION_TECH_LTE);
                } else {
                    registerImsInner();
                }
                return null;
            } , mSlotId);
            Log.d(LOG_TAG, "registerIms serial = " + serial + " - slotid = " + mSlotId);
            // Phoneid, value
            RilHolder.INSTANCE.getRadio(mSlotId).setImsSwitch(serial,1);
            mbImsRegister=true;
        } catch (RemoteException e) {
            HwImsService.Companion.getInstance().getRegistration(mSlotId).notifyDeregistered(new ImsReasonInfo(), ImsRegistrationImplBase.REGISTRATION_TECH_LTE);
        }


    }

    public void unregisterIms() {
        Log.d(LOG_TAG, "unregisterIms");
        if (mbImsRegister) {
            mMmtelCapabilities.removeCapabilities(MmTelCapabilities.CAPABILITY_TYPE_VOICE);
            mbImsRegister=false;
        }
    }

    @Override
    public ImsCallProfile createCallProfile(int callSessionType, int callType) {
        if (callSessionType == ImsCallProfile.SERVICE_TYPE_EMERGENCY) {
            return null;
        }
        /**
         * It is for a special case. It helps that the application can make a call
         * without IMS connection (not registered).
         * In the moment of the call initiation, the device try to connect to the IMS network
         * and initiates the call.
         */
        if (callSessionType == ImsCallProfile.SERVICE_TYPE_NONE) {
            // Check status of IMS
            Log.d(LOG_TAG,"createCallProfile for service type none");
            // Register IMS
            registerImsAndSwitch();
        }
        if (callSessionType == ImsCallProfile.SERVICE_TYPE_NORMAL) {
            Log.d(LOG_TAG,"createCallProfile for service type normal");
        }

        return new ImsCallProfile(callSessionType, callType);
        // Is this right?

    }

    @Override
    public synchronized ImsCallSessionImplBase createCallSession(@NonNull ImsCallProfile profile) {
        return new HwImsCallSession(mSlotId, profile);
    }

    @Override
    public void onFeatureRemoved() {
        super.onFeatureRemoved();
    }

    @Override
    public void onFeatureReady() {
        super.onFeatureReady();
        getImsSwitch();
    }

}
