/**
 * @author Angelo Guelle
 *
 * Note: Download http://nsis.sourceforge.net/AccessControl_plug-in and copy it to the NSIS installation directory
 */

!include "MUI.nsh"

Name "MIDI Automator Installer"
RequestExecutionLevel admin
!define INSTALLATIONNAME "Midi Automator"
!define PROJECTPATH "..\"
!define ICON "MidiAutomatorIcon.ico"
!define EXE "${INSTALLATIONNAME}.exe"
!define LNK "${INSTALLATIONNAME}.lnk"
!define BUILD "NSIS\build"
!define UNINSTALLER "uninstall.exe"
!define APPDIR "$APPDATA\${INSTALLATIONNAME}"
InstallDir "$PROGRAMFILES64\${INSTALLATIONNAME}"

!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES

!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

!insertmacro MUI_LANGUAGE "English"

# Default section
Section
    # Copy files to program directory
    SetOutPath $INSTDIR
    SetOverwrite on
    File "${PROJECTPATH}\images\${ICON}"
    File /r "${PROJECTPATH}\${BUILD}\*"
    AccessControl::GrantOnFile "$INSTDIR\midiautomator.properties" "(BU)" "FullAccess"
    AccessControl::GrantOnFile "$INSTDIR\file_list.mido" "(BU)" "FullAccess"
    AccessControl::GrantOnFile "$INSTDIR\MidiAutomator.log" "(BU)" "FullAccess"
    
    # Create uninstaller
    WriteUninstaller $INSTDIR\${UNINSTALLER}
        
    # Start menu entries
    SetOutPath $INSTDIR
    SetShellVarContext all
    CreateShortCut "$SMPROGRAMS\${LNK}" "$INSTDIR\${EXE}" "" "$INSTDIR\${EXE}" 0
      
    # Set uninstall RegKeys
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "DisplayName" "${INSTALLATIONNAME}"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "DisplayIcon" '"$INSTDIR\${ICON}"'
    WriteRegExpandStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "UninstallString" '"$INSTDIR\${UNINSTALLER}"'
    WriteRegExpandStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "InstallLocation" '"$INSTDIR"'
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "Publisher" "Angelo Guelle"
    WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "NoModify" 1
    WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "NoRepair" 1
SectionEnd

Section "Uninstall"
    # Remove Uninstall RegKeys
    DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}"
    # Delete start menu entries
    SetShellVarContext all
    Delete "$SMPROGRAMS\${LNK}"
    # Delete install directory
    RMDir /r $INSTDIR
SectionEnd
