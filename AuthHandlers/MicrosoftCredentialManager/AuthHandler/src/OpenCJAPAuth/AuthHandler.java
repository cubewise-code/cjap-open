package OpenCJAPAuth;

import OpenCJAPAuth.utils.FileProperties;
import OpenCJAPAuth.utils.FileTool;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class AuthHandler{
	public String execute(String namespace, String paramString1, String paramString2){
		String username = "";
		try {
			if(!paramString1.equals("") && !paramString2.equals("")){
				String scriptPath = FileProperties.getInstance().getValue("PythonScriptPath");
				String returnUserName = FileProperties.getInstance().getValue("returnUserName");

				String cmd = "python " + scriptPath + " " + paramString1;
				Process process = Runtime.getRuntime().exec(cmd);
				InputStreamReader isr = new InputStreamReader(process.getInputStream(),"utf-8");
				LineNumberReader input = new LineNumberReader(isr);
				String output = input.readLine();
				input.close();
				isr.close();
				process.waitFor();

				if(paramString2.equals(output)){
					FileTool.appendFile("login success");
					username = returnUserName;
				}else{
					FileTool.appendFile("login failed");
				}
			}

			//return empty username if authentication failed
			return username;
		} catch (Exception e) {
			return "";
		}
	}
}