package com.twitstreet.db.mgr;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitstreet.db.base.HibernateConnection;
import com.twitstreet.db.base.IConnection;
import com.twitstreet.db.data.StockDO;
import com.twitstreet.servlet.StockQuoteServlet;

public class StockMgrImpl implements StockMgr{
	private HibernateConnection connection;  
	private static Logger logger = LoggerFactory.getLogger(StockMgrImpl.class);
	@Override
	public void setConnection(IConnection connection) {
		this.connection = (HibernateConnection)connection;
	}

	@Override
	public IConnection getConnection() {
		return connection;
	}

	@Override
	public void makePersistent(StockDO stockDO) {
		try{
			Session session = (Session) this.getConnection().getSession(); 
			session.save(stockDO);
		}
		catch(HibernateException ex){
			this.getConnection().rollbackTransaction();  
            logger.error("Exception in StockMgrImpl.makePersistent().", ex);  
		}
		finally{
			this.getConnection().closeSession();
		}
	}

	@Override
	public void makePersistentUpdate(StockDO stockDO) {
		
	}

	@Override
	public void makeTransient(StockDO stockDO) {
		
	}

	@Override
	public StockDO getStock(String name) {
		StockDO stockDO = null;
		try{
		Session session = (Session) this.getConnection().getSession();  
		Criteria criteria = session.createCriteria(StockDO.class);
		criteria.add(Restrictions.eq("name", name));
		List<StockDO> stockDOList = criteria.list();
		stockDO =  stockDOList.size() == 0 ? null : stockDOList.get(0);
		this.getConnection().commitTransaction();
		}catch(HibernateException ex){
			ex.printStackTrace();
		}
		finally{
			this.getConnection().closeSession();
		}
		
		return stockDO;
	}

	@Override
	public void updateTotal(StockDO stockDO) {
		try{
			Session session = (Session) this.getConnection().getSession();
			String hql = "update Stock set total =:total where id =:id";
			Query query = session.createQuery(hql);
			query.setInteger("total", stockDO.getTotal());
			query.setLong("id", stockDO.getId());
			query.executeUpdate();
			this.getConnection().commitTransaction();
		}
		catch(HibernateException ex){
			this.getConnection().rollbackTransaction();
			logger.error("Exception in StockMgrImpl.updateTotal().", ex);
		}
		finally{
			this.getConnection().closeSession();
		}
		
	}

}
