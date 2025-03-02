//================================================================================
// POV-Ray spectral rendering example
//
// Demonstrates the use of custom tailored materials based on the ones from
// "spectral_materials.inc"
//
// Be warned, depending on the used AA settings this might render quite slow!
//            
// command line (image dimension, OpenEXR):
// +w880 +h660 +FE
// 
// command line (AA if no focal blur is used):
// +A0.01 +AM2 +R4
// 
// command line to run spectral calculation:
// +KI1 +KF36 +KFI38 +KFF73
//
//
// - Ive, November 2013
//
//================================================================================

#version 3.7; 

//================================================================================
// control center
//================================================================================

#declare Radio       = 1;
#declare Photons     = 300000;
#declare Sunlight    = 1;
#declare SkyEmission = 5;

#declare RoomDesign  = 3;

#declare Cam_Pos   = < 72.0, 87.0, 155.0>;
#declare Cam_Look  = < 64.5, 80.5, 167.5>;
#declare Cam_Angle = 27;

#declare FocalBlur = off;

//================================================================================

#include "spectral.inc"
#include "world.inc"
#include "ring.inc"

//================================================================================
// sky, room ...
//================================================================================

object {Sky}        

object {Room}

object { Table
  rotate y*6
  translate <70,0,175>
}


//================================================================================
// objects in scene
//================================================================================  

#declare M_Enamel = material {
  texture {   
    pigment { bozo
      color_map {
        [0.0 C_Spectral_Filter (D_RGB(0.03, 0.03, 0.67), Value_1) ]
        [1.0 C_Spectral_Filter (D_RGB(0.03, 0.05, 0.75), Value_1) ]
      }
      scale 0.1 warp {spherical orientation y dist_exp 0}
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
    FadeColor_Spectral (D_RGB(0.03, 0.05, 0.75))
  }
} 



// *** put the rings ***

object { Ring ( M_GoldSilver_Alloy(75, 0.85), M_Enamel )
  translate <65, 80, 169> 
  photons {target}
}


object { Ring ( M_GoldCopper_Alloy(25, 0.80), M_Silver(0.85) ) 
  rotate y*5
  translate <65, 80, 165> 
  photons {target}
}


object { Ring ( M_GoldSilver_Alloy(25, 0.75), M_Enamel )
  rotate z*90  
  rotate -y*71
  translate <63, 80 + Ring_OuterDiameter, 169> 
  photons {target}
}



//================================================================================
     