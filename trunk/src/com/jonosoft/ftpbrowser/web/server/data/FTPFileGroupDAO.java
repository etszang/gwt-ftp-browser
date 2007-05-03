package com.jonosoft.ftpbrowser.web.server.data;

import com.jonosoft.ftpbrowser.web.client.FTPFileGroup;
import com.jonosoft.ftpbrowser.web.client.FTPFileGroupItem;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * Data access object (DAO) for domain model class FTPFileGroup.
 * @see com.jonosoft.ftpbrowser.web.client.FTPFileGroup
 * @author MyEclipse - Hibernate Tools
 */
public class FTPFileGroupDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(FTPFileGroupDAO.class);

	//property constants
	public static final String USER_ID = "userId";
	public static final String NAME = "name";

    
    public void save(FTPFileGroup transientInstance) {
        log.debug("saving FTPFileGroup instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	/**
	 * <ol>
	 * <li>Deletes the specified {@link FTPFileGroup}</li>
	 * <li>Deletes the associated {@link FTPFileGroupItem FTPFileGroupItems}</li>
	 * </ol>
	 * 
	 * @param persistentInstance
	 */
	public void delete(FTPFileGroup persistentInstance) {
        log.debug("deleting FTPFileGroup instance");
        try {
        	// TODO Configure Hibernate to do this automatically
        	FTPFileGroupItemDAO dao = new FTPFileGroupItemDAO();
        	List list = dao.findByFtpFileGroupId(persistentInstance.getFtpFileGroupId());
        	for (Iterator it = list.iterator(); it.hasNext();)
        		dao.delete((FTPFileGroupItem) it.next());
        	
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public FTPFileGroup findById( java.lang.Integer id) {
        log.debug("getting FTPFileGroup instance with id: " + id);
        try {
            FTPFileGroup instance = (FTPFileGroup) getSession()
                    .get("com.jonosoft.ftpbrowser.web.client.FTPFileGroup", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(FTPFileGroup instance) {
        log.debug("finding FTPFileGroup instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.jonosoft.ftpbrowser.web.client.FTPFileGroup")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding FTPFileGroup instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from FTPFileGroup as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}
	
	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}
	
    public FTPFileGroup merge(FTPFileGroup detachedInstance) {
        log.debug("merging FTPFileGroup instance");
        try {
            FTPFileGroup result = (FTPFileGroup) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(FTPFileGroup instance) {
        log.debug("attaching dirty FTPFileGroup instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(FTPFileGroup instance) {
        log.debug("attaching clean FTPFileGroup instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}