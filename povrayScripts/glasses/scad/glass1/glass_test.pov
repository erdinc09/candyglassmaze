#version  3.7;
global_settings
{ 
    assumed_gamma 1.9
}

#include "colors.inc"           // Standard colors library
#include "shapes.inc"           // Commonly used object shapes
#include "textures.inc"         // LOTS of neat textures.  Lots of NEW textures.
#include "stones.inc"
#include "glass.pov"

scad_background()
    
object { 
    glass1()
}

scad_camera(<0.267571,-0.431923,0.609763>,<148.8,0,102.2>,35.5861,800,450)
scad_light_source(<-0.578176,-0.0654301,1.68207>,<94.2,0,354.2>,39.5401)