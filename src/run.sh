#!/bin/bash

function compile {
  javac -d ../ -cp ../ $1
  if [[ $? -ne 0 ]]; then
    echo "Error compiling $1"
    exit
  fi
}

compile "Vector.java"
compile "Matrix.java"
compile "Operations.java"
compile "TextDialog.java"
compile "PrecisionDialog.java"
compile "ResultDialog.java"
compile "GUI.java"

# module commands are not necessary if javaFX has been added to javac?
# javac -d ../ -cp ../ --module-path $FX_PATH --add-modules GUI.java
# if [[ $? -ne 0 ]]; then
#   echo "Error compiling GUI.java"
# fi

javac -cp ../ Main.java
if [[ $? -ne 0 ]]; then
  echo "Error compiling Main"
  exit
fi

# Make sure the display is active
export DISPLAY=:0

java -cp ../ Main.java
