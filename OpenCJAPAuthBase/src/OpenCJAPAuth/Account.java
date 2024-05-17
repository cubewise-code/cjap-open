package OpenCJAPAuth;

import com.cognos.CAM_AAA.authentication.IAccount;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

public class Account extends UiClass
        implements IAccount {
    private String businessPhone;
    private String email;
    private Locale contentLocale;
    private Locale productLocale;
    private String faxPhone;
    private String givenName;
    private String homePhone;
    private String mobilePhone;
    private String pagerPhone;
    private String postalAddress;
    private String surname;
    private String userName;
    private HashMap customProperties;

    public Account(String paramString) {
        super(paramString);
        this.businessPhone = null;
        this.email = null;
        this.contentLocale = null;
        this.productLocale = null;
        this.faxPhone = null;
        this.givenName = null;
        this.homePhone = null;
        this.mobilePhone = null;
        this.pagerPhone = null;
        this.postalAddress = null;
        this.surname = null;
        this.userName = null;
        this.customProperties = null;
    }

    public String getBusinessPhone() {
        return this.businessPhone;
    }

    public String getEmail() {
        return this.email;
    }

    public Locale getContentLocale() {
        return this.contentLocale;
    }

    public String getFaxPhone() {
        return this.faxPhone;
    }

    public String getGivenName() {
        return this.givenName;
    }

    public String getHomePhone() {
        return this.homePhone;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public String getPagerPhone() {
        return this.pagerPhone;
    }

    public String getPostalAddress() {
        return this.postalAddress;
    }

    public Locale getProductLocale() {
        return this.productLocale;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setBusinessPhone(String paramString) {
        this.businessPhone = paramString;
    }

    public void setContentLocale(Locale paramLocale) {
        this.contentLocale = paramLocale;
    }

    public void setEmail(String paramString) {
        this.email = paramString;
    }

    public void setFaxPhone(String paramString) {
        this.faxPhone = paramString;
    }

    public void setGivenName(String paramString) {
        this.givenName = paramString;
    }

    public void setHomePhone(String paramString) {
        this.homePhone = paramString;
    }

    public void setMobilePhone(String paramString) {
        this.mobilePhone = paramString;
    }

    public void setPagerPhone(String paramString) {
        this.pagerPhone = paramString;
    }

    public void setPostalAddress(String paramString) {
        this.postalAddress = paramString;
    }

    public void setProductLocale(Locale paramLocale) {
        this.productLocale = paramLocale;
    }

    public void setSurname(String paramString) {
        this.surname = paramString;
    }

    public void setUserName(String paramString) {
        this.userName = paramString;
    }

    public String[] getCustomPropertyNames() {
        if (this.customProperties != null) {
            Set localSet = this.customProperties.keySet();
            return (String[]) (String[]) localSet.toArray(new String[localSet.size()]);
        }
        return null;
    }

    public String[] getCustomPropertyValue(String paramString) {
        if (this.customProperties != null) {
            Vector localVector = (Vector) this.customProperties.get(paramString);
            if (localVector != null) {
                return (String[]) (String[]) localVector.toArray(new String[localVector.size()]);
            }
        }
        return null;
    }

    public void addCustomProperty(String paramString1, String paramString2) {
        if (this.customProperties == null) {
            this.customProperties = new HashMap();
        }
        Vector localVector = (Vector) this.customProperties.get(paramString1);
        if (localVector == null) {
            localVector = new Vector();
            this.customProperties.put(paramString1, localVector);
        }
        localVector.add(paramString2);
    }
}
