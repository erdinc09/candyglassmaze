#include "textures.inc"
#include "colors.inc"
#include "woods.inc"
#include "stones.inc"
#include "metals.inc"
#include "skies.inc"
#include "coordinates.pov"
#include "ballbox.pov"

global_settings { 
    assumed_gamma 1.0
}

#ifndef(CUBE_1)
    #declare CUBE_1 = 0;
#end
#ifndef(CUBE_2)
    #declare CUBE_2 = 0;
#end
#ifndef(CUBE_3)
    #declare CUBE_3 = 0;
#end
#ifndef(CUBE_4)
    #declare CUBE_4 = 0;
#end
#ifndef(CUBE_5)
    #declare CUBE_5 = 0;
#end
#ifndef(CUBE_6)
    #declare CUBE_6 = 0;
#end
#ifndef(CUBE_7)
    #declare CUBE_7 = 0;
#end
#ifndef(CUBE_8)
    #declare CUBE_8 = 0;
#end
//scad_camera(<0,0,0>,<55,0,25>,140,1200,1200)
//scad_light_source(<0,0,0>,<55,0,25>,140)
//scad_camera(<0.385154,-0.0995395,-0.682662>,<73.9,0,15.2>,28.2733,800,800)
//scad_light_source(<0.385154,-0.0995395,-0.682662>,<73.9,0,15.2>,28.2733)
//scad_camera(<0.385154,-0.0995395,-0.682662>,<73.9,0,15.2>,28.2733,800,800)
//scad_light_source(<0.385154,-0.0995395,-0.682662>,<73.9,0,15.2>,28.2733)
//scad_camera(<0.385154,-0.0995395,-0.682662>,<73.9,0,15.2>,28.2733,800,800)
scad_light_source(<0.385154,-0.0995395,-0.682662>,<73.9,0,15.2>,28.2733)
scad_camera(<1.2076,0.0657314,-0.66675>,<77.4,0,11>,25.3568,800,800)
object { 
    #if(CUBE_1)
        mazeCube1()
    #end
    #if(CUBE_2)
        mazeCube2()
    #end
    #if(CUBE_3)
        mazeCube3()
    #end
    #if(CUBE_4)
        mazeCube4()
    #end
    #if(CUBE_5)
        mazeCube5()
    #end
    #if(CUBE_6)
       mazeCube6()
    #end
     #if(CUBE_7)
       mazeCube7()
    #end
     #if(CUBE_8)
       mazeCube8()
    #end

    //T_Wood29 scale2 color rgb <120/255,120/255,120/255>
    //T_Wood33  scale 3
    texture {T_Wood29  scale 2}// T_Wood2, , T_Wood14, T_Wood17, , T_Wood19, T_Wood20, T_Wood23, T_Wood24, T_Wood29, T_Wood30
    //T_Wood1,T_Wood13,T_Wood18
}

/*
#macro 
	scad_light_source(openscad_vpt, openscad_vpr, openscad_vpd)
	
	light_source {
		<1,1,1>*openscad_vpd color rgb <1,1,1> 
		rotate openscad_vpr 
		translate openscad_vpt 
	} 
#end
*/