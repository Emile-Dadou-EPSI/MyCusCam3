package com.example.mycuscam.Visu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.mycuscam.R;

public class loadingDialog {
    Activity activity;
    AlertDialog dialog;
    CharSequence data;
    Boolean is2;
    TextView textView;

    public loadingDialog(Activity myActivity, Boolean is) {
        activity = myActivity;
        is2 = is;
    }

    public void startLoadingDialog() {
        if (is2) {
            //savedInstance.get
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.custom_dialog, null));
            dialog = builder.create();
            dialog.show();
        }
        else {
            //textView = textView.findViewById(R);
            textView.setText(data);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            LayoutInflater inflater = activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.activity_loading_dialog2, null));

            dialog = builder.create();
            dialog.show();
        }

    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public void setData(CharSequence data) {
        this.data =  data;
    }

}
