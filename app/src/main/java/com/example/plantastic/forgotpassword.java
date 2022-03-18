package com.example.plantastic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {

    private TextInputLayout email;
    private Button sendEmail;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        email = (TextInputLayout) findViewById(R.id.emailForget);
        sendEmail = (Button) findViewById(R.id.resetBTN);

        auth = FirebaseAuth.getInstance();

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String emailEntered = email.getEditText().getText().toString().trim();

        if (emailEntered.isEmpty()){
            email.setError("Email Required");
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailEntered).matches()){
            email.setError("Enter a valid email");
            return;
        }

        auth.sendPasswordResetEmail(emailEntered).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(forgotpassword.this, passwordForgetSplash.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(forgotpassword.this, "Try again, something wrong happened", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}