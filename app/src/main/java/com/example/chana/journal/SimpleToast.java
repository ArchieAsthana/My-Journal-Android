package com.example.chana.journal;

import android.content.Context;
import android.widget.Toast;

public class SimpleToast {

    public static void toastWithMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
