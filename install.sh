set -e

read -n 1 -p "Change default (/opt/bin) binary location?(y/N)?" choice
echo

case "$choice" in
  y|Y ) read -p "Binary location:" BIN_LOCATION;echo;;
  * ) export BIN_LOCATION='/opt/bin';;
esac

read -n 1 -p "Change default (/opt/ConjugaMe) instalation folder?(y/N)?" choice
echo

case "$choice" in
  y|Y ) read -p "Instalation folder:" JAR_LOCATION;echo;;
  * ) export JAR_LOCATION='/opt/ConjugaMe';;
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
chmod +x conjugame
echo

echo "## Installing ##"
if [ ! -d "$JAR_LOCATION"/Jar ]; then
  echo "Creating directory for program's files"
  mkdir --parent "$JAR_LOCATION"/Jar
fi
if [ ! -d "$BIN_LOCATION" ]; then
  echo "Creating directory for program's binary"
  mkdir --parent "$BIN_LOCATION"
fi
make install
echo

echo "## Cleanning unecessary files ##"
  make clean
echo

echo "All done!"
echo -e "Run 'conjugame \e[4mverb on infinitive form\e[0m'"
echo "Run 'man conjugame' for more details"
