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
    
    /*
    texture { 
        pigment { 
            brick
            color rgb<1,1,1>        // mortar
            color rgb<0.8,0.25,0.1> // brick
            brick_size <0.2484, 0.0507, 0.125>
            mortar 0.008
            scale <0.4,1.3,1.1>
            warp { 
                spherical
                orientation <0,0,1>
                dist_exp 0
            } // end of warp
        } // end of pigment --------------------
        rotate<-90,0,0> scale 1 translate<0,0,0>
    } // end of texture ------------------------------
    */
    // scale your object first!!!
    texture { 
        pigment { 
            crackle scale 1.5 turbulence 0.1
            color_map { 
                [0.04 color Black]
                [0.09 color Black]
                [0.12 color rgb<1,0.65,0>]
                [1.00 color rgb<1,0.65,0>]
            } // end of color_map
            scale 0.15
        } // end of pigment
        
        normal { bumps 0.75 scale 0.02 } 
        finish { phong 1 } 
        rotate<0,0,0> scale 5  translate<0.01, 0.04, 0.00>
    } // end of texture -------------------------
    
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
