package com.example.a222latest;

import android.app.SearchManager;
//import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPost adapterPost;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //logIn();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //init
        firebaseAuth = FirebaseAuth.getInstance();

        //recycler view and its properties
        recyclerView = view.findViewById(R.id.postRecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //show newest post first, for this from load last
        layoutManager.setReverseLayout(true);
        layoutManager.setReverseLayout(true);

        //setlayout to recycler
        recyclerView.setLayoutManager(layoutManager);
        //init post list
        postList = new ArrayList<>();

        loadPosts();
        return view;
    }
    private void loadPosts(){
        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    postList.add(modelPost);

                    //adapter
                    adapterPost = new AdapterPost(getActivity(), postList);
                    //set adapter to recycler
                    recyclerView.setAdapter(adapterPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //if there is an error
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchPost(String searchQuery){
        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    if(modelPost.getpTitle().toLowerCase().contains(searchQuery.toLowerCase())){
                        postList.add(modelPost);
                    }

                    //adapter
                    adapterPost = new AdapterPost(getActivity(), postList);
                    //set adapter to recycler
                    recyclerView.setAdapter(adapterPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //if there is an error
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    //Check user status


  // @RequiresApi(api = Build.VERSION_CODES.M)
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.menu_post,menu);
       MenuItem searchItem = menu.findItem(R.id.post_search);
       SearchView searchView = (SearchView) MenuItemCompat.getActionView((MenuItem) searchItem);
       searchView.getQueryHint();
       SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

       if (searchItem != null) {
           searchView = (SearchView) searchItem.getActionView();
       }
       if (searchView != null) {
           searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));}
       queryTextListener = new SearchView.OnQueryTextListener() {

           @Override
           public boolean onQueryTextSubmit(String query) {
               //called when pressed search button
               if (!TextUtils.isEmpty(query)) {
                   searchPost(query);
                   return true;
               } else {
                   loadPosts();
               }
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               //called as and when user press any letter
               if (!TextUtils.isEmpty(newText)) {
                   searchPost(newText);
               } else {
                   loadPosts();
               }
               return false;
           }
       };
       //Searchview to search posts by post title
       /* MenuItem searchItem = menu.findItem(R.id.post_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView((MenuItem) searchItem);

       // searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        //SearchManager  searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
             searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
          searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));


           queryTextListener = new SearchView.OnQueryTextListener() {
               @Override
               public boolean onQueryTextSubmit(String query) {
                   //called when pressed search button
                   if (!TextUtils.isEmpty(query)) {
                       searchPost(query);
                       return true;
                   } else {
                       loadPosts();
                   }
                   return false;
               }

               @Override
               public boolean onQueryTextChange(String newText) {
                   //called as and when user press any letter
                   if (!TextUtils.isEmpty(newText)) {
                       searchPost(newText);
                   } else {
                       loadPosts();
                   }
                   return false;
               }
           };
           searchView.setOnQueryTextListener(queryTextListener);
       }
        super.onCreateOptionsMenu(menu,inflater);
*/
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
         if(id == R.id.post_search) {
             Toast.makeText(getActivity(),"sad",Toast.LENGTH_LONG).show();
             return true;
         }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
    private void logIn() {
        String email = "deneme@gmail.com";
        String password = "123456";


        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
               // Toast.makeText(this,"Loggedin",Toast.LENGTH_SHORT).show();
            }
            //else
                //Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        });
    }

}