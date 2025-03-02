
#include "functions.inc"


#macro Ring (M_Base, M_Ornament)


#local F_Ornament = function {
  pigment {
    image_map {png "ornament_bump" interpolate 2}
    rotate x*90 translate -z*0.5 scale <0.1667, 0.78, 0.78> 
    warp {cylindrical orientation y dist_exp 0 }
  }
}   


// *** make a ring ***

union {
  #local D = 0.3;
  #local SR = sqrt(1+pow(D,2));
  
  union { 
    difference {                
      sphere { 0, SR + 0.05}
      cylinder {<0,-D*1.1, 0>, <0, D*1.1, 0>, 0.95}
      plane { y, 0 translate -y*(D+SR*0.05*D)}
      plane {-y, 0 translate  y*(D+SR*0.05*D)}
    } 
    torus {1, 0.05 translate -y*D}
    torus {1, 0.05 translate  y*D}
  
    material {M_Base}
  }

  difference {  
    #local T = 0.012;
    isosurface {
      function {f_sphere(x,y,z, 1) + T - F_Ornament(x,y,z).gray*T}
      contained_by { sphere {0,1} }
      max_gradient 1
    }  
    cylinder {<0,-1.1,0>, <0,1.1,0>, 0.944}
    
    scale SR*(1.0455 + T)
    
    material {M_Ornament}
  }  

  translate y*(D+0.05)
}
 
 
#end 


#declare Ring_OuterDiameter = sqrt(1+pow(0.3,2)) * (1.0455 + 0.012);