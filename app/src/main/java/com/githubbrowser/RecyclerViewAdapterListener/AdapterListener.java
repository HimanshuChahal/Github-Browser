package com.githubbrowser.RecyclerViewAdapterListener;

import android.view.View;

import java.util.ArrayList;

public interface AdapterListener {

    void onBindViewHolder(ArrayList<View> viewsArrayList, int position, String type);

    int getItemCount(String type);

}
