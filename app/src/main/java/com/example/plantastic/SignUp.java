package com.example.plantastic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity {

   private Button register, login;
   private TextInputLayout username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        register = (Button) findViewById(R.id.register1);
        login = (Button) findViewById(R.id.login1);
        username = (TextInputLayout) findViewById(R.id.username1);
        password = (TextInputLayout) findViewById(R.id.password3);

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

    void openRegister() {
        Intent registerIntent = new Intent(SignUp.this, Register.class);
        startActivity(registerIntent);
    }

    private Boolean validateUsername(){

        String val = username.getEditText().getText().toString();
        if (val.isEmpty()){
            username.setError("Field can't be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
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
        if (!validateUsername() | !validatePassword()){
            return;
        }
        else{
            doesUserExist();
        }

    }

    private void doesUserExist() {

        String usernameEntered = username.getEditText().getText().toString().trim();
        String passwordEntered = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUserExist = reference.orderByChild("username").equalTo(usernameEntered);

        checkUserExist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordDB = snapshot.child(usernameEntered).child("password").getValue(String.class);
                    if (passwordDB.equals(passwordEntered)){

                        username.setError(null);
                        username.setErrorEnabled(false);


                        String firstnameDB = snapshot.child(usernameEntered).child("firstname").getValue(String.class);
                        String lastnameDB = snapshot.child(usernameEntered).child("lastname").getValue(String.class);
                        String emailDB = snapshot.child(usernameEntered).child("email").getValue(String.class);
                        String usernameDB = snapshot.child(usernameEntered).child("username").getValue(String.class);

                        //The class that will open when pressing log-in button
                        Intent intent = new Intent(getApplicationContext(), MonthlyView.class);


                        intent.putExtra("firstname", firstnameDB);
                        intent.putExtra("lastname", lastnameDB);
                        intent.putExtra("username", usernameDB);
                        intent.putExtra("email", emailDB);
                        intent.putExtra("password", passwordDB);

                        startActivity(intent);



                    }
                    else{
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }
                else{
                    username.setError("No such username Exists");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}