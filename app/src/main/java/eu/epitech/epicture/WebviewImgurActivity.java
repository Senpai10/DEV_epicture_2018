package eu.epitech.epicture;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewImgurActivity extends AppCompatActivity {

    String username = null;
    String accessToken = null;
    String refreshToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_imgur);
        WebView imgurWebView = findViewById(R.id.LoginWebview);
        imgurWebView.setBackgroundColor(Color.TRANSPARENT);
        imgurWebView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=253d989b464134f&response_type=token");
        imgurWebView.getSettings().setJavaScriptEnabled(true);

        imgurWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("epicture/oauth2/callback")) {
                    Intent mainActivity = new Intent(WebviewImgurActivity.this, MainActivity.class);
                    splitUrl(url, view);
                    mainActivity.putExtra("token", accessToken);
                    mainActivity.putExtra("username", username);
                    startActivity(mainActivity);
                } else {
                    view.loadUrl(url);
                }

                return true;
            }
        });
    }
    private void splitUrl(String url, WebView view) {
        String[] outerSplit = url.split("\\#")[1].split("\\&");

        int index = 0;

        for (String s : outerSplit) {
            String[] innerSplit = s.split("\\=");

            switch (index) {
                case 0:
                    accessToken = innerSplit[1];
                    break;

                case 3:
                    refreshToken = innerSplit[1];
                    break;

                case 4:
                    username = innerSplit[1];
                    break;
                default:

            }

            index++;
        }
    }
}
