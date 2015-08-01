package com.example.ankit.job_depot.candidate.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.candidate.model.DAO.AuthQuery;
import com.example.ankit.job_depot.candidate.model.DAO.CandidateQuery;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.parse.Parse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    public static final String WELCOME_MESSAGE = "com.example.ankit.job_depot.Welcome";
    private static final String TAG = "Main Activity";
    private Button Parsesignin;
    private ImageButton linkedInsignin;
    private TextView signup;
    private EditText username;
    private EditText password;
    private SharedPreferences sharedPreferences;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        Parse Initialisation
         */

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ftqZNLU8FZ8PPApaRGSZbW99xYERIqw0cWaNsKuh", "LQxbAOhhPdFDjiG3Gb1lQolW6fEgXCO94zadYO27");

        /*
        Initialize View
         */

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(sharedPreferences.contains("username"))
            username.setText(sharedPreferences.getString("username", ""));

        Parsesignin = (Button) findViewById(R.id.signin);
        linkedInsignin = (ImageButton) findViewById(R.id.linkedInSignIn);
        signup = (TextView) findViewById(R.id.signup);
        /*
        implement Checkbox Logic
         */
        try {
            init();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
    }

    private void init() throws NullPointerException {

        Parsesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signIn();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
            }
        });

        linkedInsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAuthentication();
                linkedInAuth();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Create methods for sign up and call here
                 */
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
    linkedInAuth: Authorises the app for using the basic profile of the candidate
    The first time this method is called, there is no acess token, so the init method will not have token
    On subsequent calls the access token generated for this user will be used.
     */

    public void linkedInAuth() {
        final TextView textView = (TextView) findViewById(R.id.error_text);

        final Activity thisActivity = this;

        // Build the list of member required permissions
        List<String> scope = new ArrayList<>();
        scope.add("r_basicprofile");
        scope.add("r_emailaddress");
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
                try {
                    if (!sharedPreferences.contains("username")) {
                        /*CandidateQuery candidateQuery = new CandidateQuery();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("ObjectId", parseUsername);
                        editor.putString("username", username.getText().toString());
                        //editor.putString("password", username.getText().toString());
                        editor.commit();*/

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
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


    public void signIn() throws NullPointerException {

        /*
        Do authentication with Parse Here
         */
        if ((username.getText().toString().equals("") || password.getText().toString().equals(""))) {
            Toast.makeText(getApplicationContext(),
                    "UserName or Password Missing!",
                    Toast.LENGTH_LONG).show();
        } else {
            AuthQuery authQuery = new AuthQuery();
            if (authQuery.verifyCredential(username.getText().toString(), password.getText().toString())) {
                Log.i(TAG, "User verified");
                /*
                Get signed in user's object ID and put it in shared preferences
                check if its already there in shared preferences, if not then put it there
                 */

                if (sharedPreferences.getString("ObjectId", "") == null || sharedPreferences.getString("ObjectId", "").equals("")) {
                    CandidateQuery candidateQuery = new CandidateQuery();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ObjectId", candidateQuery.getObjectId(username.getText().toString()));
                    editor.putString("username", username.getText().toString());
                    editor.putString("password", username.getText().toString());
                    editor.commit();
                }
                /*
                Try to open in same activity
                 */
                Intent intent = new Intent(getApplicationContext(), candidateHome.class);
                startActivity(intent);
            } else
                Toast.makeText(getApplicationContext(), "UserName or Password Incorrect", Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkAuthentication() {
        LISessionManager liSessionManager = LISessionManager.getInstance(getApplicationContext());
        LISession liSession = liSessionManager.getSession();
        return liSession.isValid();
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    public String getEmailaddress() {
        String topCardUrl = "https://api.linkedin.com/v1/people/~:(id,firstName,headline,positions,picture-url)?format=json";
        APIHelper apiHelper = null;
        //Log.i(TAG, "Context="+get.toString());
        if (getApplicationContext() != null) {
            Log.i(TAG, "------------");
            apiHelper = APIHelper.getInstance(getApplicationContext());
            apiHelper.getRequest(getApplicationContext(), topCardUrl, new ApiListener() {
                @Override
                public void onApiSuccess(ApiResponse apiResponse) {
                    Log.i(TAG, "API sucess");

                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                    Log.i(TAG, jsonObject.toString());
                    try {
                        email = jsonObject.getString("email-address");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onApiError(LIApiError LIApiError) {
                    Log.i(TAG, LIApiError.toString());
                }
            });
        }
        Log.i(TAG, email);
        return email;
    }
}
