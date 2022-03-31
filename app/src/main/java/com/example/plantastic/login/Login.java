package com.example.plantastic.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.plantastic.MonthlyView;
import com.example.plantastic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {


   private static final String FILE_NAME = "myFile";
   private Button register, login, forgot;
   private TextInputLayout email, password;
   private TextInputEditText emailEdit, passwordEdit;
   private CheckBox rememberMe;

   private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        register = (Button) findViewById(R.id.register1);
        login = (Button) findViewById(R.id.login1);
        email = (TextInputLayout) findViewById(R.id.email1);
        password = (TextInputLayout) findViewById(R.id.password3);
        forgot = (Button) findViewById(R.id.forgotBTN);
        rememberMe = (CheckBox) findViewById(R.id.rememberCheck);
        this.emailEdit = (TextInputEditText) findViewById(R.id.emailEdit);
        this.passwordEdit = (TextInputEditText) findViewById(R.id.passwordEdit);
        mAuth = FirebaseAuth.getInstance();

        setLoginInfo();

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

    //This function checks whether you've ever checked the 'Remember me' checkbox while logging in
    //and retrieves this information the next time you login.
    private void setLoginInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String emailShared = sharedPreferences.getString("email", "");
        String passwordShared = sharedPreferences.getString("password", "");

        emailEdit.setText(emailShared);
        passwordEdit.setText(passwordShared);

    }

    //Opens the activity where you can request a new password
    private void openResetPassword() {
        Intent resetPasswordIntent = new Intent(Login.this, forgotpassword.class);
        startActivity(resetPasswordIntent);
    }

    //Opens the activity where you can register
    void openRegister() {
        Intent registerIntent = new Intent(Login.this, Register.class);
        startActivity(registerIntent);
    }

    //The function checks  the email you entered in the textbox. It checks if:
    //* The textbox is not empty
    //* The textbox has an email that follows a valid e-mail pattern.
    //It returns true if it follows both rules.
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

    //The function checks the password you entered in the textbox. It checks if:
    //* It is not empty
    //The function returns true if you've entered a password.
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

    //Calls two methods to validate login information before proceeding to login.
    public void loginUser(){
        //Validate Login Info
        if (!validateEmail() | !validatePassword()){
            return;
        }
        else{
            doesUserExist();
        }

    }

    //Checks the Firebase authentication database and validates whether the email and password
    //entered where valid, and it opens the calendar activity if it was. Otherwise it will
    //notify you what went wrong.
    private void doesUserExist() {

        String emailEntered = email.getEditText().getText().toString().trim();
        String passwordEntered = password.getEditText().getText().toString().trim();

        if (rememberMe.isChecked()){
            StoredDataUsingSharedPref(emailEntered, passwordEntered);
        }

        mAuth.signInWithEmailAndPassword(emailEntered, passwordEntered).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()){
                        Intent intent = new Intent(Login.this, MonthlyView.class);
                        startActivity(intent);
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(Login.this, "Failed to Login", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //Stores the login information (Email, password), so that the user doesn't have to full that in
    //the next time they try to login.
    private void StoredDataUsingSharedPref(String email, String password) {

        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    //Overrides the ability of pressing the back bottom of the phone when on the login screen,
    //since it would otherwise return to the splashscreen.
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}