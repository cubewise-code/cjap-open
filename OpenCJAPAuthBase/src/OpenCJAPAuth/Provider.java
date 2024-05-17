package OpenCJAPAuth;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import com.cognos.CAM_AAA.authentication.IBiBusHeader;
import com.cognos.CAM_AAA.authentication.IBiBusHeader2;
import com.cognos.CAM_AAA.authentication.INamespaceAuthenticationProvider2;
import com.cognos.CAM_AAA.authentication.INamespaceConfiguration;
import com.cognos.CAM_AAA.authentication.IQuery;
import com.cognos.CAM_AAA.authentication.IQueryResult;
import com.cognos.CAM_AAA.authentication.IVisa;
import com.cognos.CAM_AAA.authentication.QueryResult;
import com.cognos.CAM_AAA.authentication.ReadOnlyDisplayObject;
import com.cognos.CAM_AAA.authentication.SystemRecoverableException;
import com.cognos.CAM_AAA.authentication.TextDisplayObject;
import com.cognos.CAM_AAA.authentication.TextNoEchoDisplayObject;
import com.cognos.CAM_AAA.authentication.UnrecoverableException;
import com.cognos.CAM_AAA.authentication.UserRecoverableException;

public class Provider extends Namespace implements INamespaceAuthenticationProvider2 {
    protected static Provider.Credential getCredentialValues(IBiBusHeader paramIBiBusHeader) {
        Provider.Credential localCredential = new Provider.Credential();
        String[] arrayOfString1 = paramIBiBusHeader.getCredentialValue("username");
        String[] arrayOfString2 = paramIBiBusHeader.getCredentialValue("password");

        if ((null != arrayOfString1) && (0 < arrayOfString1.length)) {
            localCredential.setUsername(arrayOfString1[0]);
        }
        if ((null != arrayOfString2) && (0 < arrayOfString2.length)) {
            localCredential.setPassword(arrayOfString2[0]);
        }
        return localCredential;
    }

    private static String getErrorDetails(UnrecoverableException paramUnrecoverableException) {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream localPrintStream = new PrintStream(localByteArrayOutputStream);

        localPrintStream.println(paramUnrecoverableException.getClass().getName() + " : ");
        String[] arrayOfString = paramUnrecoverableException.getMessages();
        for (int i = 0; i < arrayOfString.length; i++)
            localPrintStream.println(arrayOfString[i]);
        localPrintStream.close();
        return localByteArrayOutputStream.toString();
    }

    protected static Provider.Credential getFormFieldValues(IBiBusHeader paramIBiBusHeader) {
        Provider.Credential localCredential = new Provider.Credential();
        String[] arrayOfString1 = paramIBiBusHeader.getFormFieldValue("CAMUsername");
        String[] arrayOfString2 = paramIBiBusHeader.getFormFieldValue("CAMPassword");

        if ((null != arrayOfString1) && (0 < arrayOfString1.length)) {
            localCredential.setUsername(arrayOfString1[0]);
        }
        if ((null != arrayOfString2) && (0 < arrayOfString2.length)) {
            localCredential.setPassword(arrayOfString2[0]);
        }
        return localCredential;
    }

    protected static Provider.Credential getTrustedCredentialValues(IBiBusHeader2 paramIBiBusHeader2) {
        Provider.Credential localCredential = new Provider.Credential();
        String[] arrayOfString1 = paramIBiBusHeader2.getTrustedCredentialValue("username");
        String[] arrayOfString2 = paramIBiBusHeader2.getTrustedCredentialValue("password");

        if ((null != arrayOfString1) && (0 < arrayOfString1.length)) {
            localCredential.setUsername(arrayOfString1[0]);
        }
        if ((null != arrayOfString2) && (0 < arrayOfString2.length)) {
            localCredential.setPassword(arrayOfString2[0]);
        }
        return localCredential;
    }

    protected static Provider.Credential getTrustedEnvironmentVaribleValue(IBiBusHeader2 paramIBiBusHeader2) {
        Provider.Credential localCredential = new Provider.Credential();

        String[] arrayOfString = paramIBiBusHeader2.getTrustedEnvVarValue("REMOTE_USER");

        if ((arrayOfString != null) && (arrayOfString.length > 0)) {
            localCredential.setUsername(arrayOfString[0]);

            localCredential.setPassword(arrayOfString[0]);
        }

        return localCredential;
    }

    public void init(INamespaceConfiguration paramINamespaceConfiguration) throws UnrecoverableException {
        super.init(paramINamespaceConfiguration);

        addName(Locale.getDefault(), paramINamespaceConfiguration.getDisplayName());
    }

    public void logoff(IVisa paramIVisa, IBiBusHeader paramIBiBusHeader) {
        try {
            OpenCJAPAuth.JDBCVisa localJDBCVisa = (OpenCJAPAuth.JDBCVisa) paramIVisa;
            localJDBCVisa.destroy();
        } catch (UnrecoverableException localUnrecoverableException) {
            localUnrecoverableException.printStackTrace();
        }
    }

    public IVisa logon(IBiBusHeader2 paramIBiBusHeader2)
            throws UserRecoverableException, SystemRecoverableException, UnrecoverableException {
        OpenCJAPAuth.JDBCVisa localJDBCVisa = null;

        Provider.Credential localCredential = getTrustedCredentialValues(paramIBiBusHeader2);
        if (localCredential.isEmpty()) {
            localCredential = getCredentialValues(paramIBiBusHeader2);
        }
        if (localCredential.isEmpty()) {
            localCredential = getFormFieldValues(paramIBiBusHeader2);
        }

        if (localCredential.isEmpty()) {
            localCredential = getTrustedEnvironmentVaribleValue(paramIBiBusHeader2);
        }
        if (localCredential.isEmpty()) {
            SystemRecoverableException localSystemRecoverableException = new SystemRecoverableException(
                    "Challenge for REMOTE_USER", "REMOTE_USER");
            throw localSystemRecoverableException;
        }

        if (localCredential.isEmpty()) {
            generateAndThrowExceptionForLogonPrompt(null);
        }

        try {
            localJDBCVisa = new OpenCJAPAuth.JDBCVisa();
            localJDBCVisa.init(this, localCredential.getUsername(), localCredential.getPassword());
        } catch (UnrecoverableException localUnrecoverableException) {
            String str = getErrorDetails(localUnrecoverableException);

            generateAndThrowExceptionForLogonPrompt(str);
        }
        return localJDBCVisa;
    }

    public IQueryResult search(IVisa paramIVisa, IQuery paramIQuery) throws UnrecoverableException {
        OpenCJAPAuth.JDBCVisa visa = (JDBCVisa) paramIVisa;
        QueryResult result = new QueryResult();
        result.addObject(visa.getAccount());
        return result;
    }

    private void generateAndThrowExceptionForLogonPrompt(String paramString) throws UserRecoverableException {
        UserRecoverableException localUserRecoverableException = new UserRecoverableException(
                "Please type your credentials for authentication.", paramString);

        localUserRecoverableException.addDisplayObject(
                new ReadOnlyDisplayObject("Namespace:", "CAMNamespaceDisplayName", getName(Locale.getDefault())));
        localUserRecoverableException.addDisplayObject(new TextDisplayObject("User ID:", "CAMUsername"));
        localUserRecoverableException.addDisplayObject(new TextNoEchoDisplayObject("Password:", "CAMPassword"));
        throw localUserRecoverableException;
    }

    static class Credential {
        private String password;
        private String username;

        public String getPassword() {
            return this.password;
        }

        public String getUsername() {
            return this.username;
        }

        public boolean isEmpty() {
            return (null == getUsername()) && (null == getPassword());
        }

        public void setPassword(String paramString) {
            this.password = paramString;
        }

        public void setUsername(String paramString) {
            this.username = paramString;
        }
    }
}