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

package com.huawei.ims

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Handler
import android.os.IHwBinder
import android.os.Looper
import android.os.Message
import android.os.RemoteException
import android.util.Log
import com.android.internal.telephony.RILRequest
import vendor.huawei.hardware.hisiradio.V1_0.IHisiRadio
import vendor.huawei.hardware.radio.V2_0.IRadio
import vendor.huawei.hardware.radio.ims.V1_0.IRadioIms
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong


/*
phhgsi_arm64_ab:/ # lshal |grep radio
Y android.hardware.radio@1.0::IRadio/slot1                                     0/1        800    1556 395
Y android.hardware.radio@1.1::IRadio/slot1                                     0/1        800    1556 395
Y vendor.huawei.hardware.hisiradio@1.0::IHisiRadio/slot1                       0/1        800    395
Y vendor.huawei.hardware.hisiradio@1.1::IHisiRadio/slot1                       0/1        800    395
Y vendor.huawei.hardware.radio.chr@1.0::IRadioChr/slot1                        0/1        800    395
Y vendor.huawei.hardware.radio.deprecated@1.0::IOemHook/slot1                  0/1        800    395
Y vendor.huawei.hardware.radio.ims@1.0::IRadioIms/rildi                        0/1        800    1556 395
Y vendor.huawei.hardware.radio@2.0::IRadio/slot1                               0/1        800    395
 */

object RilHolder {

    private const val LOG_TAG = "HwImsRilHolder"

    val mRadioProxyCookie: AtomicLong? = AtomicLong(0L)
    val mRadioProxyDeathRecipient: RadioProxyDeathRecipient? = RadioProxyDeathRecipient()


    // Radio Hisi - hal vendor.huawei.hardware.hisiradio@1.1::IHisiRadio
    private val serviceHisiNames =  arrayOf("slot1", "slot2", "slot3")
    private val radioHisiImpls = arrayOfNulls<IHisiRadio>(3)
    private val responseHisiCallbacks = arrayOfNulls<HwHisiRadioResponse>(3)
    private val indicationHisiCallbacks = arrayOfNulls<HwHisiRadioIndication>(3)
    private val hisicallbacks = ConcurrentHashMap<Int, (vendor.huawei.hardware.hisiradio.V1_0.RadioResponseInfo, vendor.huawei.hardware.hisiradio.V1_0.RspMsgPayload?) -> Unit>()

    // IMS radio - hal vendor.huawei.hardware.radio.ims@1.0::IRadioIms
    private val serviceImsNames = arrayOf("rildi", "rildi2", "rildi3")
    private val radioImsImpls = arrayOfNulls<IRadioIms>(3)
    private val responseImsCallbacks = arrayOfNulls<HwImsRadioResponse>(3)
    private val indicationImsCallbacks = arrayOfNulls<HwImsRadioIndication>(3)
    private val imscallbacks = ConcurrentHashMap<Int, (vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo, vendor.huawei.hardware.radio.ims.V1_0.RspMsgPayload?) -> Unit>()

    // Radio
    private val serviceNames = arrayOf("slot1", "slot2", "slot3")
    private val responseCallbacks = arrayOfNulls<HwRadioResponse>(3)
    private val indicationCallbacks = arrayOfNulls<HwRadioIndication>(3)
    private val radioImpls = arrayOfNulls<IRadio>(3)
    private val callbacks = ConcurrentHashMap<Int, (vendor.huawei.hardware.radio.V2_0.RadioResponseInfo, vendor.huawei.hardware.radio.V2_0.RspMsgPayload?) -> Unit>()

    private var nextSerial = -1
    private val serialToSlot = ConcurrentHashMap<Int, Int>()
    private val blocks = ConcurrentHashMap<Int, BlockingCallback>()


    init {
        Log.i(LOG_TAG,"Init")

    }

    fun getRadio(slotId: Int): IRadio? {
        if (radioImpls[slotId] == null) {
            try {
                try {
                    Log.i(LOG_TAG, "Try to get service huawei ")
                    radioImpls[slotId] = IRadio.getService(serviceNames[slotId])
                    Log.i(LOG_TAG, "getRadio found IRadio service on slotid : " + slotId)
                } catch (e: NoSuchElementException) {
                    Log.e(LOG_TAG, "Index oob in rilholder for IRadioIms. Bail Out!!!", e)
                    val notificationManager = HwImsService.instance!!.getSystemService(NotificationManager::class.java) as NotificationManager
                    val channel = NotificationChannel("HwIms", "HwIms", NotificationManager.IMPORTANCE_HIGH)
                    notificationManager.createNotificationChannel(channel)
                    notificationManager.cancelAll()
                    val n = Notification.Builder(HwImsService.instance, "HwIms")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("HwIms not supported")
                        .setContentText("Please uninstall HwIms application from settings ASAP! Caused by broken IImsRadio or SELinux, try permissive.")
                        .setAutoCancel(true)
                        .build()
                    notificationManager.notify(0, n)
                    // TODO Iceows
                    android.os.Process.killProcess(android.os.Process.myPid())
                    // We're dead.
                }
                responseCallbacks[slotId] = HwRadioResponse(slotId)
                indicationCallbacks[slotId] = HwRadioIndication(slotId)
            } catch (e: RemoteException) {
                Log.e(LOG_TAG, "remoteexception getting service. will throw npe later ig.")
                throw RuntimeException("Failed to get service due to internal error")
            }
        }
        try {
            //getNextSerial
            // radioImpls[slotId]!!.linkToDeath(this.mRadioProxyDeathRecipient, this.mRadioProxyCookie.incrementAndGet());
            radioImpls[slotId]!!.setResponseFunctionsHuawei(responseCallbacks[slotId], indicationCallbacks[slotId])
        } catch (e: RemoteException) {
            Log.e(LOG_TAG, "Failed to update response functions!, Err : " + e.printStackTrace())
        }

        return radioImpls[slotId]!!
    }

    fun getHisiRadio(slotId: Int): IHisiRadio? {
        if (radioHisiImpls[slotId] == null) {
            try {
                try {
                    Log.i(LOG_TAG, "Try to get service huawei hisi radio")
                    radioHisiImpls[slotId] = IHisiRadio.getService(serviceHisiNames[slotId])
                    Log.i(LOG_TAG, "getRadio found IHisiRadio service on slotid : " + slotId)
                } catch (e: NoSuchElementException) {
                    Log.e(LOG_TAG, "Index oob in rilholder for IHisiRadio. Bail Out!!!", e)
                    val notificationManager = HwImsService.instance!!.getSystemService(NotificationManager::class.java) as NotificationManager
                    val channel = NotificationChannel("HwIms", "HwIms", NotificationManager.IMPORTANCE_HIGH)
                    notificationManager.createNotificationChannel(channel)
                    notificationManager.cancelAll()
                    val n = Notification.Builder(HwImsService.instance, "HwIms")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("HwIms not supported")
                        .setContentText("Please uninstall HwIms application from settings ASAP! Caused by broken IImsRadio or SELinux, try permissive.")
                        .setAutoCancel(true)
                        .build()
                    notificationManager.notify(0, n)
                    // TODO Iceows
                    android.os.Process.killProcess(android.os.Process.myPid())
                    // We're dead.
                }
                responseHisiCallbacks[slotId] = HwHisiRadioResponse(slotId)
                indicationHisiCallbacks[slotId] = HwHisiRadioIndication(slotId)
            } catch (e: RemoteException) {
                Log.e(LOG_TAG, "remoteexception getting service. will throw npe later ig.")
                throw RuntimeException("Failed to get service due to internal error")
            }
        }

        try {
            //getNextSerial
            // radioImpls[slotId]!!.linkToDeath(this.mRadioProxyDeathRecipient, this.mRadioProxyCookie.incrementAndGet());
            radioHisiImpls[slotId]!!.setResponseFunctionsHuawei(responseHisiCallbacks[slotId], indicationHisiCallbacks[slotId])

        } catch (e: RemoteException) {
            Log.e(LOG_TAG, "Failed to update response functions!, Err : " + e.printStackTrace())
        }

        return radioHisiImpls[slotId]!!
    }

    @Synchronized
    fun getImsRadio(slotId: Int): IRadioIms? {
        if (radioImsImpls[slotId] == null) {
            try {
                try {
                    Log.i(LOG_TAG, "Try to get service huawei ims")
                    radioImsImpls[slotId] = IRadioIms.getService(serviceImsNames[slotId])
                    Log.i(LOG_TAG, "getRadio found IRadioIms service on slotid : " + slotId)
                } catch (e: NoSuchElementException) {
                    Log.e(LOG_TAG, "Index oob in rilholder for IRadioIms. Bail Out!!!", e)
                    val notificationManager = HwImsService.instance!!.getSystemService(NotificationManager::class.java) as NotificationManager
                    val channel = NotificationChannel("HwIms", "HwIms", NotificationManager.IMPORTANCE_HIGH)
                    notificationManager.createNotificationChannel(channel)
                    notificationManager.cancelAll()
                    val n = Notification.Builder(HwImsService.instance, "HwIms")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("HwIms not supported")
                        .setContentText("Please uninstall HwIms application from settings ASAP! Caused by broken IImsRadio or SELinux, try permissive.")
                        .setAutoCancel(true)
                        .build()
                    notificationManager.notify(0, n)
                    // TODO Iceows
                    android.os.Process.killProcess(android.os.Process.myPid())
                    // We're dead.
                }
                responseImsCallbacks[slotId] = HwImsRadioResponse(slotId)
                indicationImsCallbacks[slotId] = HwImsRadioIndication(slotId)
            } catch (e: RemoteException) {
                Log.e(LOG_TAG, "remoteexception getting service. will throw npe later ig.")
                throw RuntimeException("Failed to get service due to internal error")
            }
        }
        try {
            this.mRadioProxyCookie?.let { radioImsImpls[slotId]!!.linkToDeath(this.mRadioProxyDeathRecipient, it.incrementAndGet()) };
            radioImsImpls[slotId]!!.setResponseFunctionsHuawei(responseImsCallbacks[slotId], indicationImsCallbacks[slotId])
        } catch (e: RemoteException) {
            Log.e(LOG_TAG, "Failed to update response functions!, Err : " + e.printStackTrace())
        }

        return radioImsImpls[slotId]!!
    }

    class BlockingCallback {
        private val lock = Object()
        private var done = false
        private var radioResponseInfo: vendor.huawei.hardware.radio.V2_0.RadioResponseInfo? = null

        fun run(radioResponseInfo: vendor.huawei.hardware.radio.V2_0.RadioResponseInfo, rspMsgPayload: vendor.huawei.hardware.radio.V2_0.RspMsgPayload?) {
            synchronized(lock) {
                if (done)
                    throw RuntimeException("May not call the callback twice for the same serial!")
                this.radioResponseInfo = radioResponseInfo
                done = true
                lock.notifyAll()
            }
        }

        fun get(): vendor.huawei.hardware.radio.V2_0.RadioResponseInfo {
            synchronized(lock) {
                while (!done) {
                    lock.wait()
                }
            }
            return radioResponseInfo!!
            // The lock ensures it's never null. An NPE here means something went really wrong.
        }
    }

    @Synchronized
    fun callback(cb: (vendor.huawei.hardware.radio.V2_0.RadioResponseInfo, vendor.huawei.hardware.radio.V2_0.RspMsgPayload?) -> Unit, slotId: Int): Int {
        val serial = getNextSerial()
        serialToSlot[serial] = slotId
        callbacks[serial] = cb
        Log.v(LOG_TAG, "Setting callback for serial $serial")
        return serial
    }

    @Synchronized
    fun imscallback(cb: (vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo, vendor.huawei.hardware.radio.ims.V1_0.RspMsgPayload?) -> Unit, slotId: Int): Int {
        val serial = getNextSerial()
        serialToSlot[serial] = slotId
        imscallbacks[serial] = cb
        Log.v(LOG_TAG, "Setting imscallback for serial $serial")
        return serial
    }

    @Synchronized
    fun hisicallback(cb: (vendor.huawei.hardware.hisiradio.V1_0.RadioResponseInfo, vendor.huawei.hardware.hisiradio.V1_0.RspMsgPayload?) -> Unit, slotId: Int): Int {
        val serial = getNextSerial()
        serialToSlot[serial] = slotId
        hisicallbacks[serial] = cb
        Log.v(LOG_TAG, "Setting hisicallback for serial $serial")
        return serial
    }
    @Synchronized
    fun getNextSerial(): Int {
        return ++nextSerial
    }

    fun triggerImsCB(serial: Int, radioResponseInfo: vendor.huawei.hardware.radio.ims.V1_0.RadioResponseInfo, rspMsgPayload: vendor.huawei.hardware.radio.ims.V1_0.RspMsgPayload?) {
        Log.i(LOG_TAG, "triggerImsCB - Incoming response for slot " + serialToSlot[serial] + ", serial " + serial + ", radioResponseInfo " + radioResponseInfo + ", rspMsgPayload " + rspMsgPayload)
        if (imscallbacks.containsKey(serial))
            imscallbacks[serial]!!(radioResponseInfo, rspMsgPayload)
    }

    fun triggerCB(serial: Int, radioResponseInfo: vendor.huawei.hardware.radio.V2_0.RadioResponseInfo, rspMsgPayload: vendor.huawei.hardware.radio.V2_0.RspMsgPayload?) {
        Log.i(LOG_TAG, "triggerCB - Incoming response for slot " + serialToSlot[serial] + ", serial " + serial + ", radioResponseInfo " + radioResponseInfo + ", rspMsgPayload " + rspMsgPayload)
        if (callbacks.containsKey(serial))
            callbacks[serial]!!(radioResponseInfo, rspMsgPayload)
    }

    fun triggerHisiCB(serial: Int, radioResponseInfo: vendor.huawei.hardware.hisiradio.V1_0.RadioResponseInfo, rspMsgPayload: vendor.huawei.hardware.hisiradio.V1_0.RspMsgPayload?) {
        Log.i(LOG_TAG, "triggerHisiCB - Incoming response for slot " + serialToSlot[serial] + ", serial " + serial + ", radioResponseInfo " + radioResponseInfo + ", rspMsgPayload " + rspMsgPayload)
        if (hisicallbacks.containsKey(serial))
            hisicallbacks[serial]!!(radioResponseInfo, rspMsgPayload)
    }
    fun prepareBlock(slotId: Int): Int {
        val cb = BlockingCallback()
        val serial = callback(cb::run, slotId)
        blocks[serial] = cb
        return serial
    }

    /*
 * It is safe to call this method multiple times, it will always return the same for the same serial.
 */
    fun blockUntilComplete(serial: Int): vendor.huawei.hardware.radio.V2_0.RadioResponseInfo {
        return blocks[serial]?.get()
            ?: throw RuntimeException("prepareBlock was not called for this request!")

    }


    class RadioProxyDeathRecipient internal constructor() : IHwBinder.DeathRecipient {
        // android.os.IHwBinder.DeathRecipient
        override fun serviceDied(cookie: Long) {
            Log.w(LOG_TAG,"Service IMS is dead...")

        }
    }
}


