#include "textures.inc"
#include "woods.inc"


#macro CandySpiralTexture()
    sphere{ <0,0,0>, 1 
            texture{ pigment{ spiral1 5 rotate<90,0,0>
                            color_map{
                            [ 0.0 color rgb<1,1,1> ]
                            [ 0.5 color rgb<1,1,1> ]
                            [ 0.5 color rgb<1,0,0> ]
                            [ 1.0 color rgb<1,0,0> ]
                            } // end color_map
                            scale 0.75
                            } // end pigment
                    //normal  { bumps 0.5 scale  0.005 }
                    finish  { phong 1 reflection 0.00 }
                } // end of texture ------------------
        scale<0.2,0.2,0.2> 
    }
#end

#macro Candy (pigment_)  sphere{ <0,0,0>, 1 
         texture { 
                    pigment_  
                    finish { phong 1 reflection 0.00}
                 } // end of texture 
        scale<0.1,0.1,0.1> 
        rotate<-45,0,0> 
  }
#end

#macro CandyCane() 

    union{
            #declare A=1;
            
            #while(A<=360)
                cylinder{<0,0,0><0,1,0>,4
                    
                    #if
                        (A<=179)rotate y*A*8 
                        translate<20,-A/2,0>
                    #end
                    #if
                        (A>=180)rotate y*A*4 
                        translate<20,0,0>
                        rotate z*(A-180)
                    #end 
                }
                #declare A=A+1;
            #end 
            translate x*-20
            texture {Candy_Cane
                finish { phong 1 reflection 0.00}
                scale 12
            }
    }
#end

#macro SpiralCandy (texture_)
    #declare Ball =
    sphere{ <0,0,0>,0.8
            //scale <0.7,1.5,1> // !!!
            texture{
                texture_
            scale 0.6
            finish { phong 1 reflection 0.00}
            } //---------------
        } //-------------------------------------------------
    //---------------------------------------------------------


    #declare Radius0  = 0.0;
    #declare N_rev    = 4;
    #declare N_p_rev  = 2500; 
    #declare D_p_rev  = 1.0;
    #declare D = D_p_rev/N_p_rev;
    //---------------------------------------------------------
    union{
        #declare Nr = 0;                // start
        #declare EndNr = N_rev*N_p_rev; // end
        #while (Nr< EndNr)              // loop
        object{Ball
                translate<Radius0+Nr*D,0,0> 
                rotate<0,Nr * 360/N_p_rev ,0>} 
        #declare Nr = Nr + 1;  // next Nr
        #end // ----------------// end of loop 
        
        cylinder{
            <0,0,-0.5><0,21.5,-0.5>,0.2
            
            rotate -90*x
            translate <0,0.5,0>
            
            texture{T_Wood19}
        }

        rotate<0,0,0>
    } // end union  --------------------------------------------
#end






