package com.twitstreet.db.base;

import org.hibernate.Session;

public class HibernateConnection implements IConnection<Session>{  
  
    private HibernateUtil hibernateUtil;  
  
    public HibernateConnection(HibernateUtil hibernateUtil) {  
        this.hibernateUtil = hibernateUtil;  
    }  
  
    public void connect() {  
       hibernateUtil.Configure(true);  
    }  
    
    public void beginTransaction(){
    	hibernateUtil.beginTransaction();
    }
  
    public void disConnect() {  
        hibernateUtil.closeSessionFactory();  
    }  
  
    public Session getSession() {  
        return hibernateUtil.getSession();  
    }  
  
    public void rollbackTransaction() {  
        hibernateUtil.rollbackTransaction();  
    }  
  
    public void commitTransaction() {  
        hibernateUtil.commitTransaction();  
    }  
  
    public void closeSession() {  
        hibernateUtil.closeSession();  
    }  
  
}