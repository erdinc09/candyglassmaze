//================================================================================
// A simple vine glass that can be filled ;)
//
// parameters:
//   material for the glass
//   material for the fluid (pereferable vine)
//   fill height 0.0 .. 1.0
//================================================================================


#macro Wineglass (M_Glass)

  #local RH40 = sin(40*pi/180) * y;
  #local RW40 = cos(40*pi/180);

  merge {
   
    torus { RW40*4, 0.05 sturm
      translate RH40*4
      scale <1,1.4,1> 
      translate y*12.65
    } 

    difference {
      sphere { 0, 4.05 }
      sphere { 0, 3.95 }
      cone   { 0, 0, RH40*6.5, RW40*6.5} 
      plane {y, 0}  
      scale <1, 1.4, 1>
      translate y*12.65
    }  

    difference {
      sphere { 0, 4.05 }
      sphere { 0, 3.95 scale <1, 0.98, 1> }
      plane {-y, 0 translate y*0.0001}
      scale <1, 0.93, 1>
      translate y*12.65
    }  

    lathe {cubic_spline 16,
      <-0.010000,0.015359>,
       <0.000000,0.015359>,
       <0.034906,0.015359>,
       <0.356046,0.001496>,
       <0.383971,0.043284>,
       <0.202457,0.071209>,
       <0.097738,0.127059>,
       <0.048869,0.203854>,
       <0.034906,0.294610>,
       <0.034906,0.518012>,
       <0.034906,0.811226>,
       <0.041888,0.922927>,
       <0.076794,0.992739>,
       <0.013963,0.999721>,
       <0.000000,0.999721>
      <-0.010000,0.999721>
      sturm  
      translate -y*0.0015
      scale 9.0
    }    
   
    material {M_Glass}

    photons {  
      target on
      reflection on
      refraction on
      collect on
    }

    split_union off 
 
  }  

#end


#macro Wineglass_filled (M_Glass, M_Fluid, FillHeight)

#if (FillHeight > 0) 
union { 
  #if (FillHeight > 1)
    #local FillHeight = 1;
  #end  
#end

  Wineglass (M_Glass)

#if (FillHeight > 0)  

  #local FH = (-3.95*0.93*0.98) + FillHeight * 7.2;  

  #if (FH = 0)
    #local TW = 3.85;   
    #local TH = FH;
  #else
    #if (FH < 0)
      #local TW = sqrt( pow(3.95,2) - pow(FH/(0.93*0.98),2) );
      #local D  = atan(abs(FH)/TW);
      #local TH = FH+sin(D)*0.1;
      #local TW = TW-cos(D)*0.1;
    #else
      #local TW = sqrt( pow(3.95,2) - pow(FH/1.4,2) ) - 0.1; 
      #local TH = FH;
    #end
  #end   
    
  // fluid
  difference {  
    merge {
      difference {
        sphere { 0, 3.95 scale <1, 1.4, 1> }
        plane {y, 0}  
      }  
      sphere { 0, 3.95 scale <1, 0.93*0.98, 1> }
    }  

    union {
      plane {-y, 0 translate y*FH}
      torus {TW, 0.1 translate y*TH}
      cylinder {<0,0.1,0>, <0,-0.1,0>, TW translate y*TH} 
    }  

    scale 0.9995

    translate y*12.65  
      
    material {M_Fluid}

    photons {  
      target on
      reflection on
      refraction on
      collect on
    }
  }  

}  

#end // FillHeight > 0

#end // macro