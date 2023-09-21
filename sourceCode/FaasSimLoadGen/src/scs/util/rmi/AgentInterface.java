package scs.util.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
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
public interface AgentInterface extends Remote {
	public void redirectRequest(RequestBean request,String sourceAgent) throws RemoteException; //cachelet专用，由cachemanager暴露给cachelet
	public int receiveRequest(RequestBean request,String pattern) throws RemoteException; //return latency
	public StatisticsBean display(int agentIndex) throws RemoteException;
	public boolean probe(RequestBean request) throws RemoteException;// check the available instance to handle request
	public TwoTuple<Integer,Float> calServerLoad(long arrivalTime) throws RemoteException;
	public int serverMemDisplay() throws RemoteException; //faascache用于统计内存占用的接口 ，其它服务不用这个
	public void testSchedule(int funcMemory, long arrivalTime, boolean evictionEnableFlag) throws RemoteException; //flaem测试sheduler的并发性延迟的接口，其它服务不用这个
}
