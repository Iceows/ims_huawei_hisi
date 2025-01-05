# ims_huawei_hisi
 IMS for VOLTE (Huawei device)

Voici un IMS qui permet d'utiliser la voip sur une porteuse LTE (4G) pour les telephones Huawei hi6250 sous EMUI8 et sous android9 (PRA-LX1, WAS_LX1 etc..)

Pour l'installer vous devez demarrer le telephone sous recovery TWRP :

Ouvrir une session shell (adb shell), puis executer les ligens suivantes :

    echo " " >> /system/etc/prop.default;
    echo "# Ims" >> /system/etc/prop.default;
    echo "persist.sys.phh.ims.hw=true" >> /system/etc/prop.default;
    echo "persist.radio.calls.on.ims=1" >> /system/etc/prop.default;
    echo "persist.dbg.ims_volte_enable=1" >> /system/etc/prop.default;
    echo "persist.dbg.volte_avail_ovr=1" >> /system/etc/prop.default;
    echo "persist.dbg.vt_avail_ovr=0" >> /system/etc/prop.default;
    echo "persist.dbg.wfc_avail_ovr=0" >> /system/etc/prop.default;

    # Huawei config specific on EMUI 8 (Android 8)
    echo "ro.config.hw_volte_dyn=true" >> /system/etc/prop.default;
    echo "ro.config.hw_volte_on=true" >> /system/etc/prop.default;
    echo "ro.config.hw_volte_icon_rule=0" >> /system/etc/prop.default;

    # Iceows enable volte for my IMS Huawei
    echo "ro.hw.volte.enable=1" >> /system/etc/prop.default;

(facultatif) : Installer l'overlay qui permet de declarer au system l'ims volte, overlay qui porte le nom suivant : "treble-overlay-hw-ims.apk"

     adb shell mkdir /system/app/treble-overlay-telephony-hw-ims
     adb push treble-overlay-telephony-hw-ims.apk /system/app/treble-overlay-telephony-hw-ims/treble-overlay-telephony-hw-ims.apk
     adb shell chmod 644 /system/app/treble-overlay-telephony-hw-ims/treble-overlay-telephony-hw-ims.apkpk
     adb shell restorecon /system/overlay/treble-overlay-hw-ims.apk


(facultatif) : Installer l'application trebleapps qui permet de paramétrer l'IMS (creation de l'IMS/affiche les propriétés appel 4G etc...)

     adb push TrebleApp.apk /system/priv-app/TrebleApp
     adb shell chmod 644 /system/priv-app/TrebleApp/TrebleApp.apk 

Vous devez ensuite demarrer le telephone et créer une APN de type ims avec les meme mcc et 
