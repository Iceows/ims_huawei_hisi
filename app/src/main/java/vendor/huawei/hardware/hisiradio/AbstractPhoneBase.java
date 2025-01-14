/*
 * This file is not complete. It's just a part that allows you to correctly compile the hal hisiradio classes
 */

package vendor.huawei.hardware.hisiradio;

public class AbstractPhoneBase {
    private static final String LOG_TAG = "HwPhoneBase";

    public static final int BUFFER_SIZE = 120;
    public static final int DEVICE_ID_MASK_ALL = 2;
    public static final int DEVICE_ID_MASK_IMEI = 1;
    private static final int DEVICE_ID_RETRY_COUNT = 5;
    private static final int DEVICE_ID_RETRY_INTERVAL = 6000;
    public static final int EVENT_CSCON_MODE_INFO = 116;
    public static final int EVENT_ECC_NUM = 104;
    protected static final int EVENT_GET_CALL_FORWARD_TIMER_DONE = 110;
    public static final int EVENT_GET_IMSI_DONE = 105;
    public static final int EVENT_GET_LTE_RELEASE_VERSION_DONE = 108;
    public static final int EVENT_GET_NVCFG_RESULT_INFO_DONE = 115;
    public static final int EVENT_HW_CUST_BASE = 100;
    public static final int EVENT_HW_LAA_STATE_CHANGED = 112;
    public static final int EVENT_RETRY_GET_DEVICE_ID = 1000;
    protected static final int EVENT_SET_CALL_FORWARD_TIMER_DONE = 109;
    public static final int EVENT_SET_MODE_TO_AUTO = 111;
    public static final int EVENT_UNSOL_HW_CALL_ALT_SRV_DONE = 113;
    public static final int EVENT_UNSOL_SIM_NVCFG_FINISHED = 114;
    public static final int HW_ENCRYPT_CALL = 0;
    public static final int HW_KMC_REMOTE_COMMUNICATION = 1;

    public static final int LTE_RELEASE_VERSION_R10 = 1;
    public static final int LTE_RELEASE_VERSION_R10_WITH_HIGH_THROUGHPUT = 3;
    public static final int LTE_RELEASE_VERSION_R9 = 0;
    public static final int LTE_RELEASE_VERSION_R9_WITH_HIGH_THROUGHPUT = 2;
    public static final int SET_TO_AOTO_TIME = 5000;
    public static final int SPEECH_INFO_CODEC_NB = 1;
    public static final int SPEECH_INFO_CODEC_WB = 2;
}
