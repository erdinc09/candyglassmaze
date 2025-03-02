#!/usr/bin/env python

from gimpfu import *
from gimpenums import *
import os

def crop_save_ball(img,layer,x,y,r) :
    
    pdb.gimp_progress_init("Cropping " + layer.name + "...",None)

    # Set up an undo group, so the operation will be undone in one step.
    pdb.gimp_undo_push_group_start(img)


    pdb.gimp_layer_add_alpha(layer)
    pdb.gimp_ellipse_select(img,x,y,r,r,CHANNEL_OP_REPLACE,True,False,0);
    
    pdb.gimp_image_crop(img,r,r,x,y)
    pdb.gimp_selection_invert(img)

    pdb.gimp_edit_clear(layer)
    pdb.gimp_progress_update(0.5)
    image_full_name = pdb.gimp_image_get_filename(img)
    
    dir=os.path.dirname(image_full_name)
    name_with_ext=os.path.basename(image_full_name)
    export_name = "exp_"+name_with_ext
    export_full_name = os.path.join(dir,export_name)
   
    pdb.file_png_save(img,layer,export_full_name,export_name,False,9,False,False,False,False,False)
    
    pdb.gimp_progress_update(1)
    pdb.gimp_undo_push_group_end(img)
   

register(
    "python_fu_crop_save_ball",
    "crop_save_ball",
    "Display a 'hello world' message",
    "JFM",
    "Open source (BSD 3-clause license)",
    "2018",
    "<Image>/Filters/Glass Ball/Crop&Save",
    "*",
    [
        (PF_INT, "x", "x", 679),
        (PF_INT, "y", "y", 479),
        (PF_INT, "r", "R", 242),
        ],
    [],
    crop_save_ball)

main()


