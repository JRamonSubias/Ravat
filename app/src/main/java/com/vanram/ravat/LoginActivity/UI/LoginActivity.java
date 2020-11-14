package com.vanram.ravat.LoginActivity.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vanram.ravat.MapClientActivity;
import com.vanram.ravat.MapDriverActivity;
import com.vanram.ravat.R;
import com.vanram.ravat.app.SharedPreferenceManager;

import static com.vanram.ravat.app.MyApp.getContext;

public class LoginActivity extends AppCompatActivity {
    private Button btnSingIn;
    private TextView tvSingUp;
    private TextInputLayout etUser, etPassword;
    private CheckBox chRemember;
    private String email, password;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        findId();

        tvSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectOpcion = SharedPreferenceManager.getSomeStringValue("user");
                if(selectOpcion.equals("client")){
                    SingUp singUp = new SingUp();
                    singUp.show(getSupportFragmentManager(),"SingUp");
                }else if(selectOpcion.equals("driver")){
                    SingUpDriver singUpDriver = new SingUpDriver();
                    singUpDriver.show(getSupportFragmentManager(), "SingUpDriver");
                }

            }
        });

        getStringSingIn();
        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInformation();
            }
        });

    }

    private void validateInformation() {
        etUser.setError(null);
        etUser.setError(null);

        if(email.isEmpty()){
            etUser.setError("Ingrese un Correo");
        }else if(password.isEmpty()){
            etPassword.setError("Ingrese Contraseña");
        }else{
            Authentication(email,password);
        }

    }

    private void Authentication(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                        String selectOpcion  = SharedPreferenceManager.getSomeStringValue("user");
                        if(selectOpcion.equals("client")){

                            Intent intent = new Intent(LoginActivity.this, MapClientActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else if(selectOpcion.equals("driver")){

                            Intent intent = new Intent(LoginActivity.this, MapDriverActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                }else{
                    Toast.makeText(getContext(), "Hubo un problema, Intentelo de nuevo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getStringSingIn() {
        email = etUser.getEditText().getText().toString();
        password = etPassword.getEditText().getText().toString();
    }

    private void findId() {
        btnSingIn = findViewById(R.id.SI_BtnSingIn);
        tvSingUp = findViewById(R.id.SI_TVSingIn);
        etUser = findViewById(R.id.SI_user);
        etPassword = findViewById(R.id.SI_password);
    }
}