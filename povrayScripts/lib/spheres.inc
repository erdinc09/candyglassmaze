#include "glass.inc"

#declare sphere_ = sphere { <0,0,0>, 0.3
        texture{T_Dark_Green_Glass}
        interior{I_Glass4}
}

#declare sphere_button = sphere { <0,0,0>, 0.3
        texture{T_Glass3}
        interior{I_Glass4}
}

#declare sphere_all = sphere { <0,0,0>, 0.5

        texture{T_Dark_Green_Glass}
        interior{I_Glass4}
}


#declare sphere_low = difference{
	sphere { <0,0,0>, 0.5
	       
	}
	
	
	cylinder{
		<0, 0, 0>,     
	    <0, 2, 0>,     
	    1            
	}
	
	box{
		<-1, -1, -1>,     
	    <1, 1, 1>
	    scale 0.3
	    translate <0,0.25,0>
	    rotate 45*x 
	    rotate 45*z 
	    translate <0,0.15,-0.12>    
	}
	
	box{
		<-1, -1, -1>,     
	    <1, 1, 1>
	    scale 0.37
	    translate <0,0.26,0>
	    rotate 45*x 
	    rotate 48*z 
	    rotate 88*y
	    translate <-0.1,0.13,-0.17>    
	}
	
 	//texture{T_Dark_Green_Glass}
	//        interior{I_Glass4}
}

#declare sphere_high = difference{
	sphere { <0,0,0>, 0.5
		       
		}
		
	object{
		sphere_low
		scale 1.001
	}
	
	//texture{T_Dark_Green_Glass}
	//        interior{I_Glass4}
}