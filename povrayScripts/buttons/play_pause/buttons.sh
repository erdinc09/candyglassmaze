#!/usr/bin/env bash
povray +ua -Ipause.pov +Opause_pressed.png Declare=PRESSED=1 Declare=RELEASED=0 button.ini
povray +ua -Ipause.pov +Opause_released.png Declare=PRESSED=0 Declare=RELEASED=1 button.ini