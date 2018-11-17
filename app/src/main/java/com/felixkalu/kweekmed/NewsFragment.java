package com.felixkalu.kweekmed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);

        //hide the activity action bar on this fragment since this fragment has its own toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setTitle("News Updates");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        WebView webView = (WebView)v.findViewById(R.id.newsWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://www.medicalnewstoday.com/");

        return v;
    }

}
