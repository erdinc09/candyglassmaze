use <scadpov.scad>;

module standardCube(){
	cube(center = true, size = 2);
}

module mazeCube1(){
	difference(){
		standardCube();
		translate([0, -2.8, 0])
		rotate([30, 30, 0])
		{
			cube(center = true,size=3);
		}
	}
}

module mazeCube2(){
	difference(){
		standardCube();
		translate([0.5, -2.9, 1.5])
		rotate([30, 30, 30])
		scale(1)
		{
			cube(center = true,size=3);
		}
	}
}

module mazeCube3(){
	difference(){
		standardCube();
		translate([0, -2.9, 1])
		rotate([40, 40, 70])
		scale(1.1)
		{
			cube(center = true,size=3);
		}
	}
}
module mazeCube4(){
	difference(){
		standardCube();
		translate([1, -2.2, 1])
		rotate([90, 40, 70])
		scale(0.7)
		{
			cube(center = true,size=3);
		}
		
		translate([-1, -2.2, -0.5])
		rotate([90, 40, 70])
		scale(0.7)
		{
			cube(center = true,size=3);
		}
	}
}

module mazeCube5(){
	difference(){
		standardCube();
		translate([0, -2.9, 1])
		rotate([40, 40, 70])
		scale(1)
		{
			cube(center = true,size=3);
		}
		
		translate([-1, -2.2, -0.5])
		rotate([90, 40, 70])
		scale(0.7)
		{
			cube(center = true,size=3);
		}
	}
}

module mazeCube6(){
	difference(){
		standardCube();
		translate([1, -2.9, 1])
		rotate([40, 40, 70])
		scale(1)
		{
			cube(center = true,size=3);
		}
		
		translate([-1, -2.2, -0.5])
		rotate([110, 40, 70])
		scale(0.75)
		{
			cube(center = true,size=3);
		}
	}
}

module mazeCube7(){
	difference(){
		standardCube();
		translate([1, -2.2, 1])
		rotate([90, 40, 70])
		scale(0.8)
		{
			cube(center = true,size=3);
		}
		
		translate([-1, -2.2, -0.5])
		rotate([90, 50, 70])
		scale(0.8)
		{
			cube(center = true,size=3);
		}
		
		translate([-1, -2.2, 1])
		rotate([90, 50, 70])
		scale(0.6)
		{
			cube(center = true,size=3);
		}
	}
}


module mazeCube8(){
	difference(){
		standardCube();
		translate([0.5, -2.9, 1])
		rotate([20, 40, 70])
		scale(1.0)
		{
			cube(center = true,size=3);
		}
		
		translate([1, -2.4, -0.5])
		rotate([190, 70, 80])
		scale(0.8)
		{
			cube(center = true,size=3);
		}
		
	}
}

mazeCube8();

printCameraAndLightSourceParameters();
