package scs.util.rmi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.omg.CORBA.SystemException;

import scs.repository.Repository;
import scs.util.tool.ExecutorDriverThread;
import scs.util.tool.FileOperation;
import scs.util.tool.LoadDriver;

public class Main {

	public static void main(String[] args){
	
		try {
			new Main().evaluateIcebreakerManual(args);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main3(String[] args){


		ArrayList<TestBean> testList=new ArrayList<TestBean>();

		/*testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.7f, 8, true, 300000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.9f, 8, true, 300000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.5f, 8, true, 600000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.7f, 8, true, 600000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.9f, 8, true, 600000));*/

		/*testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.5f, 8, true, 600000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.7f, 8, true, 600000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.9f, 8, true, 600000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.5f, 8, true, 60000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.7f, 8, true, 60000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.9f, 8, true, 60000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.5f, 8, true, 180000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.7f, 8, true, 180000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.9f, 8, true, 180000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.5f, 8, true, 900000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.7f, 8, true, 900000));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache", 0.9f, 8, true, 900000));*/ //109


		/*testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.7f, 8, true, 300000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.9f, 8, true, 300000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.5f, 8, true, 600000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.7f, 8, true, 600000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.9f, 8, true, 600000));*/
		/*testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.5f, 8, true, 60000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.7f, 8, true, 60000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.9f, 8, true, 60000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.5f, 8, true, 180000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.7f, 8, true, 180000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.9f, 8, true, 180000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.5f, 8, true, 900000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.7f, 8, true, 900000));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache", 0.9f, 8, true, 900000)); */ // 129

		/*testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.7f, 8, true, 300000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.9f, 8, true, 300000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.5f, 8, true, 600000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.7f, 8, true, 600000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.9f, 8, true, 600000));
		/*testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.5f, 8, true, 60000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.7f, 8, true, 60000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.9f, 8, true, 60000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.5f, 8, true, 180000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.7f, 8, true, 180000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.9f, 8, true, 180000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.5f, 8, true, 900000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.7f, 8, true, 900000));
		testList.add(new TestBean("h", 1652, "rehash", "full","faascache", 0.9f, 8, true, 900000));*/ //136

		/*testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.7f, 8, true, 300000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.9f, 8, true, 300000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.5f, 8, true, 600000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.7f, 8, true, 600000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.9f, 8, true, 600000));*/
		/*testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.5f, 8, true, 60000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.7f, 8, true, 60000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.9f, 8, true, 60000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.5f, 8, true, 180000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.7f, 8, true, 180000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.9f, 8, true, 180000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.5f, 8, true, 900000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.7f, 8, true, 900000));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache", 0.9f, 8, true, 900000));*/ //137

		/*testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.7f, 8, true, 300000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.9f, 8, true, 300000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.5f, 8, true, 600000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.7f, 8, true, 600000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.9f, 8, true, 600000));
		/*testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.5f, 8, true, 60000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.7f, 8, true, 60000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.9f, 8, true, 60000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.5f, 8, true, 180000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.7f, 8, true, 180000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.9f, 8, true, 180000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.5f, 8, true, 900000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.7f, 8, true, 900000));
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache", 0.9f, 8, true, 900000)); *///120

		//testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		//testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.7f, 8, true, 300000));
		//testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.9f, 8, true, 300000));
		//testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.5f, 8, true, 600000));
		//testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.7f, 8, true, 600000));
		//testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.9f, 8, true, 600000));
		/*testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.5f, 8, true, 60000));
		testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.7f, 8, true, 60000));
		testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.9f, 8, true, 60000));
		testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.5f, 8, true, 180000));
		testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.7f, 8, true, 180000));
		testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.9f, 8, true, 180000));
		testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.5f, 8, true, 900000));
		testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.7f, 8, true, 900000));
		testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.9f, 8, true, 900000)); //140
		testList.add(new TestBean("d", 636, "rehash", "full","faascache", 0.9f, 8, true, 900000));*/

		/*testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.7f, 8, true, 300000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.9f, 8, true, 300000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.5f, 8, true, 600000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.7f, 8, true, 600000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.9f, 8, true, 600000));
		/*testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.5f, 8, true, 60000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.7f, 8, true, 60000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.9f, 8, true, 60000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.5f, 8, true, 180000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.7f, 8, true, 180000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.9f, 8, true, 180000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.5f, 8, true, 900000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.7f, 8, true, 900000));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache", 0.9f, 8, true, 900000));*/ //141

		/*testList.add(new TestBean("f", 5970, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("f", 5970, "rehash", "full","faascache", 0.7f, 8, true, 300000));
		testList.add(new TestBean("f", 5970, "rehash", "full","faascache", 0.9f, 8, true, 300000));
		testList.add(new TestBean("f", 5970, "rehash", "full","faascache", 0.5f, 8, true, 600000));
		testList.add(new TestBean("f", 5970, "rehash", "full","faascache", 0.7f, 8, true, 600000));
		testList.add(new TestBean("f", 5970, "rehash", "full","faascache", 0.9f, 8, true, 600000));*/
		//		
		//		
		//		ArrayList<TestBean> testList2=new ArrayList<TestBean>();
		//		
		//		for(int i=testList.size()-1;i>=0;i--){
		//			testList2.add(testList.get(i));
		//		}
		//		testList.clear();
		//		testList.addAll(testList2);
		//		
		//		testList.add(new TestBean("h", 1652, "rehash", "full","faascache",0.9f)); 
		//		testList.add(new TestBean("g", 9018, "rehash", "full","faascache",0.9f));
		//		testList.add(new TestBean("f", 5970, "rehash", "full","faascache",0.9f));
		//		testList.add(new TestBean("e", 3430, "rehash", "full","faascache",0.9f));
		//		testList.add(new TestBean("d", 636, "rehash", "full","faascache",0.9f));
		//		testList.add(new TestBean("c", 4446, "rehash", "full","faascache",0.9f));
		//		testList.add(new TestBean("b", 1144, "rehash", "full","faascache",0.9f));
		//		testList.add(new TestBean("a", 1906, "rehash", "full","faascache",0.9f));




		/*testList.add(new TestBean("h", 1652, "rehash", "full","faascache",0.95f)); 
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache",0.95f));
		testList.add(new TestBean("f", 5970, "rehash", "full","faascache",0.95f));
		testList.add(new TestBean("d", 636, "rehash", "full","faascache",0.95f));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache",0.95f));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache",0.95f));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache",0.95f));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache",0.95f));*/

		/*testList.add(new TestBean("h", 1652, "rehash", "full","faascache",0.9f)); // windows
		testList.add(new TestBean("g", 9018, "rehash", "full","faascache",0.9f));
		testList.add(new TestBean("f", 5970, "rehash", "full","faascache",0.9f));
		testList.add(new TestBean("e", 3430, "rehash", "full","faascache",0.9f));
		testList.add(new TestBean("d", 636, "rehash", "full","faascache",0.9f));
		testList.add(new TestBean("c", 4446, "rehash", "full","faascache",0.9f));
		testList.add(new TestBean("b", 1144, "rehash", "full","faascache",0.9f));
		testList.add(new TestBean("a", 1906, "rehash", "full","faascache",0.9f));*/


		/*testList.add(new TestBean("h", 1906, "rehash", "full","keepalive")); //localhost
		testList.add(new TestBean("g", 2414, "rehash", "full","keepalive"));
		testList.add(new TestBean("f", 11558, "rehash", "full","keepalive"));
		testList.add(new TestBean("e", 4192, "rehash", "full","keepalive"));
		testList.add(new TestBean("d", 1144, "rehash", "full","keepalive"));
		testList.add(new TestBean("c", 5208, "rehash", "full","keepalive"));
		testList.add(new TestBean("b", 1398, "rehash", "full","keepalive"));
		testList.add(new TestBean("a", 2414, "rehash", "full","keepalive"));*/

		/*testList.add(new TestBean("a", 10542, "round", "full","faascache"));
		testList.add(new TestBean("b", 4096, "round", "full","faascache"));
		testList.add(new TestBean("c", 5632, "round", "full","faascache"));
		testList.add(new TestBean("d", 1792, "round", "full","faascache"));
		testList.add(new TestBean("e", 8192, "round", "full","faascache"));
		testList.add(new TestBean("f", 12288, "round", "full","faascache"));
		testList.add(new TestBean("g", 14336, "round", "full","faascache"));
		testList.add(new TestBean("h", 12288, "round", "full","faascache"));*/

		/*testList.add(new TestBean("a", 16384, "round", "full","keepalive"));
		testList.add(new TestBean("b", 3684, "round", "full","keepalive"));
		testList.add(new TestBean("c", 6732, "round", "full","keepalive"));
		testList.add(new TestBean("d", 1652, "round", "full","keepalive"));
		testList.add(new TestBean("e", 16384, "round", "full","keepalive"));
		testList.add(new TestBean("f", 16384, "round", "full","keepalive"));
		testList.add(new TestBean("g", 16384, "round", "full","keepalive"));
		testList.add(new TestBean("h", 16384, "round", "full","keepalive"));*/

		//		testList.add(new TestBean("a", 10542, "round", "full","faascache", 0.9f, 8, true, 180000));
		//		testList.add(new TestBean("b", 4096, "round", "full","faascache", 0.9f, 8, true, 180000));
		//		testList.add(new TestBean("c", 5632, "round", "full","faascache", 0.9f, 8, true, 180000));
		//		testList.add(new TestBean("d", 1792, "round", "full","faascache", 0.9f, 8, true, 180000));
		//		testList.add(new TestBean("e", 8192, "round", "full","faascache", 0.9f, 8, true, 180000));
		//		testList.add(new TestBean("f", 12288, "round", "full","faascache", 0.9f, 8, true, 180000));
		//		testList.add(new TestBean("g", 14336, "round", "full","faascache", 0.9f, 8, true, 180000));
		//		testList.add(new TestBean("h", 12288, "round", "full","faascache", 0.9f, 8, true, 180000));
		//		testList.add(new TestBean("g", 14336, "round", "full","faascache", 0.5f, 8, true, 300000));
		//		testList.add(new TestBean("h", 12288, "round", "full","faascache", 0.5f, 8, true, 300000));

		//		testList.add(new TestBean("a", 953, "rehash", "full","faascache", 0.5f, 16, true, 300000));
		//		testList.add(new TestBean("b", 572, "rehash", "full","faascache", 0.5f, 16, true, 300000));
		//		testList.add(new TestBean("c", 2223, "rehash", "full","faascache", 0.5f, 16, true, 300000));
		//		testList.add(new TestBean("d", 318, "rehash", "full","faascache", 0.5f, 16, true, 300000));
		//		testList.add(new TestBean("e", 1715, "rehash", "full","faascache", 0.5f, 16, true, 300000));
		//		testList.add(new TestBean("f", 2985, "rehash", "full","faascache", 0.5f, 16, true, 300000));
		//		testList.add(new TestBean("g", 4509, "rehash", "full","faascache", 0.5f, 16, true, 300000));
		//		testList.add(new TestBean("h", 826, "rehash", "full","faascache", 0.5f, 16, true, 300000));


		/*testList.add(new TestBean("a", 3812, "rehash", "full","faascache", 0.5f, 8, true, 300000));
	    testList.add(new TestBean("b", 2288, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("c", 8892, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("d", 1272, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("e", 6860, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("f", 11940, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("g", 18036, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("h", 3304, "rehash", "full","faascache", 0.5f, 8, true, 300000));*///129 done

		/*testList.add(new TestBean("h", 826, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("g", 4509, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("f", 2985, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("e", 1715, "rehash", "full","faascache", 0.5f, 8, true, 300000)); //dell done
		testList.add(new TestBean("d", 318, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("c", 2223, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		testList.add(new TestBean("b", 572, "rehash", "full","faascache", 0.5f, 8, true, 300000));
		/*testList.add(new TestBean("a", 953, "rehash", "full","faascache", 0.5f, 8, true, 300000));*/

		//		testList.add(new TestBean("a", 1906, "random", "full","faascache", 0.5f, 8, true, 300000));
		//		testList.add(new TestBean("b", 1144, "random", "full","faascache", 0.5f, 8, true, 300000));
		//		testList.add(new TestBean("c", 4446, "random", "full","faascache", 0.5f, 8, true, 300000));
		//		testList.add(new TestBean("d", 636, "random", "full","faascache", 0.5f, 8, true, 300000));
		//		testList.add(new TestBean("e", 3430, "random", "full","faascache", 0.5f, 8, true, 300000));
		//		testList.add(new TestBean("f", 5970, "random", "full","faascache", 0.5f, 8, true, 300000));
		//		testList.add(new TestBean("g", 9018, "random", "full","faascache", 0.5f, 8, true, 300000));
		//		testList.add(new TestBean("h", 1652, "random", "full","faascache", 0.5f, 8, true, 300000));

		testList.add(new TestBean("a", 84336, "round", "full","faascache", 0.5f, 1, true, 300000));
		testList.add(new TestBean("b", 32768, "round", "full","faascache", 0.5f, 1, true, 300000));
		testList.add(new TestBean("c", 45056, "round", "full","faascache", 0.5f, 1, true, 300000));
		testList.add(new TestBean("d", 14336, "round", "full","faascache", 0.5f, 1, true, 300000));
		testList.add(new TestBean("e", 65536, "round", "full","faascache", 0.5f, 1, true, 300000));
		testList.add(new TestBean("f", 98304, "round", "full","faascache", 0.5f, 1, true, 300000));
		testList.add(new TestBean("g", 114688, "round", "full","faascache", 0.5f, 1, true, 300000));
		testList.add(new TestBean("h", 98304, "round", "full","faascache", 0.5f, 1, true, 300000));

		for(TestBean item:testList){
			try {
				boolean recordLatency=true;
				float[] res = new Main().evaluateOurworkAuto(item.getCacheletCount(), item.getKeepaliveTime(), 
						item.getAgentMemorySize(), item.getHotspotThreshold(), item.getPattern(), item.getTrace(), 
						item.getRequestCount(), recordLatency, item.getBaseline());

				System.out.println(item.toString());
				System.out.println("sloViolationRate="+res[0]+ "memoryUsage="+res[1]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main2(String[] args){
		ArrayList<TestBean> testList=new ArrayList<TestBean>();
		testList.add(new TestBean("h", 12288, "CHRLU", "full","none",0.9f, 8, true, 300000)); 
		testList.add(new TestBean("g", 14336, "CHRLU", "full","none",0.9f, 8, true, 300000));
		testList.add(new TestBean("f", 12288, "CHRLU", "full","none",0.9f, 8, true, 300000));
		testList.add(new TestBean("e", 8192, "CHRLU", "full","none",0.9f, 8, true, 300000));
		testList.add(new TestBean("d", 1792, "CHRLU", "full","none",0.9f, 8, true, 300000));
		testList.add(new TestBean("c", 5632, "CHRLU", "full","none",0.9f, 8, true, 300000));
		testList.add(new TestBean("b", 4096, "CHRLU", "full","none",0.9f, 8, true, 300000));
		testList.add(new TestBean("a", 10542, "CHRLU", "full","none",0.9f, 8, true, 300000));
		for(TestBean item:testList){
			try {
				boolean recordLatency=true;
				float[] res = new Main().evaluateFaasCacheAuto(item.getAgentMemorySize(), item.getHotspotThreshold(), item.getPattern(),
						item.getTrace(), item.getRequestCount(), recordLatency, item.getBaseline(), item.getCacheletCount());

				System.out.println(item.toString());
				System.out.println("sloViolationRate="+res[0]+ "memoryUsage="+res[1]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}
	private void evaluateIcebreakerManual(String[] args) throws InterruptedException, IOException {
		long start=System.currentTimeMillis();
		String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
		String funLevelOutputFile="D:\\cacheResearch\\output\\overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix="D:\\cacheResearch\\coldstartRate\\temp\\";
		String pattern="rehash";
		boolean recordLatency=false;
		int serverMemory=40960;
		String trace="e";
		int workloadCount=Repository.reqCountMap.get(trace);
		String fileName="trace_"+trace+"_"+serverMemory+"_8_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                        
		new LoadDriver().readFile("D:\\cacheResearch\\cacheTrace\\new_trace\\"+trace+".csv",outputPathPrefix+"latency_"+fileName, pattern, "",workloadCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,1);
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);
	}
	private void evaluateFaasCacheManual(String[] args) throws InterruptedException, IOException {
		long start=System.currentTimeMillis();
		String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
		String funLevelOutputFile="D:\\cacheResearch\\output\\overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix="D:\\cacheResearch\\coldstartRate\\temp\\";
		String pattern="rehash";
		boolean recordLatency=false;
		int serverMemory=23040;
		String trace="a";
		int workloadCount=Repository.reqCountMap.get(trace);
		String fileName="trace_"+trace+"_"+serverMemory+"_8_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                        
		new LoadDriver().readFile("D:\\cacheResearch\\cacheTrace\\new_trace\\"+trace+".csv",outputPathPrefix+"latency_"+fileName, pattern, "",workloadCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,1);
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);
	}
	
	private void evaluateFaasCacheManualLinux(String[] args) throws InterruptedException, IOException {
		long start=System.currentTimeMillis();
		String workspacePath="/home/tank/1_yanan/cacheResearch/";
		String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
		String funLevelOutputFile=workspacePath+"output/overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix=workspacePath+"coldstartRate/temp/";
		
		/*String pattern="rehash";
		boolean recordLatency=false;
		int serverMemory=8192;
		String trace="a";*/
		if(args.length<6){
			System.out.println("pattern recordLatency serverMemory trace controllerCount subtrollerCount");
			System.out.println("e.g., rehash false 4096 a 8 1");
			System.exit(0);
		}
		String pattern=args[0];
		boolean recordLatency=Boolean.parseBoolean(args[1]);
		int serverMemory=Integer.parseInt(args[2]);
		String trace=args[3];
		int controllerCount=Integer.parseInt(args[4]);
		int subtrollerCount=Integer.parseInt(args[5]);;
		int workloadCount=Repository.reqCountMap.get(trace);
		String fileName="trace_"+trace+"_"+serverMemory+"_"+controllerCount+"_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                        
		new LoadDriver().readFile(workspacePath+"cacheTrace/new_trace/"+trace+".csv",outputPathPrefix+"latency_"+fileName, pattern, "",workloadCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,subtrollerCount);
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);
	}

	private float[] evaluateFaasCacheAuto(int serverMemory, float threshold, String pattern, String trace, 
			String workloadCount, boolean recordLatency, String baseline,int nodeCount) throws InterruptedException, IOException {

		String workspacePath="D:\\cacheResearch\\";

		ExecutorDriverThread stopJavaThread=new ExecutorDriverThread(workspacePath+"workspace\\stopJava.bat");
		stopJavaThread.run();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ExecutorDriverThread startFaasCacheThread=new ExecutorDriverThread("sh "+workspacePath+"workspace\\startJava-faascache.sh "+serverMemory);
		startFaasCacheThread.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**
		 * evaluate
		 */
		int portStart=22221;
		long start=System.currentTimeMillis();
		StringBuilder str=new StringBuilder();
		for(int i=0;i<nodeCount;i++){
			str.append("agent").append(i+1).append("_localhost_").append(portStart+i).append("#");
		}
		String parm=(String) str.toString().subSequence(0, str.length()-1);
		//String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
		String funLevelOutputFile=workspacePath+"output\\overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix=workspacePath+"coldstartRate\\temp\\";
		int requestCount=-1;
		if(workloadCount.equals("full")){
			requestCount=Integer.MAX_VALUE;
		}else{
			requestCount=Integer.parseInt(workloadCount);
		}

		String fileName="trace_"+trace+"_"+serverMemory+"_"+nodeCount+"_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                          
		new LoadDriver().readFile(workspacePath+"cacheTrace\\new_trace\\"+trace+".csv",outputPathPrefix+"latency_"+fileName,pattern, "", requestCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,1);
		//168,52,0,4096,0.99975586,10000,0.0086,0.0052,0.9862,0.9914
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);


		/**
		 * 结果数据汇总
		 */
		String resultFold="faascache_"+pattern+"_trace_"+trace+"_"+serverMemory+"_"+workloadCount;
		ExecutorDriverThread resultHandlerThread=new ExecutorDriverThread("sh "+workspacePath+"workspace\\resultHandler.sh "+resultFold);
		resultHandlerThread.run();

		ExecutorDriverThread stopJavaThread2=new ExecutorDriverThread(workspacePath+"workspace\\stopJava.bat");
		stopJavaThread2.run();

		return res;
	}
	private float[] evaluateFaasCacheAutoLinux(int serverMemory, float threshold, String pattern, String trace, 
			String workloadCount, boolean recordLatency, String baseline, int nodeCount) throws InterruptedException, IOException {

		String workspacePath="/home/tank/1_yanan/cacheResearch/";

		ExecutorDriverThread stopJavaThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/stopJava.sh");
		stopJavaThread.run();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ExecutorDriverThread startThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/startJava-faascache-x"+nodeCount+".sh "+serverMemory);
		startThread.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**
		 * evaluate
		 */

		long start=System.currentTimeMillis();
		int portStart=22221;
		StringBuilder str=new StringBuilder();
		for(int i=0;i<nodeCount;i++){
			str.append("agent").append(i+1).append("_localhost_").append(portStart+i).append("#");
		}
		String parm=(String) str.toString().subSequence(0, str.length()-1);
		//String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
		String funLevelOutputFile=workspacePath+"output/overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix=workspacePath+"coldstartRate/temp/";
		int requestCount=-1;
		if(workloadCount.equals("full")){
			requestCount=Integer.MAX_VALUE;
		}else{
			requestCount=Integer.parseInt(workloadCount);
		}

		String fileName="trace_"+trace+"_"+serverMemory+"_"+nodeCount+"_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                          
		new LoadDriver().readFile(workspacePath+"cacheTrace/new_trace/"+trace+".csv",outputPathPrefix+"latency_"+fileName,pattern, "", requestCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,1);
		//168,52,0,4096,0.99975586,10000,0.0086,0.0052,0.9862,0.9914
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);


		/**
		 * 结果数据汇总
		 */
		String resultFold="faascache_"+pattern+"_trace_"+trace+"_"+serverMemory+"_"+workloadCount;
		ExecutorDriverThread resultHandlerThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/resultHandler.sh "+resultFold);
		resultHandlerThread.run();

		ExecutorDriverThread stopJavaThread2=new ExecutorDriverThread("sh "+workspacePath+"workspace/stopJava.sh");
		stopJavaThread2.run();

		return res;
	}
	private void evaluateFaasKeepaliveManual(String[] args) throws InterruptedException, IOException {
		long start=System.currentTimeMillis();
		String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
		String funLevelOutputFile="D:\\cacheResearch\\output\\overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix="D:\\cacheResearch\\coldstartRate\\temp\\";

		String pattern="rehash";
		boolean recordLatency=false;
		int workloadCount=Integer.MAX_VALUE;
		int serverMemory=9018;
		String trace="g";
		String fileName="trace_"+trace+"_"+serverMemory+"_8_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                          
		new LoadDriver().readFile("D:\\cacheResearch\\cacheTrace\\new_trace\\"+trace+".csv",outputPathPrefix+"latency_"+fileName, pattern, "",workloadCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,1);
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);
	}
	private void evaluateKeepaliveManualLinux(String[] args) throws InterruptedException, IOException {
		long start=System.currentTimeMillis();
		String workspacePath="/home/tank/1_yanan/cacheResearch/";
		String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
		String funLevelOutputFile=workspacePath+"output/overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix=workspacePath+"coldstartRate/temp/";
		
		/*String pattern="rehash";
		boolean recordLatency=false;
		int serverMemory=8192;
		String trace="a";*/
		if(args.length<6){
			System.out.println("pattern recordLatency serverMemory trace controllerCount subtrollerCount");
			System.out.println("e.g., rehash false 4096 a 8 1");
			System.exit(0);
		}
		String pattern=args[0];
		boolean recordLatency=Boolean.parseBoolean(args[1]);
		int serverMemory=Integer.parseInt(args[2]);
		String trace=args[3];
		int controllerCount=Integer.parseInt(args[4]);
		int subtrollerCount=Integer.parseInt(args[5]);
		int workloadCount=Repository.reqCountMap.get(trace);
		String fileName="trace_"+trace+"_"+serverMemory+"_"+controllerCount+"_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                        
		new LoadDriver().readFile(workspacePath+"cacheTrace/new_trace/"+trace+".csv",outputPathPrefix+"latency_"+fileName, pattern, "",workloadCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,subtrollerCount);
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);
	}
	


	private float[] evaluateFaasKeepaliveAuto(int serverMemory, float threshold, String pattern, String trace, 
			String workloadCount, boolean recordLatency, String baseline) throws InterruptedException, IOException {

		String workspacePath="D:\\cacheResearch\\";

		ExecutorDriverThread stopJavaThread=new ExecutorDriverThread(workspacePath+"workspace\\stopJava.bat");
		stopJavaThread.run();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ExecutorDriverThread startFaasCacheThread=new ExecutorDriverThread("sh "+workspacePath+"workspace\\startJava-keepalive.sh "+serverMemory);
		startFaasCacheThread.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**
		 * evaluate
		 */

		long start=System.currentTimeMillis();
		String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
		String funLevelOutputFile="D:\\cacheResearch\\output\\overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix=workspacePath+"coldstartRate\\temp\\";
		int requestCount=-1;
		if(workloadCount.equals("full")){
			requestCount=Integer.MAX_VALUE;
		}else{
			requestCount=Integer.parseInt(workloadCount);
		}

		String fileName="trace_"+trace+"_"+serverMemory+"_8_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                          
		new LoadDriver().readFile(workspacePath+"cacheTrace\\new_trace\\"+trace+".csv",outputPathPrefix+"latency_"+fileName,pattern, "", requestCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,1);
		//168,52,0,4096,0.99975586,10000,0.0086,0.0052,0.9862,0.9914
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);


		/**
		 * 结果数据汇总
		 */
		String resultFold="keepalive_"+pattern+"_trace_"+trace+"_"+serverMemory+"_"+workloadCount;
		ExecutorDriverThread resultHandlerThread=new ExecutorDriverThread("sh "+workspacePath+"workspace\\resultHandler.sh "+resultFold);
		resultHandlerThread.run();

		ExecutorDriverThread stopJavaThread2=new ExecutorDriverThread(workspacePath+"workspace\\stopJava.bat");
		stopJavaThread2.run();

		return res;
	}

	private float[] evaluateFaasKeepaliveAutoLinux(int serverMemory, float threshold, String pattern, String trace, 
			String workloadCount, boolean recordLatency, String baseline, int nodeCount) throws InterruptedException, IOException {

		String workspacePath="/home/tank/1_yanan/cacheResearch/";

		ExecutorDriverThread stopJavaThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/stopJava.sh");
		stopJavaThread.run();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ExecutorDriverThread startThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/startJava-keepalive-x"+nodeCount+".sh "+serverMemory);
		startThread.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**
		 * evaluate
		 */

		long start=System.currentTimeMillis();
		int portStart=22221;
		StringBuilder str=new StringBuilder();
		for(int i=0;i<nodeCount;i++){
			str.append("agent").append(i+1).append("_localhost_").append(portStart+i).append("#");
		}
		String parm=(String) str.toString().subSequence(0, str.length()-1);
		//String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
		String funLevelOutputFile=workspacePath+"output/overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix=workspacePath+"coldstartRate/temp/";
		int requestCount=-1;
		if(workloadCount.equals("full")){
			requestCount=Integer.MAX_VALUE;
		}else{
			requestCount=Integer.parseInt(workloadCount);
		}

		String fileName="trace_"+trace+"_"+serverMemory+"_"+nodeCount+"_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                          
		new LoadDriver().readFile(workspacePath+"cacheTrace/new_trace/"+trace+".csv",outputPathPrefix+"latency_"+fileName,pattern, "", requestCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,1);
		//168,52,0,4096,0.99975586,10000,0.0086,0.0052,0.9862,0.9914
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);


		/**
		 * 结果数据汇总
		 */
		String resultFold="keepalive_"+pattern+"_trace_"+trace+"_"+serverMemory+"_"+workloadCount;
		ExecutorDriverThread resultHandlerThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/resultHandler.sh "+resultFold);
		resultHandlerThread.run();

		ExecutorDriverThread stopJavaThread2=new ExecutorDriverThread("sh "+workspacePath+"workspace/stopJava.sh");
		stopJavaThread2.run();

		return res;
	}
	public void evaluateOurworkManual(String[] args) throws InterruptedException, IOException {
		long start=System.currentTimeMillis();
		String parm="cacheManager-dev_localhost_33330";
		String funLevelOutputFile="D:\\cacheResearch\\output\\overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix="D:\\cacheResearch\\coldstartRate\\temp\\";
		String pattern="rehash";
		boolean recordLatency=true;
		int serverMemory=23040;
		String trace="a";
		int workloadCount=Repository.reqCountMap.get(trace);
		String fileName="trace_"+trace+"_"+serverMemory+"_8_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                              
		new LoadDriver().readFile("D:\\cacheResearch\\cacheTrace\\new_trace\\"+trace+".csv",outputPathPrefix+"latency_"+fileName, "", pattern, workloadCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,8);
		//168,52,0,4096,0.99975586,10000,0.0086,0.0052,0.9862,0.9914
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);

	}
	public void evaluateOurworkManualLinux(String[] args) throws InterruptedException, IOException {
		long start=System.currentTimeMillis();
		String workspacePath="/home/tank/1_yanan/cacheResearch/";
		String parm="cacheManager-dev_localhost_33330";
		String funLevelOutputFile=workspacePath+"output/overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

        String outputPathPrefix=workspacePath+"coldstartRate/temp/";
		
		/*String pattern="rehash";
		boolean recordLatency=false;
		int serverMemory=8192;
		String trace="a";*/
		if(args.length<6){
			System.out.println("pattern recordLatency serverMemory trace controllerCount subtrollerCount");
			System.out.println("e.g., rehash false 4096 a 8 1");
			System.exit(0);
		}
		String pattern=args[0];
		boolean recordLatency=Boolean.parseBoolean(args[1]);
		int serverMemory=Integer.parseInt(args[2]);
		String trace=args[3];
		int controllerCount=Integer.parseInt(args[4]);
		int subtrollerCount=Integer.parseInt(args[5]);;
		int workloadCount=Repository.reqCountMap.get(trace);
		String fileName="trace_"+trace+"_"+serverMemory+"_"+controllerCount+"_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                        
		new LoadDriver().readFile(workspacePath+"cacheTrace/new_trace/"+trace+".csv",outputPathPrefix+"latency_"+fileName,"",pattern,workloadCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,subtrollerCount);
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);

	}
	private float[] evaluateOurworkAuto(int cacheletCount, int keepaliveTime, int serverMemory, float threshold, String pattern, String trace, 
			String workloadCount, boolean recordLatency, String baseline) throws InterruptedException, IOException {

		String workspacePath="D:\\cacheResearch\\";
		ExecutorDriverThread stopJavaThread=new ExecutorDriverThread(workspacePath+"workspace\\stopJava.bat");
		stopJavaThread.run();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ExecutorDriverThread startThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/startJava-cachelet-x"+cacheletCount+".sh "+serverMemory+" "+keepaliveTime);
		startThread.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		boolean awareDispatcherFlag=false;
		int portStart=22221;
		ExecutorDriverThread startCacheManagerThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/startJava-cacheManager.sh "+cacheletCount+" "+portStart+" "+awareDispatcherFlag+" "+threshold);
		startCacheManagerThread.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**
		 * evaluate
		 */

		long start=System.currentTimeMillis();
		String parm="cacheManager-dev_localhost_33330";
		String funLevelOutputFile="D:\\cacheResearch\\output\\overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix=workspacePath+"coldstartRate\\temp\\";
		int requestCount=-1;
		if(workloadCount.equals("full")){
			requestCount=Integer.MAX_VALUE;
		}else{
			requestCount=Integer.parseInt(workloadCount);
		}

		String fileName="trace_"+trace+"_"+serverMemory+"_"+cacheletCount+"_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                          
		new LoadDriver().readFile(workspacePath+"cacheTrace\\new_trace\\"+trace+".csv",outputPathPrefix+"latency_"+fileName,"", pattern, requestCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,cacheletCount);
		//168,52,0,4096,0.99975586,10000,0.0086,0.0052,0.9862,0.9914
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);

		String resultFold="baseline_"+baseline+"_cachelet_"+pattern+"_trace_"+trace+"_"+serverMemory+"_"+workloadCount+"_"+threshold;
		ExecutorDriverThread resultHandlerThread=new ExecutorDriverThread("sh "+workspacePath+"workspace\\resultHandler.sh "+resultFold);
		resultHandlerThread.run();

		ExecutorDriverThread stopJavaThread2=new ExecutorDriverThread(workspacePath+"workspace\\stopJava.bat");
		stopJavaThread2.run();

		return res;
	}

	private float[] evaluateOurworkAutoLinux(int cacheletCount, int keepaliveTime, int serverMemory, float threshold, String pattern, String trace, 
			String workloadCount, boolean recordLatency, String baseline) throws InterruptedException, IOException {

		String workspacePath="/home/tank/1_yanan/cacheResearch/";
		ExecutorDriverThread stopJavaThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/stopJava.sh");
		stopJavaThread.run();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ExecutorDriverThread startCacheletThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/startJava-cachelet-x"+cacheletCount+".sh "+serverMemory+" "+keepaliveTime);
		startCacheletThread.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		boolean awareDispatcherFlag=true;
		int portStart=22221;

		ExecutorDriverThread startCacheManagerThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/startJava-cacheManager.sh "+cacheletCount+" "+portStart+" "+awareDispatcherFlag+" "+threshold);
		startCacheManagerThread.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**
		 * evaluate
		 */

		long start=System.currentTimeMillis();
		String parm="cacheManager-dev_localhost_33330";
		String funLevelOutputFile=workspacePath+"output/overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		String outputPathPrefix=workspacePath+"coldstartRate/temp/";

		int requestCount=-1;
		if(workloadCount.equals("full")){
			requestCount=Integer.MAX_VALUE;
		}else{
			requestCount=Integer.parseInt(workloadCount);
		}

		String fileName="trace_"+trace+"_"+serverMemory+"_"+cacheletCount+"_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                          
		new LoadDriver().readFile(workspacePath+"cacheTrace/new_trace/"+trace+".csv",outputPathPrefix+"latency_"+fileName,"", pattern, requestCount, recordLatency);
		float[] res=Repository.display(outputPathPrefix+fileName,cacheletCount);
		//168,52,0,4096,0.99975586,10000,0.0086,0.0052,0.9862,0.9914
		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);


		String resultFold="baseline_"+baseline+"_cachelet_"+pattern+"_trace_"+trace+"_"+cacheletCount+"nodes_"+serverMemory+"MBs_"+keepaliveTime+"ms_"+workloadCount+"reqs_"+threshold+"_"+awareDispatcherFlag;
		ExecutorDriverThread resultHandlerThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/resultHandler.sh "+resultFold);
		resultHandlerThread.run();

		ExecutorDriverThread stopJavaThread2=new ExecutorDriverThread("sh "+workspacePath+"workspace/stopJava.sh");
		stopJavaThread2.run();

		return res;
	}


	public static void calMemCost(String trace, int serverMemory) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<Integer> keepaliveTimeList=new ArrayList<Integer>();
		//		keepaliveTimeList.add(60000);
		//keepaliveTimeList.add(180000);
		keepaliveTimeList.add(300000);
		//		keepaliveTimeList.add(600000);
		//		keepaliveTimeList.add(900000);

		ArrayList<String> thresholdList=new ArrayList<String>();
		thresholdList.add("0.5");
		//		thresholdList.add("0.7");
		//		thresholdList.add("0.9");
		//String trace="a";
		String baseline="faascache";
		String pattern="round";
		int cacheletCount=8;
		//int serverMemory=1906;
		String workloadCount="full";
		boolean awareDispatcherFlag=true;
		for(int keepaliveTime:keepaliveTimeList){
			for(String threshold:thresholdList){
				String resultFold="baseline_"+baseline+"_cachelet_"+pattern+"_trace_"+trace+"_"+cacheletCount+"nodes_"+serverMemory+"MBs_"+keepaliveTime+"ms_"+workloadCount+"reqs_"+threshold+"_"+awareDispatcherFlag;
				//System.out.println(resultFold);
				/*if(trace.equals("f")){
					resultFold=trace+"_"+threshold+"_"+serverMemory+"_"+keepaliveTime/1000+"s";
					workloadCount="2147483647";
				}*/
				List<String> fileStrList=new FileOperation().readStringFile("C:\\Users\\DELL\\Desktop\\baseline2\\temp\\"+resultFold+"\\trace_"+trace+"_"+serverMemory+"_"+cacheletCount+"_"+workloadCount+".csv");
				if(fileStrList.size()>0){
					String[] splits=fileStrList.get(fileStrList.size()-1).split(",");
					if(splits.length==13){
						System.out.println(splits[6]);
					}
				}
				//				for(String line:fileStrList){
				//					System.out.println(line);
				//				}
			}
		}
	}

	public static void calColdstartRate(String trace, int serverMemory) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<Integer> keepaliveTimeList=new ArrayList<Integer>();
		//		keepaliveTimeList.add(60000);
		//		keepaliveTimeList.add(180000);
		keepaliveTimeList.add(300000);
		//		keepaliveTimeList.add(600000);
		//		keepaliveTimeList.add(900000);

		ArrayList<String> thresholdList=new ArrayList<String>();
		thresholdList.add("0.5");
		//		thresholdList.add("0.7");
		//		thresholdList.add("0.9");
		//String trace="d";
		String baseline="faascache";
		String pattern="round";
		int cacheletCount=8;
		//int serverMemory=636;
		String workloadCount="full";
		boolean awareDispatcherFlag=true;
		for(int keepaliveTime:keepaliveTimeList){
			for(String threshold:thresholdList){
				String resultFold="baseline_"+baseline+"_cachelet_"+pattern+"_trace_"+trace+"_"+cacheletCount+"nodes_"+serverMemory+"MBs_"+keepaliveTime+"ms_"+workloadCount+"reqs_"+threshold+"_"+awareDispatcherFlag;
				//System.out.println(resultFold);
				/*if(trace.equals("f")){
					resultFold=trace+"_"+threshold+"_"+serverMemory+"_"+keepaliveTime/1000+"s";
					workloadCount="2147483647";
				}*/
				List<String> fileStrList=new FileOperation().readStringFile("C:\\Users\\DELL\\Desktop\\baseline2\\temp\\"+resultFold+"\\trace_"+trace+"_"+serverMemory+"_"+cacheletCount+"_"+workloadCount+".csv");
				if(fileStrList.size()>0){
					String[] splits=fileStrList.get(fileStrList.size()-1).split(",");
					if(splits.length==13){
						System.out.println(splits[12]);
					}
				}
				//				for(String line:fileStrList){
				//					System.out.println(line);
				//				}
			}
		}
	}
}
class TestBean{
	private String trace;
	private int agentMemorySize;
	private String pattern;
	private String requestCount;
	private String baseline;
	private int cacheletCount;
	private float hotspotThreshold;
	private boolean cacheAwareDispatch;
	private int keepaliveTime;

	/*public TestBean(String trace, int agentMemorySize, String pattern, String requestCount, String baseline) {
		super();
		this.trace = trace;
		this.agentMemorySize = agentMemorySize;
		this.pattern = pattern;
		this.requestCount = requestCount;
		this.baseline = baseline;
	}*/

	public TestBean(String trace, int agentMemorySize, String pattern, String requestCount, String baseline,float threshold, int cacheletCount, boolean cacheAwareDispatch, int keepaliveTime) {
		super();
		this.trace = trace;
		this.agentMemorySize = agentMemorySize;
		this.pattern = pattern;
		this.requestCount = requestCount;
		this.baseline = baseline;
		this.hotspotThreshold=threshold;
		this.cacheletCount=cacheletCount;
		this.cacheAwareDispatch=cacheAwareDispatch;
		this.keepaliveTime=keepaliveTime;
	}

	public String getTrace() {
		return trace;
	}
	public int getAgentMemorySize() {
		return agentMemorySize;
	}
	public String getPattern() {
		return pattern;
	}
	public String getRequestCount() {
		return requestCount;
	}
	public void setTrace(String trace) {
		this.trace = trace;
	}
	public void setAgentMemorySize(int agentMemorySize) {
		this.agentMemorySize = agentMemorySize;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public void setRequestCount(String requestCount) {
		this.requestCount = requestCount;
	}

	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}

	public String getBaseline() {
		return baseline;
	}
	public void setHotspotThreshold(float hotspotThreshold) {
		this.hotspotThreshold = hotspotThreshold;
	}

	public float getHotspotThreshold() {
		return hotspotThreshold;
	}

	public int getCacheletCount() {
		return cacheletCount;
	}

	public boolean isCacheAwareDispatch() {
		return cacheAwareDispatch;
	}

	public void setCacheletCount(int cacheletCount) {
		this.cacheletCount = cacheletCount;
	}

	public void setCacheAwareDispatch(boolean cacheAwareDispatch) {
		this.cacheAwareDispatch = cacheAwareDispatch;
	}

	public int getKeepaliveTime() {
		return keepaliveTime;
	}

	public void setKeepaliveTime(int keepaliveTime) {
		this.keepaliveTime = keepaliveTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestBean [trace=");
		builder.append(trace);
		builder.append(", agentMemorySize=");
		builder.append(agentMemorySize);
		builder.append(", pattern=");
		builder.append(pattern);
		builder.append(", requestCount=");
		builder.append(requestCount);
		builder.append(", baseline=");
		builder.append(baseline);
		builder.append("]");
		return builder.toString();
	}
}
