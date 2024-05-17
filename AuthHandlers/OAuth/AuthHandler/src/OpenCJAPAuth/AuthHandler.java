package OpenCJAPAuth;

import com.ibm.json.java.JSONObject;
import OpenCJAPAuth.utils.FileProperties;
import OpenCJAPAuth.utils.FileTool;
import OpenCJAPAuth.utils.HttpUtils;

import java.util.HashMap;

public class AuthHandler {
    public String execute(String namespace, String paramString1, String paramString2) {
        String username = "";
        try {
            if (!"".equals(paramString1)) {
                String OAuthRequestUrl = FileProperties.getInstance().getValue("OAuthRequestUrl");
                String OAuthAppId = FileProperties.getInstance().getValue("OAuthAppId");
                String OAuthAppSecret = FileProperties.getInstance().getValue("OAuthAppSecret");

                if (!paramString1.equals("")) {
                    String token = paramString1;
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("appId", OAuthAppId);
                    params.put("appSecret", OAuthAppSecret);

                    String resultStr = HttpUtils.request(OAuthRequestUrl, "POST", params);
                    JSONObject result = JSONObject.parse(resultStr);

                    if (result.containsKey("username")) {
                        username = (String) result.get("username");
                        FileTool.appendFile("login success, user=" + username);
                    }
                }
            }
            //return empty username if authentication failed
            return username;
        } catch (Exception e) {
            return "";
        }
    }
}