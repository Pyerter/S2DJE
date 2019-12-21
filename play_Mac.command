#!/bin/bash -ex

#cd $(pwd)/Documents/GitHub/scifirpg/

cd $(find $(pwd) -type f -name 'scifirpgMacPlayShellScript.sh'|sed -E 's|/[^/]+$||' |sort -u)
sh $(pwd)/scifirpgMacPlayShellScript.sh