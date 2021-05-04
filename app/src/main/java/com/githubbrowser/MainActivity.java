package com.githubbrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githubbrowser.Adapter.RecyclerViewAdapter;
import com.githubbrowser.RecyclerViewAdapterListener.AdapterListener;
import com.githubbrowser.VolleyAPI.VolleyDataReceived;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements VolleyDataReceived, AdapterListener {

    private ArrayList<String> namesArrayList;
    private ArrayList<String> descriptionsArrayList;
    private View splashScreenView;
    SQLiteDatabase sqLiteDatabase;
    private int noOfRepos;
    private ArrayList<Integer> idsArrayList;

    public void addOnClick(View view)
    {

        startActivity(new Intent(MainActivity.this, AddRepoActivity.class));
        overridePendingTransition(R.anim.anim_start, R.anim.anim_end);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        namesArrayList=new ArrayList<>();
        descriptionsArrayList=new ArrayList<>();
        idsArrayList=new ArrayList<>();
        sqLiteDatabase=this.openOrCreateDatabase("Repositories", MODE_PRIVATE, null);

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Repos(id INTEGER PRIMARY KEY, owner VARCHAR, name VARCHAR)");

        splashScreenView=LayoutInflater.from(MainActivity.this).inflate(R.layout.splash_screen_layout, null, false);

        splashScreenView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ((RelativeLayout) findViewById(R.id.relativeLayout)).addView(splashScreenView);

        VolleyResponse volleyResponse=new VolleyResponse(MainActivity.this, MainActivity.this, "All Repositories", new HashMap<>(), "GET");
        volleyResponse.setSplashScreen(true);
        volleyResponse.doInBackground(Utilities.allReposURL);

    }

    @Override
    public void onDataReceived(String data, String type) {

        if(type.equals("All Repositories"))
        {
            try {

                JSONArray array=new JSONArray(data);

                for(int i=0;i<array.length();i++)
                {
                    JSONObject object=array.getJSONObject(i);

                    namesArrayList.add(object.getString("name"));
                    if(object.getString("description").equals("null"))
                    {
                        descriptionsArrayList.add("No Description");
                    } else {
                        descriptionsArrayList.add(object.getString("description"));
                    }

                }

                noOfRepos=array.length();

                Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM Repos", null);

                int nameIndex=cursor.getColumnIndex("name");
                int idsIndex=cursor.getColumnIndex("id");

                cursor.moveToFirst();

                while(!cursor.isAfterLast())
                {
                    namesArrayList.add(cursor.getString(nameIndex));
                    descriptionsArrayList.add("No Description");
                    idsArrayList.add(cursor.getInt(idsIndex));
                    cursor.moveToNext();
                }

                cursor.close();

                ((RelativeLayout) findViewById(R.id.relativeLayout)).removeView(splashScreenView);

                findViewById(R.id.repoLinearLayout).setVisibility(View.VISIBLE);

                RecyclerView repoRecyclerView=findViewById(R.id.repoRecyclerView);

                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);

                repoRecyclerView.setLayoutManager(layoutManager);

                RecyclerViewAdapter adapter=new RecyclerViewAdapter(MainActivity.this, "Repositories");

                repoRecyclerView.setAdapter(adapter);

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBindViewHolder(ArrayList<View> viewsArrayList, int position, String type) {

        ((TextView) viewsArrayList.get(0)).setText(namesArrayList.get(position));
        ((TextView) viewsArrayList.get(1)).setText(descriptionsArrayList.get(position));

        ((ImageView) viewsArrayList.get(2)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                String repoDetails=namesArrayList.get(position)+"\n"+descriptionsArrayList.get(position)+"\n";

                if(position+1<=noOfRepos)
                {
                    repoDetails+="https://api.github.com/repos/HimanshuChahal/"+namesArrayList.get(position);
                }

                intent.putExtra(Intent.EXTRA_TEXT, repoDetails);

                startActivity(Intent.createChooser(intent, "Share"));

            }
        });

        viewsArrayList.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean webSearch=true;

                int idIndex=0;

                if(position+1>noOfRepos)
                {
                    webSearch=false;
                    idIndex=position-noOfRepos;
                }

                int id=((idsArrayList.size()>0)?idsArrayList.get(idIndex):-1);

                startActivity(new Intent(MainActivity.this, RepoDetailsActivity.class).putExtra("Name", namesArrayList.get(position)).putExtra("Description", descriptionsArrayList.get(position)).putExtra("Web Search", webSearch).putExtra("id", id));
                overridePendingTransition(R.anim.anim_start, R.anim.anim_end);

            }
        });

    }

    @Override
    public int getItemCount(String type) {
        return namesArrayList.size();
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.anim_start, R.anim.anim_end);

    }
}