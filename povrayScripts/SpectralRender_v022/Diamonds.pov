//================================================================================
// POV-Ray spectral rendering example
//            
// command line (image dimension, OpenEXR)
// +w1024 +h640 +FE 
// 
// command line (AA if no focal blur is used):
// +A0.1 +AM2 +R4
// 
// spectral calculation command line:
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

#declare MaxTrace    = 40;
#declare Radio       = 1;
#declare Photons     = 9000000;
#declare Sunlight    = 0;
#declare SkyEmission = 5;

#declare RoomDesign  = 1;

#declare Cam_Pos     = < 50, 85.0, 165>;
#declare Cam_Look    = < 50, 80.1, 175>; 
#declare Cam_Angle   = 21;
 
//================================================================================

#include "spectral.inc"

#include "world.inc"


//================================================================================
// sky, room ...
//================================================================================

object { Sky }        

object { Room }

object { Table  
  scale <0.6, 1, 0.6>
  rotate y*25
  translate <60,0,175>
}


//================================================================================
// objects in scene
//================================================================================

#declare Lamp = union {
  light_source {0, SpectralEmission(E_Blackbody(5000)) * 60
  fade_power 2
  fade_distance 5

   #if (AreaLight)
    area_light z*5, y*5, 9,9 adaptive 1 circular orient
   #end 

    photons {reflection on refraction on }
  }

  sphere { 0, 5
    pigment { SpectralEmission(E_Blackbody(5000)) }    
    finish {emission 3 ambient 0 diffuse 0}
    no_shadow
    no_radiosity
  }
  
  translate <50, 140, 160>
}  
    
object {Lamp}    



#include "brilliant.inc"
   
#declare MyGem = object { Brilliant rotate x*90 translate y*0.66 scale 0.5
  M_Diamond_FancyYellow(0.75)
  photons {target refraction on reflection on}
} 


#declare PavlAng = 40.75;

union {
  object { MyGem  rotate -x*PavlAng  translate -z * 0.6 }
  object { MyGem  rotate  z*PavlAng  translate -x * 1.0 }  
  object { MyGem  rotate  x*PavlAng  translate  x * 1.2 }  

  rotate -y*0
  
  translate <50, 80, 175>
}           

    
    
//================================================================================
     