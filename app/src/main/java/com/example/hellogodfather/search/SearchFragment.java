package com.example.hellogodfather.search;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellogodfather.MyApp;
import com.example.hellogodfather.PostAdaptor;
import com.example.hellogodfather.R;
import com.example.hellogodfather.dao.PostFirebaseDao;
import com.example.hellogodfather.model.Post;
import com.example.hellogodfather.model.User;
import com.example.hellogodfather.tok_parser.Parser;
import com.example.hellogodfather.tok_parser.Query;
import com.example.hellogodfather.tok_parser.Tokenizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 * @author Peng Zhao
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    SearchView searchView;
    TextView textView;
    PostFirebaseDao postFirebaseDao = PostFirebaseDao.getInstance();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyApp myApp = ((MyApp) getActivity().getApplicationContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = requireActivity().findViewById(R.id.search_view);

        textView = v.findViewById(R.id.textView2);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.blank_RecyclerView);

        /*
          Set query listener
          When query is submitted,
          first check if the query is valid through tokenizer and parser
          then check whether the database has the corresponding record.
          If yes, get the corresponding data and show in the UI.
          If no, show 'No results for ..'
         */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String input = searchView.getQuery().toString();

                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = new User();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String key = ds.getKey();
                            if (key != null) {
                                if (key.equals(firebaseAuth.getUid())) {
                                    user = ds.getValue(User.class);
                                }
                            }
                        }
                        postFirebaseDao.loadDataForRecyclerViewByQuery(getContext(), recyclerView, textView, input, user, myApp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "Failed");
                    }
                });

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return v;
    }
}