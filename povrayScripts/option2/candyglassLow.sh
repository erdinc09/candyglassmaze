GLASS_TYPE=1

 povray  -L"${POVRAY_SCAD_LIB}" -Icandyglass.pov +L../lib \
 +Oout/candyGlassLow.png Declare=SHOW_FOG=0 \
 Declare=SHOW_MEDIA=0 \
 Declare=PHOTON=0 Declare=GLASS_TYPE=$GLASS_TYPE \
 Declare=AXES=0  quickresLow.ini


#povray  -L"${POVRAY_SCAD_LIB}" -Icandyglass.pov +L../lib \
#+Oout/candyGlassLowText.png Declare=SHOW_FOG=0 \
#Declare=TEXT=1 \
#Declare=OTHER_THAN_TEXT=0 \
#Declare=FLOOR=0 \
#Declare=FLOOR_WITHOUT_LINES=1 \
#Declare=PHOTON=0 Declare=GLASS_TYPE=$GLASS_TYPE \
#Declare=AXES=1  quickresLow.ini