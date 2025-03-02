#!/usr/bin/env bash
povray -L${POVRAY_SCAD_LIB} +ua hint.ini \
+L../lib \
Declare=FLOOR=1 \
Declare=AXES=1 \
Declare=PHOTON=1 \
Declare=LOOP=1 \
Declare=GLASS_TYPE=5 \
Declare=FLOOR_WITHOUT_LINES=1