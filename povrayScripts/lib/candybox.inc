#include "glass.inc"


#declare SQR_ROOT_OF_THTEE = 1.73205080757;

#declare candybox_full2 = prism {
    linear_sweep
    linear_spline
    0, // sweep the following shape from here ...
    2, // ... up through here
    6, // the number of points making up the shape ...
    <-1,SQR_ROOT_OF_THTEE>,
    <1,SQR_ROOT_OF_THTEE>,
    <2,0>,
    
    <1,-SQR_ROOT_OF_THTEE>,
    <-1,-SQR_ROOT_OF_THTEE>,
    <-2,0>
    
    
    texture{T_Glass3}
    interior{I_Glass4}
    //texture{T_Dark_Green_Glass}
	//interior{I_Glass4}
  }
  
#local candybox_full = box {
	<-1,0,-1>,
	<1,3,1>

	//texture{T_Dark_Green_Glass}
	//interior{I_Glass4}
}


#declare candybox = difference{
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


#declare brokenCandybox = difference {
	object{
		candybox
	}
	
	object{
		candybox_full
		scale 1.1
		rotate 30*y
		translate <1,0,-1>
	}

	texture{T_Dark_Green_Glass}
	interior{I_Glass4}
}

#declare brokenCandyboxOther = intersection {
	object{
		candybox
	}
	
	object{
		candybox_full
		scale 1.1
		rotate 30*y
		translate <1,0,-1>
	}

	texture{T_Dark_Green_Glass}
	interior{I_Glass4}
}














