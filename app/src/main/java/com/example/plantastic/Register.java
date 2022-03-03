package com.example.plantastic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    TextInputLayout firstname, lastname, email, password1, password2;
    Button registerBtn, forgotBtn, loginBtn;

    DatabaseReference databaseReference;
    FirebaseDatabase rootNode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        firstname = findViewById(R.id.firstName);
        lastname = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);

        registerBtn = findViewById(R.id.registerBtn);
        forgotBtn = findViewById(R.id.forgot);
        loginBtn = findViewById(R.id.login);
        
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                
                rootNode = FirebaseDatabase.getInstance();
                databaseReference = rootNode.getReference("users");
                
                String firstnameString = firstname.getEditText().getText().toString();
                String lastnameString = lastname.getEditText().getText().toString();
                String emailString = email.getEditText().getText().toString();
                String password1String = password1.getEditText().getText().toString();
                String password2String = password2.getEditText().getText().toString();

                registerHelperClass helperClass = new registerHelperClass(firstnameString,
                        lastnameString, emailString, password1String, password2String);

                databaseReference.push().setValue(helperClass);

            }
        });
    }
}