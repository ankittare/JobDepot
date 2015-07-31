package com.example.ankit.job_depot.candidate.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.ankit.job_depot.R;

public class job_web_view extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_job_web_view, container, false);
        Bundle extras=getArguments();
        String URL=extras.getString("CompanyURL");
        WebView webView=(WebView)v.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);
        return v;
    }
}
