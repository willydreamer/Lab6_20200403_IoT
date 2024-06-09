package com.example.lab6_20200403_iot;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class CerrarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStat) {
        View cerrarView = inflater.inflate(R.layout.activity_cerrar_fragment, container, false);


        // Configurar el botón de cerrar sesión
        AppCompatButton buttonCerrarSesion = cerrarView.findViewById(R.id.buttonCerrarSesion);
        buttonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar sesión
                FirebaseAuth.getInstance().signOut();

                // Redirigir a LoginFragment (que es una actividad)
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return cerrarView;
    }
}