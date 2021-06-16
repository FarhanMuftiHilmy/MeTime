package com.rechit.metime.Utils;
import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rechit.metime.R;
import com.squareup.picasso.Picasso;

public class AppUtils {

    public static void loadProfilePicFromUrl(ImageView imageView, String url){
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_no_profile)
                .error(R.drawable.ic_no_profile)
                .into(imageView);
    }

    // Mendapatkan string tanpa spasi berlebihan
    public static String getFixText(EditText editText){
        return (editText.getText().toString().trim()).replaceAll("\\s+", " ");
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
