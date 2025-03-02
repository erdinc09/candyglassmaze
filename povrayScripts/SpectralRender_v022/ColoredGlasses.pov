//================================================================================
// POV-Ray spectral rendering example
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
// - Ive, September 2012
//
//================================================================================

#version 3.7; 

//================================================================================
// control center
//================================================================================

#declare MaxTrace   = 16;
#declare Radio      = 1;
#declare Sunlight   = 0;
#declare Photons    = 4000000; 
#declare FocalBlur  = off;
                                
#declare Cam_Pos    = < 80, 90, 110>;
#declare Cam_Look   = < 66, 88, 170>;
#declare Cam_Angle  = 30;

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

#declare M_BlueGlass  = M_Spectral_Filter (D_Average(D_CC_B2, 1, Value_1, 0.6), IOR_FlintGlass_F5, 6);
#declare M_AmberGlass = M_Spectral_Filter (D_Average(D_CC_F2, 1, Value_1, 0.5), IOR_FlintGlass_F2, 6);

object { Wineglass (M_BlueGlass)   translate <59, 80, 175> }             
object { Wineglass (M_AmberGlass)  translate <67, 80, 164> }             
object { Wineglass (M_AmberGlass)  translate <68, 80, 180> }
object { Wineglass (M_BlueGlass)   translate <75, 80, 168> }             

//================================================================================
     