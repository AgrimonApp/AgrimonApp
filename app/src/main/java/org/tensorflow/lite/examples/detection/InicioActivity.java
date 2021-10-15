package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InicioActivity extends AppCompatActivity {

    InicioFragment inicioFragment = new InicioFragment();
    EscanerFragment escanerFragment = new EscanerFragment();
    RegistroFragment registroFragment = new RegistroFragment();
    PreciosFragment preciosFragment = new PreciosFragment();
    PerfilFragment perfilFragment = new PerfilFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(inicioFragment);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            CharSequence title = item.getTitle();
            if ("Inicio".equals(title)) {
                loadFragment(inicioFragment);
                return true;
            } else if ("Escanear".equals(title)) {
                loadFragment(escanerFragment);
                return true;
            } else if ("Registros".equals(title)) {
                loadFragment(registroFragment);
                return true;
            } else if ("Gráficos".equals(title)) {
                loadFragment(preciosFragment);
                return true;
            } else if ("Perfil".equals(title)) {
                loadFragment(perfilFragment);
                return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

}