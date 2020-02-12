#!/bin/bash -ex

sudo launchctl load -w /System/Library/LaunchDaemons/com.apple.locate.plist

cd $(locate scifirpgMacPlayShellScript)

sh scifirpgMacPlayShellScript.sh
