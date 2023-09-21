# cacheResearch

# Installation
	  
Users need to prepare a local testbed with Linux operating system (e.g., Ubuntu 16.04), and download the source code and scripts from Github to the server. The base workspace is named cacheResearch. 
The following is the directory structure of the source code, scripts, and instructions:
	
- README.md: This file has detailed instructions to conduct experiments.
- sourceCode: The source code of Flame for CacheManager and Cachelet.
- cacheTrace: The workload trace files in Flame's evaluation.
- workspace: The workspace of Flame's evaluation, which contains the compiled  binary files of Flame's, the scripts to launch Flame's CacheManager and Cachelet, the scripts to start Flame's evaluations, and the scripts to display the comparison results of Flame.
- coldstartRate: The evaluation results of Flame's competing techniques.


	
# Launching Flame
After downloading the source code and scripts, users can choose to manually compile the source code of Flame's CacheManager and Cachelet. For easy-to-user, we provide the compiled binary files of Flame's core components, these runnable files (.jar) can be listed as following commands:
  
```
$ mkdir /home/tank/1_yanan/ && cd /home/tank/1_yanan/
$ git clone http://github.com/Flame/cacheResearch/ 
$ cd cacheResearch/workspace/
$ ls 
... 
cacheLoadGen_linux_flameManual.jar 
cachelet-linux.jar 
cacheManager-linux.jar 
result.jar 
start-flame.sh 
... 
```

Then, using the following commands to launch Flame system and conduct the evaluations:
	

```
$ source /etc/profile
$ sh start-flame.sh 23040 a rehash 300000 0.5f
............... Evaluation start ................
1000 0.1\%
2000 0.2\%
...
3110000 99.9\%
1921s rehash
............... Evaluation stopped ................
```

 
The start-flame.sh file is the script to launch an experiment of Flame's cache management. It requires five parameters, including the server memory configuration, the trace type, the load balancing policy, the keep-alive time for non-hotspot functions, and the hotspot region size.

We can change the input parameters to evaluate Flame's effectiveness under different workload traces. For example, 
- sh start-flame.sh 23040 a rehash 300000 0.5
- sh start-flame.sh 50176 b rehash 300000 0.5
- sh start-flame.sh 81920 c rehash 300000 0.5

> Note that users can arbitrarily change the configuration of server memory and the workload trace to be evaluated, e.g., using `sh start-flame.sh 10000 a rehash 300000 0.5` or `sh start-flame.sh 25600 a rehash 300000 0.5`. However, we only provide the experimental results of the competing systems from a specific server memory configuration for each workload trace, if the user want to compare the cache efficiency of Flame with the other four techniques, please use the following server memory configuration for each workload trace.


Workload | Server memory (MB)
---|---
a | 23040
b | 50176
c | 81920
d | 38912
e | 40960
f | 262144
g | 18432
h | 38912


We can also change the load balancing policy from consistent hash to round-robin, for example,

- sh start-flame.sh 23040 a round 300000 0.5
- sh start-flame.sh 50176 b round 300000 0.5
- sh start-flame.sh 81920 c round 300000 0.5

 
When the experiment finishes, user can use the following commands to get the parse the evaluation results of Flame, and the comparison results will also display from the shell script.

The evaluation result of Flame is recorded in directory  'xxx/cacheResearch/coldstartRate/temp/', user should first collect the results of Flame in specific directory using the following commands:
    
```
$ cd ../coldstartRate/temp/ 
$ mv *csv Flame/rehash/
```
	
# Comparison Result: 

We have conducted the evaluation results of four competing techniques in directory 'coldstartRate/temp/', each of them is stored in a separated fold which is named by their system names. User can use the following commands to compare the cache efficiency of the competing systems:
	
```
$ java -jar result.jar a 23040  
Workload trace=a, server cache capacity=23040 MB 
Workspace base path=xxx 
Evaluation Output: 
-----------------------             
Keepalive policy: 
# of Requests        CacheUsage (MB)      ColdstartRate 
xxx              xxx        xxx 
FaasCache policy: 
# of Requests       CacheUsage (MB)     ColdstartRate 
xxx              xxx        xxx 
... 
... 
Flame policy: 
# of Requests       CacheUsage (MB)     ColdstartRate 
xxx ;             xxx  ;      xxx 
```

Due to the storage space limitatiton in GitHub, which does not allow files exceed 100MB be uploaded in the repository. Therefore, we only update the sub-set for each workload trace, and this would lead to an unrepresentative comparison result for Flame. 

For a complete evaluation, we also provide a local testbed for the user, and the local testbed can be accessed from a remote shell commands in the following:

```
# Log in the remote server 123.56.105.188, password is xxx
$ ssh  root@123.56.105.188

then, execute the following commands in server 123.56.105.188

$ ssh tank@localhost -p 8387     
$ cd /home/xxx/cacheResearch/
```
 
After that, please follows the instructions in `Launching Flame` section.

Any question please contact us from ynyang@tju.edu.cn.