package com.seraphic.comonolo.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.seraphic.comonolo.R;

/**
 * Created by user on 12/10/17.
 */

public class WebViewActivity extends AppCompatActivity {
    WebView web;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web = (WebView) findViewById(R.id.web);


        String restaurant_id = getResources().getString(R.string.restaurant_id);
        String company_id = getResources().getString(R.string.company_id);

        url = "" + company_id + restaurant_id;

        String domain = "localhost";

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(domain, "name=value");
        cookieManager.setCookie(domain, "path=/");
        cookieManager.setCookie(domain, "HttpOnly");

        //enable cookies
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);
        }
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAppCacheEnabled(true);
        //  web.loadDataWithBaseURL("file:///android_asset/",data, "text/html", "UTF-8","");
        //  web.loadData(data, "text/html", "UTF-8");
        web.loadUrl(url);
        web.addJavascriptInterface(new WebAppInterface(this), "Android");
        web.setWebViewClient(new MyWebViewClient());
        // web.setWebChromeClient(new WebChromeClient());
        CookieSyncManager.createInstance(web.getContext());

        CookieSyncManager.getInstance().sync();

    }

    protected class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("url", "" + url);
            web.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }
}
