package com.twitstreet.base;

import com.twitstreet.db.base.IConnection;

public interface IGenericMgr<T> {  
  
    public void setConnection(IConnection connection);
  
    public IConnection getConnection(); 
  
    public void makePersistent(T var) ; 
  
    public void makePersistentUpdate(T var);
  
    public void makeTransient(T var);  
  
}