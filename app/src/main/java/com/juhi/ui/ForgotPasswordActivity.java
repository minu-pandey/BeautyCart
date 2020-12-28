package com.juhi.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.juhi.R;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText mailInput;
    Button requestPassword;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mailInput =findViewById(R.id.Forgot_email);
        requestPassword=findViewById(R.id.Forgot_requestpassword);
        requestPassword.setOnClickListener(this);
        auth=FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
       forgotPassword();
    }
    private void forgotPassword() {
        String mail=mailInput.getText().toString();
                if(!mail.isEmpty());
        {
            auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please check your mail", Toast.LENGTH_SHORT).show();
                    finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgotPasswordActivity.this, "E mail u have entered is not valid", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

}
