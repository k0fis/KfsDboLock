package kfs.kfsdbolock;

/**
 *
 * @author pavedrim
 * @param <Id> Id 
 */
public interface KfsLockTool<Id> {

    String getLockedUser(Id lockId);

    boolean lockObject(Id lockId, String user);

    void unlockObject(Id lockId, String user);
    
}
