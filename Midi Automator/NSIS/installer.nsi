/**
 * @author Angelo G�lle
 */

!include "MUI.nsh"
!include "EnvVarUpdate.nsh"

Name "Midi Automator Installer"
!define INSTALLATIONNAME "Midi Automator"
!define PROJECTPATH "..\"
!define ICON "MidiAutomatorIcon.ico"
!define EXE "${INSTALLATIONNAME}.exe"
!define LNK "${INSTALLATIONNAME}.lnk"
!define DISTDIR "..\build\Windows"
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
    File /r /x "midiautomator.properties" "${PROJECTPATH}\bin\Windows\*"
    
    # Copy files to data directory
    SetOutPath "${APPDIR}"
    File "${PROJECTPATH}\bin\Windows\file_list.mido"
    File "${PROJECTPATH}\bin\Windows\midiautomator.properties"
    WriteUninstaller $INSTDIR\${UNINSTALLER}
    
    # Set PATH environment variable
    ${EnvVarUpdate} $0 "PATH" "A" "HKLM" "$INSTDIR\lib\libs"
    
    # Startmenu entries
    SetShellVarContext all
    CreateShortCut "$SMPROGRAMS\${LNK}" "$INSTDIR\${EXE}" "" "$INSTDIR\${EXE}" 0
    
    # Set uninstall RegKeys
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "DisplayName" "${INSTALLATIONNAME}"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "DisplayIcon" '"$INSTDIR\${ICON}"'
    WriteRegExpandStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "UninstallString" '"$INSTDIR\${UNINSTALLER}"'
    WriteRegExpandStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "InstallLocation" '"$INSTDIR"'
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${INSTALLATIONNAME}" "Publisher" "Angelo G�lle"
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
