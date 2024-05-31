package com.fyp.voicevision.helpers.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogUtils {

    public static ProgressDialog buildProgressDialog(Context context, String title, String message, boolean isCancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancelable);
        return progressDialog;
    }

    public static void buildDialog(Context context, String title, String message, boolean isCancellable, boolean isFinish) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(isCancellable);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
            if (isFinish)
                ((Activity) context).finish();
        });
        builder.show();
    }

    public static MaterialAlertDialogBuilder buildDialogWithListener(Context context, String title, String message, boolean isCancellable) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(isCancellable);
        return builder;
    }
}
