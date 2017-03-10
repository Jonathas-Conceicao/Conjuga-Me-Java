#!/bin/bash

set -e

read -n 1 -p "Change default (/opt/bin) binary location?(y/N)?" choice
echo
case "$choice" in
  y|Y ) read -p "Binary location:" BIN_LOCATION; echo;;
  * ) export BIN_LOCATION='/opt/bin';;
esac

read -n 1 -p "Change default (/opt/ConjugaMe) instalation folder?(y/N)?" choice
echo
case "$choice" in
  y|Y ) read -p "Instalation folder:" JAR_LOCATION; echo;;
  * ) export JAR_LOCATION='/opt/ConjugaMe';;
esac

read -n 1 -p "Change default (/usr/local/share/man/man1) manual folder?(y/N)?" choice
echo
case "$choice" in
  y|Y ) read -p "Manual folder:" MAN_LOCATION; echo;;
  * ) export MAN_LOCATION='/usr/local/share/man/man1';;
esac

read -n 1 -p "Change default (/etc/bash_completion.d/) bash complete folder?(y/N)?" choice
echo
case "$choice" in
  y|Y ) read -p "Manual folder:" AUTO_COMPLETE_LOCATION; echo;;
  * ) export AUTO_COMPLETE_LOCATION='/etc/bash_completion.d';;
esac
echo

echo "## Making Package ##"
make all
echo

echo "## Making Jar file ##"
make jar
echo

echo "## Making executable ##"
sed 's|PROGRAM_FOLDER|'"$JAR_LOCATION"'|g' < executable_sample.sh > conjugame
echo "executable created under the name 'conjugame'"
sed 's|PROGRAM_FOLDER|'"$JAR_LOCATION"'|g' < complete_script > complete_conjugame
chmod 777 conjugame
echo

echo "## Installing ##"
if [ ! -d "$JAR_LOCATION"/.Jar ]; then
  echo "Creating directory for program's files"
  mkdir --parent "$JAR_LOCATION"/.Jar
fi
if [ ! -d "$BIN_LOCATION" ]; then
  echo "Creating directory for program's binary"
  mkdir --parent "$BIN_LOCATION"
fi

if [ ! -d "$MAN_LOCATION" ]; then
  echo "Creating directory for program's manual page"
  mkdir --parent "$MAN_LOCATION"
fi

if [ ! -d "$AUTO_COMPLETE_LOCATION" ]; then
  echo "Creating directory for auto complete file"
  mkdir --parent "$AUTO_COMPLETE_LOCATION"
fi

make install
. $AUTO_COMPLETE_LOCATION/conjugame
echo

echo "## Cleanning unecessary files ##"
  make clean
echo

echo "All done!"
echo -e "Run 'conjugame \e[4mverb on infinitive form\e[0m'"
echo "Run 'man conjugame' for more details"
