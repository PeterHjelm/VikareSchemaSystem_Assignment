#!/bin/bash

echo 'INSTRUCTIONS'
echo 'Start by extracting the zip-files and then in your terminal you navigate to the folder "my-lab". In
that folder you can compile all the Java-files with:

javac -cp www/WEB-INF/classes 
www/WEB-INF/classes/se/yrgo/schedule/servlet/*.java \
www/WEB-INF/classes/se/yrgo/schedule/data/*.java \
www/WEB-INF/classes/se/yrgo/schedule/format/*.java \
www/WEB-INF/classes/se/yrgo/schedule/domain/*.java

Also in the folder "my-lab" you start winstone with the command: 
java -jar lib/winstone.jar --webroot=www --httpPort=8080'
echo
echo 'Now the Servlet is up and running! Which means you can test the program'

echo 'The script to test the API you start by typing "./Instructions.sh" in the Terminal'
echo
echo 'Here we test what result we get when we pass the format json together with the parameter of substitue_id=666' 
echo
echo 'curl http://localhost:8080/v1?format=json&substitute_id=666'
curl 'http://localhost:8080/v1?format=json&substitute_id=666'
echo
echo 'The outcome should be only an empty array "[]"'
echo
echo 'The next test is to see all teachers assignments for a spexcific date:'
echo 'curl http://localhost:8080/v1?format=json&day=2018-01-16'
curl 'http://localhost:8080/v1?format=json&day=2018-01-16'
echo
echo 'Here we are getting all teachers assignments for the date 2018-01-16. The assignments includes the name
and the address for the school that each teacher are planned to work at that day.'
echo
