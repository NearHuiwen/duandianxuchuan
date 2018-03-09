package com.yunhong.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MainActivity extends AppCompatActivity {
    String url = "http://192.168.42.1:8080/DCIM/NOR/NOR_20170811_101921A.mp4";

    private OkHttpClient mClient = new OkHttpClient();//OKHttpClient;
    private Button button;
    private ImageView imageView;
    private Bitmap bitmap;
    private static final int IS_SUCCESS = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {


            switch (msg.what) {
                case IS_SUCCESS:


                     bitmap =  (Bitmap) msg.obj;
                    System.out.println("=====" + bitmap);
                    imageView.setImageBitmap(bitmap);
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getImage(323477584, 34560L);

        button = (Button) findViewById(R.id.test);
        imageView = (ImageView) findViewById(R.id.image);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


//                getImageThread();


            }
        });


    }

    public void getImage(long paramLong1, long paramLong2) {
        String localObject = "bytes=" + paramLong1 + "-" + (paramLong1 + paramLong2 - 1L);
        Request request = new Request.Builder()
                .addHeader("RANGE", localObject) //断点续传要用到的，指示下载的区间
                .url(url)
                .build();
        System.out.println("========");

        Call call = mClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("IOException==" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                    bitmap=   BitmapUtil.rawByteArray2RGBABitmap2(response.body().bytes(), 192, 90);


                    Message msg = new Message();
                    msg.what = IS_SUCCESS;
                    msg.obj = bitmap;

                    handler.sendMessage(msg);




            }

        });
    }
}