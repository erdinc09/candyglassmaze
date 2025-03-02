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
    <1500, 1500, 1500>// hepsi 1500
    color White
}

camera { 
    location <0,10,0>
    look_at  <0,0,0>
    //up    <0,1.33,0>
    right  <1,0,0>
}



sphere { 
    <0,0,0>, 0.45
    
    translate <0,0,0>
    scale<9.5,9.5,9.5>
    texture { T_Stone23 } //T_Stone24,[23],29,33,34
    rotate clock*z
    
}


#if(FLOOR)
    plane
    { 
        <0,1,0> // normal vector
        , -10 // distance from origin
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
        //translate <0,0,10*clock>
        translate <10*clock,0,0>
    }
#end
