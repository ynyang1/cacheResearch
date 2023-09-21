package scs.util.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import scs.pojo.RequestBean;
import scs.repository.Repository;

public class LoadDriver {
	public void readFile(String inputFilePath,String outputFilePath,String pattern,String childPattern,int rows,boolean recordLatency)throws IOException {
		String funcName="";
		int memorySize;
		int startUpTime;
		int execTime;
		long arrivalTime;
		int i=0;
		long requestStartTime=0;
		long requestEndTime=0;

		RequestBean request=new RequestBean("",0,0,0,0);//虚拟请求
		FileWriter writer=new FileWriter(outputFilePath);

		long currentTime=System.currentTimeMillis();

		File file = new File(inputFilePath); 
		BufferedReader reader = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String line = "";
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] splits=line.split(",");
				if(splits.length==5){
					funcName=splits[0];
					execTime=(int) Float.parseFloat(splits[1]);
					startUpTime=(int) (Float.parseFloat(splits[2])-execTime);
					memorySize=Integer.parseInt(splits[3]);
					arrivalTime= (long) Float.parseFloat(splits[4]);

					if(requestStartTime==0){
						requestStartTime=arrivalTime;
					}else{
						requestEndTime=arrivalTime;
					}

					request.setArrivalTime(currentTime+arrivalTime);
					request.getFuncMetadata().setFuncName(funcName);
					request.getFuncMetadata().setExecutionTime(execTime);
					request.getFuncMetadata().setMemoryConsume(memorySize);
					request.getFuncMetadata().setStartUpTime(startUpTime);
					request.setExtraLatency(0);
					request.setTryTimes(1);

					int[] result=Repository.sendRequest(request,pattern,childPattern);
					//System.out.println("request "+ (i++) +"-->"+result[0]+" "+result[1]+" "+request.toString());
					i++;
					if(recordLatency){
						writer.write(result[1]+"\n");// requestId,agentIndex,latency
						if(i%1000==0){
							writer.flush();
						}
					}
					if(i>rows){
						//System.out.println("LoadDriver.readFile() timeSpan="+((requestEndTime-startUpTime)*1.0f/1000/60)+"min");
						break;
					}else if(i%10000==0){
						System.out.println(i+" "+ (i*100.0f/rows)+"%");
					}
				}
			}
			if (reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void readLatency(String inputFilePath,String outputFilePath,String pattern,int rows,boolean recordLatency)throws IOException {
		String funcName="";
		int memorySize;
		int startUpTime;
		int execTime;
		long arrivalTime;
		int i=0;
		int count0to200=0;
		int count200to400=0;
		int countTotal=0;
		FileWriter writer=new FileWriter(outputFilePath);
		Map<String,Integer> funcMap=new HashMap<String, Integer>();
		Map<String,Integer> funcMap200to400=new HashMap<String, Integer>();
		Map<String,Integer> funcMap0to200=new HashMap<String, Integer>();
		long currentTime=System.currentTimeMillis();

		File file = new File(inputFilePath);
		BufferedReader reader = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String line = "";
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] splits=line.split(",");
				if(splits.length==5){
					funcName=splits[0];
					execTime=Integer.parseInt(splits[1]);
					startUpTime=Integer.parseInt(splits[2])-execTime;
					memorySize=Integer.parseInt(splits[3]);
					arrivalTime= (long) Float.parseFloat(splits[4]);

					if(funcMap.containsKey(funcName)){
						funcMap.put(funcName,funcMap.get(funcName)+1);
					}else{
						funcMap.put(funcName,1);
					}

					if(startUpTime<=200){
						count0to200++;
						if(funcMap0to200.containsKey(funcName)){
							funcMap0to200.put(funcName,funcMap0to200.get(funcName)+1);
						}else{
							funcMap0to200.put(funcName,1);
						}
					}
					if(200<startUpTime&&startUpTime<=400){
						count200to400++;
						if(funcMap200to400.containsKey(funcName)){
							funcMap200to400.put(funcName,funcMap200to400.get(funcName)+1);
						}else{
							funcMap200to400.put(funcName,1);
						}
					}
					countTotal++;
					//System.out.println("request "+ (i++) +"-->"+result[0]+" "+result[1]+" "+request.toString());
					i++;
					//System.out.println("request "+ i +"-->"+result[0]+" "+result[1]+" ");
					if(recordLatency){
						writer.write(execTime+"\n");// requestId,agentIndex,latency
						if(i%1000==0){
							writer.flush();
						}
					}
					if(i>rows){
						//System.out.println("LoadDriver.readFile() timeSpan="+((requestEndTime-startUpTime)*1.0f/1000/60)+"min");
						break;
					}else if(i%100000==0){
						System.out.println("request "+ (i*100.0f/rows)+"%");
					}
				}
			}
			if (reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println(funcMap.size()+","+funcMap0to200.size()+","+funcMap200to400.size());
		//System.out.println(funcMap0to200.size());
		//System.out.println(funcMap200to400.size());
		System.out.println(countTotal+","+count0to200+","+count0to200*100.0f/countTotal+","+count200to400+", "+count200to400*100.0f/countTotal+","+(funcMap.size()+","+funcMap0to200.size()+","+funcMap200to400.size()));
	}
	
//	public static void main(String[] args){
//
//		String fileName="h";
//		try {
//			new LoadDriver().readTest("D:\\cacheResearch\\cacheTrace\\new_trace\\"+fileName+".csv", "D:\\cacheResearch\\cacheTrace\\new_trace\\memory\\"+fileName+".csv");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	  
//	}
	public void readTest(String inputFilePath, String outputFilePath)throws IOException {

		Map<String,Float> map=new HashMap<String,Float>();
		Map<String,Integer> countMap=new HashMap<String,Integer>();
		Map<String,Integer> metadataMap=new HashMap<String,Integer>();
		Map<String,Integer> memoryMap=new HashMap<String,Integer>();
		String funcName="";
		int memorySize;
		int execTime;
		File file = new File(inputFilePath); 
		BufferedReader reader = null;
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String line = "";
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] splits=line.split(",");
				if(splits.length==5){
					funcName=splits[0];
					execTime=Integer.parseInt(splits[1]);
					memorySize=Integer.parseInt(splits[3]);

					float consume=execTime/1000.0f*memorySize;
					if(!map.containsKey(funcName)){
						map.put(funcName, consume);
						countMap.put(funcName, 1);
						metadataMap.put(funcName,execTime);
						memoryMap.put(funcName,memorySize);
					}else{
						map.put(funcName, map.get(funcName)+consume);
						countMap.put(funcName, countMap.get(funcName)+1);
					}
				}
			}
			if (reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			/**
			 * output
			 */
			
			System.out.println(map.size());
			FileWriter writer=new FileWriter(outputFilePath);
			writer.write("funcName,count,memorySize,exectime,consume\n");
			Set<String> set=map.keySet();
			int total=0;
			for(String key:set){
				total+=map.get(key);
				writer.write(key+","+countMap.get(key)+","+memoryMap.get(key)+","+metadataMap.get(key)+","+map.get(key)+"\n");
				writer.flush();
			}
			System.out.println(total);
			writer.close();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void addNewHotspotCounter(Map<String,ArrayList<Integer>> map){
		Set<String> keySet=map.keySet();
		for(String key:keySet){
			map.get(key).add(0);
		}
	}


	private String outputFile(String fileOutputPath, Map<String, Integer> counterMap, Map<String,ArrayList<Integer>> timerMap) throws IOException{
		Writer writer=new FileWriter(fileOutputPath);
		StringBuilder str=new StringBuilder();
		str.append("funcName,totalInvocCount").append("\n");
		writer.write(str.toString());

		Set<String> keySet=counterMap.keySet();
		int count=0;
		for(String key:keySet){
			str.setLength(0);
			str.append(key).append(",").append(counterMap.get(key)).append(listToStr(timerMap.get(key))).append("\n");
			writer.write(str.toString());
			if((count++)%100==0){
				writer.flush();
			}
		}

		writer.flush();
		writer.close();

		return fileOutputPath;

	}
	private String listToStr(ArrayList<Integer> list){
		StringBuilder str=new StringBuilder();
		for(int item:list){
			str.append(",").append(item);
		}
		return str.toString();
	}

}
