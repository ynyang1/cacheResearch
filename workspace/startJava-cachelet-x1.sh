cd /home/tank/1_yanan/cacheResearch/workspace
rm log_agent*txt
java -jar cachelet-linux.jar agent1-dev $1 localhost 22221 $2 >> log_agent1.txt &
