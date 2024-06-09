package com.example.lab6_20200403_iot;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_20200403_iot.Adapter.IngresoAdapter;
import com.example.lab6_20200403_iot.Bean.Ingreso;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class IngresosFragment extends Fragment {
    private RecyclerView recyclerViewIngresos;
    private FloatingActionButton fabAddIngreso;
    private List<Ingreso> ingresos;
    private IngresoAdapter adapter;
    private FirebaseFirestore db;
    private String userId;
    private ImageButton btnReload;


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


        TextView saludo = view.findViewById(R.id.saludo);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getDisplayName() != null) {
            String displayName = currentUser.getDisplayName();
            String greetingMessage = String.format("Hola! ", userId);
            saludo.setText(greetingMessage);
        } else {
            saludo.setText("Hola :)");
        }

        recyclerViewIngresos.setAdapter(adapter);

        fabAddIngreso.setOnClickListener(v -> showAddIngresoDialog());

        btnReload = view.findViewById(R.id.btnReload);
        //btnReload.setOnClickListener(v -> reloadIngresos());

        obtenerIngresosDeFirestore();

        return view;
    }

    private void obtenerIngresosDeFirestore() {
        db.collection("ingresos")
                .whereEqualTo("userId", userId)  // Filtro para obtener solo los ingresos del usuario actual
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Ingreso ingreso = document.toObject(Ingreso.class);
                            ingresos.add(ingreso);
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los ingresos", Toast.LENGTH_SHORT).show();
                });
    }


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


                    String idSitio = generarIdSitio(titulo);

                    Ingreso nuevoIngreso = new Ingreso(idSitio, titulo, monto, descripcion, fecha, userId);
                    addIngreso(nuevoIngreso, idSitio);
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
    private String generarIdSitio(String titulo) {
        String letrasDepartamento = titulo.substring(0, Math.min(titulo.length(), 2)).toUpperCase();
        Random random = new Random();
        int numeroAleatorio = random.nextInt(9000) + 1000; // Generar un número entre 100 y 999
        return letrasDepartamento + numeroAleatorio;
    }

    private void addIngreso(Ingreso ingreso, String idSitio) {
        db.collection("ingresos")
                .document(idSitio)
                .set(ingreso)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test", "Ingreso guardado exitosamente");
                    Toast.makeText(getContext(), "Ingreso creado exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test", "Error al guardar el ingreso", e);
                });
    }

}