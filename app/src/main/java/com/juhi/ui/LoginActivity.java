package com.juhi.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.juhi.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView forgotPassword;
    TextView signUp;
    Button login;
    TextInputEditText inputMail;
    TextInputEditText inputPassword;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";
    TextView userType;
    boolean isBeauticain;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        forgotPassword = findViewById(R.id.Login_forgot_password);
        signUp = findViewById(R.id.Login_signup);
        login = findViewById(R.id.Login_login);
        inputMail = findViewById(R.id.Login_input_mail);
        userType=findViewById(R.id.Login_usertype);
        inputPassword = findViewById(R.id.Login_input_password);
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
        userType.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        sharedPreferences=getSharedPreferences("beautycart",MODE_PRIVATE);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.Login_login:
                boolean isAllDetailsOk = validateInput();
                if (!isAllDetailsOk)
                    Toast.makeText(this, "Username or password is empty", Toast.LENGTH_SHORT).show();

                if(!emailValidator(inputMail.getText().toString()))
                {
                    Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    login();
                }

                break;
            case R.id.Login_signup:
                startActivity(new Intent(this, SignUpActivity.class));

                break;
            case R.id.Login_forgot_password:
                startActivity(new Intent(this,ForgotPasswordActivity.class));

                break;
            case R.id.Login_usertype:
                if(!isBeauticain)
                {
                    isBeauticain=true;
                    userType.setText("User login");
                }
                else
                {
                    isBeauticain=false;
                    userType.setText("Beautician login");
                }
                break;


        }

    }
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void login() {
        String email = inputMail.getText().toString();
        String password = inputPassword.getText().toString();
        if (!email.isEmpty()
                && !password.isEmpty()
        ) {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sharedPreferences.edit().putBoolean("usertype",isBeauticain).apply();

                                // Sign in success, update UI with the signed-in user's information
                                startActivity(new Intent(LoginActivity.this,UserMainActivity.class));
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "username or password is not valid",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }


    }


    private void updateUI(FirebaseUser user) {
        startActivity(new Intent(this, UserMainActivity.class));
        finish();

    }

    private boolean validateInput() {
        if (!inputPassword.getText().toString().isEmpty()
                && !inputMail.getText().toString().isEmpty()) {

            return true;
        } else {

            return false;
        }
    }
}
