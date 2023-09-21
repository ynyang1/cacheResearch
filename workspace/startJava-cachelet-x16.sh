cd /home/tank/1_yanan/cacheResearch/workspace
rm log_agent*txt
java -jar cachelet-linux.jar agent1-dev $1 localhost 22221 $2 >> log_agent1.txt &
java -jar cachelet-linux.jar agent2-dev $1 localhost 22222 $2 >> log_agent2.txt &
java -jar cachelet-linux.jar agent3-dev $1 localhost 22223 $2 >> log_agent3.txt &
java -jar cachelet-linux.jar agent4-dev $1 localhost 22224 $2 >> log_agent4.txt &
java -jar cachelet-linux.jar agent5-dev $1 localhost 22225 $2 >> log_agent5.txt &
java -jar cachelet-linux.jar agent6-dev $1 localhost 22226 $2 >> log_agent6.txt &
java -jar cachelet-linux.jar agent7-dev $1 localhost 22227 $2 >> log_agent7.txt &
java -jar cachelet-linux.jar agent8-dev $1 localhost 22228 $2 >> log_agent8.txt &
java -jar cachelet-linux.jar agent9-dev $1 localhost 22229 $2 >> log_agent9.txt &
java -jar cachelet-linux.jar agent10-dev $1 localhost 22230 $2 >> log_agent10.txt &
java -jar cachelet-linux.jar agent11-dev $1 localhost 22231 $2 >> log_agent11.txt &
java -jar cachelet-linux.jar agent12-dev $1 localhost 22232 $2 >> log_agent12.txt &
java -jar cachelet-linux.jar agent13-dev $1 localhost 22233 $2 >> log_agent13.txt &
java -jar cachelet-linux.jar agent14-dev $1 localhost 22234 $2 >> log_agent14.txt &
java -jar cachelet-linux.jar agent15-dev $1 localhost 22235 $2 >> log_agent15.txt &
java -jar cachelet-linux.jar agent16-dev $1 localhost 22236 $2 >> log_agent16.txt &
