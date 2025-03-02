#version  3.7;

#include "../lib/candies.pov"
#include "../lib/spheres.inc"
#include "../lib/texts.inc"
#include "../lib/candybox.inc"
#include "coordinates.pov"
#include "functions.inc"

//#declare SHOW_FOG = 0; //1 ise fog var
//#declare SHOW_MEDIA = 0; //1 ise media var
#declare TEXT=0;
#declare OTHER_THAN_TEXT=1;
#declare FLOOR=1;

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

#macro brokenSphereGlassType()
    #if(GLASS_TYPE = 1)
        texture { T_Glass3 } 
        interior { I_Glass4 } 
    #elseif (GLASS_TYPE = 2)
        texture { T_Dark_Green_Glass } 
        interior { I_Glass4 } 
    #elseif (GLASS_TYPE = 3)
        texture { T_Vicksbottle_Glass } 
        interior { I_Glass4 } 
    #elseif (GLASS_TYPE = 4)
        texture { T_Orange_Glass } 
        interior { I_Glass4 } 
    #elseif (GLASS_TYPE = 5)
        texture { T_Ruby_Glass } 
        interior { I_Glass4 } 
    #end
#end


#declare candies = union { 
    
    object { 
        CandyGlassLike2()
        translate <0.2,0.1,-1.6>
    }
    
    object { 
        CandyGlassLike2()
        rotate 30*z
        translate <-0.2,0.3,-1.6>
    }
    
    object { 
        CandyGlassLike1()
        translate <-0.6,0.1,-2.2>
        
    }
    object { 
        CandyGlassLike3()
        translate <0.2,0.1,-2.2>
        
    }
    
    object { 
        CandyGlassLike4()
        translate <0.5,0.1,-1.0> 
    }
    
    object { 
        Candy(pigment { color rgb< 0.0, 1.0, 0.0> } ) //black
        translate <0.8, 0.1,-2.2>
    }
    
    object { 
        Candy(pigment { color rgb< 1.0, 1.0, 0.0> } ) //yellow
        translate <0.3, 0.1,-2.8>
    }
    object { 
        Candy(pigment { color rgb< 1.0, 0.0, 0.0> } ) //  red
        translate <0.7, 0.1,-2.7>
    }
}

union { 
    
    #if (OTHER_THAN_TEXT)
        union { 
            //big spiral
            object { 
                CandySpiralTexture()
                scale 1
                translate <-0.75,0.55,-1.5>
            }
            
            object { 
                sphere_high
                brokenSphereGlassType()
                #if(PHOTON)
                    photon_in_object()
                #end
                rotate 210*x
                rotate -20*z
                translate <-0.8,0.5,-1.4>
            }
        }
        
        
        object { 
            candies
            translate <-0.5,0,0.5>
        }
        
        #local Candy_Cane_Green = pigment { 
            gradient x+y
            color_map { 
                [0.25 rgb <0/255,128/255,0/255>]
                [0.25 rgb <1,1,1>]
                [0.75 rgb <1,1,1>]
                [0.75 rgb <0/255,128/255,0/255>]
            }
        }
    #end
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
