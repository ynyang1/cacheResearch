package scs.util.rmi; 

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import scs.pojo.RequestBean;
import scs.repository.Repository;


public class RmiService {
	private static RmiService agentService=null;
	private RmiService(){}
	public synchronized static RmiService getInstance() {
		if (agentService == null) {
			agentService = new RmiService();
		}
		return agentService;
	}  

	public void service(String ip,int port) {
		try {
			System.setProperty("java.rmi.server.hostname",ip);
			LocateRegistry.createRegistry(port);
			CacheletInterface agent = new CacheletInterfaceImpl();  
			Naming.rebind("rmi://"+ip+":"+port+"/agent", agent);
			System.out.println(""+ip+":"+port+" rmi server started");

		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println(port+"is already used");
		} catch (MalformedURLException e) {
		}
	}

	
	public AgentInterface setupCacheManagerRmiConnection(String ip, int port, String status){
		int waitingTimeout=120*1000;
		long startTime=System.currentTimeMillis();
		AgentInterface cacheManager=null;
		while(cacheManager==null&&(System.currentTimeMillis()-startTime)<waitingTimeout){
			try {
				Thread.sleep(3000);
				cacheManager=(AgentInterface) Naming.lookup("rmi://"+ip+":"+port+"/agent");
			} catch (MalformedURLException | RemoteException | NotBoundException | InterruptedException e) {
				continue;
			}
		}
		if(cacheManager!=null){
			System.out.println("rmi://"+ip+":"+port+"/agent " + status +" connection build successfully");
		}else{
			System.out.println("rmi://"+ip+":"+port+"/agent " + status +" connection build unsuccessfully");
		}
		return cacheManager;
	}

	public boolean cacheManagerRedirectRequestRmiCall(AgentInterface cacheManager,RequestBean request, boolean evictionEnableFlag){
		boolean result=false;
		try {
			result=cacheManager.redirectRequest(request, evictionEnableFlag);
		} catch (RemoteException e) {
			e.printStackTrace(); 
		}
		return result;
	}
}
