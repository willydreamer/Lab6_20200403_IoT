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

import com.example.lab6_20200403_iot.Bean.Egreso;
import com.example.lab6_20200403_iot.InformacionEgresoActivity;
import com.example.lab6_20200403_iot.R;

import java.util.List;

public class EgresoAdapter extends RecyclerView.Adapter<EgresoAdapter.EgresoViewHolder> {
    private List<Egreso> egresos;
    private Context context;

    public EgresoAdapter(List<Egreso> egresos, Context context) {
        this.egresos = egresos;
        this.context = context;
    }

    @NonNull
    @Override
    public EgresoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_egreso, parent, false);
        return new EgresoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EgresoViewHolder holder, int position) {
        Egreso egreso = egresos.get(position);
        holder.textTitulo.setText(egreso.getTitulo());
        String monto = "Monto: s/. " + String.valueOf(egreso.getMonto());
        holder.textMonto.setText(monto);
        holder.textDescripcion.setText(egreso.getDescripcion());
        String fecha = "Fecha: " + egreso.getFecha();
        holder.textFecha.setText(fecha);

        holder.flecha1.setOnClickListener(view -> {
            Intent intent = new Intent(context, InformacionEgresoActivity.class);
            intent.putExtra("Egreso", egreso);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return egresos.size();
    }

    public static class EgresoViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitulo, textMonto, textDescripcion, textFecha;
        public ImageButton flecha1;
        Egreso Egreso;


        public EgresoViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textMonto = itemView.findViewById(R.id.textMonto);
            textDescripcion = itemView.findViewById(R.id.textDescripcion);
            textFecha = itemView.findViewById(R.id.textFecha);
            flecha1 = itemView.findViewById(R.id.flecha1);
        }
    }
}

