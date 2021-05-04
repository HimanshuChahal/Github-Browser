package com.githubbrowser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.WindowManager;
import android.widget.ProgressBar;

public abstract class Utilities {

    static AlertDialog alertDialog;

    public static int maxTimeOut=20000;
    public static String baseURL="https://api.github.com/";
    public static String allReposURL=baseURL+"users/HimanshuChahal/repos";
    public static String reposBranchURL=baseURL+"repos/HimanshuChahal/";
    public static String reposIssuesURL=baseURL+"repos/HimanshuChahal/";
    public static String repoBranchCommitsURL=baseURL+"repos/HimanshuChahal/";

    public static void showAlertDialog(Context context)
    {

        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);

        ProgressBar progressBar=new ProgressBar(context);

        progressBar.setPadding(20, 20, 20, 20);

        progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.BLACK));

        alertDialog=alertDialogBuilder.create();

        alertDialog.setView(progressBar);

        alertDialog.show();

        WindowManager.LayoutParams params=new WindowManager.LayoutParams();

        params.copyFrom(alertDialog.getWindow().getAttributes());

        params.width=250;
        params.height=250;

        alertDialog.getWindow().setAttributes(params);

    }

    public static void dismissAlertDialog()
    {
        if(alertDialog!=null && alertDialog.isShowing())
        {
            alertDialog.dismiss();
        }
    }

}
