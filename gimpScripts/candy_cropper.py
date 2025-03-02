#!/usr/bin/env python

from gimpfu import *
from gimpenums import *
import os
# import crop_save_ball
from progressbar import AnimatedMarker, Bar, BouncingBar, Counter, ETA, \
    FileTransferSpeed, FormatLabel, Percentage, \
    ProgressBar, ReverseBar, RotatingMarker, \
    SimpleProgress, Timer, AdaptiveETA, AdaptiveTransferSpeed


def candy_crop_all(x, y, width, height, inputFolder, templateFolder, outputFolder) :
    pdb.gimp_progress_init("Cropping " , None)
    files = os.listdir(inputFolder)
    idx = 0
    pbar = ProgressBar(widgets=[Percentage(), Bar()], maxval=len(files)).start()
    for file in files:
        idx = idx + 1
        try:
            inputPath = os.path.join(inputFolder, file)  # inputFolder + "\\" + file
            templatePath = os.path.join(templateFolder, file)  # inputFolder + "\\" + file

            image = None
            if(file.lower().endswith(('.png'))):
                image = pdb.file_png_load(inputPath, inputPath)
            else:
                continue
            
            # gimp.message ("Info: tamplate path ="+templatePath)
            template_layer = pdb.gimp_file_load_layer(image, templatePath)
            pdb.gimp_image_set_active_layer(image, image.layers[0])
            pdb.gimp_image_insert_layer(image, template_layer, None, -1)
            
            if(image == None):
                gimp.message ("ERROR: None Image")
                return
            if(len(image.layers) != 2):
                gimp.message ("ERROR: layer count=" + str(len(image.layers)))
                return
            
            candy_crop(image, image.layers[1], image.layers[0], x, y, width, height, outputFolder)
        
            # if(idx==2):
            #    return
            # pdb.gimp_display_new(image)
            
        except Exception as err:
            gimp.message("Unexpected error: " + str(err))
        
        pdb.gimp_progress_set_text("Proccessed: " + str(float(idx) / len(files)))
        pdb.gimp_progress_update(float(idx) / len(files))
        pbar.update(idx)
        
    pbar.finish()
    pdb.gimp_progress_end()

 
def candy_crop(img, layerReal, layerTemplate, x, y, width, height, outputFolder) :
    pdb.gimp_layer_add_alpha(layerReal) #bunu life resimlerine alpha eklenmedigi icin yaptik
    #onceki resimler bu olmadan yaoildi!!!!

    pdb.gimp_selection_layer_alpha(layerTemplate)
    pdb.gimp_selection_invert(img)
    pdb.gimp_edit_clear(layerReal)
    
    pdb.gimp_image_crop(img, width, height, x, y)
    # pdb.gimp_image_scale(img, 100, 100)
    image_full_name = pdb.gimp_image_get_filename(img)

    exp_name = os.path.basename(image_full_name)
    export_full_name = os.path.join(outputFolder, exp_name)
    pdb.file_png_save(img, layerReal, export_full_name, exp_name, False, 9, False, False, False, False, False)
    

register(
    "python_fu_candy_crop",
    "candy_crop",
    "Display a 'hello world' message",
    "JFM",
    "Open source (BSD 3-clause license)",
    "2018",
    "Candy & Crop ALL",
    "",
    [    
        #life
        (PF_INT, "x", "x", 643),
        (PF_INT, "y", "y", 597),
        (PF_INT, "width", "w", 250),
        (PF_INT, "height", "h", 120),
        
      
        (PF_DIRNAME, "inputFolder", "inputFolder", "<project_folder>/povrayScripts/life"),
        (PF_DIRNAME, "templateFolder", "templateFolder", "<project_folder>/povrayScripts/life/template"),
        (PF_DIRNAME, "outputFolder", "outputFolder", "<project_folder>/povrayScripts/life/out")
        
        ],
    [],
    candy_crop_all, menu="<Image>/File/Candy Crop"
   )

main()

