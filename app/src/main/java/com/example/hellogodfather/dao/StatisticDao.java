package com.example.hellogodfather.dao;

public class StatisticDao {
    private static StatisticDao dao;
    private StatisticDao() {}
    public static StatisticDao getInstance() {
        if (dao == null) {
            dao = new StatisticDao();
        }
        return dao;
    }

}
