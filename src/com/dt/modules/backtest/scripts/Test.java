//
//	Algo1.java
//	DataTrader
//
//  Created by Rob Graeber on Nov 3, 2012.
//  Copyright 2012 Rob Graeber. All rights reserved.
//
package com.dt.modules.backtest.scripts;
import java.util.ArrayList;
import java.util.List;

import com.dt.OrderManager;
import com.dt.StockDatabase;
import com.dt.StockDatabase.ListType;
import com.dt.classes.Stock;
import com.dt.classes.Order.OrderType;
import com.dt.modules.backtest.Script;

//To add new algorithm for testing, see MainSystem.java->init()
//Here's a sample algorithm to shows off the basic ordering structure:

//handleEnterTick() is called every new tick, in this case 1 tick = 1 day
//Access stocks from the StockDatabase, and Stock objects have price and volume info 
//Each algo has an attached portfolio accessed by getPortfolio()
//Submit orders to the OrderManager, calculate on your own the quantity of shares to buy
//Note: Keep in mind survivorship bias, commissions, and not using future info when crafting strategies

public class Test extends Script{
	public Test(){
		
	}
	public void handleEnterTick(){
		List<Stock> selectedStocks = new ArrayList<Stock>();
		for (Stock stock : StockDatabase.getStockArray(ListType.SP500)){
			if(stock.getPrice()>1.0 && stock.getMinimumVolume(20)>0){
				selectedStocks.add(stock);
			}
		}
		double totalFundsPerDay = getPortfolio().initialFunds;
		double fundsPerOrder = totalFundsPerDay / selectedStocks.size();
		for(Stock stock : selectedStocks){
			int quantity = (int) Math.floor(fundsPerOrder/stock.getOpen());
			if(quantity > 0){
				OrderManager.submitOrder(OrderType.BUY_AT_OPEN, stock.symbol, quantity, getPortfolio(), 0);
				OrderManager.submitOrder(OrderType.SELL_AT_CLOSE, stock.symbol, quantity, getPortfolio(), 0);
			}
		}
	}
}