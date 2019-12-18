package com.example.chatfirebase.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatfirebase.R;
import com.example.chatfirebase.ui.main.MainActivity;
import com.example.chatfirebase.ui.regid_user.RegisUserActivity;
import com.example.chatfirebase.ui.resetpassword.ResetPasswordActivity;
import com.example.chatfirebase.util.ActivityTransition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    private MaterialEditText login_email;
    private MaterialEditText login_password;
    private Button login_button_regis;
    private TextView text_login_register;
    private TextView forgot_password;
    //    firebase
    private FirebaseAuth firebaseAuth;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setView();

        setOnClick();

        setInit();
    }


    private void setView() {

        login_email = (MaterialEditText) findViewById(R.id.login_email);
        login_password = (MaterialEditText) findViewById(R.id.login_password);
        login_button_regis = (Button) findViewById(R.id.login_btn_register);
        text_login_register = (TextView) findViewById(R.id.text_login_register);
        forgot_password = (TextView) findViewById(R.id.forgot_password);

    }

    private void setOnClick() {

        login_button_regis.setOnClickListener(v -> {
            String text_email = login_email.getText().toString();
            String text_password = login_password.getText().toString();

            if (checkValidateFeild()) {
                getLogin(text_email, text_password);
            }

        });

        forgot_password.setOnClickListener(v -> startResetPasswordActivity());

        text_login_register.setOnClickListener(v -> startRegisterActivity());
    }

    private void setInit() {

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void getLogin(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ActivityTransition.GoLeft(LoginActivity.this);
        finish();
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisUserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityTransition.GoLeft(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    private void startResetPasswordActivity() {
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }

    private boolean checkValidateFeild() {

        if (login_email.getText().toString().trim().isEmpty()) {
            login_email.setFocusable(true);
            login_email.setError("กรุณากรอก Email");
            return false;
        } else if (login_password.getText().toString().trim().isEmpty()) {
            login_password.setFocusable(true);
            login_password.setError("กรุณากรอก Password");
            return false;
        } else {
            return true;
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "กดอีกครั้งเพื่อออกจากแอปพลิเคชัน", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

}
