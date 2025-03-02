GLASS_TYPE=5
rm ./piece1/*.png
rm ./piece1/out/*.png
rm ./piece1/template/*.png
povray  -L${POVRAY_SCAD_LIB} +O./piece1/glass.png Declare=FLOOR=1 Declare=LOOP=1 Declare=PIECE1=1 Declare=GLASS_TYPE=$GLASS_TYPE Declare=PHOTON=1 Declare=FLOOR_WITHOUT_LINES=1 loop.ini
povray  -L${POVRAY_SCAD_LIB} +ua +O./piece1/template/glass.png Declare=LOOP=1  Declare=PIECE1=1 loop.ini

