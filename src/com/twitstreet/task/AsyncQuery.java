package com.twitstreet.task;

import com.twitstreet.task.asyncquery.TransactionParams;

public interface AsyncQuery extends Runnable{

	public abstract void addTransaction(TransactionParams transactionParams);

}