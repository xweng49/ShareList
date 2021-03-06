package com.task.sharelist.sharelist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class ShareList extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private Button loginBtn;
    private Button registerBtn;
    private EditText userName;
    private EditText userPassword;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_list);

        firebaseAuth = FirebaseAuth.getInstance();

        //if user already logged, go to profile page
        if(firebaseAuth.getCurrentUser() != null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        loginBtn = (Button)findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        userName = (EditText) findViewById(R.id.loginName);
        userPassword = (EditText) findViewById(R.id.loginPassword);
        progressDialog = new ProgressDialog(this);


        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    private void userLogin()
    {
        String email = userName.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        // check if field is empty
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        // display progress dialog otherwise
        progressDialog.setMessage("Logging in");
        progressDialog.show();

        final CharSequence title, message, ok;
        title = "Error";
        message = "Login Was Unsuccessful";
        ok = "OK";

        // logging in
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // successful login, go to profile page
                        progressDialog.dismiss();
                        if (task.isSuccessful())
                        {
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShareList.this);
                            builder.setMessage(title)
                                    .setTitle(message)
                                    .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked OK button
                                }
                            })
                                    .show();
                            AlertDialog dialog = builder.create();
                            userName.setText("");
                            userPassword.setText("");
                        }
                    }
                });

    }
    @Override
    public void onClick(View view)
    {
        if(view == loginBtn)
        {
            userLogin();
        }
        if(view == registerBtn)
        {
            finish();
            startActivity(new Intent(this, RegistrationActivity.class));
        }
    }



}
