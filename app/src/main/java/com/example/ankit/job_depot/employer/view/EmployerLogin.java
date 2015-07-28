package com.example.ankit.job_depot.employer.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankit.job_depot.employer.controller.EmployerHome;
import com.example.ankit.job_depot.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class EmployerLogin extends ActionBarActivity {

    Button btnLogin;
    EditText textBoxUserName, textBoxPassword;
    TextView signUpLink;
    public static String EMPLOYER_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "ftqZNLU8FZ8PPApaRGSZbW99xYERIqw0cWaNsKuh", "LQxbAOhhPdFDjiG3Gb1lQolW6fEgXCO94zadYO27");

        setContentView(R.layout.activity_employer_login);

        btnLogin = (Button) findViewById(R.id.buttonEmployerSignUp);
        textBoxPassword = (EditText) findViewById(R.id.textBoxPassword);
        textBoxUserName = (EditText) findViewById(R.id.textBoxEmployerName);
        signUpLink = (TextView) findViewById(R.id.signUpLink);

        signUpLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployerSignUp.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((textBoxUserName.getText().toString() == "") || textBoxPassword.getText().toString() == "") {
                    Toast.makeText(getApplicationContext(),
                            "UserName or Password Incorrect",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("EmployerCredentials");
                    query.whereEqualTo("EmployerName", textBoxUserName.getText().toString());

                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                Log.d("EmployerCredentials", "Retrieved  " + list.size() + " EmployerCredentials");
                                if(list.size() > 0) {
                                    ParseObject item = list.get(0);
                                    Log.d("item", item.toString());
                                    String Pass = (String) item.get("EmployerPassword");
                                    Log.d("Pass", Pass);
                                    if(Pass.equals(textBoxPassword.getText().toString())) {
                                        Log.d("Password Authenticated", "Access Allowed");
                                        Intent intent = new Intent(getApplicationContext(), EmployerHome.class);
                                        intent.putExtra(EMPLOYER_NAME, textBoxUserName.getText().toString());
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),
                                                "UserName or Password Incorrect",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Log.d("EmployerCredentials", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_employer_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
