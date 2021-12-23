package com.example.blocnotas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
     ArrayList<Usuarios> list;
     ArrayList<Usuarios> listaOriginal;


    public MyAdapter(Context context, ArrayList<Usuarios> list ) {
        this.context = context;
        this.list = list;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(list);

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.iteam,parent, false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
        final Usuarios item = list.get(position);
        Usuarios user = list.get(position);

        holder.txtnombre.setText(user.getName1());
        holder.txttelefono.setText(user.getTelefono());
        holder.txtlatitud.setText(user.getLatitud());
        holder.txtlongitud.setText(user.getLongitud());
        Glide.with(holder.img1.getContext()).load(user.getUrl()).into(holder.img1);

        final Usuarios infoData = list.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.txttelefono.getContext());
                builder.setTitle("Maps");
                builder.setMessage("Deseas ir a la Ubicacion de"+ " \n" +user.getName1());
                builder.setPositiveButton("ir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(holder.itemView.getContext(),Maps.class);
                        intent.putExtra("itemDetalle",item);

                        holder.itemView.getContext().startActivity(intent);

                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.txttelefono.getContext(), "Cancelar", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();


            }
        });



        holder.btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus =DialogPlus.newDialog(holder.img1.getContext())
                        .setContentHolder(new ViewHolder(R.layout.activity_detalle))
                        .setExpanded(true,1500)
                        .create();



                View view =dialogPlus.getHolderView();

                EditText nombre13 = view.findViewById(R.id.txtnombre3);
                EditText telefono13 = view.findViewById(R.id.txttelefono3);
                EditText latitud13 = view.findViewById(R.id.txtlongitud3);
                EditText longitud13 = view.findViewById(R.id.txtlatitud3);
                EditText urlimg13 = view.findViewById(R.id.txturl);


                Button btnactualizar = view.findViewById(R.id.btnModificar);

                nombre13.setText(user.getName1());
                telefono13.setText(user.getTelefono());
                latitud13.setText(user.getLatitud());
                longitud13.setText(user.getLongitud());
                urlimg13.setText("img");

                dialogPlus.show();


                btnactualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("name1", nombre13.getText().toString());
                        map.put("telefono", telefono13.getText().toString());
                        map.put("latitud", latitud13.getText().toString());
                        map.put("longitud", longitud13.getText().toString());
                        map.put("url", urlimg13.getText().toString());
                        map.put("key", user.getKey().toString());


                        FirebaseDatabase.getInstance().getReference().child("nuevos").child(user.getKey())
                                .updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.txtnombre.getContext(), "actializada Correctamente", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();


                                        Intent intent = new Intent(holder.itemView.getContext(),userlist.class);
                                        holder.itemView.getContext().startActivity(intent);


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.txtnombre.getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                                Intent intent = new Intent(holder.itemView.getContext(),userlist.class);


                                holder.itemView.getContext().startActivity(intent);
                            }
                        });
                    }
                });

                notifyDataSetChanged();


            }
        });


        holder.eliminarlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.txttelefono.getContext());
                builder.setTitle("Estas Seguro(a)");
                builder.setMessage("Los datos no se podran recuperar");
                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String key1 = list.get(position).getKey();
                        FirebaseDatabase.getInstance().getReference().child("nuevos")
                                .child(key1).removeValue();

                        Intent intent = new Intent(holder.itemView.getContext(),userlist.class);


                        holder.itemView.getContext().startActivity(intent);

                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.txttelefono.getContext(), "Cancelar", Toast.LENGTH_SHORT).show();


                    }
                });
                builder.show();

            }
        });



    }


    public void filtrar(ArrayList<Usuarios> filtroUsuarios) {
        this.list = filtroUsuarios;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img1;
        TextView txtnombre,txttelefono,txtlongitud,txtlatitud;
        Button btnModificar,eliminarlist;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            eliminarlist = itemView.findViewById(R.id.eliminarlist);
            btnModificar= itemView.findViewById(R.id.btndetallelist);
            img1 = itemView.findViewById(R.id.img1);
            txtnombre = itemView.findViewById(R.id.txtnombrelist);
            txttelefono = itemView.findViewById(R.id.txttelefonolist);
            txtlongitud = itemView.findViewById(R.id.txtlongitudlist);
            txtlatitud = itemView.findViewById(R.id.txtlatitudlist);






        }
    }




}
