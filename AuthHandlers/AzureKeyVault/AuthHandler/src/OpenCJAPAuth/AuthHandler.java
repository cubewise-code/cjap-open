package OpenCJAPAuth;

import OpenCJAPAuth.utils.FileProperties;
import OpenCJAPAuth.utils.FileTool;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

public class AuthHandler{
	public String execute(String namespace, String paramString1, String paramString2){
		String username = "";
		try {
			if(!"".equals(paramString1)){
				String azureSecretUsername =  FileProperties.getInstance().getValue("AzureSecretUsername");
				String azureEndpoint = FileProperties.getInstance().getValue("AzureEndpoint");
				String azureSecretName = FileProperties.getInstance().getValue("AzureSecretName");
				if(azureSecretUsername.equals(paramString1)){
					SecretClient secretClient = new SecretClientBuilder()
						.vaultUrl(azureEndpoint)
						//set credential if not local automate authentication to Azure
						//.credential(new ClientSecretCredentialBuilder().tenantId(tenant_id).clientId(client_id).clientSecret(client_secret).build())
						.credential(new DefaultAzureCredentialBuilder().build())
						.buildClient();

					KeyVaultSecret retrievedSecret = secretClient.getSecret(azureSecretName);
					if(retrievedSecret.getValue().equals(paramString2)){
						FileTool.appendFile("Azure login success");
						username = azureSecretUsername;
					}else{
						FileTool.appendFile("Azure login failed");
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