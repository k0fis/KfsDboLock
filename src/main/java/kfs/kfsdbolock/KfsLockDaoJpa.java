package kfs.kfsdbolock;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pavedrim
 * @param <Data>
 * @param <Id>
 */
public class KfsLockDaoJpa<Data extends KfsLock<Id>, Id> implements KfsLockDao<Data, Id>{

    @PersistenceContext()
    protected EntityManager em;
    
    @Override
    public void insert(Data data) {
        em.persist(data);
    }

    @Override
    public void update(Data data) {
        em.merge(data);
    }
    
    @Override
    public void delete(Data data, Class<Data> cls) {
        deleteById(data.getId(), cls);
    }

    @Override
    public Data find(Data data, Class<Data> cls) {
        return findById(data.getId(), cls);
    }

    @Override
    public void deleteById(Id data, Class<Data> cls) {
        em.remove(findById(data, cls));
    }

    @Override
    public Data findById(Id id, Class<Data> cls) {
        return em.find(cls, id);
    }

    @Override
    public void deleteByUser(String userName, Class<Data> cls) {
        List<Data> lst = em.createQuery("SELECT a FROM "+ cls.getSimpleName() +" a WHERE a.userName = :userName")
                .setParameter("userName", userName)
                .getResultList();
        for (Data data : lst) {
            deleteById(data.getId(), cls);
        }
    }

}
