package com.twitstreet.session;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.twitstreet.base.Result;
import com.twitstreet.db.base.HibernateConnection;
import com.twitstreet.db.base.IConnection;
import com.twitstreet.db.data.UserDO;
import com.twitstreet.twitter.TwitterAccessData;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

public class SessionMgrImpl implements SessionMgr {
    @Inject private HibernateConnection connection;
    private static Logger logger = LoggerFactory.getLogger(SessionMgrImpl.class);
    private static final int initialCash = 10000;
	public Key<SessionData> getKey() {
		return Key.get(SessionData.class);
	}

	public Result<SessionData> login(long userId) {
		//TODO remove this method. We probably don't need it anymore.
		TwitterAccessData accessData = new TwitterAccessData("oauthToken", "oauthTokenSecret", userId, "name");
		return Result.success(new SessionData(accessData));
	}

	public Result<UserDO> register(TwitterAccessData accessData) {
		UserDO userDO = getUserById(accessData.getUserId()).getPayload();
		if(userDO == null){
			userDO = new UserDO();
			userDO.setUserId(accessData.getUserId());
			userDO.setScreenName(accessData.getScreenName());
			userDO.setFirstLogin(Calendar.getInstance().getTime());
			userDO.setLastLogin(Calendar.getInstance().getTime());
			userDO.setPortfolio(0);
			userDO.setCash(initialCash);
			userDO.setOauthToken(accessData.getOauthToken());
			userDO.setOauthTokenSecret(accessData.getOauthTokenSecret());
			makePersistent(userDO);
		}
		return Result.success(userDO);
	}
	

    public Result<UserDO> getUserById(long id){
        UserDO userDO = null;
        try {
            Session session = (Session) this.getConnection().getSession();
            Criteria criteria = session.createCriteria(UserDO.class);
            criteria.add(Restrictions.eq("id", id));
            List<UserDO> userList = criteria.list();
            userDO = userList.size() == 0 ? null : userList.get(0);
            this.getConnection().commitTransaction();
        } catch (HibernateException ex) {
            ex.printStackTrace();
            logger.error("Exception in SessionMgrImpl.getUserById" , ex);
            return Result.fail(ex);
        } finally {
            this.getConnection().closeSession();
        }
        return Result.success(userDO);
    }


    public void setConnection(IConnection connection) {
        this.connection = (HibernateConnection) connection;
    }

    public IConnection getConnection() {
        return connection;
    }

    public Result<UserDO> makePersistent(UserDO userDO) {
    	try {
            Session session = (Session) this.getConnection().getSession();
            this.getConnection().beginTransaction();
            session.save(userDO);
            this.getConnection().commitTransaction();
        } catch (HibernateException ex) {
            this.getConnection().rollbackTransaction();
            logger.error("Exception in SessionMgrImpl.makePersistent().", ex);
            Result.fail(ex);
        } finally {
            this.getConnection().closeSession();
        }
        return Result.success(userDO);
    }

    public Result<UserDO> makePersistentUpdate(UserDO var) {
    	return Result.success(var);
    }

    public Result<UserDO> makeTransient(UserDO var) {
    	return Result.success(var);
    }
}
