package vn.vnpt.vanquan223.projectnews.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import vn.vnpt.vanquan223.projectnews.R;

public class WebViewActivity extends AppCompatActivity {
    WebView wvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        wvNews = findViewById(R.id.wvNews);

        Intent intent = getIntent();
        String content = intent.getStringExtra("CONTENT");

        wvNews.setWebViewClient(new WebViewClient());
        wvNews.getSettings().setLoadsImagesAutomatically(true);
        wvNews.getSettings().setJavaScriptEnabled(true);
        wvNews.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvNews.loadData(content, "text/html", "UTF-8");
//        wvNews.loadDataWithBaseURL("", content, "text/html", "UTF-8", null);
    }
}
