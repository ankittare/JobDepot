package com.example.ankit.job_depot;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.linkedin.platform.AccessToken;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import com.linkedin.platform.LISession;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.utils.Scope;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    public static final String WELCOME_MESSAGE="com.example.ankit.job_depot.Welcome";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
        Parse Initialisation
         */

        super.onCreate(savedInstanceState);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ftqZNLU8FZ8PPApaRGSZbW99xYERIqw0cWaNsKuh", "LQxbAOhhPdFDjiG3Gb1lQolW6fEgXCO94zadYO27");

        /*
        Parse API callback for saving object
         */
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkAuthentication();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);



        return true;
    }


   /* public void sendMessage(View view){
        Intent intent=new Intent(this, Candidate_main_Activity.class);
        EditText username=(EditText)findViewById(R.id.username);
        //Pass password=findViewById(R.id.p)
        String message=null;

        if(username.getText().toString().equals("ankit")){
            message="Welcome, "+username.getText().toString()+"!";
            intent.putExtra(WELCOME_MESSAGE, message);

        }
        else{
            TextView textView=(TextView)findViewById(R.id.error_text);
            message="@string/error_text";
            textView.setText(message);
        }
        startActivity(intent);
    }
    */
    /*
    linkedInAuth: Authorises the app for using the basic profile of the candidate
    The first time this method is called, there is no acess token, so the init method will not have token
    On subsequent calls the access token generated for this user will be used.
     */

    public void linkedInAuth(View view){
        final TextView textView=(TextView)findViewById(R.id.error_text);

        final Activity thisActivity = this;

        // Build the list of member required permissions
        List<String> scope = new ArrayList<>();
        scope.add("r_fullprofile");
        scope.add("w_share");


        LISessionManager.getInstance(getApplicationContext()).init(thisActivity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK
                Log.i("Init Sucess", String.valueOf(checkAuthentication()));
                /*
                Moving to candidate home Page
                 */
                startActivity(new Intent(thisActivity, candidateHome.class));

            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
                Log.i("Init Failed", String.valueOf(checkAuthentication()));
                Log.i("Error Message", error.toString());
                textView.setText("Authentication Failed");

            }
        }, true);
    }


    public void signIn(View v){
        /*
        Do authentication with PArse Here
         */
        startActivity(new Intent(this, candidateHome.class));
    }

    public boolean checkAuthentication(){
        LISessionManager liSessionManager=LISessionManager.getInstance(getApplicationContext());
        LISession liSession=liSessionManager.getSession();
        return liSession.isValid();
    }

    private static Scope buildScope() {

        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }
}
