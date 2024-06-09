package com.example.lab6_20200403_iot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab6_20200403_iot.Bean.Egreso;
import com.example.lab6_20200403_iot.databinding.ActivityInformacionEgresoBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class InformacionEgresoActivity extends AppCompatActivity {

    ActivityInformacionEgresoBinding binding;
    private FirebaseFirestore db;
    private Egreso egreso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformacionEgresoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        // Recibir egreso
        Intent intent = getIntent();
        egreso = (Egreso) intent.getSerializableExtra("Egreso");

        if (egreso != null) {
            binding.editMonto.setText(String.valueOf(egreso.getMonto()));
            binding.editDescripcion.setText(egreso.getDescripcion());
            binding.editFecha.setText(egreso.getFecha());
            binding.editEgreso.setText(egreso.getTitulo());
        }

        binding.btnEditar.setOnClickListener(v -> toggleEditMode(true));
        binding.btnGuardar.setOnClickListener(v -> {
            toggleEditMode(false);
            saveChanges();
        });

        binding.buttonEliminarEgreso.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(InformacionEgresoActivity.this);
            builder.setMessage("¿Desea eliminar definitivamente este Egreso?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Código para eliminar Egreso
                            db.collection("Egresos").document(egreso.getId()).delete()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Egreso eliminado", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error al eliminar Egreso", Toast.LENGTH_SHORT).show());
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
        egreso.setMonto(Double.parseDouble(binding.editMonto.getText().toString()));
        egreso.setDescripcion(binding.editDescripcion.getText().toString());
        egreso.setFecha(binding.editFecha.getText().toString());
        egreso.setTitulo(binding.editEgreso.getText().toString());

        db.collection("Egresos").document(egreso.getId()).set(egreso)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplicationContext(), "Cambios guardados", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error al guardar cambios", Toast.LENGTH_SHORT).show());
    }
}
