package com.twitstreet.session;

import com.google.inject.Key;
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

import java.util.List;

public class SessionMgrImpl implements SessionMgr {
    private HibernateConnection connection;
    private static Logger logger = LoggerFactory.getLogger(SessionMgrImpl.class);
	public Key<SessionData> getKey() {
		return Key.get(SessionData.class);
	}

	public Result<SessionData> login(long userId) {
		//TODO remove this method. We probably don't need it anymore.
		TwitterAccessData accessData = new TwitterAccessData("oauthToken", "oauthTokenSecret", userId, "name");
		return Result.success(new SessionData(accessData));
	}

	public Result<SessionData> register(TwitterAccessData accessData) {
		// TODO 
		return Result.success(new SessionData(accessData));
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
        return null;
    }

    public void makePersistent(UserDO var) {
    }

    public void makePersistentUpdate(UserDO var) {
    }

    public void makeTransient(UserDO var) {
    }
}
