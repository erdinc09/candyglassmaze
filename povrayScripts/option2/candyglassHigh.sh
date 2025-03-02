GLASS_TYPE=2

#povray  -L${POVRAY_SCAD_LIB} -Icandyglass.pov +ua +Oout/candyGlassHigh.png +L../lib Declare=SHOW_FOG=0 Declare=SHOW_MEDIA=0 Declare=PHOTON=1 Declare=AXES=0 Declare=GLASS_TYPE=$GLASS_TYPE  quickresHigh.ini
#povray  -L${POVRAY_SCAD_LIB} -Icandyglass.pov +Oout/candyGlassHigh.png +L../lib Declare=SHOW_FOG=0 Declare=SHOW_MEDIA=0 Declare=PHOTON=1 Declare=AXES=0 Declare=TEXT=1 Declare=GLASS_TYPE=$GLASS_TYPE  quickresHigh.ini

#povray  -Icandyglass.pov +Oout/candyGlassHigh_FOG.png Declare=SHOW_FOG=1 Declare=SHOW_MEDIA=0  quickresHigh.ini
#povray  -Icandyglass.pov +Oout/candyGlassHigh_MEDIA.png Declare=SHOW_FOG=0 Declare=SHOW_MEDIA=1  quickresHigh.ini
#povray  -Icandyglass.pov +Oout/candyGlassHigh_FOG_MEDIA.png Declare=SHOW_FOG=1 Declare=SHOW_MEDIA=1  quickresHigh.ini

povray  -L${POVRAY_SCAD_LIB} -Icandyglass.pov +L../lib \
+Oout/candyGlassHighText.png Declare=SHOW_FOG=0 \
Declare=TEXT=1 \
Declare=OTHER_THAN_TEXT=0 \
Declare=FLOOR=1 \
Declare=PHOTON=0 Declare=GLASS_TYPE=$GLASS_TYPE \
Declare=FLOOR_WITHOUT_LINES=1 \
Declare=AXES=0  quickresHigh.ini

povray +ua -L${POVRAY_SCAD_LIB} -Icandyglass.pov +L../lib \
+Oout/candyGlassHighTextTemplate.png Declare=SHOW_FOG=0 \
Declare=TEXT=1 \
Declare=OTHER_THAN_TEXT=0 \
Declare=FLOOR=0 \
Declare=PHOTON=0 Declare=GLASS_TYPE=$GLASS_TYPE \
Declare=FLOOR_WITHOUT_LINES=1 \
Declare=AXES=0  quickresHigh.ini