#include "candies.pov"
#include "spheres.inc"
#include "texts.inc"
#include "candybox.inc"
#include "functions.inc"


camera { 
	orthographic
	location <1, 5, -6> 
    up    <0,1.33,0>
    right  <1,0,0>
    look_at <0,0,-0.8> 
    angle 35 
}

light_source { 
    <200, 250, 0>, 1 area_light z*60, y*60, 12, 12 adaptive 0 
}
#if(ALL) 
	object {
	    sphere_all
	    rotate 60*x
		#if(FLOOR)
		    texture{T_Glass3}
		    interior{I_Glass4}
	    #else
			texture{
				pigment{ color rgb< 0.0, 0.0, 0.0>}
				finish { phong 1 reflection 0.00}
			}
	    #end
	    scale 1.5
	    translate <1.5,1,-3.2>
	}
#end


#if(PIECE_1)    
	object {
	    sphere_high
	    rotate 60*x
		#if(FLOOR)
		    texture{T_Glass3}
		    interior{I_Glass4}
	    #else
			texture{
				pigment{ color rgb< 0.0, 0.0, 0.0>}
				finish { phong 1 reflection 0.00}
			}
	    #end
	    rotate clock*x
	    //rotate clock*y
	    scale 1.5
	    translate <1.5,1,-3.2>
	   
	}
#end

#if(PIECE_2)    
	object {
	    sphere_low
	    rotate 60*x
	    #if(FLOOR)
		    texture{T_Glass3}
		    interior{I_Glass4}
		#else
			texture{
				pigment{ color rgb< 0.0, 0.0, 0.0>}
				finish { phong 1 reflection 0.00}
			}
	    #end
	    rotate -clock*x
	    scale 1.5
	    translate <1.5,1,-3.2>
	}
#end
 
#if(FLOOR)
	plane
	{ y, 0
	  pigment
	  { wood color_map { [0 rgb <.9,.7,.3>][1 rgb <.8,.5,.2>] }
	    turbulence .5
	    scale <1, 1, 20>*.2
	  }
	  finish { specular 1 }
	  normal
	  { gradient x 1
	    slope_map
	    { [0 <0, 1>] // 0 height, strong slope up
	      [.05 <1, 0>] // maximum height, horizontal
	      [.95 <1, 0>] // maximum height, horizontal
	      [1 <0, -1>] // 0 height, strong slope down
	    }
	  }
	}
#end