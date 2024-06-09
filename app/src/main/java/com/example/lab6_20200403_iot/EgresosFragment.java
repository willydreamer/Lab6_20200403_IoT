package com.example.lab6_20200403_iot;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_20200403_iot.Adapter.EgresoAdapter;
import com.example.lab6_20200403_iot.Bean.Egreso;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class EgresosFragment extends Fragment {
    private RecyclerView recyclerViewEgresos;
    private FloatingActionButton fabAddEgreso;
    private List<Egreso> egresos;
    private EgresoAdapter adapter;
    private FirebaseFirestore db;
    private String userId;
    private ImageButton btnReload;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_egresos_fragment, container, false);

        recyclerViewEgresos = view.findViewById(R.id.recyclerViewEgresos);
        recyclerViewEgresos.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddEgreso = view.findViewById(R.id.fabAddEgreso);

        egresos = new ArrayList<>();
        adapter = new EgresoAdapter(egresos, getContext());
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        TextView saludo = view.findViewById(R.id.saludo);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getDisplayName() != null) {
            String displayName = currentUser.getDisplayName();
            Log.d("name autenticado", "onCreateView: " + displayName);
            String greetingMessage = String.format("Hola! " + displayName);
            saludo.setText(greetingMessage);
        } else {
            saludo.setText("Hola :)");
        }

        recyclerViewEgresos.setAdapter(adapter);

        fabAddEgreso.setOnClickListener(v -> showAddEgresoDialog());

        btnReload = view.findViewById(R.id.btnReload);

        obtenerEgresosDeFirestore();

        return view;
    }

    private void obtenerEgresosDeFirestore() {
        db.collection("egresos")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Egreso egreso = document.toObject(Egreso.class);
                            egresos.add(egreso);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los egresos", Toast.LENGTH_SHORT).show();
                });
    }


    private void showAddEgresoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_egreso, null);
        builder.setView(dialogView)
                .setTitle("Agregar Egreso")
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

                    Egreso nuevoEgreso = new Egreso(idSitio, titulo, monto, descripcion, fecha, userId);
                    addEgreso(nuevoEgreso, idSitio);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        Button fechaButton = dialogView.findViewById(R.id.fechaButton);
        fechaButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, monthOfYear, dayOfMonth1) -> {
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
        int numeroAleatorio = random.nextInt(9000) + 1000;
        return letrasDepartamento + numeroAleatorio;
    }

    private void addEgreso(Egreso egreso, String idSitio) {
        db.collection("egresos")
                .document(idSitio)
                .set(egreso)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test", "Egreso guardado exitosamente");
                    Toast.makeText(getContext(), "Egreso creado exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test", "Error al guardar el egreso", e);
                });
    }

}