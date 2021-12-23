package com.example.blocnotas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.PrimitiveIterator;

import id.zelory.compressor.Compressor;

public class detalle extends AppCompatActivity {

    private Context context;
    private TextView txtll;
    private EditText txtNombre3, txttelefono3, txtlongitud3, txtlatitud3;
    private Button btnRegistrar,btnmapa;
    private ImageView foto;

    private  Usuarios user;
    private String img ="";


    private static int GALLERY_INTENT=1;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        foto = findViewById(R.id.img_foto);
        txtNombre3 = findViewById(R.id.txtnombre3);
        txttelefono3 = findViewById(R.id.txttelefono3);
        txtlongitud3 = findViewById(R.id.txtlongitud3);
        txtlatitud3 = findViewById(R.id.txtlatitud3);
        txtll = findViewById(R.id.ll);
        context = getApplicationContext();

        user = (Usuarios) getIntent().getExtras().getSerializable("itemDetalle");


        //Glide.with(this.context).load(user.getUrl()).into(holder.foto);
       // Glide.with (context) .load (user.getUrl()).into(foto);
        txtNombre3.setText(user.getName1());
        txttelefono3.setText(user.getTelefono());
        txtlongitud3.setText(user.getLongitud());
        txtlatitud3.setText(user.getLatitud());







    }






}