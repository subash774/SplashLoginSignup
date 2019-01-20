package com.md.splashloginsignup;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.md.splashloginsignup.databinding.ActivityLoginBinding;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;


public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    Button loginButton;
    Button facebookButton;
    EditText emailAddress, passwordFromForm;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);


        loginButton = (Button) findViewById(R.id.btn_login);
        facebookButton = (Button) findViewById(R.id.btn_login_fb);

        emailAddress = (EditText) findViewById(R.id.et_email_address);
        passwordFromForm = (EditText) findViewById(R.id.et_password);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(emailAddress.getText().toString(), passwordFromForm.getText().toString());
            }
        });


        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Sorry", Toast.LENGTH_LONG).show();
            }
        });
    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //TODO: Send user to the notes page;
    }


    public void signup(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }


    private void signIn(final String email, final String password) {

        if(email.length() == 0 || password.length() == 0){
            Toast.makeText(getBaseContext(), "Please enter credentials", Toast.LENGTH_LONG).show();
        } else {
            // [START sign_in_with_email]
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                // Check if the email is verified
                                Boolean currentUserVerified = user.isEmailVerified();

                                if(currentUserVerified){
                                    Toast.makeText(getBaseContext(), "Logged in", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getBaseContext(), "Please verify your email", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Log.w(TAG, email + " " + password);
                                Toast.makeText(getBaseContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // [END_EXCLUDE]
                        }
                    });
            // [END sign_in_with_email]
        }

    }


    @Override
    public void onBackPressed(){
        finish();
    }



}
