//================================================================================
// POV-Ray spectral rendering example
//
// demonstrates the use of spectral_minerals.inc and gemcut*.inc
//            
// command line (image dimension, OpenEXR)
// +w880 +h550 +FE 
// 
// command line (AA if no focal blur is used):
// +A0.05 +AM2 +R4
// 
// spectral calculation command line:
// +KI1 +KF36 +KFI38 +KFF73
//
//
// - Gems (gemcut21039.inc) by Richard B. Conley
// - Ive, September 2012
//
//================================================================================

#version 3.7; 

//================================================================================
// control center
//================================================================================

#declare MaxTrace    = 60;
#declare Radio       = 1;
#declare Photons     = 10000000;
#declare Sunlight    = 0;
#declare SkyEmission = 5;

#declare RoomDesign  = 1;

#declare Cam_Pos     = < 51, 85.0, 165>;
#declare Cam_Look    = < 50, 80.5, 175>; 
#declare Cam_Angle   = 30;
 
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
  light_source {0, SpectralEmission(E_Blackbody(5500)) * 60
  fade_power 2
  fade_distance 5

   #if (AreaLight)
    area_light z*5, y*5, 9,9 adaptive 1 circular orient
   #end 

    photons {reflection on refraction on }
  }

  sphere { 0, 5
    pigment { SpectralEmission(E_Blackbody(5500)) }    
    finish {emission 3 ambient 0 diffuse 0}
    no_shadow
    no_radiosity
  }
  
  translate <50, 130, 220>
}  
    
object {Lamp}    



// gem cut by Richard B. Conley
#include "gemcut21039.inc"



// feel free to experiment with variations of the crown/pavilion
// angles depending on the used minerals (i.e. the different IOR's)


#declare CrownAngle    = 30;
#declare PavilionAngle = 42;

#declare Sapphire = object { 
  Gem (CrownAngle, PavilionAngle)
  translate -y*Y10
  rotate -x*(90-PavilionAngle)

  M_Sapphire (0.6)   

  photons {target refraction on reflection on}
} 



#declare CrownAngle    = 33;
#declare PavilionAngle = 44;

#declare Emerald  = object {
  Gem (CrownAngle, PavilionAngle)
  translate -y*Y10
  rotate -x*(90-PavilionAngle)

  M_Emerald (0.45)

  photons {target refraction on reflection on}
} 



#declare CrownAngle    = 29;
#declare PavilionAngle = 42;

#declare Ruby     = object {
  Gem (CrownAngle, PavilionAngle)
  translate -y*Y10
  rotate -x*(90-PavilionAngle)

  M_Ruby (0.5)

  photons {target refraction on reflection on}
} 


union {
  object { Sapphire  rotate  y*60  translate <-1.4, 0.0001, 0.4> }
  object { Emerald   rotate  y*0   translate <-0.0, 0.0001, 0.0> }  
  object { Ruby      rotate -y*60  translate < 1.4, 0.0001, 0.4> }  

  rotate -y*0
  
  translate <50, 80, 175>
}           

    
    
//================================================================================
     