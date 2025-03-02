#povray  -L${POVRAY_SCAD_LIB} +O./build/hint.png \
#loop_all.ini \
#+L../lib \
#Declare=FLOOR=1 \
#Declare=AXES=0 \
#Declare=PHOTON=1 \
#Declare=LOOP=1 \
#Declare=GLASS_TYPE=5 \
#Declare=FLOOR_WITHOUT_LINES=1



povray  -L${POVRAY_SCAD_LIB} +ua  +O./build/template/hint.png \
loop_all.ini \
+L../lib \
Declare=FLOOR=0 \
Declare=AXES=0 \
Declare=PHOTON=1 \
Declare=LOOP=1 \
Declare=GLASS_TYPE=5 \
Declare=FLOOR_WITHOUT_LINES=1

