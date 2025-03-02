#version  3.7;

#include "colors.inc"           // Standard colors library
#include "shapes.inc"           // Commonly used object shapes
#include "textures.inc"         // LOTS of neat textures.  Lots of NEW textures.
#include "coordinates.pov"
#include "metals.inc"

#macro scad_axes(axe_length)
    object { 
        AxisXYZ( axe_length, axe_length, axe_length, Tex_Dark, Tex_White)
    }
#end


#ifndef(FLOOR_WITHOUT_LINES)
    #declare FLOOR_WITHOUT_LINES=0;
#end
#ifndef(PHOTON)
    #declare PHOTON=0;
#end

#if(AXES)
    scad_axes(2)
#end

#ifndef(FLOOR_TYPE)
    #declare FLOOR_TYPE=2;// 1 --> light, 2-->dark
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
        <2000, 2500, -1000>
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

#macro photon_in_object()
    photons { 
        collect on
        refraction on
        reflection off
        target  1
    }
#end






sphere { 
    <0,0,0>, 0.5
    #if(FLOOR)
        texture { T_Chrome_5A } //T_Silver_2A  T_Silver_1D T_Chrome_5A
        
    #else
        texture { 
            pigment { color rgb< 0.0, 0.0, 0.0> } 
        }
    #end
    
    #if(PHOTON)
        photon_in_object()
    #end
    translate <1.5,0.5,-3.2>
}


#if(FLOOR)
    plane
    { 
        y, 0
        
        #if(FLOOR_TYPE=1)
            pigment
            { 
                wood color_map { 
                    [0 rgb <.9,.7,.3>]
                    [1 rgb <.8,.5,.2>]
                }
                turbulence .5
                scale <1, 1, 20>*.2
            }
        #end
        #if(FLOOR_TYPE=2)
            pigment
            { 
                DMFWood4
                //DMFLightOak
                turbulence 2.5
                //scale <1, 1, 20>*.22
                scale 5
            }
        #end
        
        finish { specular 1 } 
        
        #if(FLOOR_WITHOUT_LINES=0)
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
        #end
        
    }
#end

