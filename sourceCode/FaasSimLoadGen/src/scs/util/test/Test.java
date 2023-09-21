package scs.util.test;

import java.awt.datatransfer.FlavorTable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Test {

	/**
	 * 将超过100M的trace文件拆分为固定行数的csv文件
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String[] traceList=new String[]{"b1","b2","b3","b4"};
		String funcName="case7-float-operation_800_768m_100000";
		String outputPath="D:\\cacheResearch\\cacheTrace\\new_trace\\sub-trace\\arriveTime\\"+funcName+".csv";
		FileWriter writer=new FileWriter(outputPath);

		for(String trace:traceList){
			String inputPath="D:\\cacheResearch\\cacheTrace\\new_trace\\sub-trace\\b\\"+trace+".csv";; //id,warm_time,run_time,mem_size,time

			int count=0;
			float lastArrivalTime=-1;
			HashMap<String,Integer> map=new HashMap<>();
			File file = new File(inputPath); 
			BufferedReader reader = null;
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				reader = new BufferedReader(inputStreamReader);
				String line = ""; 
				line=reader.readLine();
				while ((line = reader.readLine()) != null) {
					if(line.contains(funcName)){
						count++;
						float arrivalTime=Float.parseFloat(line.split(",")[4]);
						if(lastArrivalTime==-1){
							lastArrivalTime=arrivalTime;
						}else{

							writer.write((arrivalTime-lastArrivalTime)+"\n");
							lastArrivalTime=arrivalTime;
							if(count%1000==0){
								writer.flush();
							}
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

			
		}
		writer.flush();
		writer.close();


	}

	/**
	 * 将超过100M的trace文件拆分为固定行数的csv文件
	 * @param args
	 * @throws IOException
	 */
	public static void main2(String[] args) throws IOException {
		String[] traceList=new String[]{"b","c","d"};
		for(String trace:traceList){
			String outputPath="D:\\cacheResearch\\cacheTrace\\new_trace\\sub-trace\\"+trace+"4.csv";
			FileWriter writer=new FileWriter(outputPath);

			String filePath2="D:\\cacheResearch\\cacheTrace\\new_trace\\"+trace+".csv"; //id,warm_time,run_time,mem_size,time

			int count=0;
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
					count++;
					if(count<1300000*3){
						continue;
					}
					if(count==1300000*4){
						break;
					}else{
						writer.write(line+"\n"); 
						if(count%1000==0){
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

			System.out.println(map.size());
		}

	}
}
