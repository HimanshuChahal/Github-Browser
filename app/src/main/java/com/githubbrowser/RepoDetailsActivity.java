package com.githubbrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.githubbrowser.Adapter.RecyclerViewAdapter;
import com.githubbrowser.RecyclerViewAdapterListener.AdapterListener;
import com.githubbrowser.VolleyAPI.VolleyDataReceived;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RepoDetailsActivity extends AppCompatActivity implements VolleyDataReceived, AdapterListener {

    private ArrayList<String> branchNamesArrayList;
    private ArrayList<String> issuesTitleArrayList;
    private ArrayList<String> avatarURLArrayList;
    private ArrayList<String> avatarNamesArrayList;

    public void backOnClick(View view)
    {
        finish();
    }

    public void deleteOnClick(View view)
    {
        if(!getIntent().getBooleanExtra("Web Search", false))
        {
            SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("Repositories", MODE_PRIVATE, null);

            sqLiteDatabase.delete("Repos", "id="+getIntent().getIntExtra("id", 0), null);

            sqLiteDatabase.close();

            startActivity(new Intent(RepoDetailsActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.anim_start, R.anim.anim_end);

        } else
        {
            Toast.makeText(RepoDetailsActivity.this, "The Repository exists on Github, you cannot delete this Repository", Toast.LENGTH_LONG).show();
        }
    }

    public void webSearchOnClick(View view)
    {
        if(getIntent().getBooleanExtra("Web Search", false))
        {

            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://github.com/HimanshuChahal/"+getIntent().getStringExtra("Name")));
            startActivity(intent);

        } else
        {
            Toast.makeText(RepoDetailsActivity.this, "The Repository exists on the Device Storage, you cannot search the Repository on Web Browser", Toast.LENGTH_LONG).show();
        }
    }

    public void branchTabOnClick(View view)
    {
        if(getIntent().getBooleanExtra("Web Search", false)) {
            ((TextView) findViewById(R.id.tabTitleTextView)).setText(getText(R.string.branch));

            VolleyResponse volleyResponse = new VolleyResponse(RepoDetailsActivity.this, RepoDetailsActivity.this, "Branch", new HashMap<>(), "GET");
            volleyResponse.doInBackground(Utilities.reposBranchURL + getIntent().getStringExtra("Name") + "/branches");
        }

    }

    public void issuesTabOnClick(View view)
    {
        if(getIntent().getBooleanExtra("Web Search", false)) {
            ((TextView) findViewById(R.id.tabTitleTextView)).setText(getText(R.string.issues));

            VolleyResponse volleyResponse = new VolleyResponse(RepoDetailsActivity.this, RepoDetailsActivity.this, "Issues", new HashMap<>(), "GET");
            volleyResponse.doInBackground(Utilities.reposIssuesURL + getIntent().getStringExtra("Name") + "/issues?state=open");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);

        branchNamesArrayList=new ArrayList<>();
        issuesTitleArrayList=new ArrayList<>();
        avatarURLArrayList=new ArrayList<>();
        avatarNamesArrayList=new ArrayList<>();

        ((TextView) findViewById(R.id.repoDetailsNameTextView)).setText(getIntent().getStringExtra("Name"));
        ((TextView) findViewById(R.id.repoDetailsDescriptionTextView)).setText(getIntent().getStringExtra("Description"));

    }

    @Override
    public void onDataReceived(String data, String type) {

        if(type.equals("Branch"))
        {

            branchNamesArrayList.clear();

            try {

                JSONArray array=new JSONArray(data);

                JSONObject object;

                for(int i=0;i<array.length();i++)
                {
                    object=array.getJSONObject(i);

                    branchNamesArrayList.add(object.getString("name"));
                }

            } catch(Exception e)
            {
                e.printStackTrace();
            }

            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(RepoDetailsActivity.this, RecyclerView.VERTICAL, false);
            ((RecyclerView) findViewById(R.id.tabRecyclerView)).setLayoutManager(layoutManager);

            RecyclerViewAdapter adapter=new RecyclerViewAdapter(RepoDetailsActivity.this, "Branch");
            ((RecyclerView) findViewById(R.id.tabRecyclerView)).setAdapter(adapter);

        } else if(type.equals("Issues"))
        {

            issuesTitleArrayList.clear();
            avatarURLArrayList.clear();
            avatarNamesArrayList.clear();

            try {

                JSONArray array=new JSONArray(data);

                JSONObject object;

                for(int i=0;i<array.length();i++)
                {
                    object=array.getJSONObject(i);

                    issuesTitleArrayList.add(object.getString("title"));
                    avatarURLArrayList.add(object.getJSONObject("user").getString("avatar_url"));
                    avatarNamesArrayList.add(object.getJSONObject("user").getString("login"));

                }

            } catch(Exception e)
            {
                e.printStackTrace();
            }

            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(RepoDetailsActivity.this, RecyclerView.VERTICAL, false);
            ((RecyclerView) findViewById(R.id.tabRecyclerView)).setLayoutManager(layoutManager);

            RecyclerViewAdapter adapter=new RecyclerViewAdapter(RepoDetailsActivity.this, "Issues");
            ((RecyclerView) findViewById(R.id.tabRecyclerView)).setAdapter(adapter);

        }

    }

    @Override
    public void onBindViewHolder(ArrayList<View> viewsArrayList, int position, String type) {

        if(type.equals("Branch"))
        {

            ((TextView) viewsArrayList.get(0)).setText(branchNamesArrayList.get(position));
            viewsArrayList.get(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(RepoDetailsActivity.this, BranchCommitsActivity.class).putExtra("Repository Name", getIntent().getStringExtra("Name")).putExtra("Branch Name", branchNamesArrayList.get(position)));
                    overridePendingTransition(R.anim.anim_start, R.anim.anim_end);

                }
            });

        } else if(type.equals("Issues"))
        {

            ((TextView) viewsArrayList.get(0)).setText(issuesTitleArrayList.get(position));
            Picasso.with(RepoDetailsActivity.this).load(avatarURLArrayList.get(position)).into((ImageView) viewsArrayList.get(1));
            ((TextView) viewsArrayList.get(2)).setText(avatarNamesArrayList.get(position));

        }

    }

    @Override
    public int getItemCount(String type) {

        if(type.equals("Branch"))
        {

            return branchNamesArrayList.size();

        } else if(type.equals("Issues"))
        {

            return issuesTitleArrayList.size();

        }

        return 0;
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.anim_start, R.anim.anim_end);

    }
}