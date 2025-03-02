#include "candies.pov"
#include "spheres.inc"
#include "texts.inc"
#include "candybox.inc"
#include "functions.inc"

#declare SHOW_FOG = 0; //1 ise fog var
#declare SHOW_MEDIA = 0; //1 ise media var
#declare TEXT=0;
#declare OTHER_THAN_TEXT=1;
//#declare FLOOR=1;

camera { 
	location <1, 5, -6> 
    up    <0,1.33,0>
    right  <1,0,0>
    look_at <0,0,-0.8> 
    angle 35 
}


light_source { 
    <200, 250, 0>, 1 area_light z*60, y*60, 12, 12 adaptive 0 
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


#declare candies = union{
 /*  

			object{ //-------------------------
			  CandyGlassLike1()
			  rotate  -40*x
			  translate <-0.3,0.3,-0.7>
			}
	        
	     
	        object { 
				CandyGlassLike2()
	         	translate <0.2,0.1,-1.6>
	        }
	        
	        object { 
				CandyGlassLike2()
				rotate 30*z
	         	translate <-0.2,0.3,-1.6>
	        } 
	        
	        object { 
				CandyGlassLike1()
	         	translate <-0.6,0.1,-2.2>
	         	
	        }
	        */ 
	        
	        
	        object { 
				CandyGlassLike5()  // turuncu cam seker CandyGlassLike3()
				rotate clock*x
	         	translate <0.0,0.4,-2.2>
	         	
	        }
	        /*
	        object { 
				CandyGlassLike4()
	         	translate <0.5,0.1,-1.0>
	         	
	        }
	        
	        object{
		        Candy(pigment{ color rgb< 0.0, 1.0, 0.0>}) //green
		        	translate <0.8,0.3,1.0> 
	        }
	        object{
		        Candy(pigment{ color rgb< 1.0, 0.15, 0.0> }) //  red orange
		        	translate <1,0.3,1.3> 
	        }
	        object{
		        Candy(pigment{ color rgb< 1.0, 0.0, 0.0> }) //  red
		        	rotate -30*z
		        	translate <1.1,0.3,1.15> 
	        }
	        
	        object{
		        Candy(pigment{ color rgb< 0.0, 1.0, 1.0> }) //cyan
		        	translate <-0.8,0.3,0.0> 
	        }
	        
	        object{
		        Candy(pigment{ color rgb< 1.0, 1.0, 0.0> }) //yellow
		        	translate <-0.8,0.3,0.4> 
	        }
	        object{
		        Candy(pigment{ color rgb< 1.0, 1.0, 1.0> }) //white
		        	translate <-0.4,0.3,0.2> 
	        }
	        
	        object{
		        Candy(pigment{ color rgb< 0.75, 0.5, 1.0> }) //light violet
		        	translate <-0.0,0.3,0.2> 
	        }
	        
	        
	        object{
		        Candy(pigment{ color rgb< 0.0, 0.0, 0.0>}) //black
		        	translate <0.8, 0.1,-2.2> 
	        }
	        
	        object{
		        Candy(pigment{ color rgb< 1.0, 1.0, 0.0>}) //yellow
		        	translate <0.3, 0.1,-2.8> 
	        }
	        object{
		        Candy(pigment{ color rgb< 1.0, 0.0, 0.0> }) //  red 
		        	translate <0.7, 0.1,-2.7> 
	        }
	        */

}


#declare box_with_candies = union{
  /*
        object{
        	brokenCandybox
			rotate y*20
			
        }
        
        object{
        	brokenCandyboxOther
        	rotate -y*30
        	translate <0.7,0,-0.8>
        	
        }
        
        */
        object{
            candies
        }
}
   
union{

#if (OTHER_THAN_TEXT)
	object{
		box_with_candies
        translate <0.5,0,0.5>
	}
	
#end    
    
}

 
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
