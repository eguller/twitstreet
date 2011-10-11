package com.twitstreet.base;

import com.twitstreet.db.base.IConnection;

public interface IGenericMgr<T> {  
  
    public void setConnection(IConnection connection);
  
    public IConnection getConnection(); 
  
    public Result<T> makePersistent(T var) ; 
  
    public Result<T> makePersistentUpdate(T var);
  
    public Result<T> makeTransient(T var);  
  
}