#include "candies.pov"
#include "spheres.inc"
#include "texts.inc"
#include "candybox.inc"
#include "functions.inc"
#include "coordinates.pov"

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


#if(AXES)
	object{ AxisXYZ( 1, 2, 2, Tex_Dark, Tex_White)}
#end

#declare glassShape = object {
		sphere { <0,0,0>, 0.3
			       
				}
	}

#if(ALL) 
	object {
		glassShape
	 	#if(FLOOR)
		    texture{T_Glass3}
		    interior{I_Glass4}
	    #else
			texture{
				pigment{ color rgb< 0.0, 0.0, 0.0>}
				finish { phong 1 reflection 0.00}
			}
	    #end
		scale 2
	    translate <1.5,0.7,-3.2>
	}
#end

#declare glassPiece1Shape = 	difference {
	    object{
	    	glassShape
	    }
	    
	union{
		box{
			<0, 0.3, 0>, <0.3, -0.3, -0.3>
			translate -0.15*x
			translate 0.15*z
			rotate 0*x
			rotate 30*y
			rotate 30*z
			translate 0.15*x
			translate -0.15*z
			texture{T_Glass3}
		    interior{I_Glass4}
		}
		box{
			<0, 0.3, 0>, <0.3, -0.3, -0.3>
			translate -0.15*x
			translate 0.15*z
			rotate 40*x
			rotate 70*y
			rotate -60*z
			translate 0.25*x
			translate -0.15*z
			texture{T_Glass3}
		    interior{I_Glass4}
		}
		scale 1
	}
}


union{
	box{
		<0, 0.3, 0>, <0.3, -0.3, -0.3>
		translate -0.15*x
		translate 0.15*z
		rotate 0*x
		rotate 30*y
		rotate 30*z
		translate 0.15*x
		translate -0.15*z
		texture{T_Glass3}
	    interior{I_Glass4}
	}
	box{
		<0, 0.3, 0>, <0.3, -0.3, -0.3>
		translate -0.15*x
		translate 0.15*z
		rotate 40*x
		rotate 70*y
		rotate -60*z
		translate 0.25*x
		translate -0.15*z
		texture{T_Glass3}
	    interior{I_Glass4}
	}
}


#if(PIECE_1)    
	object {
		object{
	    	glassPiece1Shape
	    }
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
	    rotate clock*y
	    scale 2
	    translate <1.5,0.7,-3.2>
	   
	}
#end

#declare glassPiece2Shape = 	difference {
		object{
			glassShape
		}
	    object{
	    	glassPiece1Shape
	    	scale 1.0001
	    }
}

#if(PIECE_2)    
	object {
	    glassPiece2Shape
	    #if(FLOOR)
		    texture{T_Glass3}
		    interior{I_Glass4}
		#else
			texture{
				pigment{ color rgb< 0.0, 0.0, 0.0>}
				finish { phong 1 reflection 0.00}
			}
	    #end
	    rotate -clock*y
	    rotate -clock*z
	    scale 2
	    translate <1.5,0.7,-3.2>
	   
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

