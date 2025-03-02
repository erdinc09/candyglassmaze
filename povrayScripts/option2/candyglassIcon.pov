#include "candies.pov"
#include "spheres.inc"
#include "texts.inc"
#include "candybox.inc"
#include "functions.inc"



#ifndef(PHOTON)
    #declare PHOTON=0;
#end
#ifndef(FLOOR)
    #declare FLOOR=1;
#end
camera { 
    //location <-1, 3, -5>
    location <0, 4, -5>
    up    <0,1.33,0>
    right  <1,0,0>
    //look_at <-2,0,-0.8>
    look_at<-1.2,0.5,-1.4>
    angle 35
}

#if(PHOTON)
    global_settings
    { 
        assumed_gamma 1
    }
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

#macro photon_in_object()
    photons { 
        collect on
        refraction on
        reflection off
        target  1
    }
#end


light_source { 
    #if(PHOTON)
        <2000, 2500, 2000>
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

union { 
    //big spiral
    object { 
        CandySpiralTexture()
        scale 1
        translate <-1.15,0.55,-1.5>
    }
    
    object { 
        sphere_high
        texture { T_Dark_Green_Glass } 
        interior { I_Glass4 } 
        #if(PHOTON)
            photon_in_object()
        #end
        rotate 210*x
        rotate -20*z
        translate <-1.2,0.5,-1.4>
    }
}

#if(FLOOR)
    
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
#end
