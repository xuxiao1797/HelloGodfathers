package com.example.hellogodfather.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDAO implements BaseDAO {
    private static FirebaseDAO firebaseDAO;
    private FirebaseDatabase database = null;
    private DatabaseReference userDatabaseReference = null;
    private DatabaseReference postDatabaseReference = null;
    private FirebaseDAO() {}
    public static FirebaseDAO getInstance() {
        if (firebaseDAO == null) {
            firebaseDAO = new FirebaseDAO();
        }
        firebaseDAO.initFirebase();
        return firebaseDAO;
    }
    private void initFirebase() {
        if (this.database == null) this.database = FirebaseDatabase.getInstance();
        if (this.userDatabaseReference == null) this.userDatabaseReference = database.getReference("User"); // ref = "User"
        if (this.postDatabaseReference == null) this.postDatabaseReference = database.getReference("Post"); // ref = "Post"
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
