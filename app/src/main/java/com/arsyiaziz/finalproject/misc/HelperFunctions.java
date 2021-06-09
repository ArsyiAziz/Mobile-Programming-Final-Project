package com.arsyiaziz.finalproject.misc;

import android.view.View;
import android.widget.TextView;

public class HelperFunctions {
    public static void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideView(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    public static void showError(TextView textView, String msg) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(msg);
    }

    public static void hideError(TextView textView) {
        hideView(textView);
    }
}
