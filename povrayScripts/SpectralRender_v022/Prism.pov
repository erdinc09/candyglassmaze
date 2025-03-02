//================================================================================
// POV-Ray spectral rendering example
//
// A small lamp viewed through an amici prism.
// Fluorescent lamps are the most interesting ones as they have those significant
// peaks and no continous spectrum. To compare them with a continous one you can
// try the CIE illuminants, e.g. E_D65.
// No light_source neeeded for the scene (fast to render), the UseLight switch is
// just there to see what happens when part of the background gets also reflected
// by the prism (nothing interesting so far).
//
// P.S. If anybody feels like rendering the "Dark Side Of The Moon" cover (with a
// simple flint glass prism), I would love to see it!
//
// - Ive, September 2012
//
//================================================================================
//
// suggested command line:
// +W640 +H480 +A0.2 +FE 
//
// spectral calculation command line:
// +KI1 +KF36 +KFI38 +KFF73
//
//================================================================================

#version 3.7;        

global_settings {  
  max_trace_level 40
  assumed_gamma 1
}

#include "spectral.inc"


//================================================================================

// enable one of the lamps

//#declare Lamp = E_Nikon_SB16_XenonFlash;
//#declare Lamp = E_Osram_CoolBeam_20w;
//#declare Lamp = E_Osram_CoolFluor_36w;
//#declare Lamp = E_Phillips_PLS_11w; 
#declare Lamp = E_Phillips_Mastercolor_3K;
//#declare Lamp = E_Mitsubishi_Daylight_Fluorescent;
//#declare Lamp = E_Mitsubishi_Moon_Fluorescent;


#declare UseLight = off;  // looks better without - and is faster!


//================================================================================

camera {
  location  <0, 0, -80>
  look_at   <0, 0,  0>
  right     x*image_width/image_height
  angle 6
}
                 

//================================================================================

#declare  AmiciPrism = union {

  #local W1 = 48;
  #local W2 = 15;

  #local T = tan(radians(W1));

  difference {
    box {<0,0,-4>, <1,1,4>}
    plane { z, 0 rotate  y*W2 translate -T*z}
    plane {-z, 0 rotate -y*W2 translate  T*z}
    translate <-0.5, -0.5, 0> 
    scale 5
    M_Spectral_Filter (Value_1, IOR_CrownGlass_K7, 100)
  }  

  difference {
    box {<-1,0,-2>, <0,1,2>}
    plane { z, 0 rotate -y*W1}
    plane {-z, 0 rotate  y*W1}
    translate <0.4999, -0.5, 0>
    scale 5
    M_Spectral_Filter (Value_1, IOR_FlintGlass_F2, 100)
  }  
}   


//================================================================================

#declare Flash = union {   
  union {
    box {<-1,-3,0>, <1,3,1>}
    cylinder {<0,-3,0>, <0,-3,1>, 1}
    cylinder {<0, 3,0>, <0, 3,1>, 1} 
    scale 0.5
    no_shadow
    T_Spectral_Emitting(Lamp, 1.15)
  }
  
  box {<-1,-4, 0.5> <1, 4, 1>
    scale 0.6
    no_shadow
    T_Spectral_Emitting(Lamp, 0.5)
  }
  
 #if (UseLight)
  light_source { <0, 0, -1>,
    SpectralEmission(Lamp) * 1
    fade_power 2
    fade_distance 50
  }
 #end
}


//================================================================================

object {AmiciPrism translate <0.1, 0,-73>}


object {Flash}


#if (UseLight)  // some "walls" - otherwise the lightsource is pointless
  plane {-z, 0 translate  z*100 T_Spectral_Matte(D_CC_E4) }
  plane { z, 0 translate -z*100 T_Spectral_Matte(D_CC_E4) }
#end


//================================================================================
