package kfs.kfsdbolock;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author pavedrim
 * @param <Id>
 */
@MappedSuperclass
public abstract class KfsLock<Id> {

    @Column(nullable = false, unique = true)
    private String userName;
    
    @Column(nullable = false)
    private Timestamp lockTime;

    public void now() {
        lockTime = new Timestamp(new Date().getTime());
    }

    public boolean isBeforeMinutes(int minutes) {
        return lockTime.getTime() + (60000000l*minutes) < (new Date().getTime());
    }
    
    abstract public Id getId();
    abstract public void setId(Id data);
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getLockTime() {
        return lockTime;
    }

    public void setLockTime(Timestamp lockTime) {
        this.lockTime = lockTime;
    }
    
}
