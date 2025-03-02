use <scadpov.scad>;

module sphereTemplate(){
	sphere(r = 2, $fn = 120);
}
module breakerTemplate(){
	translate([0, 0, 0])
	scale([1.4, 1.3, 1.3])
	{
		intersection(){
			union(){
				cube(center = true, size = [1, 5, 1]);

				rotate([45, 20, 0])
				{
					cube(center = true, size = [1, 5, 1]);
				}

				rotate([0, 20, 30])
				{
					cube(center = true, size = [1, 5, 1]);
				}
			}
			rotate([30, 50, 0])
			{
				cube(2, center = true);
			}
		}
	}
}

module glass1(){
	difference(){
		sphereTemplate();

		translate([2, 1, 0])
		scale(2.3)
		rotate([0, 0, 0])
		{
			breakerTemplate();
		}
	}
}

module glass2(){
	difference(){
		sphereTemplate();
		scale(1.001)
		{
			glass1();
		}
	}
}

module glass1_1(){
	difference(){
		glass1();
		glass1_breaker();
	}
}

module glass1_2(){
	intersection(){
		glass1();
		glass1_breaker();
	}
}

module glass1_breaker(){
	translate([0, 3, 0])
	rotate([0, 40, 50])
	{
		cube(center = true, size = [5, 5, 5]);
	}
}

module glass2_breaker(){
	translate([0, 3, 0])
	rotate([0, 40, 50])
	{
		cube(center = true, size = [5, 5, 5]);
	}
}

module glass2_1(){
	difference(){
		glass2();
		glass2_breaker();
	}
}

module glass2_2(){
	intersection(){
		glass2();
		glass2_breaker();
	}
}

module crackTemplate1(){
	union(){
		cube(center = true, size = [0.01, 5, 5]);
		rotate([0, 140, 50])
		{
			cube(center = true, size = [0.01, 5, 5]);
		}
		rotate([80, 130, 150])
		{
			cube(center = true, size = [0.01, 5, 5]);
		}
	}
}

module glassCracked1(){
	difference(){
		sphereTemplate();
		translate([2.3, 0, 1])
		rotate([90, 90, 90])
		scale(0.8)
		{
			breakerTemplate();
		}
	}
}

module glassCracked2(){
	difference(){
		sphereTemplate();
		scale(1.01)
		{
			glassCracked1();
		}

	}
}

module glassCracked3(){
	difference(){
		sphereTemplate();
		crackTemplate2();
	}
}

module crackTemplate2(){
	union(){
		translate([2.3, 2, 1])
		rotate([90, 90, 90])
		scale(0.8)
		{
			crackTemplate1();
		}
	}
}

//glassCracked3();
//crackTemplate2();
//crackTemplate1();
//glassCracked1();
//sphereTemplate();
//glass1();
glass1_1();
//glass1_2();
//glass2();
//breakerTemplate();
//glassCracked1();
//glassCracked2();
//glass2();
//glass2_1();
//glass2_2();
printCameraAndLightSourceParameters();
