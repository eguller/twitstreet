package com.twitstreet.db.base;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.EmptyInterceptor;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {  
  
        /** Logger. */  
	private static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);  
  
    /** configuration. */  
    private  Configuration configuration;  
  
    /** Session Factory */  
    private  SessionFactory sessionFactory;  
  
    /** JNDI name of sessionfactory. */  
    private  final String JNDI_SESSIONFACTORY = "java:hibernate/HibernateFactory";  
  
    /** If running unit tests set to true. */  
    private  boolean offlineMode = true;  
  
    /** threadlocal. */  
    private  final ThreadLocal threadSession = new ThreadLocal();  
  
    /** threadlocal. */  
    private  final ThreadLocal threadTransaction = new ThreadLocal();  
  
    /** threadlocal. */  
    private  final ThreadLocal threadInterceptor = new ThreadLocal();  
  
    /** Interceptor class */  
    private  final String INTERCEPTOR_CLASS = "hibernate.util.interceptor_class";  
  
    /** 
     * Create the initial SessionFactory from hibernate.xml.cfg or JNDI). 
     * #### Use this Function to initialize Hibernate! #### 
     * 
     * @param offlineMode true=hibernate.cfg.xml , false=JNDI 
     */  
    public  void Configure(boolean offlineMode) {  
        logger.debug("HibernateUtil.Configure() - Trying to initialize Hibernate.");  
        try {  
            // Use hibernate.cfg.xml (true) or JNDI (false)  
            setOfflineMode(offlineMode);  
            sessionFactory = getSessionFactory();  
  
        } catch (Throwable x) {  
            // We have to catch Throwable, otherwise we will miss  
            // NoClassDefFoundError and other subclasses of Error  
            logger.error("HibernateUtil.Configure() - Building SessionFactory failed.", x);  
            throw new ExceptionInInitializerError(x);  
        }  
    }  
  
    /** 
     * Use hibernate.cfg.xml (true) to create sessionfactory or bound 
     * sessionfactory to JNDI (false) 
     */  
    public  void setOfflineMode(boolean mode)  
    {  
        if (mode==true)  
            logger.debug("HibernateUtil.setOfflineMode() - Setting mode to hibernate.cfg.xml .");  
        else  
            logger.debug("HibernateUtil.setOfflineMode() - Setting mode to JNDI.");  
  
        offlineMode = mode;  
    }  
  
        /** 
     * Returns the SessionFactory used for this  class. If offlineMode has 
     * been set then we use hibernate.cfg.xml to create sessionfactory, if not 
     * then we use sessionfactory bound to JNDI. 
     * 
     * @return SessionFactory 
     */  
    public  SessionFactory getSessionFactory() {  
        if (sessionFactory == null) {  
            if (offlineMode == true) {  
                logger.debug("HibernateUtil.getSessionFactory() - Using hibernate.cfg.xml to create a SessionFactory");  
                try {  
                    configuration = new Configuration();  
                    sessionFactory = configuration.configure().buildSessionFactory();  
                } catch (HibernateException x) {  
                    x.printStackTrace();  
                }  
  
            } else {  
                logger.debug("HibernateUtil.getSessionFactory() - Using JDNI to create a SessionFactory");  
                try {  
                    Context ctx = new InitialContext();  
                    sessionFactory = (SessionFactory) ctx.lookup(JNDI_SESSIONFACTORY);  
                } catch (NamingException x) {  
                    x.printStackTrace();  
                }  
            }  
        }  
  
        if (sessionFactory == null) {  
            throw new IllegalStateException("HibernateUtil.getSessionFactory() - SessionFactory not available.");  
        }  
        return sessionFactory;  
    }  
  
    /** 
     * Sets the given SessionFactory. 
     * 
     * @param sessionFactory 
     */  
    public  void setSessionFactory(SessionFactory sessionFactory) {  
        this.sessionFactory = sessionFactory;  
    }  
  
        /** 
     * Returns the original Hibernate configuration. 
     * 
     * @return Configuration 
     */  
    public  Configuration getConfiguration() {  
        return configuration;  
    }  
  
        /** 
     * Rebuild the SessionFactory with the  Configuration. 
     * 
     */  
    public  void rebuildSessionFactory() {  
        logger.debug("HibernateUtil.rebuildSessionFactory() - Rebuilding the SessionFactory with the  Configuration.");  
        synchronized (sessionFactory) {  
            try {  
                sessionFactory = getConfiguration().buildSessionFactory();  
            } catch (Exception x) {  
                x.printStackTrace();  
            }  
        }  
    }  
  
        /** 
     * Rebuild the SessionFactory with the given Hibernate Configuration. 
     * 
     * @param cfg 
     */  
    public  void rebuildSessionFactory(Configuration cfg) {  
        logger.debug("HibernateUtil.rebuildSessionFactory() - Rebuilding the SessionFactory from the given Hibernate Configuration.");  
        synchronized (sessionFactory) {  
            try {  
                if (sessionFactory != null && !sessionFactory.isClosed())  
                    sessionFactory.close();  
  
                sessionFactory = cfg.buildSessionFactory();  
                configuration = cfg;  
            } catch (Exception x) {  
                x.printStackTrace();  
            }  
        }  
    }  
  
        /** 
     * Destroy this SessionFactory and release all resources (caches, connection 
     * pools, etc). 
     * 
     * @author Siegfried Bolz 
     * @param cfg 
     */  
    public  void closeSessionFactory() {  
        synchronized (sessionFactory) {  
            try {  
                logger.debug("HibernateUtil.closeSessionFactory() - Destroy the current SessionFactory.");  
                sessionFactory.close();  
                // Clear  variables  
                configuration = null;  
                sessionFactory = null;  
            } catch (Exception x) {  
                x.printStackTrace();  
            }  
        }  
    }  
  
        /** 
     * Retrieves the current Session local to the thread. <p/> 
     * 
     * If no Session is open, opens a new Session for the running thread. 
     * 
     * @return Session 
     */  
    public  Session getSession() {  
        Session s = (Session) threadSession.get();  
        try {  
            if (s == null) {  
                logger.debug("HibernateUtil.getSession() - Opening new Session for this thread.");  
                if (getInterceptor() != null) {  
                    logger.debug("HibernateUtil.getSession() - Using interceptor: "+ getInterceptor().getClass());  
                    s = getSessionFactory().openSession(getInterceptor());  
                } else {  
                    s = getSessionFactory().openSession();  
                }  
                threadSession.set(s);  
            }  
        } catch (HibernateException x) {  
            x.printStackTrace();  
        }  
        return s;  
    }  
  
        /** 
     * Closes the Session local to the thread. 
     */  
    public  void closeSession() {  
        try {  
            Session s = (Session) threadSession.get();  
            threadSession.set(null);  
            if (s != null && s.isOpen()) {  
                logger.debug("HibernateUtil.closeSession() - Closing Session of this thread.");  
                s.close();  
            }  
        } catch (HibernateException x) {  
            x.printStackTrace();  
        }  
    }  
  
        /** 
     * Start a new database transaction. 
     */  
    public  void beginTransaction() {  
        Transaction tx = (Transaction) threadTransaction.get();  
        try {  
            if (tx == null) {  
                logger.debug("HibernateUtil.beginTransaction() - Starting new database transaction in this thread.");  
                tx = getSession().beginTransaction();  
                threadTransaction.set(tx);  
            }  
        } catch (HibernateException x) {  
            x.printStackTrace();  
        }  
    }  
  
        /** 
     * Commit the database transaction. 
     */  
    public  void commitTransaction() {  
        Transaction tx = (Transaction) threadTransaction.get();  
        try {  
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {  
                logger.debug("HibernateUtil.commitTransaction() - Committing database transaction of this thread.");  
                tx.commit();  
            }  
            threadTransaction.set(null);  
        } catch (HibernateException x) {  
            rollbackTransaction();  
            x.printStackTrace();  
        }  
    }  
  
        /** 
     * Rollback the database transaction. 
     */  
    public  void rollbackTransaction() {  
        Transaction tx = (Transaction) threadTransaction.get();  
        try {  
            threadTransaction.set(null);  
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {  
                logger.debug("HibernateUtil.rollbackTransaction() - Tyring to rollback database transaction of this thread.");  
                tx.rollback();  
            }  
        } catch (HibernateException x) {  
            x.printStackTrace();  
        } finally {  
            closeSession();  
        }  
    }  
  
        /** 
     * Reconnects a Hibernate Session to the current Thread. 
     * 
     * @param session The Hibernate Session to be reconnected. 
     */  
    public  void reconnect(Session session) {  
        logger.debug("HibernateUtil.reconnect() - Reconnecting Hibernate Session to the current Thread.");  
        try {  
            session.reconnect(session.connection());  
            threadSession.set(session);  
        } catch (HibernateException x) {  
            x.printStackTrace();  
        }  
    }  
  
       /** 
     * Disconnect and return Session from current Thread. 
     * 
     * @return Session the disconnected Session 
     */  
    public  Session disconnectSession() {  
        logger.debug("HibernateUtil.disconnectSession() - Disconnecting Session from current Thread.");  
        Session session = getSession();  
        try {  
            threadSession.set(null);  
            if (session.isConnected() && session.isOpen()) {  
                session.disconnect();  
            }  
        } catch (HibernateException x) {  
            x.printStackTrace();  
        }  
        return session;  
    }  
  
       /** 
     * Register a Hibernate interceptor with the current thread. 
     * 
     * Every Session opened is opened with this interceptor after registration. 
     * Has no effect if the current Session of the thread is already open, 
     * effective on next close()/getSession(). 
     */  
    public  void registerInterceptor(Interceptor interceptor) {  
        threadInterceptor.set(interceptor);  
    }  
  
        /** 
     * Get Hibernate interceptor. 
     * 
     * @return Interceptor 
     */  
    private  Interceptor getInterceptor() {  
        Interceptor interceptor = (Interceptor) threadInterceptor.get();  
        return interceptor;  
    }  
  
        /** 
     * Resets global interceptor to default state. 
     */  
    public  void resetInterceptor() {  
        logger.debug("HibernateUtil.resetInterceptor() - Resetting global interceptor to configuration setting");  
        setInterceptor(configuration, null);  
    }  
  
        /** 
     * Either sets the given interceptor on the configuration or looks it up 
     * from configuration if null. 
     */  
    private  void setInterceptor(Configuration configuration, Interceptor interceptor) {  
        String interceptorName = configuration.getProperty(INTERCEPTOR_CLASS);  
        if (interceptor == null && interceptorName != null) {  
            try {  
                logger.debug("HibernateUtil.setInterceptor() - Configuring interceptor.");  
                Class interceptorClass = HibernateUtil.class.getClassLoader().loadClass(interceptorName);  
                interceptor = (Interceptor) interceptorClass.newInstance();  
            } catch (Exception x) {  
                throw new RuntimeException("HibernateUtil.setInterceptor() - Error, could not configure interceptor: " + interceptorName, x);  
            }  
        }  
        if (interceptor != null) {  
            configuration.setInterceptor(interceptor);  
        } else {  
            configuration.setInterceptor(EmptyInterceptor.INSTANCE);  
        }  
    }  
  
}