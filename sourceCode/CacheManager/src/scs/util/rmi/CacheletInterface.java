package scs.util.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import scs.pojo.CacheAreaBean;
import scs.pojo.RequestBean;
import scs.pojo.StatisticsBean;
import scs.pojo.TwoTuple; 
/**
 * RMI interface class, which is used to control the load generator
 * The functions can be call by remote client code
 * @author Yanan Yang
 * @date 2019-11-11
 * @address TianJin University
 * @version 2.0
 */
public interface CacheletInterface extends Remote {
	public int reciveRequest(RequestBean request) throws RemoteException;
	public boolean probe(RequestBean request) throws RemoteException; //used in rehash policy
	public StatisticsBean display() throws RemoteException;
	public void updateHotspotFunc(Map<String,TwoTuple<Integer,Boolean>> hotspotFuncMap) throws RemoteException;
    public CacheAreaBean statistics(int i) throws RemoteException;
    public TwoTuple<Integer,Float> calServerHotDegree(long arrivalTime, boolean evictionEnableFlag) throws RemoteException;
    public Map<String,TwoTuple<Integer,Float>> getServerStatus(RequestBean request) throws RemoteException;
    public TwoTuple<Integer,Float> test_calServerHotDegree(long arrivalTime, boolean evictionEnableFlag) throws RemoteException;
}
