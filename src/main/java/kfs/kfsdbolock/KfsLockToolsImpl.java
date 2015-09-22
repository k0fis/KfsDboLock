package kfs.kfsdbolock;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pavedrim
 * @param <Lock>
 * @param <Id>
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
public abstract class KfsLockToolsImpl<Lock extends KfsLock<Id>, Id> implements KfsLockTool<Id> {

    public abstract Class<Lock> getCls();
    public abstract KfsLockDao<Lock, Id> getDao();
    public abstract int getMinutesDelay();

    private Lock getLock(Id lockObject) {
        if (lockObject == null) {
            return null;
        }
        Lock lock = getDao().findById(lockObject, getCls());
        if ((lock != null) && !lock.isBeforeMinutes(getMinutesDelay())) {
            getDao().deleteById(lockObject, getCls());
            lock = null;
        }
        return lock;
    }

    @Override
    public boolean lockObject(Id lockId, String user) {
        getDao().deleteByUser(user, getCls());
        Lock lock = getLock(lockId);
        if (lock == null) {
            try {
                lock = getCls().newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException("Cannot init new " + getClass().getSimpleName(), ex);
            }
            lock.setId(lockId);
            lock.setUserName(user);
            lock.now();
            getDao().insert(lock);
            return true;
        } else if (lock.getUserName().equals(user)) {
            lock.now();
            getDao().update(lock);
            return true;
        }
        return false;
    }

    @Override
    public void unlockObject(Id lockId, String user) {
        Lock lock = getLock(lockId);
        if ((lock != null) && (lock.getUserName().equals(user))) {
            getDao().deleteById(lockId, getCls());
        }
    }

    @Override
    public String getLockedUser(Id lockId) {
        Lock lock = getLock(lockId);
        if (lock != null) {
            return lock.getUserName();
        }
        return null;
    }

}
