package com.twitstreet.market;

import com.google.inject.Inject;
import com.twitstreet.base.Result;
import com.twitstreet.db.base.HibernateConnection;
import com.twitstreet.db.base.IConnection;
import com.twitstreet.db.data.StockDO;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StockMgrImpl implements StockMgr {
    @Inject private HibernateConnection connection;
    private static Logger logger = LoggerFactory.getLogger(StockMgrImpl.class);

    public Result<StockDO> notifyBuy(String stock, double amount) {
        return null;
    }

    public Result<StockDO> getStock(String name) {
        StockDO stockDO = null;
        try {
            Session session = (Session) this.getConnection().getSession();
            Criteria criteria = session.createCriteria(StockDO.class);
            criteria.add(Restrictions.eq("name", name));
            List<StockDO> stockDOList = criteria.list();
            stockDO = stockDOList.size() == 0 ? null : stockDOList.get(0);
            this.getConnection().commitTransaction();
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return Result.fail(ex);
        } finally {
            this.getConnection().closeSession();
        }

        return Result.success(stockDO);
    }
    
    public Result<StockDO> getStockById(long id) {
        StockDO stockDO = null;
        try {
            Session session = (Session) this.getConnection().getSession();
            Criteria criteria = session.createCriteria(StockDO.class);
            criteria.add(Restrictions.eq("id", id));
            List<StockDO> stockDOList = criteria.list();
            stockDO = stockDOList.size() == 0 ? null : stockDOList.get(0);
            this.getConnection().commitTransaction();
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return Result.fail(ex);
        } finally {
            this.getConnection().closeSession();
        }

        return Result.success(stockDO);
    }

    public void setConnection(IConnection connection) {
        this.connection = (HibernateConnection) connection;
    }

    public IConnection getConnection() {
        return connection;
    }

    public Result<StockDO> makePersistent(StockDO stockDO) {
        try {
            Session session = (Session) this.getConnection().getSession();
            this.getConnection().beginTransaction();
            session.save(stockDO);
            this.getConnection().commitTransaction();
        } catch (HibernateException ex) {
            this.getConnection().rollbackTransaction();
            logger.error("Exception in StockMgrImpl.makePersistent().", ex);
            Result.fail(ex);
        } finally {
            this.getConnection().closeSession();
        }
        return Result.success(stockDO);
    }

    public Result<StockDO> makePersistentUpdate(StockDO var) {
    	return Result.fail(new RuntimeException("Method not supported"));
    }

    public Result<StockDO> makeTransient(StockDO var) {
    	return Result.fail(new RuntimeException("Method not supported"));
    }

    public StockDO updateTotal(long stockId, int total) {
        {
        	StockDO stockDO = null;
            try {
                Session session = (Session) this.getConnection().getSession();
                this.getConnection().beginTransaction();
                String hql = "update StockDO set total =:total where id =:id";
                Query query = session.createQuery(hql);
                query.setInteger("total", total);
                query.setLong("id", stockId);
                query.executeUpdate();
                this.getConnection().commitTransaction();
                stockDO = getStockById(stockId).getPayload();
            } catch (HibernateException ex) {
                this.getConnection().rollbackTransaction();
                logger.error("Exception in StockMgrImpl.updateTotal().", ex);
            } finally {
                this.getConnection().closeSession();
            }
            return stockDO;
        }
    }

	@Override
	public Result<Double> getPercentSold(String stockName) {
		return Result.success(0.0);
	}
}
