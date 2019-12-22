#!/bin/bash -ex

java -Xmx256m -prism.order=sw -cp $(pwd)/bin/java/ sharp.game.Driver "os: mac" "output: false"
