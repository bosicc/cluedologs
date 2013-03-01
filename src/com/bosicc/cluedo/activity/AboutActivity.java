package com.bosicc.cluedo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.bosicc.cluedo.CluedoApp;
import com.bosicc.cluedo.R;
import com.bosicc.cluedo.pojo.GamePOJO;

public class AboutActivity extends SherlockActivity {

    // private static String TAG = "About";

    private Button mBtnWebsite;
    private Button mBtnEmail;

    private static final String SEND_MAIL = "Send mail...";
    private static final String PLAIN_TEXT = "plain/text";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about);
        
        ActionBar bar = this.getSupportActionBar();
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        
        mBtnWebsite = (Button) findViewById(R.id.btnWebsite);
        mBtnWebsite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://www.abm-sg.com/products/android/cluedo-logs/"));
                startActivity(intent);
                finish();
            }
        });

        mBtnEmail = (Button) findViewById(R.id.btnEmail);
        mBtnEmail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] to = { "info@abm-sg.com" };

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, to);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Cluedo logs");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Cluedo logs.");

                emailIntent.setType(PLAIN_TEXT);
                startActivity(Intent.createChooser(emailIntent, SEND_MAIL));

                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}