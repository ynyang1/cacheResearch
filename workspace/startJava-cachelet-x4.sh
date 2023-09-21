cd /home/tank/1_yanan/cacheResearch/workspace
rm log_agent*txt
java -jar cachelet-linux.jar agent1-dev $1 localhost 22221 $2 >> log_agent1.txt &
java -jar cachelet-linux.jar agent2-dev $1 localhost 22222 $2 >> log_agent2.txt &
java -jar cachelet-linux.jar agent3-dev $1 localhost 22223 $2 >> log_agent3.txt &
java -jar cachelet-linux.jar agent4-dev $1 localhost 22224 $2 >> log_agent4.txt &
