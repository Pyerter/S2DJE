#!/bin/bash -ex

sudo launchctl load -w /System/Library/LaunchDaemons/com.apple.locate.plist

cd $(locate scifirpgMacPlayShellScript)

java -Xmx256m -cp $(pwd)/bin/java/ sharp.game.Driver "os: mac" "output: false"
