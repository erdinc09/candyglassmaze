#include "candies.pov"
#include "spheres.inc"
#include "texts.inc"
#include "candybox.inc"
#include "functions.inc"


camera { 
    orthographic
    location <1, 5, -6>
    up    <0,1.33,0>
    right  <1,0,0>
    look_at <0,0,-0.8>
    angle 35
}

light_source { 
    <200, 250, 0>, 1 area_light z*60, y*60, 12, 12 adaptive 0
}


object { 
    CandySpiralTexture()
    rotate 40*y
    translate <-0.5,0.5,-3.6>
}

/*
object { 
    sphere_button
    scale 2
    translate <-0.5,0.5,-3.6>
}





plane
{ 
    y, 0
    pigment
    { 
        wood color_map { [0 rgb <.9,.7,.3>][1 rgb <.8,.5,.2>] } 
        turbulence .5
        scale <1, 1, 20>*.2
    }
    finish { specular 1 } 
    normal
    { 
        gradient x 1
        slope_map
        { 
            [0 <0, 1>] // 0 height, strong slope up
            [.05 <1, 0>] // maximum height, horizontal
            [.95 <1, 0>] // maximum height, horizontal
            [1 <0, -1>] // 0 height, strong slope down
        }
    }
}
*/
