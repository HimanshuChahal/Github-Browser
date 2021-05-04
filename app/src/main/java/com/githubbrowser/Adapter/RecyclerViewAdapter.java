package com.githubbrowser.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.githubbrowser.R;
import com.githubbrowser.RecyclerViewAdapterListener.AdapterListener;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder> {

    AdapterListener listener;
    String type;
    static String typeValue;
    public RecyclerViewAdapter(AdapterListener listener, String type)
    {
        this.listener=listener;
        this.type=type;
        typeValue=type;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch(type) {

            case "Repositories": return new RecyclerViewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_recyclerview_layout, parent, false));

            case "Branch": return new RecyclerViewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_recyclerview_layout, parent, false));

            case "Issues": return new RecyclerViewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.issues_recyclerview_layout, parent, false));

            case "Commits": return new RecyclerViewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.commits_recyclerview_layout, parent, false));

            default: return new RecyclerViewViewHolder(new View(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {

        listener.onBindViewHolder(holder.viewsArrayList, position, type);

    }

    @Override
    public int getItemCount() {
        return listener.getItemCount(type);
    }

    static class RecyclerViewViewHolder extends RecyclerView.ViewHolder
    {

        ArrayList<View> viewsArrayList;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);

            viewsArrayList=new ArrayList<>();

            switch(typeValue) {

                case "Repositories": viewsArrayList.add(itemView.findViewById(R.id.repoNameTextView));
                    viewsArrayList.add(itemView.findViewById(R.id.repoDescriptionTextView));
                    viewsArrayList.add(itemView.findViewById(R.id.repoNavigationImageView));
                    viewsArrayList.add(itemView.findViewById(R.id.reposCardLinearLayout));

                    break;

                case "Branch": viewsArrayList.add(itemView.findViewById(R.id.branchTabTextView));
                    viewsArrayList.add(itemView.findViewById(R.id.branchLinearLayout));

                    break;

                case "Issues": viewsArrayList.add(itemView.findViewById(R.id.issueTitleTextView));
                    viewsArrayList.add(itemView.findViewById(R.id.issueAvatarImageView));
                    viewsArrayList.add(itemView.findViewById(R.id.issueAvatarNameTextView));

                    break;

                case "Commits": viewsArrayList.add(itemView.findViewById(R.id.commitDateTextView));
                    viewsArrayList.add(itemView.findViewById(R.id.commitMessageTextView));
                    viewsArrayList.add(itemView.findViewById(R.id.commitUserImageView));
                    viewsArrayList.add(itemView.findViewById(R.id.commitUserNameTextView));

            }

        }
    }

}
