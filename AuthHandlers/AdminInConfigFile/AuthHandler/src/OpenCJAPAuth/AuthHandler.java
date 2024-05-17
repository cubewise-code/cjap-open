package OpenCJAPAuth;

import OpenCJAPAuth.utils.FileProperties;
import OpenCJAPAuth.utils.FileTool;

public class AuthHandler{

	public String execute(String namespace, String paramString1, String paramString2){
		String username = "";
		try {
			String adminUsername =  FileProperties.getInstance().getValue("adminUsername");
			String adminPassword =  FileProperties.getInstance().getValue("adminPassword");

			if(adminUsername.equals(paramString1) && adminPassword.equals(paramString2)){
				FileTool.appendFile("admin login");
				username = adminUsername;
			}
			//return empty username if authentication failed
			return username;
		} catch (Exception e) {
			return "";
		}
	}
}