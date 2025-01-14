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
 *
 *  This file incorporates code covered by the following copyright and permission notice:
 *
 *     Copyright (C) 2017 The Android Open Source Project
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.huawei.ims

import android.os.AsyncResult
import android.os.Handler
import android.os.Message
import android.telephony.ims.stub.ImsConfigImplBase
import android.util.Log
import com.android.ims.ImsConfig
import com.android.ims.ImsConfigListener
import java.util.concurrent.ConcurrentHashMap


class HwImsConfig : ImsConfigImplBase() {
    private val configInt = ConcurrentHashMap<Int, Int>()
    private val configString = ConcurrentHashMap<Int, String>()
    private val LOG_TAG = "HwImsConfig"


    private val EVENT_SET_VIDEO_QUALITY_DONE = 1
    private val EVENT_GET_VIDEO_QUALITY_DONE = 2
    private val EVENT_SET_FEATURE_VALUE = 3


    init {
        Log.i(LOG_TAG,"Init")

        // We support VoLTE by default.
        configInt[ImsConfig.ConfigConstants.VLT_SETTING_ENABLED] = ImsConfig.FeatureValueConstants.ON
        configInt[ImsConfig.ConfigConstants.EAB_SETTING_ENABLED] = ImsConfig.FeatureValueConstants.OFF

    }

    override fun setConfig(item: Int, value: Int): Int {
        configInt[item] = value

        //HwImsConfig: setConfig (int):: item=26 value=0 (VOICE_OVER_WIFI_ROAMING)
        //HwImsConfig: setConfig (int):: item=27 value=1 (VOICE_OVER_WIFI_MODE)
        //HwImsConfig: setConfig (int):: item=66 value=0
        Log.i(LOG_TAG, "setConfig (int):: item=$item value=$value")

        when (item) {
            ImsConfig.ConfigConstants.VOICE_OVER_WIFI_ROAMING -> MapconController.getInstance().notifyRoaming(0)
            ImsConfig.ConfigConstants.VOICE_OVER_WIFI_MODE -> MapconController.getInstance().setDomain(0, value)
            //ImsConfig.ConfigConstants.VOICE_OVER_WIFI_SETTING_ENABLED
        }
        notifyProvisionedValueChanged(item, value)
        return ImsConfig.OperationStatusConstants.SUCCESS
    }

    override fun setConfig(item: Int, value: String): Int {
        configString[item] = value

        Log.i(LOG_TAG, "setConfig (string):: item=$item value=$value")
        notifyProvisionedValueChanged(item, value)
        return ImsConfig.OperationStatusConstants.SUCCESS
    }

    override fun getConfigInt(item: Int): Int {
        Log.i(LOG_TAG, "getConfigInt :: item=$item")
        return configInt.getOrDefault(item, null) ?: ImsConfig.FeatureValueConstants.ERROR
    }

    override fun getConfigString(item: Int): String? {
        Log.i(LOG_TAG, "getConfigString :: item=$item")
        return configString.getOrDefault(
            item,
            null
        )
    }

    private class ImsConfigImplHandler : Handler() {
        // android.os.Handler
        private val LOG_TAG = "HwImsConfigHandler"

        @Override
        override fun handleMessage(msg: Message) {
            Log.i(LOG_TAG,"Message received: what = " + msg.what)
            val ar = msg.obj as AsyncResult

            when (msg.what) {
                1 -> {
                    HwImsConfig.onSetVideoCallQualityDone(HwImsConfig.getImsConfigListener(ar), ar)
                    return
                }
                2 -> {
                    HwImsConfig.onGetVideoCallQualityDone(HwImsConfig.getImsConfigListener(ar), ar)
                    return
                }
                3 -> {
                    HwImsConfig.onSetFeatureResponseDone(HwImsConfig.getImsConfigListener(ar), ar)
                    return
                }
                else -> {
                Log.e(LOG_TAG, "handleMessage: unhandled message");
                    return
                }
            }
        }
    }


    /* Wrapper class to encapsulate the arguments and listener to the setFeatureValue and
     * getFeatureValue APIs
     */
    private class FeatureAccessWrapper(
        var feature: Int, var network: Int, var value: Int,
        var listener: ImsConfigListener
    )


    companion object {
        private val LOG_TAG = "HwImsConfig"

        private fun getImsConfigListener(ar: AsyncResult?): ImsConfigListener? {
            if (ar == null) {
                Log.e(LOG_TAG, "AsyncResult is null.")
            } else if (ar.userObj is ImsConfigListener) {
                return ar.userObj as ImsConfigListener
            } else if (ar.userObj is FeatureAccessWrapper &&
                (ar.userObj as FeatureAccessWrapper).listener is ImsConfigListener
            ) {
                return (ar.userObj as FeatureAccessWrapper).listener
            }
            Log.e(LOG_TAG, "getImsConfigListener returns null")
            return null
        }
        fun onGetVideoCallQualityDone(imsConfigListener: ImsConfigListener?, ar: AsyncResult) {
            Log.d(LOG_TAG, "onGetVideoCallQualityDone")
            val result: Int
            if (imsConfigListener != null) {
                try {
                    val status = getOperationStatus(ar.exception == null)
                    result = if (ar.result == null) {
                        -1
                    } else {
                        (ar.result as Int).toInt()
                    }
                    imsConfigListener.onGetVideoQuality(status, result)
                    return
                } catch (th: Throwable) {
                    Log.e(LOG_TAG,"onGetVideoCallQualityDone failed. ")
                    return
                }
            }
            Log.e(LOG_TAG,"onGetVideoCallQualityDone listener is not valid !!!")
        }
        fun onSetVideoCallQualityDone(imsConfigListener: ImsConfigListener?, ar: AsyncResult) {
            Log.d(LOG_TAG, "onSetVideoCallQualityDone")

            if (imsConfigListener != null) {
                try {
                    val status: Int = getOperationStatus(ar.exception == null)
                    imsConfigListener.onSetVideoQuality(status)
                    return
                } catch (th: Throwable) {
                    Log.e(LOG_TAG,"onSetVideoCallQualityDone failed.")
                    return
                }
            }
            Log.e(LOG_TAG,"onSetVideoCallQualityDone listener is not valid !!!")
        }



        fun onSetFeatureResponseDone(imsConfigListener: ImsConfigListener?, ar: AsyncResult) {
            Log.i(LOG_TAG, "onSetFeatureResponseDone")
            if (imsConfigListener != null) {
                try {
                    val status = getOperationStatus(ar.exception == null)
                    val f = ar.userObj as FeatureAccessWrapper
                    imsConfigListener.onSetFeatureResponse(f.feature, f.network, f.value, status)
                    return
                } catch (th: Throwable) {
                    Log.e(LOG_TAG,"onSetFeatureResponseDone failed.");
                    return
                }
            }
            Log.e(LOG_TAG, "onSetFeatureResponseDone listener is not valid !!!")
        }

        private fun getOperationStatus(status: Boolean): Int {
            return if (status) 0 else 1
        }
    }
}
