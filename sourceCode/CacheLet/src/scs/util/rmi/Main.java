package scs.util.rmi;
 
import scs.repository.Repository;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		if(args.length!=5){
			System.out.println("args length="+ args.length);
			System.out.println("[agentName, memorySize(int), ip, port, keepaliveTime(ms)]"); 
			System.exit(0);
		}else{
			Repository.init(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
		}
	}
 
}
