package org.tensorflow.lite.examples.detection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InicioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView tv_displayName_user,tv_precio_centro_abastos,tv_precio_mercado_centro,tv_precio_san_gil,tv_precio_socorro,tv_sin_internet,tv_sin_registro;
    ProgressBar pb_centro_abastos,pb_mercados_centro,pb_san_gil,pb_socorro,pb_ultimo_registro;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DateFormat formateadorFechaMedia = DateFormat.getDateInstance(DateFormat.MEDIUM);
    DecimalFormat decimalFormat = new DecimalFormat("#,###,###");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        tv_sin_registro = view.findViewById(R.id.tv_sin_registro);
        tv_precio_centro_abastos = view.findViewById(R.id.tv_precio_centro_abastos);
        tv_precio_mercado_centro = view.findViewById(R.id.tv_precio_mercado_centro);
        tv_precio_san_gil = view.findViewById(R.id.tv_precio_san_gil);
        tv_precio_socorro = view.findViewById(R.id.tv_precio_socorro);
        tv_sin_internet = view.findViewById(R.id.tv_sin_internet);
        pb_centro_abastos = view.findViewById(R.id.pb_centro_abastos);
        pb_mercados_centro = view.findViewById(R.id.pb_mercados_centro);
        pb_san_gil = view.findViewById(R.id.pb_san_gil);
        pb_socorro = view.findViewById(R.id.pb_socorro);
        pb_ultimo_registro = view.findViewById(R.id.pb_ultimo_registro);

        tv_precio_centro_abastos.setVisibility(View.GONE);
        tv_precio_mercado_centro.setVisibility(View.GONE);
        tv_precio_san_gil.setVisibility(View.GONE);
        tv_precio_socorro.setVisibility(View.GONE);
        tv_sin_internet.setVisibility(View.GONE);
        tv_sin_registro.setVisibility(View.GONE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tv_displayName_user = view.findViewById(R.id.tv_displayName_user);
            tv_displayName_user.setText(user.getDisplayName());
            socorro();
            san_gil();
            centro_abastos();
            mercados_centro();
            ultimo_registro();
        }

        return view;
    }

    public void socorro(){
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","Socorro (Santander)").orderBy("Date", Query.Direction.ASCENDING).limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Timestamp date = (Timestamp) document.getData().get("Date");
                        pb_socorro.setVisibility(View.GONE);
                        tv_precio_socorro.setText("$ " + decimalFormat.format(Integer.parseInt(document.getData().get("PROM_DIARIO").toString())) + " COP (" + formateadorFechaMedia.format(date.toDate()) + ")");
                        tv_precio_socorro.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void san_gil(){
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","San Gil (Santander)").orderBy("Date", Query.Direction.ASCENDING).limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Timestamp date = (Timestamp) document.getData().get("Date");
                        pb_san_gil.setVisibility(View.GONE);
                        tv_precio_san_gil.setText("$ " + decimalFormat.format(Integer.parseInt(document.getData().get("PROM_DIARIO").toString())) + " COP (" + formateadorFechaMedia.format(date.toDate()) + ")");
                        tv_precio_san_gil.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void centro_abastos(){
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","Bucaramanga, Centroabastos").orderBy("Date", Query.Direction.ASCENDING).limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Timestamp date = (Timestamp) document.getData().get("Date");
                        pb_centro_abastos.setVisibility(View.GONE);
                        tv_precio_centro_abastos.setText("$ " + decimalFormat.format(Integer.parseInt(document.getData().get("PROM_DIARIO").toString())) + " COP (" + formateadorFechaMedia.format(date.toDate()) + ")");
                        tv_precio_centro_abastos.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void mercados_centro(){
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","Bucaramanga, Mercados del centro").orderBy("Date", Query.Direction.ASCENDING).limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Timestamp date = (Timestamp) document.getData().get("Date");
                        pb_mercados_centro.setVisibility(View.GONE);
                        tv_precio_mercado_centro.setText("$ " + decimalFormat.format(Integer.parseInt(document.getData().get("PROM_DIARIO").toString())) + " COP (" + formateadorFechaMedia.format(date.toDate()) + ")");
                        tv_precio_mercado_centro.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void ultimo_registro(){
        db.collection("registros").whereEqualTo("user",user.getUid()).orderBy("fecha").limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    pb_ultimo_registro.setVisibility(View.GONE);
                    tv_sin_registro.setVisibility(View.VISIBLE);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(user.getUid().equals(document.getId()) == false){
                            Timestamp fecha = (Timestamp) document.getData().get("fecha");
                            Long estimado_centro_abastos = (Long) document.getData().get("estimado_centro_abastos");
                            Long estimado_marcados_centro = (Long) document.getData().get("estimado_marcados_centro");
                            Long estimado_san_gil = (Long) document.getData().get("estimado_san_gil");
                            Long estimado_socorro = (Long) document.getData().get("estimado_socorro");
                            Long prom = (estimado_centro_abastos + estimado_marcados_centro + estimado_san_gil + estimado_socorro)/4;
                            tv_sin_registro.setText("Fue el " + formateadorFechaMedia.format(fecha.toDate()) + ", se detectó una cantidad de " + document.getData().get("cantidad") + " bultos. Y el promedio calculado fue de: $ " + decimalFormat.format(prom) + " COP");
                        }
                    }
                }else{
                    Toast.makeText(getContext(), "Error getting documents. (RegistroFragment) " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}