package com.duynn.uber.actitivy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duynn.uber.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameET, passwordET, phoneNumberET;
    private Button btnSignUp;
    private TextView goToLogin;
    private Switch userTypeSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        phoneNumberET = findViewById(R.id.phoneNumber);
        btnSignUp = findViewById(R.id.btnSignUp);
        goToLogin = findViewById(R.id.goToLogin);
        userTypeSwitch = findViewById(R.id.userTypeSwitch);

        btnSignUp.setOnClickListener(v -> {
            String userType="Rider";
            if (userTypeSwitch.isChecked()) {
                userType = "Driver";
            }
            String username = usernameET.getText().toString();
            String password = passwordET.getText().toString();
            String phoneNumber = phoneNumberET.getText().toString();

            if(username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()){
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }else{
                ParseUser user = new ParseUser();
                user.setUsername(usernameET.getText().toString());
                user.setPassword(passwordET.getText().toString());
                user.put("phoneNumber", phoneNumberET.getText().toString());
                user.put("userType", userType);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        goToLogin.setOnClickListener(v -> {
            finish();
        });
    }
}