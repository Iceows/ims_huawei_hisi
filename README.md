# ims_huawei_hisi
 IMS for VOLTE (Huawei hisi device)

Voici un IMS qui permet d'utiliser la voip sur une porteuse LTE (4G) pour les telephones Huawei kirin sous EMUI9 et sous android9 (ANE-LX1,FIG-LX1 etc..)

Pour l'installer il faut installer l'overlay suivant, puis lui donner les droits

	chmod 644 /system/overlay/treble-overlay-hw-ims.apk
	restorecon /system/overlay/treble-overlay-hw-ims.apk

Et executer ce script sous TWRP :

    	echo " " >> /system/build.prop;
    	echo "# Ims" >> /system/build.prop;
    	echo "persist.sys.phh.ims.hw=true" >> /system/build.prop;
	echo "persist.radio.calls.on.ims=1" >> /system/build.prop;
    	echo "persist.dbg.ims_volte_enable=1" >> /system/build.prop;
    	echo "persist.dbg.volte_avail_ovr=1" >> /system/build.prop;
	

	# Pour activer mon ims
	echo "ro.hw.volte.enable=1" >> /system/build.prop;
