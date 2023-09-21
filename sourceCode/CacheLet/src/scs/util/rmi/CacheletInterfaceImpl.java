package scs.util.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import scs.pojo.CacheAreaBean;
import scs.pojo.RequestBean;
import scs.pojo.StatisticsBean;
import scs.pojo.TwoTuple;
import scs.repository.Repository; 

public class CacheletInterfaceImpl extends UnicastRemoteObject implements CacheletInterface {

	private static final long serialVersionUID = 1L;

	public CacheletInterfaceImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int reciveRequest(RequestBean request) throws RemoteException {
		
		//request.setArrivalTime(System.currentTimeMillis()); 
		return Repository.receiveRequest(request);
	}

	@Override
	public StatisticsBean display() throws RemoteException {
		// TODO Auto-generated method stub
		return Repository.display();
	}

	@Override
	public void updateHotspotFunc(Map<String,TwoTuple<Integer,Boolean>> hotspotFuncMap) throws RemoteException {
		// TODO Auto-generated method stub
		Repository.updateHotspotFunc(hotspotFuncMap);
	}

	@Override
	public CacheAreaBean statistics(int i) throws RemoteException {
		// TODO Auto-generated method stub
		return Repository.statistics(0);
	}

	@Override
	public TwoTuple<Integer,Float> calServerHotDegree(long arrivalTime, boolean evictionEnableFlag) throws RemoteException {
		// TODO Auto-generated method stub
		return Repository.calServerHotDegree(arrivalTime, evictionEnableFlag);
	}

	@Override
	public boolean probe(RequestBean request) throws RemoteException {
		// TODO Auto-generated method stub
		return Repository.probe(request);
	}

	@Override
	public Map<String, TwoTuple<Integer, Float>> getServerStatus(RequestBean request) throws RemoteException {
		// TODO Auto-generated method stub
		return Repository.getServerStatus(request);
	}

	@Override
	public TwoTuple<Integer, Float> test_calServerHotDegree(long arrivalTime, boolean evictionEnableFlag)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
 
}


