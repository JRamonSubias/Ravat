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
import com.vanram.ravat.R;
import com.vanram.ravat.app.MyApp;
import com.vanram.ravat.app.SharedPreferenceManager;

import java.util.HashMap;
import java.util.Map;

public class SingUp extends DialogFragment {
    private Button btnSingUp;
    private TextInputLayout etUser, etEmai, etPassword,etConfirmPassword;
    private View view;
    private String user,email,password,confirmPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public SingUp() {
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
        view = inflater.inflate(R.layout.fragment_sing_up, container, false);
        FindId(view);

        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInformation();
            }
        });

        return view;
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
            Authentication(user,email,password); 
        }
        
    }

    private void Authentication(final String user, final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = mAuth.getCurrentUser().getUid();
                    saveUser(id,user, email, password);
                } else {
                    Toast.makeText(MyApp.getContext(), "Error, Intentelo mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveUser(String id,String user, String email, String password) {
            Map<String,Object> mapClient = new HashMap<>();
            mapClient.put("user",user);
            mapClient.put("email",email);
            mapClient.put("password",password);

            mDatabase.child("Users").child("Client").child(id).setValue(mapClient).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> taskClient) {
                    if(taskClient.isSuccessful()){
                        Toast.makeText(MyApp.getContext(), "Registro Exitoso Client", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MapClientActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }else{
                        Toast.makeText(MyApp.getContext(), "Fallo la ingresarlo a la Database", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void getStringSingUp() {
        user = etUser.getEditText().getText().toString().trim();
        email = etEmai.getEditText().getText().toString().trim();
        password = etPassword.getEditText().getText().toString().trim();
        confirmPassword = etConfirmPassword.getEditText().getText().toString().trim();

    }

    private void FindId(View view) {
        btnSingUp = view.findViewById(R.id.SU_btnSingUp);
        etUser = view.findViewById(R.id.SU_name);
        etEmai = view.findViewById(R.id.SU_email);
        etPassword = view.findViewById(R.id.SU_password);
        etConfirmPassword = view.findViewById(R.id.SU_confirmPassword);

    }
}