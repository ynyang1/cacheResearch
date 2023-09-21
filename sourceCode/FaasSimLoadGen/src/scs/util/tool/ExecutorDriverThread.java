package scs.util.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class ExecutorDriverThread extends Thread{
	private String command;

	public ExecutorDriverThread(String command) {
		this.command=command;
	}

	@Override
	public void run() {
		//System.out.println(command);
		try {  
			String line = null,err;
			Process process = Runtime.getRuntime().exec(command); 
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			InputStreamReader isr = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(isr); 
			while (((err = br.readLine()) != null||(line = input.readLine()) != null)) {
				if (err==null) {
					//System.out.println(line);
				} else {
					System.out.println(err);
				}
			}  
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}

