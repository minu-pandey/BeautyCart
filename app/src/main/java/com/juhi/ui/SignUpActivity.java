package com.juhi.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.juhi.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignUpActivity";
    TextInputEditText mName;
    TextInputEditText mEmail;
    TextInputEditText mPassword;
    TextInputEditText mConfirmPassword;
    Button signUp;
    TextInputEditText[] fields;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mName = findViewById(R.id.SignUp_name);
        mEmail = findViewById(R.id.SignUp_email);
        mPassword = findViewById(R.id.SignUp_password);
        mConfirmPassword = findViewById(R.id.SignUp_confirmpassword);
        signUp = findViewById(R.id.SignUp_signup);
        signUp.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        fields = new TextInputEditText[]{mPassword, mName, mEmail, mConfirmPassword};
        boolean isAllFieldsOk = verifyDetails(fields);
        if(isAllFieldsOk)
        {
            Toast.makeText(this, "Check your fields", Toast.LENGTH_SHORT).show();
        }

        login();
    }
    private void login() {
        String email = mEmail.getText().toString();
        String password = mConfirmPassword.getText().toString();
        if (!email.isEmpty()
                && !password.isEmpty()
        ) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                              finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }



    }

    private boolean verifyDetails(TextInputEditText[] fields) {
        for (TextInputEditText textInputEditText : fields) {
            if (textInputEditText.getText().toString().isEmpty())
                return true;
        }
        if(!mConfirmPassword.getText().toString().equals(mPassword.getText().toString()))
        {
            return true;
        }
        return false;
    }


}
