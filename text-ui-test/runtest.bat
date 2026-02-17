@ECHO OFF

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM compile the code into the bin folder
REM Use gradle to compile with all dependencies
cd ..
call gradlew build -q
cd text-ui-test

REM run the program using gradle, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath ..\build\classes\java\main catbot.Catbot < input.txt > ACTUAL.TXT

REM compare the output to the expected output
FC ACTUAL.TXT EXPECTED.TXT
