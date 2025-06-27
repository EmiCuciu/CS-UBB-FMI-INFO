#!/bin/bash
echo Content-type: text/html
echo
sleep 2 # simulam o cautare intr-o baza de date cu milioane de utilizatori :)
read dataFromClient
user=`echo $dataFromClient | cut -d"=" -f2`
if grep $user useri.dat > /dev/null
then
 echo 0
else
 echo 1
fi
