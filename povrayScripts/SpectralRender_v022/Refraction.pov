//================================================================================
// POV-Ray spectral rendering example
//
// Example for chromatic aberration of different glass types.
//            
//
// command line (image dimension, OpenEXR)
// +w880 +h660 +FE 
// 
// command line (AA if no focal blur is used):
// +A0.1 +AM2 +R3
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

#declare MaxTrace    = 15;
#declare Radio       = 1;
#declare Sunlight    = 0;
#declare SkyEmission = 12;

#declare RoomDesign = 1;

#declare Cam_Pos   = < 60, 91,   150>;
#declare Cam_Look  = < 55, 83.5, 175>; 
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

#declare GlassType = 3;

#switch (GlassType)  
  #case (1)
    #declare IOR = IOR_Glass_BAF10;     // Optical Glass N-BAF10
    #break 
  #case (2)
    #declare IOR = IOR_Glass_BAK1;      // Optical Glass N-BAK1
    #break 
  #case (3)
    #declare IOR = IOR_Glass_BK7;       // Optical Glass N-BK7
    #break 
  #case (4)
    #declare IOR = IOR_FlintGlass_F5;   // Flint Glass F5
    #break 
  #case (5)
    #declare IOR = IOR_FlintGlass_SF2;  // Dense Flint Glass SF2
    #break 
  #case (6)
    #declare IOR = IOR_CrownGlass_K10;  // Crown Glass K10
    #break 
  #case (7)
    #declare IOR = IOR_CrownGlass_SK5;  // Dense Crown Glass SK5
    #break 
#end

    
#declare MagGlass = union {
  intersection {
    sphere {0, 16 translate  y*15.6}
    sphere {0, 16 translate -y*15.6}
    cylinder {<0,-1,0>, <0,1,0>, 3.01}

    M_Spectral_Filter (D_Average(D_CC_A4, 1, Value_1, 1), IOR, 15)
  }
            
  union {          
    difference {
      cylinder {<0,-0.4,0>, <0,0.4,0>, 3.2}
      cylinder {<0,-1,0>, <0,1,0>, 3}  
      torus {3, 0.1}
    }                               
    torus {3.1, 0.1 scale <1,0.5,1> translate  y*0.4}
    torus {3.1, 0.1 scale <1,0.5,1> translate -y*0.4}

    difference {
      cylinder {<-0.5,0,0>, <0.5,0,0>, 0.3}
      torus {0.35, 0.11 rotate z*90}
      translate x*3.5
    }                  
    
    M_GoldCopper_Alloy (35, 0.75)
  } 
  
  union {
    cylinder {<4,0,0>, <10,0,0>, 0.3}
    sphere {0, 0.3 scale <0.45,1,1> translate x*10}
    
    M_Spectral_Shiny (D_CC_E4, 0.33, IOR_15)
  } 
  
}       


#local Txt =  text { ttf "timrom.ttf"                
  "Refraction" 1, 0
  scale 0.44
  translate <-0.94, 0, -1.0001>
  M_Spectral_Matte (D_CC_F4)
}

                        
#declare Cube = union {
  box {-1, 1 M_Spectral_Matte (D_CC_A4) }
  object {Txt}
  object {Txt rotate -y*90}
  translate y*1
  scale 2.5
}


object { MagGlass
  rotate  y*15
  rotate -x*80
  rotate -y*12

  translate y*3.5
  
  translate <55.6, 81.5, 170>
}  
  

object { Cube
  translate <51, 80, 180>
}  

object { Cube
  translate <58, 80, 182>
}  

object { Cube     
  rotate -y*5
  translate <55, 85, 181>
}  


//================================================================================
     