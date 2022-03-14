package com.example.plantastic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    TextInputLayout username, firstname, lastname, email, password1, password2;
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
        username = findViewById(R.id.userName);

        registerBtn = findViewById(R.id.registerBtn);
        forgotBtn = findViewById(R.id.forgot);
        loginBtn = findViewById(R.id.login);
        
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private Boolean checkFirstName(){
        String val = firstname.getEditText().getText().toString();
        String whiteSpace = "(?=\\s+$)";
        if (val.isEmpty()){
            firstname.setError("Field can't be empty");
            return false;
        }
//        else if (!val.matches(whiteSpace)){
//            firstname.setError("No white spaces allowed");
//            return false;
//        }
        else{
            firstname.setError(null);
            firstname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean checkLastName(){
        String val = lastname.getEditText().getText().toString();

        if (val.isEmpty()){
            lastname.setError("Field can't be empty");
            return false;
        }
        else{
            lastname.setError(null);
            lastname.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean checkEmail(){

        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String whiteSpace = "(?=\\s+$)";
        if (val.isEmpty()){
            email.setError("Field can't be empty");
            return false;
        }
//        else if (!val.matches(whiteSpace)){
//            email.setError("No white spaces allowed");
//            return false;
//        }
//        else if (!val.matches(emailPattern)){
//            email.setError("Invalid email");
//            return false;
//        }
        else{
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean checkPassword(){

        String val1 = password1.getEditText().getText().toString();
        String val2 = password2.getEditText().getText().toString();
        String passwordCheck = "^" + "?=.*[a-zA-Z]" +  "$";

        if (!val2.equals(val1)){
            password1.setError("Password are not equal");
            password2.setError("Password are not equal");
            return false;
        }
        else if (val1.isEmpty()){
            password1.setError("Field can't be empty");
            return false;
        }
        else if (val2.isEmpty()){
            password2.setError("Field can't be empty");
            return false;
        }
        else if (val1.length() < 5){
            password1.setError("Password needs a length of at least 6");
            return false;
        }
//        else if(!val1.matches(passwordCheck)){
//            password1.setError("At least one special character");
//            return false;
//        }
        else{
            password1.setError(null);
            password2.setError(null);
            password1.setErrorEnabled(false);
            password2.setErrorEnabled(false);
            return true;
        }

    }


    private void register() {

        if (!checkFirstName() | !checkLastName() | !checkEmail() | !checkPassword()){
            return;
        }

        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("users");

        String usernameString = username.getEditText().getText().toString();
        String firstnameString = firstname.getEditText().getText().toString();
        String lastnameString = lastname.getEditText().getText().toString();
        String emailString = email.getEditText().getText().toString();
        String password1String = password1.getEditText().getText().toString();
        String password2String = password2.getEditText().getText().toString();

        registerHelperClass helperClass = new registerHelperClass(usernameString, firstnameString, lastnameString, emailString, password1String);

        databaseReference.child(usernameString).setValue(helperClass);

        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);

    }
}