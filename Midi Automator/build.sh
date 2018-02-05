# 
# /usr/bin/hdiutil create -srcfolder "target/MIDI Automator.app" -volname "MIDI Automator" -ov "target/MIDI Automator-tmp.dmg" -format UDRW
# /usr/bin/hdiutil attach "target/MIDI Automator-tmp.dmg" -mountroot /tmp
# mkdir "/tmp/MIDI Automator/.background"
# cp "images/DMGBackground.png" "/tmp/MIDI Automator/.background/background.png"
# cp "images/MidiAutomatorIcon.icns" "/tmp/MIDI Automator/.VolumeIcon.icns"
# cp "DS_Store" "/tmp/MIDI Automator/.DS_Store"
# /usr/bin/SetFile -a C "/tmp/MIDI Automator"
# ln -s "/Applications" "/tmp/MIDI Automator/Applications"
# chmod +x "/tmp/MIDI Automator/MIDI Automator.app/Contents/PlugIns/jre/Contents/Home/jre/lib/jspawnhelper"
# /usr/bin/hdiutil detach -force "/tmp/MIDI Automator"
# diskutil eject "/tmp/MIDI Automator"
/usr/bin/hdiutil convert "target/MIDI Automator-tmp.dmg" -format UDZO -o "target/MIDI Automator.dmg" -debug
rm "target/MIDI Automator-tmp.dmg"


cd /Users/aguelle/Documents/git/Midi\ Automator/Midi\ Automator
/usr/bin/hdiutil create -srcfolder "target/MIDI Automator.app" -volname "MIDI Automator" -ov "target/MIDI Automator-tmp.dmg" -fs HFS+ -format UDRW
/usr/bin/hdiutil attach "target/MIDI Automator-tmp.dmg" -mountroot /tmp
/usr/bin/hdiutil detach -force "/tmp/MIDI Automator"
sudo lsof | grep "MIDI Automator-tmp.dmg"
/usr/bin/hdiutil convert "target/MIDI Automator-tmp.dmg" -format UDZO -o "target/MIDI Automator.dmg" -debug