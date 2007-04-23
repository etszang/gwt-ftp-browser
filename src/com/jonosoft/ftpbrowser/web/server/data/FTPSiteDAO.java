package com.jonosoft.ftpbrowser.web.server.data;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.jonosoft.ftpbrowser.web.client.FTPFileGroupItem;
import com.jonosoft.ftpbrowser.web.client.FTPSite;

/**
 * Data access object (DAO) for domain model class FTPSite.
 * @see com.jonosoft.ftpbrowser.web.client.FTPSite
 * @author MyEclipse - Hibernate Tools
 */
public class FTPSiteDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(FTPSiteDAO.class);

	//property constants
	public static final String USER_ID = "userId";
	public static final String SERVER = "server";
	public static final String PORT = "port";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

    
    public void save(FTPSite transientInstance) {
        log.debug("saving FTPSite instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(FTPSite persistentInstance) {
        log.debug("deleting FTPSite instance");
        try {
        	// TODO Configure Hibernate to do this automatically
        	FTPFileGroupItemDAO dao = new FTPFileGroupItemDAO();
        	List list = dao.findByFtpSiteId(persistentInstance.getFtpSiteId());
        	for (Iterator it = list.iterator(); it.hasNext();)
        		dao.delete((FTPFileGroupItem) it.next());
        	
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public FTPSite findById( java.lang.Integer id) {
        log.debug("getting FTPSite instance with id: " + id);
        try {
            FTPSite instance = (FTPSite) getSession()
                    .get("com.jonosoft.ftpbrowser.web.client.FTPSite", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(FTPSite instance) {
        log.debug("finding FTPSite instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.jonosoft.ftpbrowser.web.client.FTPSite")
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
      log.debug("finding FTPSite instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from FTPSite as model where model." 
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
	
	public List findByServer(Object server) {
		return findByProperty(SERVER, server);
	}
	
	public List findByPort(Object port) {
		return findByProperty(PORT, port);
	}
	
	public List findByUsername(Object username) {
		return findByProperty(USERNAME, username);
	}
	
	public List findByPassword(Object password) {
		return findByProperty(PASSWORD, password);
	}
	
    public FTPSite merge(FTPSite detachedInstance) {
        log.debug("merging FTPSite instance");
        try {
            FTPSite result = (FTPSite) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(FTPSite instance) {
        log.debug("attaching dirty FTPSite instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(FTPSite instance) {
        log.debug("attaching clean FTPSite instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}