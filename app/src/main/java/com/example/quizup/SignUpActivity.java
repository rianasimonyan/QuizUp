package com.example.quizup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, pass, confirmPass;
    private Button signUpB;
    private ImageView backB;
    private FirebaseAuth mAuth;
    private String emailStr, passStr, confirmPassStr, nameStr;
    private Dialog progressDialog;
    private TextView dialogText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.username);
        email = findViewById(R.id.emailID);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_pass);
        signUpB = findViewById(R.id.signupB);
        backB = findViewById(R.id.backB);

        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registering user ...");


        mAuth = FirebaseAuth.getInstance();

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                if(validate()) {
                    signupNewUser();
                }
            }
        });
    }

    private boolean validate()
    {
        nameStr = name.getText().toString().trim();
        passStr = pass.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        confirmPassStr = confirmPass.getText().toString().trim();

        if(nameStr.isEmpty())
        {
            name.setError("Enter Your Name");
            return false;
        }

        if(emailStr.isEmpty())
        {
            email.setError("Enter Email ID");
            return false;
        }

        if(passStr.isEmpty())
        {
            pass.setError("Enter Password");
            return false;
        }

        if(confirmPassStr.isEmpty())
        {
            confirmPass.setError("Enter Password");
            return false;
        }

        if(passStr.compareTo(confirmPassStr) != 0)
        {
            Toast.makeText(SignUpActivity.this,"Password and confirm password should be same!", Toast.LENGTH_SHORT);
            return false;
        }

        return true;
    }

    private void signupNewUser()
    {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                           Toast.makeText(SignUpActivity.this, "Successfull Sign Up", Toast.LENGTH_SHORT).show();
                           progressDialog.dismiss();
                           Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                           startActivity(intent);
                           SignUpActivity.this.finish();

                        } else {

                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}