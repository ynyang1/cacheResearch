package scs.util.tricks;

public class DockerConfigMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i=1;i<100;i++){
			int memory=i*128;
			int cpu=200+100*i;
			//System.out.println("docker update flame --cpu-period=100000 --cpu-quota="+cpu*100+" --memory="+memory+"m");
		}

		/*String functionName="alu_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		int[] parmList=new int[]{1000,10000,100000,1000000};
		int[] parmList2=new int[]{4};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			for(int parm:parmList){
				System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+"_"+parmList2[0]+""+":latest"+","+cpu+","+memory+","+parm+","+parmList2[0]+",");
			}
		}*/
		 

		/*String functionName="key-downloader_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		int[] parmList=new int[]{512,1024,4096};
		int[] parmList2=new int[]{1};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			for(int parm:parmList){
				System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
			}
		}*/

		/*String functionName="extract-image-metadata_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="001";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/

		/*String functionName="case4-store-image-metadata_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="001";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/

		/*String functionName="case4-thumbnail_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="001";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/

		/*String functionName="case4-tail-bigparam_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="5000000";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/

		/*String functionName="case6-chameleon_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="500";
			String parm2="500";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+"_"+parm2+":latest"+","+cpu+","+memory+","+parm+","+parm2+",");
		}*/

		/*String functionName="case7-float-operation_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="10000"; 
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/

		/*String functionName="case8-linpack_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="200"; 
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/

		/*String functionName="case9-matmul_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="100"; 
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/
		
		/*String functionName="case10-pyaes_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="1000";
			String parm2="00";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+"_"+parm2+":latest"+","+cpu+","+memory+","+parm+","+parm2+",");
		}*/

		/*String functionName="case11-dd_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="4096";
			String parm2="1024";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+"_"+parm2+":latest"+","+cpu+","+memory+","+parm+","+parm2+",");
		}*/

		/*String functionName="case12-giz-compress_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="1";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/

		/*String functionName="case13-random-diskio_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="4";
			String parm2="512";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+"_"+parm2+":latest"+","+cpu+","+memory+","+parm+","+parm2+",");
		}*/

		/*String functionName="case14-image-process_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="image.jpg";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/

		/*String functionName="case16-image-scale_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="image.jpg";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/

		/*String functionName="case17-cnn-image-classification_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="image.jpg";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/

		/*String functionName="case18-video-process_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="1280";
			String parm2="720";
			String parm3="3";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+"_"+parm2+"_"+parm3+":latest"+","+cpu+","+memory+","+parm+","+parm2+","+parm3+",");
		}*/
		
		/*String functionName="case19-lr-prediction_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="10";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/
		 

		/*String functionName="case20-face-detection_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="0.2";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+":latest"+","+cpu+","+memory+","+parm+",");
		}*/

		/*String functionName="case21-rnn-generate-char-level_";
		int[] memoryList=new int[]{128,256,512,768,1024,1280,1536,1792,2048,2560,4096,8192,10240};
		for(int memory:memoryList){
			int cpu=200+100*memory/128;
			String parm="German";
			String parm2="afeigwe";
			System.out.println("ServerlessBench:"+functionName+cpu+"_"+memory+"m"+"_"+parm+","+parm2+":latest"+","+cpu+","+memory+","+parm+","+parm2+",");
		}*/
		




	}

}



