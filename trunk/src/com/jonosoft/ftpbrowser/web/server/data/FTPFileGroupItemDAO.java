package com.jonosoft.ftpbrowser.web.server.data;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.jonosoft.ftpbrowser.web.client.FTPFileItem;

/**
 * Data access object (DAO) for domain model class FTPFileGroupItem.
 * @see com.jonosoft.ftpbrowser.web.client.FTPFileGroupItem
 * @author MyEclipse - Hibernate Tools
 */
public class FTPFileGroupItemDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(FTPFileGroupItemDAO.class);

	//property constants
	public static final String FTP_SITE_ID = "ftpSiteId";
	public static final String FTP_FILE_GROUP_ID = "ftpFileGroupId";
	public static final String TYPE = "type";
	public static final String PATH = "path";

    
    public void save(FTPFileItem transientInstance) {
        log.debug("saving FTPFileGroupItem instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(FTPFileItem persistentInstance) {
        log.debug("deleting FTPFileGroupItem instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public FTPFileItem findById( java.lang.Integer id) {
        log.debug("getting FTPFileGroupItem instance with id: " + id);
        try {
            FTPFileItem instance = (FTPFileItem) getSession()
                    .get("com.jonosoft.ftpbrowser.web.client.FTPFileGroupItem", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(FTPFileItem instance) {
        log.debug("finding FTPFileGroupItem instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.jonosoft.ftpbrowser.web.client.FTPFileGroupItem")
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
      log.debug("finding FTPFileGroupItem instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from FTPFileGroupItem as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByFtpSiteId(Object ftpSiteId) {
		return findByProperty(FTP_SITE_ID, ftpSiteId);
	}
	
	public List findByFtpFileGroupId(Object ftpFileGroupId) {
		return findByProperty(FTP_FILE_GROUP_ID, ftpFileGroupId);
	}
	
	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}
	
	public List findByPath(Object path) {
		return findByProperty(PATH, path);
	}
	
    public FTPFileItem merge(FTPFileItem detachedInstance) {
        log.debug("merging FTPFileGroupItem instance");
        try {
            FTPFileItem result = (FTPFileItem) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(FTPFileItem instance) {
        log.debug("attaching dirty FTPFileGroupItem instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(FTPFileItem instance) {
        log.debug("attaching clean FTPFileGroupItem instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}