//================================================================================
// POV-Ray spectral rendering example
//            
// command line (image dimension, OpenEXR)
// +w880 +h660 +FE 
// 
// command line (AA if no focal blur is used):
// +A0.1 +AM2 +R4
// 
// spectral calculation command line:
// +KI1 +KF36 +KFI38 +KFF73
//
//
// - Ive, September 2012
//
//================================================================================

#version 3.7; 

//================================================================================
// control center
//================================================================================

#declare MaxTrace    = 24;
#declare Radio       = 1;
#declare Photons     = 2000000;
#declare Sunlight    = 1.2;
#declare SkyEmission = 10;
#declare FocalBlur   = off; // turn it only on if you are very patient !

#declare RoomDesign = 4;

#declare Cam_Pos   = < 60, 92, 120>;
#declare Cam_Look  = < 53, 88, 175>; 
#declare Cam_Angle = 30;
 
//================================================================================

#include "spectral.inc"
#include "world.inc"

//================================================================================
// sky, room ...
//================================================================================

object { Sky }        

object { Room }

object { Table
  rotate y*6
  translate <70,0,175>
}


//================================================================================
// objects in scene
//================================================================================
    
#include "wineglass.inc" 
#include "ring.inc"


object { 
  Wineglass_filled ( 
    // glass
    M_Spectral_Filter (D_Average(D_CC_A4, 1, Value_1, 2), IOR_CrownGlass_K7, 10),
    // liquid 
    M_Spectral_Filter (D_Average(D_CC_C2, 1, Value_1, 1), IOR_Water, 10),
    // liquid hight
    0.64
  )

  translate <55, 80, 175>
} 
       

#declare M_Enamel = material {
  texture {   
    pigment { bozo
      color_map {
        [0.0 C_Spectral_Filter (D_RGB(0.03, 0.04, 0.67), Value_1) ]
        [1.0 C_Spectral_Filter (D_RGB(0.03, 0.08, 0.75), Value_1) ]
      }
      scale 0.01
    }
    finish {
      ambient 0  diffuse 0
      reflection {0 1 fresnel on metallic 0.1} conserve_energy
    } 
    normal {bumps 0.005 scale 0.075}
  }
  interior {
    IOR_Spectral(IOR_CrownGlass_SK5)
    fade_power 1001
    fade_distance 0.02
    FadeColor_Spectral (D_RGB(0.05, 0.10, 0.80))
  }
} 
       
   
object {
  Ring (
    M_GoldCopper_Alloy(25, 0.85),
    M_Enamel
  ) 

  rotate y*5
  translate <50, 80, 169> 
  photons {target}
}
   

//================================================================================
     