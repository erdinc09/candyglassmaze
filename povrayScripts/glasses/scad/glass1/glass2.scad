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

module broken1(){
	scale(2.3)
	{
		intersection(){
			cube(1, center = true);

			rotate([45, 45, 45])
			{
				cube(1, center = true);
			}
		}
	}
}

module glass1(){
	intersection(){
		difference(){
			sphereTemplate();
			breakerTemplate();
		}
		translate([0, 0, -3])
		{
			cube(6, center = true);
		}
	}
}

module glass2(){
	difference(){
		sphereTemplate();
		scale([1.1, 1.1, 1.1])
		{
			union(){
				glass1();
			}
		}
	}
}

module glass1_1(){
//	union(){
	difference(){
		sphereTemplate();
//			translate([0, 0, -3])
//			{
//				cube(6, center = true);
//			}
		translate([0, 1, 0])
		scale(1.5)
		rotate([20, 20, 40])
		{
			union(){
				broken1();
				rotate([10, 0, 20])
				{
					union(){
						broken1();
					}
				}
			}
		}
	}

//	broken1();
//	}
}

//glass2();
//glass1();
glass1_1();
//broken1();
//breakerTemplate();
printCameraAndLightSourceParameters();
