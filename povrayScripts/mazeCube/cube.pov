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
    location <3,5,3>
    look_at  <0,0,0>
    //translate -.8
    //up    <0,1.33,0>
    right  <1,0,0>
}

#macro scad_axes(axe_length)
    object { 
        AxisXYZ( axe_length, axe_length, axe_length, Tex_Dark, Tex_White)
    }
#end

scad_axes(2)
box { 
    <-1,0,-1>
    <1,1,1>
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

