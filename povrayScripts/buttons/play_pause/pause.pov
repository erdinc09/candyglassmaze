#include "textures.inc"
#include "colors.inc"
#include "woods.inc"
#include "stones.inc"
#include "metals.inc"


global_settings { 
    assumed_gamma 1
    // No ambient light
    ambient_light White*0.0
}

camera { 
    orthographic
    location <0,0,1000>
    right 4*x up 4*y direction -z
    // Uncomment to render a side view
    // rotate 90*x
}

light_source { <-50,100,100>, White } 

// Rounded square button
#declare b_rsquare = superellipsoid { 
    // Adjust first parameter: 0=square, 1=circle
    <0.6,0.35>
    translate -z
    #if(PRESSED=1)
        scale <1,1,0.1> // pressed -> 0.1, released 0.5
    #end
    #if(RELEASED=1)
        scale <1,1,0.5> // pressed -> 0.1, released 0.5
    #end 
}

#declare b_rsquare2 = superellipsoid { 
    // Adjust first parameter: 0=square, 1=circle
    <0.6,0.35>
    translate -z
    #if(PRESSED=1)
        scale <1.8,1,0.1> // pressed -> 0.1, released 0.5
    #end
    #if(RELEASED=1)
        scale <1.8,1,0.5> // pressed -> 0.1, released 0.5
    #end 
}

// Hollow round button
#declare b_hround = difference { 
    sphere { <0,0,-0.1>,1 } 
    sphere { <0,0,1>,1 scale 1.4 } 
    scale <1,1,0.2>
    //translate -90 * z
}

object { 
    b_rsquare2
    texture
    { 
        //T_Wood29
        //DMFWood6 T_Wood2 T_Wood14 T_Wood29 T_Wood30
        DMFWood4 //DMFWood4 T_Wood1 T_Wood2 T_Wood20 T_Wood30
        scale 3
    } 
}
