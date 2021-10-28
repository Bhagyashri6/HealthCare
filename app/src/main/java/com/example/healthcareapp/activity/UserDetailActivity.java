package com.example.healthcareapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.healthcareapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UserDetailActivity extends AppCompatActivity {
    RoundedImageView iv_img;
    TextView tv_name,tv_email,tv_path;
    String email,first_name,last_name,avatar;
    Button btn_download;
    ProgressDialog mProgressDialog;
    Bitmap bitmap ;
    String mSavedInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        first_name = intent.getStringExtra("first_name");
        last_name = intent.getStringExtra("last_name");
        avatar = intent.getStringExtra("avatar");


        iv_img = (RoundedImageView) findViewById(R.id.iv_img);
        tv_email =(TextView) findViewById(R.id.tv_email);
        tv_name = (TextView) findViewById(R.id.tv_name);
        btn_download = (Button)findViewById(R.id.btn_download);
        tv_path = (TextView)findViewById(R.id.tv_path);

        tv_name.setText(first_name + last_name);
        tv_email.setText(email);
        try {
            Glide.with(this)
                    .load(avatar)
                    .override(600, 200)
                    .fitCenter()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(iv_img);
        }catch (Exception e){
            e.printStackTrace();
        }

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UserDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(UserDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                    ActivityCompat.requestPermissions(UserDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                    Toast.makeText(getApplicationContext(),"Need Permission to access storage for Downloading Image",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Downloading Image...",Toast.LENGTH_LONG).show();
                    //Asynctask to create a thread to downlaod image in the background
                    new DownloadsImage().execute(avatar);
                }
            }
        });

    }

    class DownloadsImage extends AsyncTask<String, Void,Void>{

        File imageFile;
        @Override
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Create Path to save Image
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+ "/" + getPackageName()); //Creates app specific folder

            if(!path.exists()) {
                path.mkdirs();
            }

            imageFile = new File(path, String.valueOf(System.currentTimeMillis())+".png"); // Imagename.png
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try{
                bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
                out.flush();
                out.close();
                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(UserDetailActivity.this,new String[] { imageFile.getAbsolutePath() }, null,new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        // Log.i("ExternalStorage", "Scanned " + path + ":");
                        //    Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
            } catch(Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tv_path.setText(imageFile.getAbsolutePath());
            Toast.makeText(UserDetailActivity.this,"Image Saved",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

    }
}