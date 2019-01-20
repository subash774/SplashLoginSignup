package com.md.splashloginsignup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.md.splashloginsignup.databinding.ActivitySignupBinding;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.util.ArrayList;


public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;

    EditText fullName, emailAddress, initialPassword, confirmedPassword;
    Button signupButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private static final String TAG = "EmailPassword";



    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //TODO: Send user to the notes page;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        signupButton = (Button) findViewById(R.id.btn_signup);
        fullName = (EditText) findViewById(R.id.et_full_name);
        emailAddress = (EditText) findViewById(R.id.et_email_address);
        initialPassword = (EditText) findViewById(R.id.et_password);
        confirmedPassword = (EditText) findViewById(R.id.et_confirm_password);


        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        mDatabase = FirebaseDatabase.getInstance().getReference();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!initialPassword.getText().toString().equals(confirmedPassword.getText().toString())){
                    Log.w(TAG,initialPassword + "     " + confirmedPassword);
                    Toast.makeText(getBaseContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                } else if (emailAddress.getText().toString().length() < 3 || initialPassword.getText().toString().length() < 6 || confirmedPassword.getText().toString().length() < 6) {
                    Toast.makeText(getBaseContext(), "Enter all credentials", Toast.LENGTH_LONG).show();
                } else {
                    createAccount(emailAddress.getText().toString(), confirmedPassword.getText().toString());
                }
            }
        });

    }


    private void createAccount(final String email, final String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Send verification link
                            sendEmailVerification();

                            // Add notes into to firebase
                            User newUser = new User(user.getUid(), fullName.getText().toString(), email, null);
                            writeNewUser(newUser);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END create_user_with_email]
    }


    private void sendEmailVerification() {

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        if (task.isSuccessful()) {
                            Toast.makeText(getBaseContext(),
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getBaseContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }


    // [START basic_write]
    private void writeNewUser(User user) {
        mDatabase.child("users").child(user.getUserId()).setValue(user);
    }
    // [END basic_write]


    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }


    @Override
    public void onBackPressed(){
        finish();
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
}
