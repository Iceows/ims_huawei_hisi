
xcopy /Y "C:\GitHub\iceows\ims_huawei_hisi\app\build\outputs\apk\release\app-release.apk" 

java.exe -jar "ApkSigner.jar" sign  --key platform.pk8 --cert platform.x509.pem  --v4-signing-enabled false --out "HuaweiIMS.apk" "app-release.apk"


adb root
adb remount /system
adb shell mkdir /system/app/HuaweiIMS
adb push HuaweiIMS.apk /system/app/HuaweiIMS
adb shell chmod 644 /system/app/HuaweiIMS/HuaweiIMS.apk

REM Iceows enable volte
REM adb shell "echo ro.hw.volte.enable=1 >>  /system/build.prop"

REM adb push TrebleApp.apk /system/priv-app/TrebleApp
REM adb shell chmod 644 /system/priv-app/TrebleApp/TrebleApp.apk 

REM adb shell mkdir /system/app/treble-overlay-telephony-hw-ims
REM adb push treble-overlay-telephony-hw-ims.apk /system/app/treble-overlay-telephony-hw-ims/treble-overlay-telephony-hw-ims.apk
REM adb shell chmod 644 /system/app/treble-overlay-telephony-hw-ims/treble-overlay-telephony-hw-ims.apk

adb reboot
