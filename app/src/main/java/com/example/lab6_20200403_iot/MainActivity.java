package com.example.lab6_20200403_iot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lab6_20200403_iot.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        replaceFragment(new IngresosFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int menuItemId =  item.getItemId();

            if (menuItemId ==  R.id.ingresos) {
                replaceFragment(new IngresosFragment());
                return true;
            }
            else if (menuItemId ==  R.id.egresos) {
                replaceFragment(new EgresosFragment());
                return true;
            }
            else if (menuItemId ==  R.id.resumen) {
                replaceFragment(new ResumenFragment());
                return true;
            }
            else if (menuItemId ==  R.id.cerrar) {
                replaceFragment(new CerrarFragment());
                return true;
            }
            return true;
        });

    }
    private  void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }
}