package OpenCJAPAuth;

import com.cognos.CAM_AAA.authentication.IBiBusHeader;
import com.cognos.CAM_AAA.authentication.ICredential;
import com.cognos.CAM_AAA.authentication.ITrustedCredential;
import com.cognos.CAM_AAA.authentication.SystemRecoverableException;
import com.cognos.CAM_AAA.authentication.UnrecoverableException;
import com.cognos.CAM_AAA.authentication.UserRecoverableException;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;

public class JDBCVisa extends Visa {
    private String password;
    private String username;

    public ICredential generateCredential(IBiBusHeader paramIBiBusHeader)
            throws UserRecoverableException, SystemRecoverableException, UnrecoverableException {
        Object localObject = new Credential();
        ((Credential) localObject).addCredentialValue("username", this.username);
        ((Credential) localObject).addCredentialValue("password", this.password);
        return (ICredential) localObject;
    }

    public ITrustedCredential generateTrustedCredential(IBiBusHeader paramIBiBusHeader)
            throws UserRecoverableException, SystemRecoverableException, UnrecoverableException {
        Provider.Credential localCredential = Provider.getCredentialValues(paramIBiBusHeader);
        if (localCredential.isEmpty()) {
            localCredential = Provider.getFormFieldValues(paramIBiBusHeader);
        }

        if ((localCredential.isEmpty()) || (!localCredential.getUsername().equals(this.username))) {
            localCredential.setUsername(this.username);
            localCredential.setPassword(this.password);
        }

        Object localObject = new TrustedCredential();
        ((TrustedCredential) localObject).addCredentialValue("username", this.username);
        ((TrustedCredential) localObject).addCredentialValue("password", this.password);
        return (ITrustedCredential) localObject;
    }

    public void init(Provider paramJDBCSample, String paramString1, String paramString2) throws UnrecoverableException {
        try {
            String namespace = paramJDBCSample.getName(Locale.getDefault());
            String username = "";
            String errorMessage = "";

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> cls = classLoader.loadClass("OpenCJAPAuth.AuthHandler");
            Method method = cls.getDeclaredMethod("execute", String.class, String.class, String.class);
            username = (String) method.invoke(cls.newInstance(), namespace, paramString1, paramString2);

            if (username != null && !"".equals(username)) {
                this.username = username;
            } else {
                throw new UnrecoverableException("Connection Error", "authentication failed." + errorMessage);
            }
            Account localAccount = QueryUtil.createAccount(this.username);
            super.init(localAccount);
        } catch (Exception e) {
            throw new UnrecoverableException("Connection Error", "Reason: " + e.getMessage());
        }
    }

}