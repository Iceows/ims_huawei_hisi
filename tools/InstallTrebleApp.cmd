xcopy /Y "C:\GitHub\iceows\treble_app_iceows\app\build\outputs\apk\debug\app-debug.apk" .
java.exe -jar "ApkSigner.jar" sign  --key platform.pk8 --cert platform.x509.pem  --v4-signing-enabled false --out "TrebleApp.apk" "app-debug.apk"

adb root
adb remount /system
adb push TrebleApp.apk /system/priv-app/TrebleApp
adb shell chmod 644 /system/priv-app/TrebleApp/TrebleApp.apk 

REM adb shell mkdir /system/app/treble-overlay-telephony-hw-ims
REM adb push treble-overlay-telephony-hw-ims.apk /system/app/treble-overlay-telephony-hw-ims/treble-overlay-telephony-hw-ims.apk
REM adb shell chmod 644 /system/app/treble-overlay-telephony-hw-ims/treble-overlay-telephony-hw-ims.apk

REM adb shell "echo '(allow radio default_android_service (service_manager (add)))' >> /system/etc/selinux/plat_sepolicy.cil"
REM adb shell "echo '(allow radio default_hisi_hwservice (hwservice_manager (find)))' >> /system/etc/selinux/plat_sepolicy.cil"
REM adb shell "echo '(allow radio odm_xml_file (dir (getattr)))' >> /system/etc/selinux/plat_sepolicy.cil"

adb reboot
