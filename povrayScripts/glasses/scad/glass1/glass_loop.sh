rm ./piece1/*.png
rm ./piece1/template/*.png
rm ./piece1/out/*.png

rm ./piece2/*.png
rm ./piece2/template/*.png
rm ./piece2/out/*.png


rm ./piece1_1/*.png
rm ./piece1_1/template/*.png
rm ./piece1_1/out/*.png

rm ./piece2_1/*.png
rm ./piece2_1/template/*.png
rm ./piece2_1/out/*.png

rm ./piece1_2/*.png
rm ./piece1_2/template/*.png
rm ./piece1_2/out/*.png

rm ./piece2_2/*.png
rm ./piece2_2/template/*.png
rm ./piece2_2/out/*.png


GLASS_TYPE=5

povray  -L${POVRAY_SCAD_LIB} +O./piece1/glass.png Declare=FLOOR=1 Declare=LOOP=1 Declare=PIECE1=1 Declare=GLASS_TYPE=$GLASS_TYPE Declare=PHOTON=1 loop.ini
povray  -L${POVRAY_SCAD_LIB} +O./piece2/glass.png Declare=FLOOR=1 Declare=LOOP=1 Declare=PIECE2=1 Declare=GLASS_TYPE=$GLASS_TYPE Declare=PHOTON=1 loop.ini


povray  -L${POVRAY_SCAD_LIB} +ua +O./piece1/template/glass.png Declare=LOOP=1 Declare=PIECE1=1 loop.ini
povray  -L${POVRAY_SCAD_LIB} +ua +O./piece2/template/glass.png Declare=LOOP=1 Declare=PIECE2=1 loop.ini

povray  -L${POVRAY_SCAD_LIB} +O./piece1_1/glass.png Declare=FLOOR=1 Declare=LOOP=1 Declare=PIECE1_1=1 Declare=GLASS_TYPE=$GLASS_TYPE Declare=PHOTON=1 loop.ini
povray  -L${POVRAY_SCAD_LIB} +O./piece1_2/glass.png Declare=FLOOR=1 Declare=LOOP=1 Declare=PIECE1_2=1 Declare=GLASS_TYPE=$GLASS_TYPE Declare=PHOTON=1 loop.ini

povray  -L${POVRAY_SCAD_LIB} +ua +O./piece1_1/template/glass.png Declare=LOOP=1 Declare=PIECE1_1=1 loop.ini
povray  -L${POVRAY_SCAD_LIB} +ua +O./piece1_2/template/glass.png Declare=LOOP=1 Declare=PIECE1_2=1 loop.ini

povray  -L${POVRAY_SCAD_LIB} +O./piece2_1/glass.png Declare=FLOOR=1 Declare=LOOP=1 Declare=PIECE2_1=1 Declare=GLASS_TYPE=$GLASS_TYPE Declare=PHOTON=1 loop.ini
povray  -L${POVRAY_SCAD_LIB} +O./piece2_2/glass.png Declare=FLOOR=1 Declare=LOOP=1 Declare=PIECE2_2=1 Declare=GLASS_TYPE=$GLASS_TYPE Declare=PHOTON=1 loop.ini

povray +ua  -L${POVRAY_SCAD_LIB} +O./piece2_1/template/glass.png Declare=LOOP=1 Declare=PIECE2_1=1  loop.ini
povray  +ua -L${POVRAY_SCAD_LIB} +O./piece2_2/template/glass.png Declare=LOOP=1 Declare=PIECE2_2=1 loop.ini
        
#povray  -L${POVRAY_SCAD_LIB} +O./piece_all/glass.png Declare=FLOOR=1 Declare=PIECE_ALL=1 Declare=GLASS_TYPE=$GLASS_TYPE Declare=PHOTON=1 glass_real.ini
#povray  -L${POVRAY_SCAD_LIB} +ua +O./piece_all/template/glass.png Declare=PIECE_ALL=1 glass_real.ini


povray  -L${POVRAY_SCAD_LIB} +O./piece_all/glass.png Declare=FLOOR=1 Declare=PIECE_ALL2=1 Declare=GLASS_TYPE=$GLASS_TYPE Declare=PHOTON=1 glass_piece_all.ini
povray  -L${POVRAY_SCAD_LIB} +ua +O./piece_all/template/glass.png Declare=PIECE_ALL2=1 glass_piece_all.ini
