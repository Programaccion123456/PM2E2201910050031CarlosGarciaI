package com.example.blocnotas;


import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.blocnotas.databinding.ActivityMapsBinding;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Usuarios user;

    private Double latitud1;
    private Double longitud1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        user = (Usuarios) getIntent().getExtras().getSerializable("itemDetalle");

        String latitud = user.getLatitud();
        String longitud = user.getLongitud();

        latitud1 = Double.parseDouble(latitud);
        longitud1=Double.parseDouble(longitud);


        Toast.makeText(this, "la_ " +latitud + longitud, Toast.LENGTH_SHORT).show();

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(longitud1,latitud1);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Ubicacion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}