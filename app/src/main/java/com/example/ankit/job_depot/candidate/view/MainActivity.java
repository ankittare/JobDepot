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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.candidate.model.DAO.AuthQuery;
import com.example.ankit.job_depot.candidate.model.DAO.CandidateQuery;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.parse.Parse;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    public static final String WELCOME_MESSAGE = "com.example.ankit.job_depot.Welcome";
    private static final String TAG = "Main Activity";
    private Button Parsesignin, linkedInsignin;
    private TextView signup;
    private EditText username;
    private EditText password;
    private SharedPreferences sharedPreferences;

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
        Parsesignin = (Button) findViewById(R.id.signin);
        linkedInsignin = (Button) findViewById(R.id.linkedinsignin);
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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


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
                /*
                Check This
                 */
                checkAuthentication();
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

    public void linkedInAuth(View view) {
        final TextView textView = (TextView) findViewById(R.id.error_text);

        final Activity thisActivity = this;

        // Build the list of member required permissions
        List<String> scope = new ArrayList<>();
        scope.add("r_basicprofile");
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


    public void signIn() throws NullPointerException {
        /*
        For Testing purpose
        logging in with out authentication

        Intent intent = new Intent(getApplicationContext(), candidateHome.class);
                        startActivity(intent);
         */

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
                 */
                CandidateQuery candidateQuery = new CandidateQuery();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ObjectId", candidateQuery.getObjectId(username.getText().toString()));
                editor.commit();
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
}
