package kfs.kfsdbolock;

/**
 *
 * @author pavedrim
 * @param <Data>
 * @param <Id>
 */
public interface KfsLockDao<Data extends KfsLock<Id>, Id> {

    void insert(Data data);
    void update(Data data);

    Data find(Data data, Class<Data> cls);
    Data findById(Id id, Class<Data> cls);
    
    void delete(Data data, Class<Data> cls);
    void deleteByUser(String userName, Class<Data> cls);
    void deleteById(Id data, Class<Data> cls);

}
