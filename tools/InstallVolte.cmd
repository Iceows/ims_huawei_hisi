
xcopy /Y "C:\GitRepo\iceows\ims_hi6250_volte\app\build\outputs\apk\release\app-release.apk" .
java.exe -jar "ApkSigner.jar" sign  --key platform.pk8 --cert platform.x509.pem  --v4-signing-enabled false --out "HuaweiIMS.apk" "app-release.apk"

xcopy /Y "C:\GitRepo\iceows\treble_app\app\build\outputs\apk\release\app-release-unsigned.apk" .
java.exe -jar "ApkSigner.jar" sign  --key platform.pk8 --cert platform.x509.pem  --v4-signing-enabled false --out "TrebleApp.apk" "app-release-unsigned.apk"

adb root
adb remount rw, /system
adb shell mkdir /system/app/HuaweiIMS
adb push HuaweiIMS.apk /system/app/HuaweiIMS
adb shell chmod 644 /system/app/HuaweiIMS/HuaweiIMS.apk 

REM adb push TrebleApp.apk /system/priv-app/TrebleApp
REM adb shell chmod 644 /system/priv-app/TrebleApp/TrebleApp.apk 

REM adb shell mkdir /system/app/treble-overlay-telephony-hw-ims
REM adb push treble-overlay-telephony-hw-ims.apk /system/app/treble-overlay-telephony-hw-ims/treble-overlay-telephony-hw-ims.apk
REM adb shell chmod 644 /system/app/treble-overlay-telephony-hw-ims/treble-overlay-telephony-hw-ims.apk

adb reboot
