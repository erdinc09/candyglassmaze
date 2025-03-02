#!/usr/bin/env bash
GLASS_TYPE=2

povray  -L${POVRAY_SCAD_LIB} -Icandyglass.pov +L../../lib \
+Oout/candyGlassLow.png Declare=SHOW_FOG=0 \
Declare=SHOW_MEDIA=0 \
Declare=PHOTON=1 Declare=GLASS_TYPE=$GLASS_TYPE \
Declare=AXES=1  quickresLow.ini
