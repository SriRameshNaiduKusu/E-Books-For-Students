package in.cwt.ebooksforstudents.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import in.cwt.ebooksforstudents.R;

import im.delight.android.webview.AdvancedWebView;

public class WebView extends AppCompatActivity implements AdvancedWebView.Listener {

    //private WebView webView;
    String url,name;


    private AdvancedWebView mWebView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvtitle;
    private ImageView goback;



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_web_view );

        //webView = findViewById( R.id.webview );
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("URL");
            name = intent.getStringExtra( "Title" );

        }
        swipeRefreshLayout = findViewById( R.id.swipeRefreshLayout );
        tvtitle = findViewById( R.id.TVTitle );
        goback = findViewById( R.id.imBack );

        tvtitle.setText( name );

        goback.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView.super.onBackPressed();
            }
        } );


        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        LoadPage(url);
                                    }
                                }
        );



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPage(url);

            }
        });


        //webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setLoadWithOverviewMode(true);
        //webView.setWebChromeClient(new WebChromeClient());
        //webView.setBackgroundColor(0x00000000);
        //webView.getSettings().setBuiltInZoomControls(false);
        //String ggrame = "<iframe width=\"100%\" height=\"100%\" src='"+ bookurl +"' frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
        //webView.loadDataWithBaseURL("https://www.youtube.com", ggrame, "text/html", "UTF-8", null);


    }

    public void LoadPage(String Url){
        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(this, this);
        mWebView.setMixedContentAllowed(false);
        mWebView.loadUrl(Url);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

        swipeRefreshLayout.setRefreshing( true );

    }

    @Override
    public void onPageFinished(String url) {

        swipeRefreshLayout.setRefreshing( false );
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}