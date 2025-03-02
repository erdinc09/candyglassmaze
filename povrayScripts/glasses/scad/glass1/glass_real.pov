#version  3.7;

#include "colors.inc"           // Standard colors library
#include "shapes.inc"           // Commonly used object shapes
#include "textures.inc"         // LOTS of neat textures.  Lots of NEW textures.
#include "stones.inc"
#include "glass.pov"
#include "coordinates.pov"
#include "glass.inc"


#ifndef(PIECE_ALL2)
    #declare PIECE_ALL2 = 0;
#end
#ifndef(PIECE2_1)
    #declare PIECE2_1 = 0;
#end
#ifndef(PIECE2_2)
    #declare PIECE2_2 = 0;
#end
#ifndef(PIECE1_1)
    #declare PIECE1_1 = 0;
#end
#ifndef(PIECE1_2)
    #declare PIECE1_2 = 0;
#end

#ifndef(PIECE1)
    #declare PIECE1 = 0;
#end
#ifndef(PIECE2)
    #declare PIECE2 = 0;
#end

#ifndef(LOOP)
    #declare LOOP = 0;
#end
#ifndef(AXES)
    #declare AXES = 0;
#end
#ifndef(FLOOR)
    #declare FLOOR = 0;
#end
#ifndef(PIECE_ALL)
    #declare PIECE_ALL = 0;
#end
#ifndef(GLASS_TYPE)
    #declare GLASS_TYPE=1;
#end
#ifndef(CRACKED_PIECE1)
    #declare CRACKED_PIECE1=0;
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

/*
TODO: try deifferent light sources
light_source { 
    <200, 250, 250>
}
*/



//scad_background()

//background { rgb <0.25,0.25,0.25>} 





#if(PIECE_ALL2=0 & CRACKED_PIECE1=0 )
    //(PF_INT, "x", "x", 487),
    //(PF_INT, "y", "y", 883),
    //(PF_INT, "width", "w", 267),
    //(PF_INT, "height", "h", 267),
    
    object { 
        #if(PIECE1)
            glass1()
        #end
        #if(PIECE2)
            glass2()
        #end
        #if(PIECE1_1)
            glass1_1()
        #end
        #if(PIECE1_2)
            glass1_2()
        #end
        #if(PIECE_ALL)
            sphereTemplate()
        #end
        #if(PIECE2_1)
            glass2_1()
        #end
        #if(PIECE2_2)
            glass2_2()
        #end
        
        rotate<0,-60,90>
        #if(LOOP & PIECE1)
            rotate -clock*x
        #end
        #if(LOOP & PIECE2)
            rotate clock*x
        #end
        #if(LOOP & PIECE2_1)
            rotate clock*x
        #end
        #if(LOOP & PIECE2_2)
            rotate clock*x
        #end
        
        #if(LOOP & PIECE1_1)
            rotate -clock*x
        #end
        
        #if(LOOP & PIECE1_2)
            rotate -clock*x
        #end
        
        #if(PHOTON)
            photon_in_object()
        #end
        #if(FLOOR)
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
        #else
            
            texture { 
                pigment { color rgb< 0.0, 0.0, 0.0> } 
                finish { phong 1 reflection 0.00 } 
            }
            
        #end
        
        scale 0.25 // NOT: tam resim icin 0.5 lik yapalim!
        translate <0.5,1,-3>
        
    }
#end

#if(PIECE_ALL2)
    
    
    //(PF_INT, "x", "x", 736),
    //(PF_INT, "y", "y", 1049),
    //(PF_INT, "width", "w", 261),
    //(PF_INT, "height", "h", 261),
    
    sphere { 
        <0,0,0>, 0.5
        #if(FLOOR)
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
        #else
            texture { 
                pigment { color rgb< 0.0, 0.0, 0.0> } 
                finish { phong 1 reflection 0.00 } 
            }
        #end
        #if(PHOTON)
            photon_in_object()
        #end
        translate <1.5,0.5,-3.2>
    }
    
#end

#if(CRACKED_PIECE1)
    
    union { 
        //sphereTemplate()
        glassCracked1()
        //glass1_1()
        //glass1_2()
        //glass1()
        //glass2()
        //glass2_1()
        //glass2_2()
        
        //glassCracked1()
        // glassCracked2()
        
        //glassCracked3()
        
        #if(PHOTON)
            photon_in_object()
        #end
        
        #if(FLOOR)
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
        #else
            texture { 
                pigment { color rgb< 0.0, 0.0, 0.0> } 
                finish { phong 1 reflection 0.00 } 
            }
        #end
        //rotate <0,-90,90>
        //rotate <-30,-90,90>
        rotate <-30,-90,90>
        scale 0.25
        //translate <1.5,0.5,-3.2>
        translate <0.5,1,-3>
    }
#end

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

