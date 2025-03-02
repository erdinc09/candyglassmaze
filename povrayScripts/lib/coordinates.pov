//---------------------------------------------------------------------
//---------------------------------------------------------------------
#include "colors.inc"
#include "textures.inc"
#macro Axis_( AxisLen, RedTexture,WhiteTexture) // --------------------- 
union{
    cylinder {<0,-AxisLen,0>,<0,AxisLen,0>,0.05
              texture{checker texture{RedTexture} texture{WhiteTexture}
                      translate<0.1,0,0.1>}}
    cone{<0,AxisLen,0>,0.2,<0,AxisLen+0.7,0>,0
         texture{RedTexture}}
     } // end of union "Axis"                  
#end // of macro Axis (AxisLen)
//--------------------------------------
#macro AxisXYZ( AxisLenX, AxisLenY, AxisLenZ, TexRed, TexWhite)
//--------------------- 3 Achsen zeichnen -----------------------------
union{
object{Axis_(AxisLenX, TexRed, TexWhite)   rotate< 0,0,-90>}   // x-Axis
object{Axis_(AxisLenY, TexRed, TexWhite)   rotate< 0,0,  0>}   // y-Axis
object{Axis_(AxisLenZ, TexRed, TexWhite)   rotate<90,0,  0>}   // z-Axis
//--------------------- Achsenbezeichnungen ---------------------------
text{ttf"timrom.ttf",  "x",  0.15,  0 no_shadow
     texture{TexRed} 
     scale 0.5 translate <AxisLenX+0.05,0.20,-0.12>}
text{ttf"timrom.ttf",  "y",  0.15,  0 no_shadow 
     texture{TexRed} 
     scale 0.5 translate <-0.35,AxisLenY+0.50,-0.05>}
text{ttf"timrom.ttf",  "z",  0.15,  0 no_shadow
     texture{TexRed} 
     scale 0.5 translate <0.25,0.2,AxisLenZ+0.50>}
} // end of union
#end// of macro "AxisXYZ(...)"
//----------------------------------------------------------------------


//--------------------------drawing the axis ---- Achsen zeichnen ------
#declare Tex_Dark = texture{ pigment{ color rgb<1,0.45,0>}
                             finish { phong 1}}
#declare Tex_White =texture{ pigment{ color rgb<1,1,1>}
                             finish { phong 1}}

//object{ AxisXYZ( 1, 2, 2, Tex_Dark, Tex_White)}
//--------------------------end of coordinates ---- Ende der Koordinaten