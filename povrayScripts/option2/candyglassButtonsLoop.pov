#version  3.7;

#include "candies.pov"
#include "spheres.inc"
#include "texts.inc"
#include "candybox.inc"
#include "functions.inc"


global_settings
{ 
    assumed_gamma 1
}


global_settings { 
    max_trace_level 35
    assumed_gamma 2.2
    ambient_light rgb< 0,0,0>
    
    photons { 
        spacing 0.05
        autostop 0
        jitter 0.4
        count 1000000
        media 0
        max_trace_level 25
    }
    
}

camera { 
    orthographic
    location <1, 5, -6>
    up    <0,1.33,0>
    right  <1,0,0>
    look_at <0,0,-0.8>
    angle 35
}

light_source { 
    <200, 250, 10>
    , 1 area_light z*60, y*60, 12, 12 adaptive 0
    media_interaction on
    photons { 
        refraction on
        reflection on
    }
}

#macro photon_in_object()
    photons { 
        collect on
        refraction on
        reflection off
        target  1
    }
#end

object { 
    CandySpiralTexture()
    rotate clock*y
    translate <-0.5,0.5,-3.6>
}

object { 
    sphere { 
        <0,0,0>, 0.3
    }
    texture { T_Glass3 } 
    interior { I_Glass4 } 
    //photon_in_object()
    scale 2
    translate <-0.5,0.5,-3.6>
}


object { 
    sphere { 
        <0,0,0>, 0.3
    }
    texture { T_Glass3 } 
    interior { I_Glass4 } 
    //photon_in_object()
    scale 2
    translate <1.5,0.5,-3.2>
}


plane
{ 
    y, 0
    pigment
    { 
        wood color_map { [0 rgb <.9,.7,.3>][1 rgb <.8,.5,.2>] } 
        turbulence .5
        scale <1, 1, 20>*.2
    }
    finish { specular 1 } 
    normal
    { 
        gradient x 1
        slope_map
        { 
            [0 <0, 1>] // 0 height, strong slope up
            [.05 <1, 0>] // maximum height, horizontal
            [.95 <1, 0>] // maximum height, horizontal
            [1 <0, -1>] // 0 height, strong slope down
        }
    }
}
