package scs.util.rmi;
 
import scs.repository.Repository;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		if(args.length!=5){
			System.out.println("args length="+ args.length);
			System.out.println("[cacheletCount(int+), portStart(int+), cacheletName(string), awareDispatcherFlag(true/false), hotspot_threshold(0.0f) ]"); 
			System.exit(0);
		} 
		int cacheletCount=Integer.parseInt(args[0]);
		int portStart=Integer.parseInt(args[1]);
		String cacheletName=args[2];
		StringBuilder str=new StringBuilder();
		//int cacheletCount=8;
		for(int i=0;i<cacheletCount;i++){
			str.append(cacheletName).append(i+1).append("-dev_localhost_").append(portStart+i).append("#");
		}
		//System.out.println(str.toString().subSequence(0, str.length()-1));
		
		//String parm="agent1-dev_localhost_33331#agent2-dev_localhost_33332#agent3-dev_localhost_33333#agent4-dev_localhost_33334#agent5-dev_localhost_33335#agent6-dev_localhost_33336#agent7-dev_localhost_33331#agent2-dev_localhost_33332#agent3-dev_localhost_33333";
		String parm=(String) str.toString().subSequence(0, str.length()-1);
		boolean awareDispatcherFlag=false;
		if(args[3]!=null&&args[3].equals("true")){
			awareDispatcherFlag=true;
		}
		Repository.init(parm, Float.parseFloat(args[4]), awareDispatcherFlag);
	}
 
}
