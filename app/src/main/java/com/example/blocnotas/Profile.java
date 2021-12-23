package com.example.blocnotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    Button btnSignOut,btnver;
    private FirebaseAuth mAuth;

    private TextView txtnombre, txtcorreo;

    private DatabaseReference mDatebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        btnSignOut = findViewById(R.id.btnSignout);
        btnver = findViewById(R.id.btnlista);
        mDatebase = FirebaseDatabase.getInstance().getReference();

        txtnombre = findViewById(R.id.txtnombre);
        txtcorreo= findViewById(R.id.txtcorreo);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mAuth.signOut();
            startActivity(new Intent(Profile.this, RegistrarUsuario.class));
            finish();
            }
        });

        btnver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, userlist.class));
            }
        });


        getUserInfo();
    }


    private void getUserInfo(){

        //Nombre del nodo Users

        String id =mAuth.getCurrentUser().getUid();

        mDatebase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();

                    txtnombre.setText(name);
                    txtcorreo.setText(email);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}