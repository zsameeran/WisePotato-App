package com.wisepotato.wp_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class customLoader {


    private Activity activity;
    private AlertDialog alertDialog;

    customLoader(Activity activity){
        this.activity = activity;
    }

    void startLoaderDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loader_alert ,null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    void dismissLoaderDialog(){

        alertDialog.dismiss();
    }
}
