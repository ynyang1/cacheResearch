package scs.util.tricks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
 
import scs.util.tool.FileOperation;

public class TraceMappingMain {
	public static void main33(String[] args){
		String filePath2="D:\\cacheResearch\\cacheTrace\\new_trace\\old\\d.csv"; //id,warm_time,run_time,mem_size,time
		HashMap<String,Integer> map=new HashMap<>();
		File file = new File(filePath2); 
		BufferedReader reader = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String line = ""; 
			line=reader.readLine();
			while ((line = reader.readLine()) != null) { 
				String[] splits=line.split(",");
				if(splits.length==5){
					if(!map.containsKey(splits[0].trim())){
						map.put(splits[0].trim(), 1);
					}
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		
		System.out.println(map.size());
	}

	/**
	 * 通过距离判断公式，将real benchmark的384个函数同azure trace里的384个函数一一映射，写入文件，逗号分隔
	 * @param args
	 * @throws IOException
	 */
	public static void main4(String[] args) throws IOException {
		// TODO Auto-generated method stub
 
		String filePath="D:\\cacheResearch\\cacheTrace\\new_trace\\functions.txt";
		List<String> lineList=new FileOperation().readStringFile(filePath,false);
		List<RealBenchmarkBean> beanList=new ArrayList<>();
		for(String line:lineList){
			String[] splits=line.split(",");
			if(splits.length>=5){
				RealBenchmarkBean bean=new RealBenchmarkBean();
				bean.setFuncName(splits[0].split(":")[1]);
				bean.setMemory(Integer.parseInt(splits[2]));
				bean.setExecTime(Float.parseFloat(splits[splits.length-1]));
				beanList.add(bean);
			}

		}
		
		HashMap<String, String> map=new HashMap<>();
		
		String filePath2="D:\\cacheResearch\\cacheTrace\\new_trace\\old\\h.csv"; //id,warm_time,run_time,mem_size,time
		List<String> lineList2=TraceMappingMain.readStringFile(filePath2,true);
		
		for(String line:lineList2){
			String[] splits=line.split(",");
			if(splits.length==5){
				if(map.containsKey(splits[0])){
					continue;
				}
				if(beanList.isEmpty()){
					break;
				}
				float execTime=Float.parseFloat(splits[1]);
				int memory=Integer.parseInt(splits[3]);
				int bestMatchIndex=TraceMappingMain.bestMatch(memory, execTime, beanList);
				
				map.put(splits[0], beanList.get(bestMatchIndex).getFuncName());
				//System.out.println(execTime+","+beanList.get(bestMatchIndex).getExecTime()+","+memory+","+beanList.get(bestMatchIndex).getMemory());
				beanList.remove(bestMatchIndex);
			}

		}
		FileWriter writer=new FileWriter("D:\\cacheResearch\\cacheTrace\\new_trace\\mapping_h.txt");
		Set<String> keys= map.keySet();
		for(String key:keys){
			//System.out.println(key+","+map.get(key));
			writer.write(key+","+map.get(key)+"\n");
		}
		writer.flush();
		writer.close();

	}
	
	/**
	 * 读取映射文件，生成新的8个trace文件
	 * @param args
	 * @throws IOException
	 */
	public static void main3(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String[] files=new String[]{"a","b","c","d","e","f","g","h"};
		for(int i=1;i<8;i++){
			Random random=new Random();
			String filePath="D:\\cacheResearch\\cacheTrace\\new_trace\\functions.txt";
			List<String> funcList=new FileOperation().readStringFile(filePath,false);
			
			String filePath2="D:\\cacheResearch\\cacheTrace\\new_trace\\mapping_"+files[i]+".txt";
			List<String> mappingList=new FileOperation().readStringFile(filePath2,false);
			String filePath3="D:\\cacheResearch\\cacheTrace\\new_trace\\old\\"+files[i]+".csv";
			HashMap<String, RealBenchmarkBean> funcMap=new HashMap<>();
			for(String line:funcList){
				String[] splits=line.split(",");
				if(splits.length>=5){
					RealBenchmarkBean bean=new RealBenchmarkBean();
					bean.setFuncName(splits[0].split(":")[1]);
					bean.setMemory(Integer.parseInt(splits[2]));
					bean.setExecTime(Float.parseFloat(splits[splits.length-2]));
					bean.setColdstartTime(Float.parseFloat(splits[splits.length-1]));
					funcMap.put(bean.getFuncName(), bean);
					//System.out.println(bean.toString());
				}
			}
			System.out.println(funcMap.size());
			HashMap<String, String> mapping=new HashMap<>();
			for(String line:mappingList){
				String[] splits=line.split(",");
				if(splits.length>=2){
				 mapping.put(splits[0].trim(),splits[1].trim());
					//System.out.println(bean.toString());
				}
			}
			System.out.println(mapping.size());
			
			FileWriter writer=new FileWriter("D:\\cacheResearch\\cacheTrace\\new_trace\\"+files[i]+".csv");
			writer.write("id,warm_time,run_time,mem_size,time\n");
			File file = new File(filePath3); 
			BufferedReader reader = null;
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				reader = new BufferedReader(inputStreamReader);
				String line = ""; 
				line=reader.readLine();
				StringBuilder builder=new StringBuilder();
				int counter=0;
				while ((line = reader.readLine()) != null) { 
					String[] splits=line.split(",");
					if(splits.length==5){
						String newFuncName=mapping.get(splits[0]);
						builder.append(newFuncName).append(",");
						RealBenchmarkBean item=funcMap.get(newFuncName);
						builder.append(item.getExecTime()).append(",");
						builder.append(item.getColdstartTime()).append(",");
						builder.append(item.getMemory()).append(",");
						builder.append(splits[4]).append("\n");
						writer.write(builder.toString());
						builder.setLength(0);
						if(++counter%1000==0){
							writer.flush();
						}
					}
				}
				writer.flush();
				writer.close();

				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
		
	}
	
	public static List<String> readStringFile(String filePath, boolean skipHead) throws IOException {  
		List<String> timeList=new ArrayList<String>();
		HashMap<String, String> map=new HashMap<>();
		File file = new File(filePath); 
		BufferedReader reader = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String line = ""; 
			if(skipHead){
				line=reader.readLine();
			}
			while ((line = reader.readLine()) != null) { 
				String[] splits=line.split(",");
				if(map.size()==384){
					break;
				}
				if(!map.containsKey(splits[0].trim())){
					map.put(splits[0].trim(),"");
					timeList.add(line);
					
				}
				
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		return timeList;
	}
	
	
	/**
	 * 处理real benchmark的执行时间和冷启动时间，docker冷启动大约1秒
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
 
		Random random=new Random();
		String filePath="D:\\cacheResearch\\cacheTrace\\new_trace\\functions.txt";
		List<String> lineList=new FileOperation().readStringFile(filePath,false);
		for(String line:lineList){
			String[] splits=line.split(",");
			if(splits.length>=5){
//				int coldtime=(int) (Float.parseFloat(splits[splits.length-1].trim())+(random.nextInt(10)*1000.0f/20+1000.0f+random.nextInt(200)));
//				line=line+","+coldtime;
				if(line.contains("case17")||line.contains("case19")||line.contains("case20")||line.contains("case21")||line.contains("case22")){
					
				}else{
					line=line.replace(splits[splits.length-2]+",","");
				}
				
				System.out.println(line);
			}

		}
		System.exit(0);
		int lines=0;
		for(String line:lineList){
			++lines;
		String[] splits=line.split(",");
		if(splits.length>=5){
			String funcName=splits[0];
			 
			int exec=Integer.parseInt(splits[splits.length-2].trim());
			int cold=Integer.parseInt(splits[splits.length-1].trim());
			 
			System.out.println(cold-exec);
			 
		}

	}
	 
	}
	 
	private static float distance(int memoryA,float execTimeA, int memoryB, float execTimeB) {
		float maxExecTime=execTimeA>execTimeB?execTimeA:execTimeB;
		float execTimeDiff=Math.abs(execTimeA-execTimeB)/maxExecTime;

		int maxMemory=memoryA>memoryB?memoryA:memoryB;
		float memoryDiff=Math.abs(memoryA-memoryB)*1.0f/maxMemory;

		return execTimeDiff+memoryDiff;
	}

	private static int bestMatch(int memory,float execTime, List<RealBenchmarkBean> beanList) {
		float minDiffScore=999.0f;
		float tempDiffScore=0;
		int index=-1;
		int length=beanList.size();
		for(int i=0;i<length;i++){
			tempDiffScore=TraceMappingMain.distance(memory, execTime, beanList.get(i).getMemory(), beanList.get(i).getExecTime());
			if(tempDiffScore<minDiffScore){
				minDiffScore=tempDiffScore;
				index=i;
			} 
		}
		return index;
	}
 

}
