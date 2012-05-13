/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package com.twitstreet.session;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.twitstreet.db.data.RankingHistoryData;
import com.twitstreet.db.data.User;

public interface UserMgr {
	public static final int MAX_LOAN = 10000;
	public static final double INVITE_MONEY_RATE = 8;
	public static final double LOAN_INTEREST_RATE = 0.01; //%1
	public static int MAX_TOPRANK_USER = 20;

    /**
     * Return user by given twitter id
     * @param userId - User id given by twitter.
     * @return - user details.
     */
    public User getUserById(long userId);
    public ArrayList<User> getUsersByIdList(ArrayList<Long> idList);


	public void saveUser(User user);
	
	public void updateUser(User user);
	
	public User random();
	
	public void increaseCash(long userId, double cash);
	
	public void updateCash(long userId, double amount);

	public ArrayList<User> getTopRank(int offset, int count);
	public ArrayList<User> getTopRankAllTime(int offset, int count);
	
	public int count();
	public void updateRankingHistory();

	public void rerank();
	ArrayList<User> searchUser(String searchText);

	
	public List<User> getAll();
	
	public void updateTwitterData(User user);
	
	public List<User> getUserListByServer();
	public ArrayList<User> getTopGrossingUsers(int limit);
	
	public void invite(long invitor, long invited);
	public void addInviteMoney(long userId);
	void deleteUser(long id);
	void resurrectUser(long id);
	User getUserByTokenAndSecret(String token, String secret);
	List<User> getAllActive();
	void updateRankingHistory(boolean neededOnly);
	RankingHistoryData getRankingHistoryForUser(long id, String from, String to);
	RankingHistoryData getRankingHistoryForUser(long id, Timestamp start, Timestamp end);
	public void resetInvitation();
	RankingHistoryData getRankingHistoryForUser(long id, int seasonId);
	int getUserCountForGroup(long id);
	ArrayList<User> getUsersForGroup(long id, int offset, int count);
	ArrayList<User> getTopRankForGroup(long id, int offset, int count);
	ArrayList<User> getTopRankAllTimeForGroup(long id, int offset, int count);
	int getUserCount();
	public List<User> getNewSeasonInfoNotSentUsers(int size);
	public void setNewSeasonInfoSent(List<User> userIdList);
	public List<User> getTopNUsers(int n);
	public void truncateRankingHistory();
	ArrayList<User> getNewUsers(int offset, int count);
	public void receiveLoan(long userId, double amount);
	public void payLoanBack(long userId, double amount);
	public void payAllLoanBack(long userId);
	public void applyLoanInterest();
}
