package com.example.lab6_20200403_iot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_20200403_iot.Bean.Ingreso;
import com.example.lab6_20200403_iot.R;

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
        holder.bind(ingreso);
    }

    @Override
    public int getItemCount() {
        return ingresos.size();
    }

    class IngresoViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView, montoTextView, descripcionTextView, fechaTextView;

        IngresoViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            montoTextView = itemView.findViewById(R.id.montoTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
            fechaTextView = itemView.findViewById(R.id.fechaTextView);
        }

        void bind(Ingreso ingreso) {
            tituloTextView.setText(ingreso.getTitulo());
            montoTextView.setText(String.valueOf(ingreso.getMonto()));
            descripcionTextView.setText(ingreso.getDescripcion());
            fechaTextView.setText(ingreso.getFecha());
        }
    }
}
