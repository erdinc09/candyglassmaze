//================================================================================
// GretagMacbeth(tm) ColorChecker Color Rendition Chart
//
// late 80ies style, like the one I do own (meanwhile it's X-Rite(tm))
//================================================================================

#include "spectral_materials.inc"

//================================================================================

#declare ColorChecker = union {

  union {
    box {<-15.6, 0, -10.2>, <15.6, 0.1, 10.2>}
    box {<-14.8, 0, -11>, <14.8, 0.1, 11>}
    cylinder {<-14.8, 0,-10.2>, <-14.8, 0.1,-10.2>, 0.8 }
    cylinder {<-14.8, 0, 10.2>, <-14.8, 0.1, 10.2>, 0.8 }
    cylinder {< 14.8, 0,-10.2>, < 14.8, 0.1,-10.2>, 0.8 }
    cylinder {< 14.8, 0, 10.2>, < 14.8, 0.1, 10.2>, 0.8 }
    
    T_Spectral_Matte ( D_Average(D_CC_E4, 0.2, D_CC_F4, 0.8) )
  }

  #for (Row, 0, 3)
    #for (Col, 0, 5)
        
      box { <-2.2, 0.05, -2.2> <2.2, 0.105, 2.2>

        #switch (Row)
          #case (0)
            #switch (Col)
              #case(0) T_Spectral_Matte(D_CC_A1) #break
              #case(1) T_Spectral_Matte(D_CC_B1) #break
              #case(2) T_Spectral_Matte(D_CC_C1) #break
              #case(3) T_Spectral_Matte(D_CC_D1) #break
              #case(4) T_Spectral_Matte(D_CC_E1) #break
              #case(5) T_Spectral_Matte(D_CC_F1) #break
            #end  
          #break

          #case (1)
            #switch (Col)
              #case(0) T_Spectral_Matte(D_CC_A2) #break
              #case(1) T_Spectral_Matte(D_CC_B2) #break
              #case(2) T_Spectral_Matte(D_CC_C2) #break
              #case(3) T_Spectral_Matte(D_CC_D2) #break
              #case(4) T_Spectral_Matte(D_CC_E2) #break
              #case(5) T_Spectral_Matte(D_CC_F2) #break
            #end  
          #break

          #case (2)
            #switch (Col)
              #case(0) T_Spectral_Matte(D_CC_A3) #break
              #case(1) T_Spectral_Matte(D_CC_B3) #break
              #case(2) T_Spectral_Matte(D_CC_C3) #break
              #case(3) T_Spectral_Matte(D_CC_D3) #break
              #case(4) T_Spectral_Matte(D_CC_E3) #break
              #case(5) T_Spectral_Matte(D_CC_F3) #break
            #end  
          #break

          #case (3)
            #switch (Col)
              #case(0) T_Spectral_Matte(D_CC_A4) #break
              #case(1) T_Spectral_Matte(D_CC_B4) #break
              #case(2) T_Spectral_Matte(D_CC_C4) #break
              #case(3) T_Spectral_Matte(D_CC_D4) #break
              #case(4) T_Spectral_Matte(D_CC_E4) #break
              #case(5) T_Spectral_Matte(D_CC_F4) #break
            #end  
          #break

        #end  // switch row
      
        translate <-12.5+Col*5, 0, 7.5-Row*5>
      }

    #end // for col
  #end // for row 

  union {
    text {ttf "cyrvetic" "GretagMacbeth   ColorChecker Color Rendition Chart" 0.1, 0  scale 0.7 translate <-8.0,-10.6,-0.105>}
    text {ttf "cyrvetic" "TM"                 0.1, 0   scale 0.25 translate < -3.3, -10.25, -0.105>}

    rotate x*90 

    T_Spectral_Matte ( D_Average(D_CC_B4, 1, D_CC_C4, 1) ) 
  }            

  scale 0.928     // scale to correct size of ca. 29cm x 20.4cm
}


//================================================================================
