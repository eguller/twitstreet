package com.twitstreet.market.impl;

import static com.twitstreet.base.Result.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.twitstreet.base.Result;
import com.twitstreet.market.api.StockMgr;
import com.twitstreet.market.api.PortfolioMgr;
import com.twitstreet.market.api.StockPriceMgr;

import atunit.AtUnit;
import atunit.Container;
import atunit.Mock;
import atunit.MockFramework;
import atunit.Unit;

@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.EASYMOCK)
@Container(Container.Option.GUICE)
public class TransactionMgrTest {

	@Inject
	@Unit
	private TransactionMgrImpl transactionMgr;

	@Mock
	private StockPriceMgr stockPriceMgr;

	@Mock
	private StockMgr stockMgr;

	@Mock
	private PortfolioMgr portfolioMgr;

	/**
	 * Initially buyer1 has 100$,<br>
	 * stock1 has 1000 followers and had never been sold.
	 * <p>
	 * buyer1 buys 100 followers of stock1.<br>
	 * His portfolio is updated with %10 of stock1.
	 */
	@Test
	public void testBuy() {
		String buyer = "buyer1";
		String stock = "stock1";
		Object[] mocks = new Object[] { stockPriceMgr, stockMgr, portfolioMgr };

		expect(stockPriceMgr.updateFollowerCount(stock, buyer)).andReturn(success(1000));
		expect(stockMgr.getPercentSold(stock)).andReturn(success(0.0));
		expect(portfolioMgr.buy(buyer, 100, stock, 10.0)).andReturn(success(null));

		replay(mocks);

		Result<Object> result = transactionMgr.buy(buyer, stock, 100);
		testSuccess(result);

		verify(mocks);
	}

	protected void testSuccess(Result<Object> result) {
		if (result != null) {
			if (!result.isSuccessful()) {
				fail("Failed with error code:" + result.getErrorCode());
			}
		} else {
			fail("result is null.");
		}
	}

	protected <T> void testSuccess(Result<T> result, T expected) {
		if (result != null) {
			if (result.isSuccessful()) {
				if (!expected.equals(result.getPayload())) {
					fail("unexpected payload:" + result.getPayload());
				}
			} else if (result.getErrorCode() == null) {
				fail("result failed with null error code.");
			} else {
				fail(result.getErrorCode().toString());
			}
		} else {
			fail("result is null.");
		}
	}
	// TODO move protect methods to superclass BeanTest
}
