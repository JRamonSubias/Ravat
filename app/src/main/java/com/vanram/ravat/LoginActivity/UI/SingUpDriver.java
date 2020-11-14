package com.vanram.ravat.LoginActivity.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vanram.ravat.MapClientActivity;
import com.vanram.ravat.MapDriverActivity;
import com.vanram.ravat.R;
import com.vanram.ravat.app.MyApp;
import com.vanram.ravat.app.SharedPreferenceManager;

import java.util.HashMap;
import java.util.Map;

public class SingUpDriver  extends DialogFragment  {
    private Button btnSingUp;
    private TextInputLayout etUser, etEmai,etModelo,etPlaca, etPassword,etConfirmPassword;
    private View view;
    private String user,email,modelo,placa,password,confirmPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public SingUpDriver() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_sing_up_driver, container, false);
        FindId(view);
        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInformation();
            }
        });
        return view;
    }



    private void FindId(View view) {
        btnSingUp = view.findViewById(R.id.SUDriver_btnSingUpDriver);
        etUser = view.findViewById(R.id.SUDriver_name);
        etEmai = view.findViewById(R.id.SUDriver_email);
        etModelo = view.findViewById(R.id.SUDriver_CarModel);
        etPlaca = view.findViewById(R.id.SUDriver_placa);
        etPassword = view.findViewById(R.id.SUDriver_password);
        etConfirmPassword = view.findViewById(R.id.SUDriver_confirmPassword);
    }

    private void getStringSingUp() {
        user = etUser.getEditText().getText().toString().trim();
        email = etEmai.getEditText().getText().toString().trim();
        modelo = etModelo.getEditText().getText().toString().trim();
        placa = etPlaca.getEditText().getText().toString().trim();
        password = etPassword.getEditText().getText().toString().trim();
        confirmPassword = etConfirmPassword.getEditText().getText().toString().trim();

    }

    private void validateInformation() {
        getStringSingUp();

        etUser.setError(null);
        etEmai.setError(null);
        etPassword.setError(null);
        etConfirmPassword.setError(null);

        if(user.isEmpty()){
            etUser.setError("Ingrese nombre de usuario");
        }else if(password.isEmpty()){
            etPassword.setError("Ingrese Contraseña");
        }else if(password.length()<6){
            etPassword.setError("Ingrese una contraseña de minimo 6 caracteres");
        }else if(!password.equals(confirmPassword)){
            etConfirmPassword.setError("Las contraseñas no son iguales");
        }else{
            Authentication(user,email,modelo,placa,password);
        }

    }

    private void Authentication(final String user, final String email, final String modelo, final String placa, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = mAuth.getCurrentUser().getUid();
                    saveUser(id,user, email,modelo,placa,password);
                    Intent intent = new Intent(getActivity(), MapDriverActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(MyApp.getContext(), "Error, Intentelo mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveUser(String id,String user, String email,String modelo,String placa,String password) {
            Map<String,Object> mapDriver = new HashMap<>();
            mapDriver.put("user",user);
            mapDriver.put("email",email);
            mapDriver.put("modelo",modelo);
            mapDriver.put("placa",placa);
            mapDriver.put("password",password);

            mDatabase.child("Users").child("Driver").child(id).setValue(mapDriver).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> taskDriver) {
                    if(taskDriver.isSuccessful()){
                        Toast.makeText(MyApp.getContext(), "Registro Exitoso Driver", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(MyApp.getContext(), "Fallo la ingresarlo a la Database", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

}