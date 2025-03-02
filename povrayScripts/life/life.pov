#version  3.7;

#include "../lib/candies.pov"
#include "../lib/spheres.inc"
#include "../lib/candybox.inc"
#include "coordinates.pov"
#include "functions.inc"

#ifndef(FLOOR)
    #declare FLOOR=0;
#end
#ifndef(PHOTON)
    #declare PHOTON=0;
#end
#ifndef(AXES)
    #declare AXES=0;
#end


#macro scad_axes(axe_length)
    object { 
        AxisXYZ( axe_length, axe_length, axe_length, Tex_Dark, Tex_White)
    }
#end

#if(AXES)
    scad_axes(2)
#end

global_settings
{ 
    assumed_gamma 1
}

#if(PHOTON)
    global_settings { 
        max_trace_level 35
        assumed_gamma 2.2
        ambient_light rgb< 0,0,0>
        
        photons { 
            spacing 0.05
            autostop 0
            jitter 0.4
            count 1000000
            media 0
            max_trace_level 25
        }
        
    }
#end

camera { 
    location <1, 5, -6>
    up    <0,1.33,0>
    right  <1,0,0>
    look_at <0,0,-0.8>
    angle 35
}


light_source { 
    #if(PHOTON)
        //<2000, 2500, 2000>
        <200, 250, 10>
        #else
        <200, 250, 0>
    #end
    
    , 1 area_light z*60, y*60, 12, 12 adaptive 0
    media_interaction on
    #if(PHOTON)
        photons { 
            refraction on
            reflection on
        }
    #end
}

#macro photon_in_object()
    photons { 
        collect on
        refraction on
        reflection off
        target  1
    }
#end

#declare X =  text { 
    ttf "SoftMarshmallow.otf" "X" 0.1, 0
    rotate 50*x
    rotate 20*y
    scale 0.4
    translate <0.05,0.03,0>
}

#declare T_Dark_Green_Glass =
texture { 
    pigment { color rgbf <0.1, 1, 0.8, 0.8> } 
    finish { F_Glass4 } 
}

object { 
    X
    #if(FLOOR)
        #if(LIFE_3 & LIFE_2 & LIFE_1)
            texture { T_Dark_Green_Glass scale 0.5 } 
            interior { I_Glass4 } 
            
            #else
            texture { T_Ruby_Glass scale 0.5 } 
            interior { I_Glass4 } 
        #end
        #else
        texture { 
            pigment { color rgb< 0.0, 0.0, 0.0> } 
            finish { phong 1 reflection 0.00 } 
        }
    #end
    photon_in_object()
}

object { 
    X
    #if(FLOOR)
        #if(LIFE_1 & LIFE_2)
            texture { T_Dark_Green_Glass scale 0.5 } 
            interior { I_Glass4 } 
            #else
            texture { T_Ruby_Glass scale 0.5 } 
            interior { I_Glass4 } 
        #end
        #else
        texture { 
            pigment { color rgb< 0.0, 0.0, 0.0> } 
            finish { phong 1 reflection 0.00 } 
        }
    #end
    photon_in_object()
    translate <0.32,0,0>
}

object { 
    X
    #if(FLOOR)
        #if(LIFE_1)
            texture { T_Dark_Green_Glass scale 0.5 } 
            interior { I_Glass4 } 
            #else
            texture { T_Ruby_Glass scale 0.5 } 
            interior { I_Glass4 } 
        #end
        #else
        texture { 
            pigment { color rgb< 0.0, 0.0, 0.0> } 
            finish { phong 1 reflection 0.00 } 
        }
    #end
    photon_in_object()
    translate <0.64,0,0>
}



#if(FLOOR)
    
    plane
    { 
        y, 0
        pigment
        { 
            wood color_map { 
                [0 rgb <.9,.7,.3>][1 rgb <.8,.5,.2>]
            }
            turbulence .5
            scale <1, 1, 20>*.2
        }
        finish { specular 1 } 
    }
#end
