package com.example.lab6_20200403_iot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_20200403_iot.Bean.Ingreso;
import com.example.lab6_20200403_iot.InformacionIngresoActivity;
import com.example.lab6_20200403_iot.R;

import java.io.Serializable;
import java.util.List;

public class IngresoAdapter extends RecyclerView.Adapter<IngresoAdapter.IngresoViewHolder> {
    private List<Ingreso> ingresos;
    private Context context;

    public IngresoAdapter(List<Ingreso> ingresos, Context context) {
        this.ingresos = ingresos;
        this.context = context;
    }

    @NonNull
    @Override
    public IngresoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingreso, parent, false);
        return new IngresoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngresoViewHolder holder, int position) {
        Ingreso ingreso = ingresos.get(position);
        holder.textTitulo.setText(ingreso.getTitulo());
        String monto = "Monto: s/. " + String.valueOf(ingreso.getMonto());
        holder.textMonto.setText(monto);
        holder.textDescripcion.setText(ingreso.getDescripcion());
        String fecha = "Fecha: " + ingreso.getFecha();
        holder.textFecha.setText(fecha);

        holder.flecha1.setOnClickListener(view -> {
            Intent intent = new Intent(context, InformacionIngresoActivity.class);
            intent.putExtra("ingreso", ingreso);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ingresos.size();
    }

    public static class IngresoViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitulo, textMonto, textDescripcion, textFecha;
        public ImageButton flecha1;
        Ingreso ingreso;


        public IngresoViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textMonto = itemView.findViewById(R.id.textMonto);
            textDescripcion = itemView.findViewById(R.id.textDescripcion);
            textFecha = itemView.findViewById(R.id.textFecha);
            flecha1 = itemView.findViewById(R.id.flecha1);
        }
    }
}

