#povray -L${POVRAY_SCAD_LIB} ball.ini \
#Declare=FLOOR=1 \
#Declare=AXES=1 \
#Declare=PHOTON=1 \
#Declare=FLOOR_WITHOUT_LINES=1 \
#Declare=FLOOR_WITHOUT_LINES=0



povray -L${POVRAY_SCAD_LIB} ball.ini \
+ua  -L${POVRAY_SCAD_LIB} +O./template/ball.png \
Declare=AXES=0 \
Declare=FLOOR=0 \
Declare=PHOTON=0