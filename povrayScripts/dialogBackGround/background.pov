// http://xahlee.org/3d/index.html

// POV-Ray transparency study

#include "textures.inc"
#include "colors.inc"
#include "woods.inc"
#include "stones.inc"
#include "metals.inc"
#include "skies.inc"
#include "coordinates.pov"

global_settings { 
    assumed_gamma 1.0
}


light_source { 
    <1500,1500,1500>
    color White
    
}

camera { 
    location <0,6,0>
    look_at  <0,0,0>
    //translate -.8
    up    <0,1.33,0>
    right  <1,0,0>
}

#macro scad_axes(axe_length)
    object { 
        AxisXYZ( axe_length, axe_length, axe_length, Tex_Dark, Tex_White)
    }
#end

//scad_axes(2)
box { 
    <-1,0,-1>
    <2,1,2>
    //<1,1,1>
    texture { 
        pigment
        { 
            DMFWood4
            //DMFLightOak
            turbulence 2.5
            //scale <1, 1, 20>*.22
            scale 5
        }
        finish { specular 1 } 
    }
    
    translate<-0.5,0,-0.5>
}



box { 
    <-1.35,0,-1.25>
    <2.35,1,2.25>
    //<1,1,1>
    texture { 
        T_Wood22 //T_Wood22 T_Wood17
    }
    
    translate<-0.5,0,-0.5>
}

/*
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
    
    /*
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
    
    */
}
*/


