//================================================================================
// This include file defines a simple environment used by most example scenes for
// the spectral render rig.
// 
// To be declared *before* including this file:
// 
// Cam_Pos
// Cam_Look
// Cam_Angle
//
// To be included *before* including this file:
//
// spectral.inc
//
//
// - Ive, December 2013
//
//================================================================================

#ifndef (Radio)       #declare Radio       = 1;    #end
#ifndef (AreaLight)   #declare AreaLight   = off;  #end
#ifndef (FocalBlur)   #declare FocalBlur   = off;  #end
#ifndef (MaxTrace)    #declare MaxTrace    = 5;    #end

#ifndef (Sunlight)    #declare Sunlight    = 1.0;  #end
#ifndef (SkyEmission) #declare SkyEmission = 12;   #end
#ifndef (Photons)     #declare Photons     = off;  #end 

#ifndef (RoomDesign)  #declare RoomDesign  = 1;    #end


//================================================================================
// global settings, radiosity, photons
//================================================================================

global_settings {
  assumed_gamma 1
  max_trace_level MaxTrace

 #if (Radio > 0)
  radiosity {
    #if (Radio=1)
     count 300
     error_bound 0.8
    #else
     count 800
     error_bound 0.15
    #end       
     pretrace_start  0.08
     pretrace_end    1/min(image_height,image_width)
     recursion_limit 2
     nearest_count 10
     always_sample off
     normal off
  }
 #end 

 #if (Photons)
  photons {
    count Photons
    autostop 0
    jitter 0.5
  }
 #end

}


//================================================================================
// camera
//================================================================================

camera {
  location Cam_Pos
  angle    Cam_Angle
  look_at  Cam_Look   
  right    x*image_width/image_height
 #if (FocalBlur)
  focal_point Cam_Look
  aperture 0.8
  confidence 0.9999
  variance 1/1000
  blur_samples 100
 #end  
}


//================================================================================
// lighting
//================================================================================

#if (Sunlight)         

  #local SunDist   = 9500; 
  #local SunRadius = SunDist/210;
  
  union {
    light_source {
      x*SunDist, SpectralEmission (E_Blackbody(5500))*Sunlight
      parallel point_at 0

     #if (AreaLight)
      area_light z*SunRadius*2, y*SunRadius*2, 9,9 adaptive 1 circular orient
     #end 

      photons {reflection on refraction on }
    }

    disc {
      x*SunDist, -x, SunRadius*10
      pigment {    
        wood
        color_map {
          [0 SpectralEmission(E_Sunlight)*2.2]
          [1 SpectralEmission(E_Sunlight) transmit 1]
        }    
        rotate y*90
        scale SunRadius*20 
      }
      finish {emission Sunlight*SkyEmission ambient 0 diffuse 0}
      no_shadow
      no_radiosity
    }
  
    rotate z*35 rotate y*39
  }  

#end


#if (!Radio) // fill lights

  light_source {
    x*1000, SpectralEmission(E_D75)*0.20
    photons {reflection off refraction off}
    shadowless
    rotate z*35 rotate y*150
  }

  light_source {
    Cam_Pos, SpectralEmission(E_D65)*0.15
    photons {reflection off refraction off}
    shadowless  
  }

#end


//================================================================================
// sky
//================================================================================

#declare Sky = sphere { 0, 1
  texture {
    pigment {
      gradient y
      scale 2
      translate -y
      color_map {
        [0.5 SpectralEmission(E_D50)*0.33]
        [1.0 SpectralEmission(E_D93)]
      }
    }
    finish {emission SkyEmission ambient 0 diffuse 0}
  }
  scale 10000
  no_shadow 
  photons {collect off}
}


//================================================================================
// textures
//================================================================================

#if (RoomDesign > 2)

/*
#declare M_WoodTable = material {
  texture {
    pigment {
      granite triangle_wave
      color_map {
        [0.8  C_RGB(0.025, 0.012, 0.002)*3 ]
        [1.0  C_RGB(0.018, 0.011, 0.002)*3 ] 
      }
    }
    finish {
      ambient 0  diffuse 0.8  
      reflection {0 0.25 fresnel on} conserve_energy 
    }
    normal {
      granite -0.002
    }
    scale <35,1,1>*0.1
  }  
  interior {IOR_Spectral(IOR_15)}
} 
*/

#declare M_WoodTable = material {
  texture {
    pigment { 
      pigment_pattern {image_map {jpeg "wood_map" interpolate 2} rotate x*90}
      pigment_map {
        [0.0  C_RGB(0.040, 0.005, 0.001)*1 ] 
        [1.0  C_RGB(0.100, 0.010, 0.002)*8 ]
      }
    }
    finish {
      ambient 0  diffuse 0.75
      reflection {0 0.25 fresnel on} conserve_energy 
    }
    normal {
      average 
      normal_map {
        [1 bump_map {jpeg "wood_map" interpolate 2} rotate x*90 bump_size 0.75]
        [1 granite 0.025 scale 0.025]
      }    
    }
    scale <6,1,4.3>*5.5   translate z*0
  }  
  interior {IOR_Spectral(IOR_15)}
}

#end


#switch (RoomDesign)
  // simple white
  #case (1)
    #declare T_Wall1  = T_Spectral_Matte (D_RGB (1.0, 1.0, 1.0))
    #declare T_Wall2  = T_Spectral_Matte (D_CC_B4)                   
    #declare T_Wall3  = T_Spectral_Matte (D_CC_B4)                   
    #declare T_Ceil   = T_Spectral_Matte (D_CC_A4) 
    #declare M_Floor  = M_Spectral_Matte (D_CC_A1)
    #declare M_Table  = M_Spectral_Shiny (D_CC_A4, 0.25, IOR_15)
    #break

  // cyan-green table
  #case (2)
    #declare T_Wall1  = T_Spectral_Matte (D_RGB (0.6, 0.0, 0.1))
    #declare T_Wall2  = T_Spectral_Matte (D_CC_B4) 
    #declare T_Wall3  = T_Spectral_Matte (D_CC_B4) 
    #declare T_Ceil   = T_Spectral_Matte (D_CC_A4)
    #declare M_Floor  = M_Spectral_Shiny (D_CC_A1, 0.20, IOR_15)
    #declare M_Table  = M_Spectral_Shiny (D_CC_F1, 0.25, IOR_15)
    #break

  #case (3)  
    #local T_WallPaper = texture {
                           pigment {
                             pigment_pattern {image_map {png "wallpaper" interpolate 2} }
                             pigment_map {
                               [0.0 C_Average(D_CC_A2, 5, D_CC_A4, 1)]
                               [0.5 C_Average(D_CC_D3, 4, D_CC_A4, 1)]
                               [1.0 C_Spectral(D_CC_A4)]
                             }     
                             turbulence 0.02
                           }
                           finish {ambient 0  diffuse 1}
                           normal {bump_map {png "wallpaper" interpolate 2} bump_size 0.4}
                           scale 25
                         }

    #declare T_Wall1  = texture {T_WallPaper}
    #declare T_Wall2  = texture {T_WallPaper rotate y*90}
    #declare T_Wall3  = T_Spectral_Matte (D_CC_B4) 
    #declare T_Ceil   = T_Spectral_Matte (D_CC_A4)
    #declare M_Floor  = M_Spectral_Shiny (D_CC_A1, 0.20, IOR_15)
    #declare M_Table  = M_WoodTable
    #break

  // purple old style wallpaper and wood table
  #case (4)  
    #local P_WallPaper1 = pigment {
      agate
      color_map {
        [0 C_Spectral(D_CC_D2)*0.8]
        [1 C_Spectral(D_CC_D2)*1.2]
      }   
      scale 0.175
    }
    #local P_WallPaper2 = pigment {  
      wrinkles
      color_map {
        [0 C_Spectral(D_CC_C1)*0.8]
        [1 C_Spectral(D_CC_C1)*1.2]
      }   
      scale 0.2
    }
    #local T_WallPaper = texture {
                           pigment {
                             pigment_pattern {image_map {png "wallpaper" interpolate 2} }
                             pigment_map {
                               [0.0 P_WallPaper1]
                               [0.5 P_WallPaper2]
                               [1.0 C_Spectral(D_CC_F2)]
                             }     
                             turbulence 0.02
                           }
                           finish {ambient 0  diffuse 1}
                           normal {bump_map {png "wallpaper" interpolate 2} bump_size 0.5}
                           scale 27
                         }
  
    #declare T_Wall1  = texture {T_WallPaper}
    #declare T_Wall2  = texture {T_WallPaper rotate y*90}
    #declare T_Wall3  = T_Spectral_Matte (D_CC_B4) 
    #declare T_Ceil   = T_Spectral_Matte (D_CC_A4)
    #declare M_Floor  = M_Spectral_Shiny (D_CC_A1, 0.20, IOR_15)
    #declare M_Table  = M_WoodTable

//    #declare M_Table  = M_Spectral_Glass(D_CC_A4, IOR_CrownGlass_KSK7, 0.85)
    #break


#end  // switch


//================================================================================
// room
//================================================================================

#local W = 320;
#local L = 500;
#local H = 270;


#local WPanel = difference {
  box {<0, 0,-2>, <90, 130, 5>}
  box {<6, 6,-3>, <84, 124, 7>}
  box {<4, 4,-4>, <86, 126,-1>}
}


#local Window = union {
  object {WPanel}
  object {WPanel translate x*90}
  T_Spectral_Matte ( D_Average (D_CC_F2, 0.9, D_CC_F4, 0.1) )
}   


#declare Room = union {

  // floor
  box {<-W-1,-1,-L-1>, <25, 1,   1>   material {M_Floor} } // brown

  // ceiling
  box {<-W-1, H,-L-1>, <25, H+1, 1>   texture {T_Ceil} }

  // front wall
  box {<-W-1,-1,-1>,   < 1, H+1, 0>   texture {T_Wall1} }  

  // rear wall
  box {<-W-1,-1,-L-1>, < 1, H+1,-L>   texture {T_Wall1} }
    
  // left wall
  box {<-W-1,-1,-L-1>, <-W, H+1, 1>   texture {T_Wall2} }
    
  // window wall
  union {   
    difference {
      box {<0, -1, -L-1>, <25, H+1, 1>}
      box {<-1,0,0>, <26,130,-180> translate <0, 98,-((L/2)-180)/2>}
      box {<-1,0,0>, <26,130,-180> translate <0, 98,-L/2-((L/2)-180)/2>}
      texture {T_Wall3}
    }
    object {Window rotate y*90     translate <0, 98,-((L/2)-180)/2>}
    object {Window rotate y*90     translate <0, 98,-L/2-((L/2)-180)/2>}
  }  
    
  photons {collect off} 
  
  translate <W/2, 0, L/2>
}


//================================================================================
// stuff
//================================================================================

// table  
#declare Table = union {
                
  merge {
    box { <-55, 77, -35>, <55, 80, 35> }
    merge {
      cylinder {<-55, 0,-35>, < 55, 0,-35>, 1}
      cylinder {<-55, 0, 35>, < 55, 0, 35>, 1}
      cylinder {<-55, 0,-35>, <-55, 0, 35>, 1}
      cylinder {< 55, 0,-35>, < 55, 0, 35>, 1}
      sphere {<-55, 0,-35>, 1}
      sphere {< 55, 0,-35>, 1}
      sphere {<-55, 0, 35>, 1}
      sphere {< 55, 0, 35>, 1}
      scale <1, 1.5, 1>
      translate y*78.5
    }  
    material { M_Table }
  }

  cylinder {<0,0,0>, <0,75,0>, 2.5 translate <-46,0,-26>}
  cylinder {<0,0,0>, <0,75,0>, 2.5 translate <-46,0, 26>}
  cylinder {<0,0,0>, <0,75,0>, 2.5 translate < 46,0,-26>}
  cylinder {<0,0,0>, <0,75,0>, 2.5 translate < 46,0, 26>}

  M_Spectral_Shiny (D_CC_E4, 0.35, IOR_15)
}                


//================================================================================
