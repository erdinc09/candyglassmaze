//================================================================================
// POV-Ray spectral rendering example
//
// This renders the GretagMacbeth(tm) ColorChecker Color Rendition Chart.
// Like the old one I do actually own.
//
// Useful as reference for the usage of the diffuse colors available within the
// "spectral-materials" include file.
//
// The DeltaE (for the L*a*b values using CIE 1994 graphic art and CIE 2000) of the
// spectral render compared to the reference values from the GretagMacbeth data
// sheet is less than 1.0 for all color patches, so the accuracy for diffuse
// reflectance of the spectral renderer rig is really good.
//
// - Ive, September 2012
//
//================================================================================
//
// suggested command line:
// +W800 +H600 +A0.1 +FE 
//
// spectral calculation command line:
// +KI1 +KF36 +KFI38 +KFF73
//
// settings for the spectral composer:
//   FName      = "ColorChecker";
//   Exposure   = 1.0;
//   Luminance  = 1.0;
//   Exponent   = 1.0;
//   Saturation = 1.0;
//   Whitepoint = 6504;
//
//================================================================================

#version 3.7;        

global_settings {
  assumed_gamma 1
}

#include "spectral.inc"
#include "ccc.inc"  


camera {
  location  <0, 0, -60>
  look_at   <0, 0,  0>
  right     x*image_width/image_height
  angle 30
}
                 
                 
light_source {<0, 0, -100>,
  SpectralEmission(E_D65)
}


object {ColorChecker rotate -x*90}


//================================================================================
