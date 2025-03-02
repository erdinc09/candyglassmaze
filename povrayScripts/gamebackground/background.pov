// http://xahlee.org/3d/index.html

// POV-Ray transparency study

#include "textures.inc"
#include "colors.inc"
#include "woods.inc"
#include "stones.inc"
#include "metals.inc"
#include "skies.inc" 


global_settings {
  assumed_gamma 1.0
}


  light_source {
     <1500,1500,1500>
    color White
  }

camera {
  location <0,3,0>
  look_at  <0,0,0>
	//translate -.8
}

plane
{  <0,1,0> // normal vector
      , -3 // distance from origin
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
  //translate <0,0,10*clock>
  translate <10*clock,0,0>
}


