package com.example.blocnotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class IniciarSesion extends AppCompatActivity {

     private EditText txtEmail, txtPassword;
     private Button btnentrar, btnresetpassword;

     private String email = "";
    private String password = "";

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        mAuth = FirebaseAuth.getInstance();


        txtEmail = findViewById(R.id.txtemail);
        txtPassword = findViewById(R.id.txtpassword);

        btnentrar = findViewById(R.id.btnEntrar1);


        btnentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();


                if (!email.isEmpty() && !password.isEmpty()){

                    loginUser();
                }
                else
                {
                    Toast.makeText(IniciarSesion.this,"Debe completar los campos", Toast.LENGTH_LONG).show();
                }


            }
        });




    }

    private void loginUser() {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(IniciarSesion.this, Profile.class));
                }
                else
                {
                    Toast.makeText(IniciarSesion.this, "No se pudo iniciar sesion, compruebe los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}