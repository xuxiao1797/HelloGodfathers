package com.example.hellogodfather.dao;

public class LocalDAO implements BaseDAO {
    private static LocalDAO localDAO;
    private String filepath; // the file storing user and post information.
    private LocalDAO() {}
    public static LocalDAO getInstance() {
        if (localDAO == null) {
            localDAO = new LocalDAO();
        }
        return localDAO;
    }

    @Override
    public void insert() {
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void query() {

    }
}
