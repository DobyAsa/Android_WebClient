package com.example.webclient;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    Button getButton;
    TextView pageView;
    TextView urlView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getButton = findViewById(R.id.get_button);
        pageView = findViewById(R.id.page_text);
        urlView = findViewById(R.id.url_text);
        imageView = findViewById(R.id.image);


        getButton.setOnClickListener(view -> new GetPage().execute());
        new GetPic().execute();
//        Glide.with(this)
//                .load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202105%2F15%2F20210515235051_78c48.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1672295643&t=3a15ae9dee8b6e207540813c0a0188f3")
//                .into(imageView);
    }

    class GetPage extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String url = urlView.getText().toString();
            url = URLUtil.normalize(url);
            return HttpUtil.get(url);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
                pageView.setText(result);
            else
                pageView.setText("Sorry, Page not found");
        }
    }

    class GetPic extends AsyncTask<Void, Void, byte[]> {

        @Override
        protected byte[] doInBackground(Void... voids) {

            Request request = new Request.Builder()
                    .url("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbkimg.cdn.bcebos.com%2Fpic%2F0eb30f2442a7d933c895c2b91019c61373f08302abe5&refer=http%3A%2F%2Fbkimg.cdn.bcebos.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1672296417&t=f0e00a0a5d4d8fb41420732ca5c0d1b0")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                byte[] pic = response.body().bytes();
                return pic;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            if (bytes != null){
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
            else
                System.out.println("Picture not Found");
        }
    }
}