package scs.util.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import scs.pojo.RequestBean;
import scs.pojo.StatisticsBean;
import scs.pojo.TwoTuple;
import scs.repository.Repository;

public class AgentInterfaceImpl extends UnicastRemoteObject implements AgentInterface {

	private static final long serialVersionUID = 1L;

	public AgentInterfaceImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
 
	@Override
	public boolean redirectRequest(RequestBean request, boolean evictionEnableFlag) throws RemoteException {
		// TODO Auto-generated method stub
		return Repository.redirectRequest(request, evictionEnableFlag);
	}

	@Override
	public int receiveRequest(RequestBean request,String pattern) throws RemoteException {
		// TODO Auto-generated method stub
		return Repository.receiveRequest(request,pattern);
	}

	@Override
	public StatisticsBean display(int agentIndex) throws RemoteException {
		// TODO Auto-generated method stub
		return Repository.display(agentIndex);
	}

	@Override
	public boolean probe(RequestBean request) throws RemoteException{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TwoTuple<Integer, Float> calServerLoad(long arrivalTime) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void testSchedule(int funcMemory, long arrivalTime, boolean evictionEnableFlag) throws RemoteException {
		// TODO Auto-generated method stub
		long start=System.nanoTime();
		Repository.test_scheduleFunc(funcMemory, arrivalTime, evictionEnableFlag);
		System.out.println((System.nanoTime()-start));
	}

}


