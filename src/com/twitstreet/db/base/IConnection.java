package com.twitstreet.db.base;
public interface IConnection<T> {  
  
    public void connect();  
  
    public void disConnect();  
  
    public T getSession();  
  
    public void rollbackTransaction();  
  
    public void commitTransaction();  
  
    public void closeSession();  
    
    public void beginTransaction();
  
}