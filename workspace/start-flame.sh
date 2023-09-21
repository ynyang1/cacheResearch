#sh start-flame.sh 23040 a rehash 300000 0.5f
sh stopJava.sh

sh startJava-cachelet-x8.sh $1 $4
sleep 2
sh startJava-cacheManager.sh 8 22221 true $5
sleep 1
java -jar cacheLoadGen_linux_flameManual.jar $3 false $1 $2 8 8
