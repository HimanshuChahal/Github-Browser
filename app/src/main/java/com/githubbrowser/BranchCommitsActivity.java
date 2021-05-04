package com.githubbrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.githubbrowser.Adapter.RecyclerViewAdapter;
import com.githubbrowser.RecyclerViewAdapterListener.AdapterListener;
import com.githubbrowser.VolleyAPI.VolleyDataReceived;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class BranchCommitsActivity extends AppCompatActivity implements VolleyDataReceived, AdapterListener {

    private ArrayList<String> commitDatesArrayList;
    private ArrayList<String> commitMessagesArrayList;
    private ArrayList<String> commitURLArrayList;
    private ArrayList<String> commitUserNamesArrayList;

    public void backOnClick(View view)
    {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_commits);

        commitDatesArrayList=new ArrayList<>();
        commitMessagesArrayList=new ArrayList<>();
        commitURLArrayList=new ArrayList<>();
        commitUserNamesArrayList=new ArrayList<>();

        ((TextView) findViewById(R.id.commitsSubtitleTextView)).setText(getIntent().getStringExtra("Branch Name"));

        VolleyResponse volleyResponse=new VolleyResponse(BranchCommitsActivity.this, BranchCommitsActivity.this, "Commits", new HashMap<>(), "GET");
        volleyResponse.doInBackground(Utilities.repoBranchCommitsURL+getIntent().getStringExtra("Repository Name")+"/commits?sha="+getIntent().getStringExtra("Branch Name"));

    }

    @Override
    public void onDataReceived(String data, String type) {

        try {

            JSONArray array=new JSONArray(data);

            for(int i=0;i<array.length();i++)
            {
                commitDatesArrayList.add(array.getJSONObject(i).getJSONObject("commit").getJSONObject("author").getString("date").substring(0, 10));
                commitMessagesArrayList.add(array.getJSONObject(i).getJSONObject("commit").getString("message"));
                commitURLArrayList.add(array.getJSONObject(i).getJSONObject("commit").getString("url"));
                commitUserNamesArrayList.add(array.getJSONObject(i).getJSONObject("commit").getJSONObject("author").getString("name"));
            }

            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(BranchCommitsActivity.this, RecyclerView.VERTICAL, false);
            ((RecyclerView) findViewById(R.id.commitsRecyclerView)).setLayoutManager(layoutManager);

            RecyclerViewAdapter adapter=new RecyclerViewAdapter(BranchCommitsActivity.this, "Commits");
            ((RecyclerView) findViewById(R.id.commitsRecyclerView)).setAdapter(adapter);

        } catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onBindViewHolder(ArrayList<View> viewsArrayList, int position, String type) {

        ((TextView) viewsArrayList.get(0)).setText(commitDatesArrayList.get(position));
        ((TextView) viewsArrayList.get(1)).setText(commitMessagesArrayList.get(position));
        Picasso.with(BranchCommitsActivity.this).load(commitURLArrayList.get(position)).into((ImageView) viewsArrayList.get(2));
        ((TextView) viewsArrayList.get(3)).setText(commitUserNamesArrayList.get(position));

    }

    @Override
    public int getItemCount(String type) {
        return commitMessagesArrayList.size();
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.anim_start, R.anim.anim_end);

    }
}