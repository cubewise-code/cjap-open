package OpenCJAPAuth;

import com.ibm.json.java.JSONObject;
import OpenCJAPAuth.utils.FileProperties;
import OpenCJAPAuth.utils.FileTool;
import OpenCJAPAuth.utils.HttpUtils;

import java.util.HashMap;

public class AuthHandler{
	public String execute(String namespace, String paramString1, String paramString2){
		String username = "";
		try {
			if(!"".equals(paramString1)){
				String authenticationAPI =  FileProperties.getInstance().getValue("authenticationAPI");
				HashMap<String,String> params=new HashMap<String,String>();
				params.put("username",paramString1);
				params.put("password",paramString2);
				String resultStr =  HttpUtils.request(authenticationAPI,"POST",params);

				JSONObject result = JSONObject.parse(resultStr);
				if("true".equals((String)result.get("success"))){
					username = (String)result.get("username");
					FileTool.appendFile("login success, user=" + username);
				}
			}
			//return empty username if authentication failed
			return username;
		} catch (Exception e) {
			return "";
		}
	}
}