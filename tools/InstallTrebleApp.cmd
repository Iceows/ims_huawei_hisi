xcopy /Y "C:\GitHub\iceows\treble_app_iceows\app\build\outputs\apk\debug\app-debug.apk" .
java.exe -jar "ApkSigner.jar" sign  --key platform.pk8 --cert platform.x509.pem  --v4-signing-enabled false --out "TrebleApp.apk" "app-debug.apk"

adb root
adb remount /system
adb push TrebleApp.apk /system/priv-app/TrebleApp
adb shell chmod 755 /system/priv-app/TrebleApp/TrebleApp.apk 

rem adb push treble-overlay-telephony-hw-ims.apk /system/product/overlay/treble-overlay-telephony-hw-ims.apk
rem adb shell chmod 644 /system/product/overlay/treble-overlay-telephony-hw-ims.apk


adb reboot
