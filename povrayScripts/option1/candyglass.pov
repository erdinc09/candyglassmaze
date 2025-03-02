#include "colors.inc"
#include "glass.inc"
#include "candies.pov"

#declare SHOW_FOG = 0; //1 ise fog var
#declare SHOW_MEDIA = 0; //1 ise media var


camera { location <1, 5, -6> 
    up    <0,1.33,0>
    right  <1,0,0>
    look_at <0,0,-0.8> 
    angle 35 
}

light_source { 
    <200, 250, 0>, 1 area_light z*40, y*40, 12, 12 adaptive 0 
    }


#declare sphere_ = sphere { <0,0,0>, 0.3
        texture{T_Dark_Green_Glass}
        interior{I_Glass4}
}

#declare sphere_button = sphere { <0,0,0>, 0.3
        texture{T_Glass3}
        interior{I_Glass4}
}




#declare sphere_ = sphere { <0,0,0>, 0.3
        texture{T_Dark_Green_Glass}
        interior{I_Glass4}
}

#declare box_ = difference{
    box{
        <-1.20,0,-1.20>
        <1.20,1,3.20>
        }
    box{
        <-1.10,0.10,-1.10>
        <1.10,2.10,3.10>
        }
        
    texture{T_Dark_Green_Glass}
    interior{I_Glass4}
}

#declare text_1 =  text {
    ttf "timrom.ttf" "Candy" 0.3, 0
    texture{Candy_Cane scale 0.2}
    interior{I_Glass4}
    rotate 90*x
    scale 0.5
    translate <0,0.03,0>
}

#declare text_2 =  text {
    ttf "timrom.ttf" "Glass" 0.3, 0
    texture{T_Dark_Green_Glass}
    interior{I_Glass4}
    rotate 90*x
    scale 0.5
    translate <0,0.03,0>
}

    
#declare candies = union{

    //little spiral
    object{
        CandySpiralTexture()
        //translate <0.8,0.3,0>
        translate <1.6,0.15,0>
        }
        
   //big spiral
    object{
        CandySpiralTexture()
        rotate 20
        scale 1.5
        //translate <-0.6,0.3,0>
        translate <1.7,0.25,1>
        }
        
    //orange
    object{
        Candy(pigment{ color rgb< 1.0, 0.15, 0.0> })
        scale 2
        translate <0,0.3,0.4>
    }
   
   //green
    object{
        Candy(pigment{ color rgb< 0.0, 1.0, 0.25> })
        scale 2
        translate <0,0.3,0.8>
    }
    //purple
    object{
        Candy(pigment{ color rgb<1.0, 0.4, 0.75> })
        scale 2
        translate <0,0.3,1.2>
    }
    
    //red
    object{
        Candy(pigment{ color rgb< 0.75, 0.0, 0.10> })
        scale 2
        translate <0.6,0.3,1.5>
    }

    
    object{ 
        CandyCane()  
        scale 0.02
        rotate 90*x
        translate <0.3,0.2,1.2>
    }
    
}

#if (SHOW_FOG)
   fog{
    color rgbt <.7,.7,.7>
    fog_type 2
    fog_alt 0.2
    fog_offset 0
    distance 0.5
    turbulence <.15, .15, .15>
    omega 0.35
    lambda 1.25
    octaves 5
    }
#end

#if (SHOW_MEDIA)
   global_settings{ assumed_gamma 1}
   box{
    <-1,-1,-2.5>, <1,3,0>
    pigment{ rgbt 1} hollow
    interior{
        media{
            scattering {1,0.15 extinction 0.01}
            samples 30
        }
    }
    rotate -70*z
   }
#end
 
 
#declare box_with_candies = union{
        object{
            box_
        }
        object{
            candies
        }
        rotate y*10
}
   
union{


    object{
        box_with_candies
    }
    
    object{
        text_1
         translate <-1,0.05,-2.2>
    }
    
    object{
        text_2
         translate <0.5,0.05,-2.2>
    }
  
    object{
        sphere_
        translate <-1.2,0.5,-2.3>
    }
  
  
  #local Candy_Cane_Green = pigment {
                gradient x+y
                color_map {
                    [0.25 rgb <0/255,128/255,0/255>]
                    [0.25 rgb <1,1,1>]
                    [0.75 rgb <1,1,1>]
                    [0.75 rgb <0/255,128/255,0/255>]
                }
            } 
  
  object{
        SpiralCandy(Candy_Cane)
        scale 0.15
        rotate -10*y
        translate <-2,0.09,2>
    }
    
    object{
        SpiralCandy(Candy_Cane_Green)
        scale 0.15
        rotate -20*y
        rotate 20*z
        rotate -12*x
        translate <-2.7,0.3,2>
    }

    
    object{
        sphere_button
        scale 1.2
        translate <-0.5,0.5,-3.6>
    }
    
    
    object{
        sphere_button
        scale 1.2
        translate <1.5,0.5,-3.2>
    }
}

 

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
