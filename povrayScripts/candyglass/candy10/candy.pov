#include "candies.pov"
#include "spheres.inc"
#include "texts.inc"
#include "candybox.inc"
#include "functions.inc"


camera { 
    orthographic
    location <1, 5, -6>
    up    <0,1,0>
    right  <1,0,0>
    look_at <0,0,0>
    angle 35
}

light_source { 
    <200, 250, 0>, 1 area_light z*60, y*60, 12, 12 adaptive 0
}

#local Candy_Cane_Orange= pigment { 
    gradient x+y
    color_map { 
        [0.25 rgb <0,128/255,0>]
        [0.25 rgb <1,1,1>]
        [0.75 rgb <1,1,1>]
        [0.75 rgb <0,128/255,0>]
    }
}

object { 
    SpiralCandy(Candy_Cane_Orange)
    scale 0.015
    rotate clock*z
    translate <-0,0,0.12>
    scale 11
    rotate 300*x
}


