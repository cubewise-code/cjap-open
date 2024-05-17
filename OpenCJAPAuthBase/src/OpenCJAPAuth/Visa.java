package OpenCJAPAuth;

import com.cognos.CAM_AAA.authentication.IAccount;
import com.cognos.CAM_AAA.authentication.IBiBusHeader;
import com.cognos.CAM_AAA.authentication.ICredential;
import com.cognos.CAM_AAA.authentication.IGroup;
import com.cognos.CAM_AAA.authentication.IRole;
import com.cognos.CAM_AAA.authentication.ITrustedCredential;
import com.cognos.CAM_AAA.authentication.IVisa;
import com.cognos.CAM_AAA.authentication.SystemRecoverableException;
import com.cognos.CAM_AAA.authentication.UnrecoverableException;
import com.cognos.CAM_AAA.authentication.UserRecoverableException;

import java.util.Vector;

public class Visa
        implements IVisa {
    private Vector roles;
    private Vector groups;
    private IAccount account;

    public Visa() {
        this.roles = null;
        this.groups = null;
    }

    public void init(IAccount paramIAccount)
            throws UnrecoverableException {
        this.account = paramIAccount;
    }

    public void destroy()
            throws UnrecoverableException {
        this.roles = null;
        this.groups = null;
        this.account = null;
    }

    public ITrustedCredential generateTrustedCredential(IBiBusHeader paramIBiBusHeader)
            throws UserRecoverableException, SystemRecoverableException, UnrecoverableException {
        return null;
    }

    public ICredential generateCredential(IBiBusHeader paramIBiBusHeader)
            throws UserRecoverableException, SystemRecoverableException, UnrecoverableException {
        return null;
    }

    public boolean isValid() {
        return true;
    }

    public IAccount getAccount() {
        return this.account;
    }

    public void addGroup(IGroup paramIGroup) {
        if (this.groups == null) {
            this.groups = new Vector();
        }
        this.groups.add(paramIGroup);
    }

    public IGroup[] getGroups() {
        if (this.groups != null) {
            IGroup[] arrayOfIGroup = new IGroup[this.groups.size()];
            return (IGroup[]) (IGroup[]) this.groups.toArray(arrayOfIGroup);
        }
        return null;
    }

    public void addRole(IRole paramIRole) {
        if (this.roles == null) {
            this.roles = new Vector();
        }
        this.roles.add(paramIRole);
    }

    public IRole[] getRoles() {
        if (this.roles != null) {
            IRole[] arrayOfIRole = new IRole[this.roles.size()];
            return (IRole[]) (IRole[]) this.roles.toArray(arrayOfIRole);
        }
        return null;
    }
}

