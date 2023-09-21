#taskkill /f /t /im java.exe #window bash
jps -l |grep linux.jar | awk '{print $1 }'|xargs kill -9
