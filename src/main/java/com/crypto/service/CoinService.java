package com.crypto.service;

import com.crypto.model.Coin;

import java.util.List;

public interface CoinService {
    List<Coin> getAllCoins(int page) throws Exception;

    String getMarketChart(String coinId, int days);

    String getCoinDetails(String coinId);

    Coin findById(String coinId);

    String searchCoin(String keyword);

    String getTop50CoinsByMarketCapRank();

    String GetTreadingCoins();
}
