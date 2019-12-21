#!/bin/bash -ex

# run the main java file
echo Sharp$ Running sharp.game.Driver (launch App)!
java  -prism.order=sw -cp bin/java/ sharp.game.Driver "os: mac"
echo Sharp$ Done!