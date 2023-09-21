package scs.util.rmi; 

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Map;

import scs.pojo.AgentBean;
import scs.pojo.RequestBean;
import scs.pojo.TwoTuple;


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
			AgentInterface agent = new AgentInterfaceImpl();
			Naming.rebind("rmi://"+ip+":"+port+"/agent", agent);
			System.out.println(""+ip+":"+port+" rmi server started");
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public int reciveRequestCall(RequestBean request, AgentBean cacheletAgent){
		int latency=-1;
		try {
			latency=cacheletAgent.getRmiInterface().reciveRequest(request);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("Repository.cacheletAgentInterfaceCall(): cacheletAgent reconnect...");
			setupCacheAgentRmiConnection(cacheletAgent.getIp(), cacheletAgent.getPort(), "re");
		}
		return latency;
	}

	public void updateHotspotFuncCall(AgentBean cacheletAgent, Map<String,TwoTuple<Integer,Boolean>> hotspotFuncMap){
		try {
			cacheletAgent.getRmiInterface().updateHotspotFunc(hotspotFuncMap);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("Repository.cacheletAgentInterfaceCall(): cacheletAgent reconnect...");
			setupCacheAgentRmiConnection(cacheletAgent.getIp(), cacheletAgent.getPort(), "re");
		}
	}
	
	public TwoTuple<Integer,Float> calServerHotDegreeCall(AgentBean cacheletAgent,long arrivalTime, boolean evictionEnableFlag){
		TwoTuple<Integer,Float> result=new TwoTuple<Integer,Float>();
		try {
			result=cacheletAgent.getRmiInterface().calServerHotDegree(arrivalTime,evictionEnableFlag);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("Repository.cacheletAgentInterfaceCall(): cacheletAgent reconnect...");
			setupCacheAgentRmiConnection(cacheletAgent.getIp(), cacheletAgent.getPort(), "re");
		}
		return result;
	}
	public TwoTuple<Integer,Float> test_calServerHotDegreeCall(AgentBean cacheletAgent,long arrivalTime, boolean evictionEnableFlag){
		TwoTuple<Integer,Float> result=new TwoTuple<Integer,Float>();
		try {
			result=cacheletAgent.getRmiInterface().test_calServerHotDegree(arrivalTime,evictionEnableFlag);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("Repository.cacheletAgentInterfaceCall(): cacheletAgent reconnect...");
			setupCacheAgentRmiConnection(cacheletAgent.getIp(), cacheletAgent.getPort(), "re");
		}
		return result;
	}

	public CacheletInterface setupCacheAgentRmiConnection(String ip, int port, String status){
		CacheletInterface agent=null;
		try {
			agent=(CacheletInterface) Naming.lookup("rmi://"+ip+":"+port+"/agent");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		if(agent!=null){
			System.out.println("rmi://"+ip+":"+port+"/agent" + status +" connection build successfully");
		}else{
			System.out.println("rmi://"+ip+":"+port+"/agent" + status +" connection build unsuccessfully");
		}
		return agent;
	}

}
