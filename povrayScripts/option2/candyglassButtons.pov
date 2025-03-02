#include "candies.pov"
#include "spheres.inc"
#include "texts.inc"
#include "candybox.inc"
#include "functions.inc"
#include "coordinates.pov"


#ifndef(PHOTON)
    #declare PHOTON=0;
#end
#ifndef(AXES)
    #declare AXES = 0;
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


camera { 
    orthographic
    location <1, 5, -6>
    up    <0,1.33,0>
    right  <1,0,0>
    look_at <0,0,-0.8>
    angle 35
}
background { rgb <0,0,0,1> } 

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

light_source { 
    #if(PHOTON)
        <2000, 2500, 2000>
    #else
        <200, 250, 0>
    #end
    , 1 area_light z*60, y*60, 12, 12 adaptive 0
    
    #if(PHOTON)
        media_interaction on
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

/*
object
{ 
    CandySpiralTexture()
    rotate 40*y
    translate <-0.5,0.5,-3.6>
}

object
{ 
    sphere_button
    scale 2
    translate <-0.5,0.5,-3.6>
}
*/

object { 
    sphere_button
    #if(PHOTON)
        photon_in_object()
    #end
    scale 2
    translate <1.5,0.65,-3.2>
}


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
    finish { 
        specular 1
    }
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
