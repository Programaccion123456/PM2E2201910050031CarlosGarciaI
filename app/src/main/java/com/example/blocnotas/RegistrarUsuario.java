package com.example.blocnotas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class RegistrarUsuario extends AppCompatActivity  {

    private TextView txtll;
    private EditText txtNombre, txttelefono1, txtlongitud12, txtlatitud12;
    private Button btnRegistrar, btnlistaver, btnfoto;

    private ImageView foto;
    private StorageReference storageReference;
    private ProgressDialog cargando;
    Bitmap thumb_bitmap = null;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    private LocationManager ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        foto = findViewById(R.id.img_foto);
        txtNombre = findViewById(R.id.txtnombre);
        txttelefono1 = findViewById(R.id.txttelefono1);
        txtlongitud12 = findViewById(R.id.txtlongitud1);
        txtlatitud12 = findViewById(R.id.txtlatitud1);
        btnlistaver = findViewById(R.id.btnlista1);
        txtll = findViewById(R.id.ll);


        btnfoto = findViewById(R.id.btnfoto);
        btnRegistrar = findViewById(R.id.btnguardar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("img_comprimidas");
        cargando = new ProgressDialog(this);




        btnlistaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrarUsuario.this, userlist.class));
            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(RegistrarUsuario.this);
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri imgaeuri = CropImage.getPickImageResultUri(this, data);

            //Recortar la IMG

            CropImage.activity(imgaeuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(640, 480)
                    .setAspectRatio(2, 1).start(RegistrarUsuario.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                File url = new File(resultUri.getPath());

                Picasso.with(this).load(url).into(foto);

                //comprimiendo imagen

                try {
                    thumb_bitmap = new Compressor(this).setMaxWidth(640).setMaxHeight(480).setQuality(90).compressToBitmap(url);
                } catch (IOException e) {

                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                //fin del compresor


                btnRegistrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!txtNombre.getText().toString().isEmpty() && !txttelefono1.getText().toString().isEmpty() && !txtlongitud12.getText().toString().isEmpty() && !txtlatitud12.getText().toString().isEmpty()) {

                        cargando.setTitle("Subiendo foto...");
                        cargando.setTitle("Espere por favor...");
                        cargando.show();

                        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                        StorageReference ref = storageReference.child(fecha);
                        UploadTask uploadTask = ref.putBytes(thumb_byte);

                        //subir imagen en storage....

                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw Objects.requireNonNull(task.getException());
                                }

                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {


                                Uri dowloaduri = task.getResult();


                                String name = txtNombre.getText().toString();
                                String telefono = txttelefono1.getText().toString();
                                String longitud = txtlongitud12.getText().toString();
                                String latitud = txtlatitud12.getText().toString();
                                String url = dowloaduri.toString();
                                String key = mDatabase.push().getKey();


                                    Usuarios user = new Usuarios(name, telefono, longitud, latitud, url, key);

                                    mDatabase.child("nuevos").child(key).setValue(user);

                                    Toast.makeText(RegistrarUsuario.this, "Datos exitosos", Toast.LENGTH_SHORT).show();



                                cargando.dismiss();

                                Toast.makeText(RegistrarUsuario.this, "Imagen cargada con exito", Toast.LENGTH_SHORT).show();


                            }
                        });

                        } else {
                            Toast.makeText(RegistrarUsuario.this, "Debe completar los campos", Toast.LENGTH_LONG).show();
                        }


                    }
                });




            }





        }




    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(RegistrarUsuario.this, RegistrarUsuario.class));
            finish();
        }

    }




    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setRegistrarUsuario(this);

        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

        txtlatitud12.setText("Localizacion agregada");
        txtlatitud12.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    //  mensaje2.setText("Mi direccion es: \n"
                    //          + DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {

        RegistrarUsuario registrarUsuario;

        public RegistrarUsuario getRegistrarUsuario() {
            return registrarUsuario;
        }

        public void setRegistrarUsuario(RegistrarUsuario registrarUsuario) {
            this.registrarUsuario = registrarUsuario;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();


            txtlongitud12.setText(String.valueOf(loc.getLatitude()));
            txtlatitud12.setText(String.valueOf(loc.getLongitude()));
            RegistrarUsuario.this.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            txtlatitud12.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            txtlatitud12.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }






}