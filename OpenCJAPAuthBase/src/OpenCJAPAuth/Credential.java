package OpenCJAPAuth;

import com.cognos.CAM_AAA.authentication.ICredential;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class Credential
        implements ICredential {
    private HashMap credentials;

    public Credential() {
        this.credentials = null;
    }

    public String[] getCredentialNames() {
        if (this.credentials != null) {
            Set localSet = this.credentials.keySet();
            String[] arrayOfString = new String[localSet.size()];
            return (String[]) (String[]) localSet.toArray(arrayOfString);
        }
        return null;
    }

    public void addCredentialValue(String paramString1, String paramString2) {
        if (this.credentials == null) {
            this.credentials = new HashMap();
        }
        Vector localVector = (Vector) this.credentials.get(paramString1);
        if (localVector == null) {
            localVector = new Vector();
            this.credentials.put(paramString1, localVector);
        }
        localVector.add(paramString2);
    }

    public String[] getCredentialValue(String paramString) {
        if (this.credentials != null) {
            Vector localVector = (Vector) this.credentials.get(paramString);
            if (localVector != null) {
                return (String[]) (String[]) localVector.toArray(new String[localVector.size()]);
            }
        }
        return null;
    }
}
