package com.example.lab6_20200403_iot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab6_20200403_iot.Bean.Ingreso;
import com.example.lab6_20200403_iot.databinding.ActivityInformacionIngresoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class InformacionIngresoActivity extends AppCompatActivity {

    ActivityInformacionIngresoBinding binding;
    private FirebaseFirestore db;
    private Ingreso ingreso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformacionIngresoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        // Recibir sitio
        Intent intent = getIntent();
        ingreso = (Ingreso) intent.getSerializableExtra("ingreso");

        if (ingreso != null) {
            binding.editMonto.setText(String.valueOf(ingreso.getMonto()));
            binding.editDescripcion.setText(ingreso.getDescripcion());
            binding.editFecha.setText(ingreso.getFecha());
            binding.editIngreso.setText(ingreso.getTitulo());
        }

        binding.btnEditar.setOnClickListener(v -> toggleEditMode(true));
        binding.btnGuardar.setOnClickListener(v -> {
            toggleEditMode(false);
            saveChanges();
        });

        binding.buttonEliminarIngreso.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(InformacionIngresoActivity.this);
            builder.setMessage("¿Desea eliminar definitivamente este ingreso?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Código para eliminar ingreso
                            db.collection("ingresos").document(ingreso.getId()).delete()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Ingreso eliminado", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error al eliminar ingreso", Toast.LENGTH_SHORT).show());
                                    finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void toggleEditMode(boolean enable) {
        binding.editDescripcion.setEnabled(enable);
        binding.editMonto.setEnabled(enable);
        binding.btnGuardar.setVisibility(enable ? View.VISIBLE : View.GONE);
        binding.btnEditar.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    private void saveChanges() {
        ingreso.setMonto(Double.parseDouble(binding.editMonto.getText().toString()));
        ingreso.setDescripcion(binding.editDescripcion.getText().toString());
        ingreso.setFecha(binding.editFecha.getText().toString());
        ingreso.setTitulo(binding.editIngreso.getText().toString());

        db.collection("ingresos").document(ingreso.getId()).set(ingreso)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplicationContext(), "Cambios guardados", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error al guardar cambios", Toast.LENGTH_SHORT).show());
    }
}
