package com.example.plantastic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity {

   private Button register, login, forgot;
   private TextInputLayout email, password;

   private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        register = (Button) findViewById(R.id.register1);
        login = (Button) findViewById(R.id.login1);
        email = (TextInputLayout) findViewById(R.id.email1);
        password = (TextInputLayout) findViewById(R.id.password3);
        forgot = (Button) findViewById(R.id.forgotBTN);
        mAuth = FirebaseAuth.getInstance();

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openResetPassword(); }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });

    }

    private void openResetPassword() {
        Intent resetPasswordIntent = new Intent(SignUp.this, forgotpassword.class);
        startActivity(resetPasswordIntent);
    }

    void openRegister() {
        Intent registerIntent = new Intent(SignUp.this, Register.class);
        startActivity(registerIntent);
    }

    private Boolean validateEmail(){

        String val = email.getEditText().getText().toString();
        if (val.isEmpty()){
            email.setError("Field can't be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            email.setError("Enter a valid email");
            return false;
        }
        else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){

        String val = password.getEditText().getText().toString();
        if (val.isEmpty()){
            password.setError("Field can't be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(){
        //Validate Login Info
        if (!validateEmail() | !validatePassword()){
            return;
        }
        else{
            doesUserExist();
        }

    }

    private void doesUserExist() {

        String emailEntered = email.getEditText().getText().toString().trim();
        String passwordEntered = password.getEditText().getText().toString().trim();


        mAuth.signInWithEmailAndPassword(emailEntered, passwordEntered).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()){
                        Intent intent = new Intent(SignUp.this, MonthlyView.class);
                        startActivity(intent);
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(SignUp.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(SignUp.this, "Failed to Login", Toast.LENGTH_LONG).show();
                }

            }
        });

//        String usernameEntered = email.getEditText().getText().toString().trim();
//        String passwordEntered = password.getEditText().getText().toString().trim();
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
//
//        Query checkUserExist = reference.orderByChild("username").equalTo(usernameEntered);
//        checkUserExist.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    email.setError(null);
//                    email.setErrorEnabled(false);
//
//                    String passwordDB = snapshot.child(usernameEntered).child("password").getValue(String.class);
//                    if (passwordDB.equals(passwordEntered)){
//
//                        email.setError(null);
//                        email.setErrorEnabled(false);
//
//
//                        String firstnameDB = snapshot.child(usernameEntered).child("firstname").getValue(String.class);
//                        String lastnameDB = snapshot.child(usernameEntered).child("lastname").getValue(String.class);
//                        String emailDB = snapshot.child(usernameEntered).child("email").getValue(String.class);
//                        String usernameDB = snapshot.child(usernameEntered).child("username").getValue(String.class);
//
//                        //The class that will open when pressing log-in button
//                        Intent intent = new Intent(getApplicationContext(), MonthlyView.class);
//
//
//                        intent.putExtra("firstname", firstnameDB);
//                        intent.putExtra("lastname", lastnameDB);
//                        intent.putExtra("username", usernameDB);
//                        intent.putExtra("email", emailDB);
//                        intent.putExtra("password", passwordDB);
//
//                        startActivity(intent);
//
//
//
//                    }
//                    else{
//                        password.setError("Wrong Password");
//                        password.requestFocus();
//                    }
//                }
//                else{
//                    email.setError("No such username Exists");
//                    email.requestFocus();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}