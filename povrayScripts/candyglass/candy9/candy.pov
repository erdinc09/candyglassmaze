#include "candies.pov"
#include "spheres.inc"
#include "texts.inc"
#include "candybox.inc"
#include "functions.inc"


camera { 
	orthographic
	location <1, 5, -6> 
    up    <0,1,0>
    right  <1,0,0>
    look_at <0,0,0> 
    angle 35 
}

light_source { 
	<200, 250, 0>, 1 area_light z*60, y*60, 12, 12 adaptive 0 
}
    
object{
	SpiralCandy(Candy_Cane)
    scale 0.015
	rotate clock*z
	translate <-0,0,0.12>
	scale 11
	rotate 300*x
}


