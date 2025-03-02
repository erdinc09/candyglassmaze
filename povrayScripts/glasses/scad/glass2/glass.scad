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

module sphereGlass(){
	sphere(r = 3, $fn = 50);
}

module glass1(){
	difference(){
		spinyBall();

		translate([2.7, 1, -0.5])
		scale(2.8)
		rotate([0, 0, 0])
		{
			breakerTemplate();
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

module glass2(){
	difference(){
		spinyBall();
		scale(1.001)
		{
			glass1();
		}
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


module boxTemplate(){
	cube(size = 3, center = true);
}

module cylinderTemplateZ(){
	cylinder(d1=3,d2=0,$fn=30,h=5);
}

module cylinderTemplateX(){
	rotate([0, 90, 0])
	{
		cylinder(d1=3,d2=0,$fn=30,h=5);
	}
}

module glass1_breaker(){
	translate([0, 3, 0])
	rotate([0, 40, 50])
	{
		cube(center = true, size = [6, 6, 6]);
	}
}

module glass2_breaker(){
	translate([0, 3, 0])
	rotate([0, 40, 50])
	{
		cube(center = true, size = [5, 5, 5]);
	}
}

module cylinderTemplateY(){
	rotate([-90, 0, 0])
	{
		cylinder(d1=3,d2=0,$fn=30,h=5);
	}
}

module spinyBall(){
	union(){
		sphere(r = 3, $fn = 50);
		// cylinderTemplateZ();
		// cylinderTemplateX();
		// cylinderTemplateY();
		spinesZ();
		spinesX();
		spinesY();
	}
}

module spinesY(){
	union(){
		rotate([0, 0, 0])
		{
			cylinderTemplateY();
		}
		rotate([0, 0, 45])
		{
			cylinderTemplateY();
		}
		rotate([0, 0, 90])
		{
			cylinderTemplateY();
		}
		rotate([0, 0, 135])
		{
			cylinderTemplateY();
		}
		rotate([0, 0, 180])
		{
			cylinderTemplateY();
		}
		rotate([0, 0, 225])
		{
			cylinderTemplateY();
		}
		rotate([0, 0, 270])
		{
			cylinderTemplateY();
		}
		rotate([0, 0, 315])
		{
			cylinderTemplateY();
		}
	}
}

module spinesX(){
	union(){
		rotate([0, 0, 0])
		{
			cylinderTemplateX();
		}

		rotate([0, 45, 0])
		{
			cylinderTemplateX();
		}
		rotate([0, 90, 0])
		{
			cylinderTemplateX();
		}
		rotate([0, 135, 0])
		{
			cylinderTemplateX();
		}
		rotate([0, 180, 0])
		{
			cylinderTemplateX();
		}
		rotate([0, 225, 0])
		{
			cylinderTemplateX();
		}
		rotate([0, 270, 0])
		{
			cylinderTemplateX();
		}
		rotate([0, 315, 0])
		{
			cylinderTemplateX();
		}
	}
}

module spinesZ(){
	union(){
		rotate([0, 0, 0])
		{
			cylinderTemplateZ();
		}
		rotate([45, 0, 0])
		{
			cylinderTemplateZ();
		}
		rotate([90, 0, 0])
		{
			cylinderTemplateZ();
		}
		rotate([135, 0, 0])
		{
			cylinderTemplateZ();
		}
		rotate([180, 0, 0])
		{
			cylinderTemplateZ();
		}
		rotate([225, 0, 0])
		{
			cylinderTemplateZ();
		}
		rotate([270, 0, 0])
		{
			cylinderTemplateZ();
		}
		rotate([315, 0, 0])
		{
			cylinderTemplateZ();
		}
	}
}

//glass2_1();
//glass2_2();
//glass1_1();
//glass1_2();
//glass1();
//glass2();
spinyBall();
printCameraAndLightSourceParameters();
