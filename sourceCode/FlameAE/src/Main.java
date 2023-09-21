import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) throws IOException{
		//
		
		args=new String[]{"rehash","trace_a_23040_8_3111827.csv"};
		
		if(args.length<2){
			System.out.println("[Loadbalancing, Filename]");
			System.out.println("e.g., java -jar result.jar rehash trace_a_23040_8_3111827.csv");
			System.exit(0);
		}

		String loadbalancing=args[0];
		String fileName=args[1];
		 
		String baseFilePath="";
		if(args.length==3){
			baseFilePath=args[2];
		}
		if(baseFilePath.equals("")){
			baseFilePath="/home/tank/1_yanan/cacheResearch/";
			//baseFilePath="D:\\cacheResearch\\";
		}
		
		System.out.println("Result filename="+fileName);
		System.out.println("Workspace base path="+baseFilePath);
		System.out.println("Evaluation Output:");
		System.out.println("-----------------------");
		
		FileOperation fileOperation=new FileOperation();
		
		String[] systems=new String[]{"Keepalive","FaasCache","CHRLU","Icebreaker","Flame"};
		for(String system:systems){
			String item=baseFilePath+"coldstartRate/temp/"+system+"/"+loadbalancing+"/"+fileName;
			//item=baseFilePath+"coldstartRate\\temp\\"+system+"\\"+loadbalancing+"\\"+fileName;
			//System.out.println(item);
			File file=new File(item);
			List<String> list=new ArrayList<>();
			if(file.exists()){
				list=fileOperation.readStringFile(item);
				String line=list.get(list.size()-1);
				String[] splits=line.split(",");
				String cacheMemUsage=splits[6];
				String invocations=splits[7];
				String coldRate=splits[12];
				System.out.println(system+" policy:");
				System.out.printf("  %-20s%-20s%-20s\n", "# of Requests", "CacheUsage (MB)", "ColdstartRate");
				System.out.printf("  %-20s%-20s%-20s\n", invocations, cacheMemUsage, coldRate);
			    
			}
		}
		
		
		
	}
}
