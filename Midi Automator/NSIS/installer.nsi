/**
 * @author Angelo Gülle
 * @date 15-02-15
 */

!include "MUI.nsh"
!include "EnvVarUpdate.nsh"

Name "Midi Automator Installer"
!define INSTALLATIONNAME "Midi Automator"
!define PROJECTPATH "Y:\Dokumente\Eclipse Workspace\MIDI Opener\Midi Automator"
!define ICON "MidiAutomatorIcon.ico"
!define EXE "${INSTALLATIONNAME}.exe"
!define LNK "${INSTALLATIONNAME}.lnk"
!define DISTDIR "Y:\Dokumente\Eclipse Workspace\MIDI Opener\Midi Automator\build\Windows"
!define UNINSTALLER "uninstall.exe"
!define APPDIR "$APPDATA\${INSTALLATIONNAME}"
OutFile "${DISTDIR}\midiautomator_setup.exe"
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
    File /r /x "file_list.mido" "${PROJECTPATH}\bin\Windows\*"
    # Copy files to data directory
    SetOutPath "${APPDIR}"
    File "${PROJECTPATH}\bin\Windows\file_list.mido"
    WriteUninstaller $INSTDIR\${UNINSTALLER}
    # Set PATH environment varibale
    ${EnvVarUpdate} $0 "PATH" "A" "HKLM" "$INSTDIR\lib\libs"
    # Startmenu entries
    SetShellVarContext all
    CreateShortCut "$SMPROGRAMS\${LNK}" "$INSTDIR\${EXE}" "" "$INSTDIR\${EXE}" 0
    # Set uninstall RegKeys
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "DisplayName" "${INSTALLATIONNAME}"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "DisplayIcon" '"$INSTDIR\${ICON}"'
    WriteRegExpandStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "UninstallString" '"$INSTDIR\${UNINSTALLER}"'
    WriteRegExpandStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "InstallLocation" '"$INSTDIR"'
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "Publisher" "Angelo Gülle"
    WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "NoModify" 1
    WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "NoRepair" 1
SectionEnd

Section "Uninstall"
    # Remove Uninstall RegKeys
    DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}"
    # Remove PATH entry
    ${un.EnvVarUpdate} $0 "PATH" "R" "HKLM" "$INSTDIR\lib\libs"
    # Delete start menu entries
    SetShellVarContext all
    Delete "$SMPROGRAMS\${LNK}"
    # Delete install directory
    RMDir /r $INSTDIR
    # Delete data directory
    SetShellVarContext current
    RMDir /r "${APPDIR}"
SectionEnd
