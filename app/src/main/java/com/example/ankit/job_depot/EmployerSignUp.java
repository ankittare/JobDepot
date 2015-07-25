package com.example.ankit.job_depot;

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

import com.parse.Parse;
import com.parse.ParseObject;


public class EmployerSignUp extends ActionBarActivity {

    Button btnSignUp;
    EditText textBoxUserName, textBoxPassword, textBoxConfirmPassword, textBoxEmail;
    TextView signInLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ftqZNLU8FZ8PPApaRGSZbW99xYERIqw0cWaNsKuh", "LQxbAOhhPdFDjiG3Gb1lQolW6fEgXCO94zadYO27");


        setContentView(R.layout.activity_employer_sign_up);

        btnSignUp = (Button) findViewById(R.id.buttonEmployerSignUp);
        textBoxPassword = (EditText) findViewById(R.id.textBoxPassword);
        textBoxUserName = (EditText) findViewById(R.id.textBoxEmployerName);
        textBoxConfirmPassword = (EditText) findViewById(R.id.textBoxConfirmPassword);
        textBoxEmail = (EditText) findViewById(R.id.textBoxEmail);
        signInLink = (TextView) findViewById(R.id.signInLink);

        signInLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployerLogin.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(textBoxPassword.getText().toString(), " "+textBoxConfirmPassword.getText().toString());
                if ((textBoxUserName.getText().toString() == "") || textBoxPassword.getText().toString() == "" ||
                        textBoxEmail.getText().toString() == "" || textBoxConfirmPassword.getText().toString() == "") {
                    Toast.makeText(getApplicationContext(),
                            "All fields must be filled",
                            Toast.LENGTH_LONG).show();
                }


//                else if(textBoxPassword.getText().toString() != textBoxConfirmPassword.getText().toString()) {
//                    Toast.makeText(getApplicationContext(),
//                            "Password are not same",
//                            Toast.LENGTH_LONG).show();
//                    return;
//                }
                else {
                    ParseObject testObject = new ParseObject("EmployerCredentials");
                    testObject.put("EmployerName", textBoxUserName.getText().toString());
                    testObject.put("EmployerEmail", textBoxEmail.getText().toString());
                    testObject.put("EmployerPassword", textBoxPassword.getText().toString());
                    testObject.saveInBackground();

                    Intent intent = new Intent(getApplicationContext(), EmployerLogin.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_employer_sign_up, menu);
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
