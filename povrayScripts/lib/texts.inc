#include "glass.inc"
#include "textures.inc"


#declare text_0 =  text {
    ttf "gosmicksans.bold.ttf" "Candy" 0.1, 0
    //texture{T_Dark_Green_Glass} 
    texture{T_Ruby_Glass scale 0.5}
    interior{I_Glass4}
    rotate 60*x
    scale 0.5
    translate <0,0.03,0>
}

#declare text_1 =  text {
    ttf "gosmicksans.bold.ttf" "Glass" 0.1, 0
    //texture{T_Dark_Green_Glass} 
    texture{T_Ruby_Glass scale 0.5}
    interior{I_Glass4}
    rotate 60*x
    scale 0.5
    translate <0,0.03,0>
}

#declare text_2 =  text {
    ttf "gosmicksans.bold.ttf" "Maze" 0.1, 0
    texture{T_Orange_Glass}
    //texture{Candy_Cane scale 0.5}
    //texture{T_Ruby_Glass scale 0.5}
    
    //interior{I_Glass4}
    rotate 60*x
    scale 0.5
    translate <0,0.03,0>
}