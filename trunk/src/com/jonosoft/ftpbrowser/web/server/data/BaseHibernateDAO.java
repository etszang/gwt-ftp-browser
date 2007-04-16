package com.jonosoft.ftpbrowser.web.server.data;

import com.jonosoft.ftpbrowser.web.server.HibernateSessionFactory;
import org.hibernate.Session;


/**
 * Data access object (DAO) for domain model
 * @author MyEclipse - Hibernate Tools
 */
public class BaseHibernateDAO implements IBaseHibernateDAO {
	
	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}
	
}