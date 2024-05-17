package OpenCJAPAuth;

import java.util.Locale;

import com.cognos.CAM_AAA.authentication.IAccount;
import com.cognos.CAM_AAA.authentication.UnrecoverableException;

public class QueryUtil {
    public static Account createAccount(String paramString1) throws UnrecoverableException {
        String str1 = paramString1;

        if (null != str1) {
            String str2 = "u:" + str1;

            Account localAccount = new Account(str2);
            setAccountProperties(localAccount);

            return localAccount;
        }

        throw new UnrecoverableException("Invalid Credentials", "Could not authenticate with the provide credentials");
    }

    private static void setAccountProperties(Account paramAccount) throws UnrecoverableException {
        String str = paramAccount.getObjectID();
        paramAccount.setUserName(str.replace("u:", ""));

        Locale localLocale = QueryUtil.getLocale("zh-cn");
        paramAccount.setContentLocale(localLocale);
        paramAccount.setProductLocale(localLocale);

        paramAccount.addName(localLocale, str.replace("u:", ""));

        paramAccount.addCustomProperty("tenant", "A");
    }

    public static String escapeSpecialChars(String paramString) {
        StringBuffer localStringBuffer = new StringBuffer(paramString);

        for (int i = 0; i < localStringBuffer.length(); ) {
            int j = localStringBuffer.charAt(i);

            switch (j) {
                case 39:
                    localStringBuffer.insert(i, "!'");
                    i += "!'".length() + 1;
                    break;
                case 37:
                    localStringBuffer.insert(i, "!%");
                    i += "!%".length() + 1;
                    break;
                default:
                    i++;
            }

        }

        return localStringBuffer.toString();
    }

    public static Locale getLocale(String paramString) {
        if (2 > paramString.length()) {
            return Locale.ENGLISH;
        }
        String str1 = paramString.substring(0, 2);

        if (5 == paramString.length()) {
            String str2 = paramString.substring(3, 5);
            return new Locale(str1, str2);
        }

        return new Locale(str1);
    }

    public static String getTenantId(IAccount paramIAccount) {
        String[] arrayOfString = paramIAccount.getCustomPropertyValue("tenant");
        return arrayOfString != null ? arrayOfString[0] : null;
    }
}

