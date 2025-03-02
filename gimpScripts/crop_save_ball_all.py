#!/usr/bin/env python

from gimpfu import *
from gimpenums import *
import os
#import crop_save_ball
from progressbar import AnimatedMarker, Bar, BouncingBar, Counter, ETA, \
    FileTransferSpeed, FormatLabel, Percentage, \
    ProgressBar, ReverseBar, RotatingMarker, \
    SimpleProgress, Timer, AdaptiveETA, AdaptiveTransferSpeed

def crop_save_ball_all(x,y,r,inputFolder,outputFolder) :
    pdb.gimp_progress_init("Cropping " ,None)
    files = os.listdir(inputFolder)
    idx=0
    pbar = ProgressBar(widgets=[Percentage(), Bar()], maxval=len(files)).start()
    for file in files:
        idx=idx+1
        try:
            # Build the full file paths.
            inputPath = os.path.join(inputFolder,file)#inputFolder + "\\" + file
            # Open the file if is a JPEG or PNG image.
            image = None
            if(file.lower().endswith(('.png'))):
                image = pdb.file_png_load(inputPath, inputPath)
                
                    
            # Verify if the file is an image.
            if(image != None and len(image.layers) > 0):
                layer = image.layers[0]
                crop_save_ball(image, layer, x, y, r,outputFolder)
        except Exception as err:
            gimp.message("Unexpected error: " + str(err))
        
        pdb.gimp_progress_set_text("Proccessed: "+str(float(idx)/len(files)))
        pdb.gimp_progress_update(float(idx)/len(files))
        pbar.update(idx)
    pbar.finish()
 
def crop_save_ball(img,layer,x,y,r,outputFolder) :
    
    #pdb.gimp_progress_init("Cropping " + layer.name + "...",None)

    # Set up an undo group, so the operation will be undone in one step.
    pdb.gimp_undo_push_group_start(img)


    pdb.gimp_layer_add_alpha(layer)
    pdb.gimp_ellipse_select(img,x,y,r,r,CHANNEL_OP_REPLACE,True,False,0);
    
    pdb.gimp_image_crop(img,r,r,x,y)
    pdb.gimp_selection_invert(img)

    pdb.gimp_edit_clear(layer)
    #pdb.gimp_progress_update(0.5)
    image_full_name = pdb.gimp_image_get_filename(img)
    
    #dir=os.path.dirname(image_full_name)
    dir=outputFolder
    name_with_ext=os.path.basename(image_full_name)
    export_name = "exp_"+name_with_ext
    export_full_name = os.path.join(dir,export_name)
    pdb.gimp_image_scale(img, 200, 200)
    pdb.file_png_save(img,layer,export_full_name,export_name,False,9,False,False,False,False,False)
    
    #pdb.gimp_progress_update(1)
    pdb.gimp_undo_push_group_end(img)
        

register(
    "python_fu_crop_save_ball_all",
    "crop_save_ball_all",
    "Display a 'hello world' message",
    "JFM",
    "Open source (BSD 3-clause license)",
    "2018",
    "Crop&Save ALL",
    "",
    [
        (PF_INT, "x", "x", 177),
        (PF_INT, "y", "y", 1026),
        (PF_INT, "r", "R", 313),
        (PF_DIRNAME, "inputFolder", "inputFolder", "<project_folder>/povrayScripts/option2"),
        (PF_DIRNAME, "outputFolder", "outputFolder", "<project_folder>/povrayScripts/option2/out")
        
        ],
    [],
    crop_save_ball_all,menu="<Image>/File/Glass Ball"
   )

main()


