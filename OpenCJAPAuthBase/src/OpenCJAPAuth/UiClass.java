package OpenCJAPAuth;

import com.cognos.CAM_AAA.authentication.IBaseClass;
import com.cognos.CAM_AAA.authentication.IUiClass;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;

public class UiClass
        implements IUiClass {
    private String objectID;
    private HashMap names;
    private HashMap descriptions;
    private Stack ancestors;

    public UiClass(String paramString) {
        this.names = null;
        this.descriptions = null;
        this.ancestors = null;
        this.objectID = paramString;
    }

    public void addDescription(Locale paramLocale, String paramString) {
        if (this.descriptions == null) {
            this.descriptions = new HashMap();
        }
        this.descriptions.put(paramLocale, paramString);
    }

    public String getDescription(Locale paramLocale) {
        if (this.descriptions != null) {
            return (String) this.descriptions.get(paramLocale);
        }
        return null;
    }

    public Locale[] getAvailableDescriptionLocales() {
        if (this.descriptions != null) {
            Set localSet = this.descriptions.keySet();
            Locale[] arrayOfLocale = new Locale[localSet.size()];
            return (Locale[]) (Locale[]) localSet.toArray(arrayOfLocale);
        }
        return null;
    }

    public void addAncestors(IBaseClass paramIBaseClass) {
        if (this.ancestors == null) {
            this.ancestors = new Stack();
        }
        this.ancestors.push(paramIBaseClass);
    }

    public IBaseClass[] getAncestors() {
        if (this.ancestors != null) {
            IBaseClass[] arrayOfIBaseClass = new IBaseClass[this.ancestors.size()];
            return (IBaseClass[]) (IBaseClass[]) this.ancestors.toArray(arrayOfIBaseClass);
        }
        return null;
    }

    public void addName(Locale paramLocale, String paramString) {
        if (this.names == null) {
            this.names = new HashMap();
        }
        this.names.put(paramLocale, paramString);
    }

    public boolean getHasChildren() {
        return false;
    }

    public String getName(Locale paramLocale) {
        if (this.names != null) {
            return (String) this.names.get(paramLocale);
        }
        return null;
    }

    public Locale[] getAvailableNameLocales() {
        if (this.names != null) {
            Set localSet = this.names.keySet();
            Locale[] arrayOfLocale = new Locale[localSet.size()];
            return (Locale[]) (Locale[]) localSet.toArray(arrayOfLocale);
        }
        return null;
    }

    public String getObjectID() {
        return this.objectID;
    }

    protected void setObjectID(String paramString) {
        this.objectID = paramString;
    }
}
