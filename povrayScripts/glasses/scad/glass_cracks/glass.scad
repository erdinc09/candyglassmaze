use <scadpov.scad>;

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

module breakerTemplate2(){
	translate([2, 0, 0])
	{
		cube(size = 4, center = true);
	}
}

module glass1(){
	difference(){
		breakerTemplate();
		breakerTemplate2();
	}
}

module glass2_template(){
	cube(center = true, size = 2);
}
module glass2_breaker(){
	rotate([30, -30, 40])
	translate([2.1, 0, 0])
	{
		cube(size = 3, center = true);
	}
}
module glass2(){
	intersection(){
		glass2_template();
		glass2_breaker();
	}
}

glass2();
//glass2_template();
//glass2_breaker();
printCameraAndLightSourceParameters();
