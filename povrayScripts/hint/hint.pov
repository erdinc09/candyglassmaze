#version  3.7;

#include "../lib/candies.pov"
#include "../lib/spheres.inc"
#include "../lib/texts.inc"
#include "../lib/candybox.inc"
#include "coordinates.pov"
#include "functions.inc"


#ifndef(PHOTON)
    #declare PHOTON=0;
#end
#ifndef(AXES)
    #declare AXES=0;
#end
#ifndef(FLOOR)
    #declare FLOOR=0;
#end
#ifndef(FLOOR_WITHOUT_LINES)
    #declare FLOOR_WITHOUT_LINES=0;
#end
#ifndef(LOOP)
    #declare LOOP=0;
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
    location <0, 10, 0>
    up    <0,1.33,0>
    right  <1,0,0>
    look_at <0,0,0>
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


#macro textureType()
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



object { 
    text { 
        ttf "gosmicksans.regular.ttf" "?" 0.1, 0
        #if(FLOOR)
            textureType()
        #else
            texture { 
                pigment { color rgb< 0.0, 0.0, 0.0> } 
                finish { phong 1 reflection 0.00 } 
            }
        #end
        #local x_= -0.150;
        #local y_ = 3;
        #local z_ = -0.07;
        
        
        
        translate<x_,y_,z_>//translate<-0.165,0,0>
        #if(LOOP)
            rotate clock*y
        #end
        translate<-x_,-y_,-z_>
        rotate 60*x //60
        translate<x_,y_,z_>
        
        translate<-0.008,0,-0.3>
        
    }
    scale 1.75
}


difference { 
    #local x_= -0.150;
    #local y_ = 2;
    #local z_ = -0.07;
    
    
    cylinder { 
        <0,y_,0>,<0,y_+0.2,0>,1.6
    }
    
    cylinder { 
        <0,y_-1,0>,<0,y_+2,0>,1.15
    }
    
    #if(FLOOR)
        textureType()
    #else
        texture { 
            pigment { color rgb< 0.0, 0.0, 0.0> } 
            finish { phong 1 reflection 0.00 } 
        }
    #end
    
     translate<0,0,0>
    
    /*
    translate<0,-y_+0.5,0>
    rotate -clock*z
    translate<0,y_-0.5,0>
    
    translate<0,-5,0>
    */
}




#if(FLOOR)
    
    plane
    { 
        y, -2
        pigment
        { 
            wood color_map { 
                [0 rgb <.9,.7,.3>][1 rgb <.8,.5,.2>]
            }
            turbulence .5
            scale <1, 1, 20>*.2
        }
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
