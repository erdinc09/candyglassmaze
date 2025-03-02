// http://xahlee.org/3d/index.html

// POV-Ray transparency study

#include "textures.inc"
#include "colors.inc"
#include "woods.inc"
#include "stones.inc"
#include "metals.inc"
#include "skies.inc"


global_settings { 
    assumed_gamma 1.0
}


light_source { 
    <1500,1500,1500>
    color White
}

camera { 
    location <0,2,0>
    look_at  <0,0,0>
    //translate -.8
}

plane
{ 
    <0,1,0> // normal vector
    , -3 // distance from origin
    pigment
    { 
        DMFWood4
        //DMFLightOak
        turbulence 2.5
        //scale <1, 1, 20>*.22
        scale 5
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


