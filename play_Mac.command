#!/bin/bash -ex

#cd $(pwd)/Documents/GitHub/scifirpg/

cd $(find $(pwd) -type f -name 'play_Mac_NoCMD.sh'|sed -E 's|/[^/]+$||' |sort -u)
sh $(pwd)/play_Mac_NoCMD.sh