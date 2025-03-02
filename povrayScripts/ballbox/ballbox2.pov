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
    <1500, 1500, 600>// hepsi 1500
    color White
}

camera { 
    location <2,10,1>
    look_at  <2,0,1>
    up    <0,1.33,0>
    right  <1,0,0>
}


#declare Test1 =
texture { pigment { P_WoodGrain19A color_map { M_Wood1A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood1B }  }  } 
#declare Test2 =
texture { pigment { P_WoodGrain19A color_map { M_Wood2A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood2B }  }  } 
#declare Test3 =
texture { pigment { P_WoodGrain19A color_map { M_Wood3A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood3B }  }  } 
#declare Test4 =
texture { pigment { P_WoodGrain19A color_map { M_Wood4A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood4B }  }  } 
#declare Test5 =
texture { pigment { P_WoodGrain19A color_map { M_Wood5A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood5B }  }  } 
#declare Test6 =
texture { pigment { P_WoodGrain19A color_map { M_Wood6A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood6B }  }  } 
#declare Test7 =
texture { pigment { P_WoodGrain19A color_map { M_Wood7A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood7B }  }  } 
#declare Test8 =
texture { pigment { P_WoodGrain19A color_map { M_Wood8A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood8B }  }  } 
#declare Test9 =
texture { pigment { P_WoodGrain19A color_map { M_Wood9A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood9B }  }  } 
#declare Test10 =
texture { pigment { P_WoodGrain19A color_map { M_Wood10A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood10B }  }  } 
#declare Test11 =
texture { pigment { P_WoodGrain19A color_map { M_Wood11A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood11B }  }  } 
#declare Test12 =
texture { pigment { P_WoodGrain19A color_map { M_Wood12A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood12B }  }  } 
#declare Test13 =
texture { pigment { P_WoodGrain19A color_map { M_Wood13A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood13B }  }  } 
#declare Test14 =
texture { pigment { P_WoodGrain19A color_map { M_Wood14A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood14B }  }  } 
#declare Test15 =
texture { pigment { P_WoodGrain19A color_map { M_Wood15A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood15B }  }  } 
#declare Test16 =
texture { pigment { P_WoodGrain19A color_map { M_Wood16A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood16B }  }  } 
#declare Test17 =
texture { pigment { P_WoodGrain19A color_map { M_Wood17A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood17B }  }  } 
#declare Test18 =
texture { pigment { P_WoodGrain19A color_map { M_Wood18A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood18B }  }  } 
#declare Test19 =
texture { pigment { P_WoodGrain19A color_map { M_Wood19A }  }  } 
texture { pigment { P_WoodGrain19B color_map { M_Wood19B }  }  } 

box { 
    < 0, 0,  -1>,  // Near lower left corner
    < 1, 1,  0>   // Far upper right corner
    
    texture { Test1 rotate x*30 } //15 14 9 [8] 2 [1]
    //translate -4.5*x
    //translate -2*z
}



#if(FLOOR)
    plane
    { 
        <0,1,0> // normal vector
        , 0 // distance from origin
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
        //translate <0,0,10*clock>
        translate <10*clock,0,0>
    }
#end
