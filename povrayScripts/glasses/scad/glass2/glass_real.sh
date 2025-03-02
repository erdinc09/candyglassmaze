#!/usr/bin/env bash
povray -L${POVRAY_SCAD_LIB} +ua glass_real.ini \
Declare=FLOOR=1 \
Declare=AXES=0 \
Declare=PIECE_ALL=1 \
Declare=PHOTON=1 \
Declare=PIECE_ALL2=0 \
Declare=PIECE2=0 \
Declare=GLASS_TYPE=1 \
Declare=CRACKED_PIECE1=0 \
Declare=FLOOR_WITHOUT_LINES=1
