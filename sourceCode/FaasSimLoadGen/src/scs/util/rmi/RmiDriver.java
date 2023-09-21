package scs.util.rmi;

 
public class RmiDriver {
	private static RmiDriver driver=null;
	private RmiDriver(){}
	public synchronized static RmiDriver getInstance() {
		if (driver == null) {
			driver = new RmiDriver();
		}
		return driver;
	}  

	
}
