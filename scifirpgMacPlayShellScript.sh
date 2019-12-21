#!/bin/bash -ex

java -Xmx256m -cp $(pwd)/bin/java/ sharp.game.Driver "os: mac"

