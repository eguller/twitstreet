package com.twitstreet.session;

import java.util.ArrayList;
import java.util.List;

import com.twitstreet.db.data.RankingHistoryData;
import com.twitstreet.db.data.User;

public interface UserMgr {
	

    /**
     * Return user by given twitter id
     * @param userId - User id given by twitter.
     * @return - user details.
     */
    public User getUserById(long userId);
    public ArrayList<User> getUsersByIdList(ArrayList<Long> idList);
	public int getRecordPerPage();


	public void saveUser(User user);
	
	public void updateUser(User user);
	
	public User random();
	
	public void increaseCash(long userId, double cash);
	
	public void updateCash(long userId, double amount);
	
	public ArrayList<User> getTopRank(int pageNumber);
	
	public int count();
	public RankingHistoryData getRankingHistoryForUser(long id);
	public void updateRankingHistory();

	public void rerank();
	ArrayList<User> searchUser(String searchText);
	
	public int getPageOfRank(int rank);
	
	public List<User> getAll();
	
	public void updateTwitterData(User user);
	
	public List<User> getUserListByServerId(int serverId);
}
