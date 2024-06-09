package com.example.lab6_20200403_iot;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_20200403_iot.Adapter.IngresoAdapter;
import com.example.lab6_20200403_iot.Bean.Ingreso;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IngresosFragment extends Fragment {
    private RecyclerView recyclerViewIngresos;
    private FloatingActionButton fabAddIngreso;
    private List<Ingreso> ingresos;
    private IngresoAdapter adapter;
    private FirebaseFirestore db;
    private String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ingresos_fragment, container, false);

        recyclerViewIngresos = view.findViewById(R.id.recyclerViewIngresos);
        recyclerViewIngresos.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddIngreso = view.findViewById(R.id.fabAddIngreso);

        ingresos = new ArrayList<>();
        adapter = new IngresoAdapter(ingresos, getContext());
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String nombre = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Log.d("User Logueado", String.valueOf(userId) + " " + nombre );


        recyclerViewIngresos.setAdapter(adapter);

        fabAddIngreso.setOnClickListener(v -> showAddIngresoDialog());

        //obtenerIngresosDeFirestore();

        return view;
    }

//    private void obtenerIngresosDeFirestore() {
//        db.collection("ingresos")
//                .whereEqualTo("userId", userId)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if (!queryDocumentSnapshots.isEmpty()) {
//                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                            Ingreso ingreso = document.toObject(Ingreso.class);
//                            ingreso.setId(document.getId()); // Asegúrate de establecer el ID del documento
//                            ingresos.add(ingreso);
//                        }
//                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(getContext(), "Error al obtener los ingresos", Toast.LENGTH_SHORT).show();
//                });
//    }

    private void showAddIngresoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_ingreso, null);
        builder.setView(dialogView)
                .setTitle("Agregar Ingreso")
                .setPositiveButton("Guardar", (dialog, which) -> {
                    EditText tituloEditText = dialogView.findViewById(R.id.tituloEditText);
                    EditText montoEditText = dialogView.findViewById(R.id.montoEditText);
                    EditText descripcionEditText = dialogView.findViewById(R.id.descripcionEditText);
                    Button fechaButton = dialogView.findViewById(R.id.fechaButton);

                    String titulo = tituloEditText.getText().toString();
                    double monto = Double.parseDouble(montoEditText.getText().toString());
                    String descripcion = descripcionEditText.getText().toString();
                    String fecha = fechaButton.getText().toString();

                    Ingreso nuevoIngreso = new Ingreso(titulo, monto, descripcion, fecha, userId);
                    addIngreso(nuevoIngreso);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        Button fechaButton = dialogView.findViewById(R.id.fechaButton);
        fechaButton.setOnClickListener(v -> {
            // Obtener la fecha actual
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Crear y mostrar el DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, monthOfYear, dayOfMonth1) -> {
                // Actualizar el texto del Button con la fecha seleccionada
                String selectedDate = dayOfMonth1 + "/" + (monthOfYear + 1) + "/" + year1;
                fechaButton.setText(selectedDate);
            }, year, month, dayOfMonth);
            datePickerDialog.show();
        });

        builder.create().show();
    }

    private void addIngreso(Ingreso ingreso) {

        db.collection("ingresos")
                .add(ingreso)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test", "Sitio guardado exitosamente con ID: " + documentReference.getId());
                    Toast.makeText(getContext(), "Ingreso creado exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test", "Error al guardar el ingreso", e);
                });
    }

//    private void updateIngreso(Ingreso ingreso) {
//        db.collection("ingresos")
//                .document(ingreso.getId())
//                .set(ingreso)
//                .addOnSuccessListener(aVoid -> {
//                    adapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
//                    // Manejar errores
//                });
//    }

//    private void deleteIngreso(String id) {
//        db.collection("ingresos")
//                .document(id)
//                .delete()
//                .addOnSuccessListener(aVoid -> {
//                    for (Ingreso ingreso : ingresos) {
//                        if (ingreso.getId().equals(id)) {
//                            ingresos.remove(ingreso);
//                            break;
//                        }
//                    }
//                    adapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
//                    // Manejar errores
//                });
//    }
}