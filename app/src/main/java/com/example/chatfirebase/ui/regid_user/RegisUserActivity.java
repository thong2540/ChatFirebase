package com.example.chatfirebase.ui.regid_user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatfirebase.ui.main.MainActivity;
import com.example.chatfirebase.R;
import com.example.chatfirebase.util.ActivityTransition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisUserActivity extends AppCompatActivity {

    private MaterialEditText regis_username;
    private MaterialEditText regis_email;
    private MaterialEditText regis_password;
    private Button button_regis;
    //    firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TextView toolbar;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis_user);

        setToolbar();

        setView();

        setOnClick();

        setInit();
    }

    private void setToolbar() {
        toolbar = (TextView) findViewById(R.id.toolbar);
        toolbar.setText("Register");
    }


    private void setView() {

        regis_username = (MaterialEditText) findViewById(R.id.username);
        regis_email = (MaterialEditText) findViewById(R.id.email);
        regis_password = (MaterialEditText) findViewById(R.id.password);
        button_regis = (Button) findViewById(R.id.btn_register);
    }

    private void setOnClick() {
        button_regis.setOnClickListener(v -> {
            String text_username = regis_username.getText().toString();
            String text_email = regis_email.getText().toString();
            String text_password = regis_password.getText().toString();

            if (checkValidateFeild()) {
                register(text_username, text_email, text_password);
            }

        });

    }

    private void setInit() {

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void register(final String username, String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imgURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startMainActivity();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisUserActivity.this, "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(RegisUserActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ActivityTransition.GoLeft(RegisUserActivity.this);
        finish();
    }

    private boolean checkValidateFeild() {
        if (regis_username.getText().toString().trim().isEmpty()) {
            regis_username.setFocusable(true);
            regis_username.setError("กรุณากรอก Username");
            return false;
        } else if (regis_email.getText().toString().trim().isEmpty()) {
            regis_email.setFocusable(true);
            regis_email.setError("กรุณากรอก Email");
            return false;
        } else if (regis_password.getText().toString().trim().isEmpty()) {
            regis_password.setFocusable(true);
            regis_password.setError("กรุณากรอก Password");
            return false;
        } else {
            return true;
        }
    }
}
