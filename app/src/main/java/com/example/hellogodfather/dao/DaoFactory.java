package com.example.hellogodfather.dao;

public class DaoFactory {
    private static final int GET_FIREBASE_DAO = 1;
    private static final int GET_LOCAL_DAO = 2;
    private static DaoFactory df;
    private BaseDAO dao;
    private DaoFactory() {}
    public static DaoFactory getInstance() {
        if (df == null) {
            df = new DaoFactory();
        }
        return df;
    }
    public BaseDAO getDAO(int dao) {
        if (dao == GET_FIREBASE_DAO) {
            return FirebaseDAO.getInstance();
        } else if (dao == GET_LOCAL_DAO) {
            return LocalDAO.getInstance();
        }
        return null;
    }
}
