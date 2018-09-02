package com.kalpeshwadekar.shaadi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MatchesCardAdapter adapter;
    private List<MatchesCard> matchesCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        matchesCardList = new ArrayList<>();
        adapter = new MatchesCardAdapter(matchesCardList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        prepareMatchesCard();
    }

    private void prepareMatchesCard() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://randomuser.me/api/?results=10";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        JSONObject profileObj;
                        MatchesCard matchesCard;
                        String picture,firstName,lastName,gender,city,state;
                        int age;
                        for(int i=0;i<results.length();i++){
                            profileObj = results.getJSONObject(i);
                            picture = profileObj.getJSONObject("picture").getString("large");
                            firstName = profileObj.getJSONObject("name").getString("first");
                            lastName = profileObj.getJSONObject("name").getString("last");
                            gender = profileObj.getString("gender");
                            city = profileObj.getJSONObject("location").getString("city");
                            state = profileObj.getJSONObject("location").getString("state");
                            age = profileObj.getJSONObject("dob").getInt("age");
                            matchesCard = new MatchesCard(picture,firstName,lastName,age,gender,city,state);
                            matchesCardList.add(matchesCard);
                        }
                        adapter.notifyDataSetChanged();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,"Something went wrong. Please try again.",Toast.LENGTH_SHORT).show();
                }
            });

        // Access the RequestQueue through your singleton class.
        if(isNetworkAvailable()){
            queue.add(jsonObjectRequest);
        }else{
            Toast.makeText(MainActivity.this,"Please check your internet connectivity.",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
