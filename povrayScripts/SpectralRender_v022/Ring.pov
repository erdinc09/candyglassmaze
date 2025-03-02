//================================================================================
// POV-Ray spectral rendering example
//
// Demonstrates the use of custom tailored materials based on the ones from
// "spectral_materials.inc"
//
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
// - Ive, September 2012
//
//================================================================================

#version 3.7; 

//================================================================================
// control center
//================================================================================

#declare Radio       = 1;
#declare Photons     = 100000;
#declare Sunlight    = 1;
#declare SkyEmission = 3;

#declare RoomDesign  = 3;

#declare Cam_Pos   = < 72, 87, 160>;
#declare Cam_Look  = < 66, 80, 170>;
#declare Cam_Angle = 25;

//================================================================================

#include "spectral.inc"
#include "world.inc"

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
        
// *** custom materials ***
        
#local Reflect = 0.85;
#local Silver  = 30;


#macro User_Spectral_Metal (N, K, Reflectance)
  material {
    texture {                              
      pigment {
        pigment_pattern {image_map{png "ornament" interpolate 2}
          rotate x*90 translate -z*0.04 scale <0.1667,0.78,0.78> 
        }
        color_map {   
          [0 C_Average( D_Metal(N, K), 1, D_CC_F4, 2) ]
          [1 C_Metal(N, K) ]
        }  
        warp {cylindrical orientation y dist_exp 0 }
      }
      finish {
        ambient 0  emission 0  diffuse (0.5 - Reflectance*0.495)
        reflection {0 Reflectance fresnel on 
          #if (WavelengthIndex < 0) metallic #end
        }
        conserve_energy
        brilliance EXT_Metal(N, K) 
        #if (WavelengthIndex < 0) metallic #end
      }  
      normal {
        bump_map {png "ornament_bump" interpolate 2}
        rotate x*90 translate -z*0.04 scale <0.1667,0.78,0.78> 
        bump_size 0.05 
        warp {cylindrical orientation y dist_exp 0 }
      } 
    }
    interior {IOR_Metal(N, K)}
  }
#end 


#macro User_Spectral_Alloy (N1, K1, F1, N2, K2 F2, Reflectance) 
  #local N = Spectral_Average (N1, F1, N2, F2);
  #local K = Spectral_Average (K1, F1, K2, F2);
  User_Spectral_Metal (N, K, Reflectance)
#end 


#macro My_Gold_Ornament ()
  #local Gold = 100 - Silver;
  User_Spectral_Alloy (
    IOR_N_Gold,   IOR_K_Gold,   Gold,
    IOR_N_Silver, IOR_K_Silver, Silver,
    Reflect
  )
#end


#macro My_Gold_Plain ()
  M_GoldSilver_Alloy (Silver, Reflect)
#end
     


// *** make a ring ***

#declare Ring = union {
  #local D = 0.3;
  difference {                
    #local SR = sqrt(1+pow(D,2));
    sphere { 0, SR + 0.05 translate y*D}
    cylinder {<0,-0.1, 0>, <0, 0.7, 0>, 0.95 My_Gold_Plain() }
    plane { y, 0 translate -y*SR*0.05*D }
    plane {-y, 0 translate  y*(0.6+SR*0.05*D)}
  } 
  torus {1, 0.05}
  torus {1, 0.05 translate y*D*2}
  translate y*0.05
  
  My_Gold_Ornament()
}



// *** put that ring ***

object { Ring 
  translate <66, 80, 169>  
  photons {target}
}             


//================================================================================
     