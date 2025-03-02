#macro standardCube()
	box { 
		<-1.0,-1.0,-1.0>,<1.0,1.0,1.0>
	}
#end
#macro mazeCube1()
	difference { 
		standardCube()
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			rotate <30.0,30.0,0.0>
			translate <0.0,-2.8,0.0>
		}
	}
#end
#macro mazeCube2()
	difference { 
		standardCube()
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <1.0,1.0,1.0>
			rotate <30.0,30.0,30.0>
			translate <0.5,-2.9,1.5>
		}
	}
#end
#macro mazeCube3()
	difference { 
		standardCube()
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <1.1,1.1,1.1>
			rotate <40.0,40.0,70.0>
			translate <0.0,-2.9,1.0>
		}
	}
#end
#macro mazeCube4()
	difference { 
		standardCube()
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <0.7,0.7,0.7>
			rotate <90.0,40.0,70.0>
			translate <1.0,-2.2,1.0>
		}
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <0.7,0.7,0.7>
			rotate <90.0,40.0,70.0>
			translate <-1.0,-2.2,-0.5>
		}
	}
#end
#macro mazeCube5()
	difference { 
		standardCube()
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <1.0,1.0,1.0>
			rotate <40.0,40.0,70.0>
			translate <0.0,-2.9,1.0>
		}
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <0.7,0.7,0.7>
			rotate <90.0,40.0,70.0>
			translate <-1.0,-2.2,-0.5>
		}
	}
#end
#macro mazeCube6()
	difference { 
		standardCube()
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <1.0,1.0,1.0>
			rotate <40.0,40.0,70.0>
			translate <1.0,-2.9,1.0>
		}
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <0.75,0.75,0.75>
			rotate <110.0,40.0,70.0>
			translate <-1.0,-2.2,-0.5>
		}
	}
#end
#macro mazeCube7()
	difference { 
		standardCube()
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <0.8,0.8,0.8>
			rotate <90.0,40.0,70.0>
			translate <1.0,-2.2,1.0>
		}
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <0.8,0.8,0.8>
			rotate <90.0,50.0,70.0>
			translate <-1.0,-2.2,-0.5>
		}
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <0.6,0.6,0.6>
			rotate <90.0,50.0,70.0>
			translate <-1.0,-2.2,1.0>
		}
	}
#end
#macro mazeCube8()
	difference { 
		standardCube()
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <1.0,1.0,1.0>
			rotate <20.0,40.0,70.0>
			translate <0.5,-2.9,1.0>
		}
		object {
			box { 
				<-1.5,-1.5,-1.5>,<1.5,1.5,1.5>
			}
			scale <0.8,0.8,0.8>
			rotate <190.0,70.0,80.0>
			translate <1.0,-2.4,-0.5>
		}
	}
#end
#macro
    openscad_camera_angle(w ,h) (11*pow(w/h,0.5))
#end // OpenSCAD camera angle is dependent on view port aspect ratio. Formula is an estimation"

#macro 
	scad_camera(openscad_vpt, openscad_vpr, openscad_vpd, openscad_vpw, openscad_vph)
	camera {
	    orthographic
	    location <0,0,openscad_vpd>
	    right <-openscad_vpw/openscad_vph,0,0>
	    angle openscad_camera_angle(openscad_vpw,openscad_vph)
	    up    <0,1.33,0>
	    rotate openscad_vpr
	    translate openscad_vpt
	    look_at <0,0,0>
	}
#end
#macro 
	scad_light_source(openscad_vpt, openscad_vpr, openscad_vpd)
	
	light_source {
		<1,1,1>*openscad_vpd color rgb <120/255,120/255,120/255>
		rotate openscad_vpr 
		translate openscad_vpt 
	} 
#end
#macro scad_background()
    #include "coordinates.pov"
    #declare openscad_cornfield = color rgb <1,1,0.898039>;
    #declare openscad_metalic = color rgb <0.666667,0.666667,1>;
    #declare openscad_sunset = color rgb <0.666667,0.266667,0.266667>;
    background { openscad_cornfield } 
    #default { texture { pigment { rgb <1,1,0> } finish { ambient 0.15 diffuse 0.85 }  }  } 
#end
/*


************ ballbox_test.ini ************
Width=1600
Height=1200
Input_File_Name="ballbox_test.pov"
Antialias=On
Antialias_Threshold=0.1
Display=On


************ ballbox_test.sh ************
povray  -L${POVRAY_SCAD_LIB} ballbox_test.ini



*/
#macro scad_axes(axe_length)
    object { 
        AxisXYZ( axe_length, axe_length, axe_length, Tex_Dark, Tex_White)
    }
#end
