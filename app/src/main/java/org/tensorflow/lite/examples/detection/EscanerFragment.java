package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EscanerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EscanerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EscanerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EscanerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EscanerFragment newInstance(String param1, String param2) {
        EscanerFragment fragment = new EscanerFragment();
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

    TextView tv_pregunta_1,tv_pregunta_2;
    Button btn_escaner_automático,btn_escaner_manual,btn_calcular;
    EditText et_bultos,et_peso;
    ProgressBar pb_estimado;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Map<String, Object> data = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_escaner, container, false);

        tv_pregunta_1 = view.findViewById(R.id.tv_pregunta_1);
        tv_pregunta_2 = view.findViewById(R.id.tv_pregunta_2);
        tv_pregunta_1.setVisibility(View.GONE);
        tv_pregunta_2.setVisibility(View.GONE);

        et_bultos = view.findViewById(R.id.et_bultos);
        et_peso = view.findViewById(R.id.et_peso);
        et_bultos.setVisibility(View.GONE);
        et_peso.setVisibility(View.GONE);

        pb_estimado = view.findViewById(R.id.pb_estimado);
        pb_estimado.setVisibility(View.GONE);

        btn_escaner_automático = view.findViewById(R.id.btn_escaner_automático);
        btn_escaner_automático.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),EscanerIActivity.class));
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        btn_calcular = view.findViewById(R.id.btn_calcular);
        btn_calcular.setVisibility(View.GONE);
        btn_calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb_estimado.setVisibility(View.VISIBLE);
                data.put("fecha",new Date());
                data.put("user",user.getUid());
                int bultos = Integer.parseInt(et_bultos.getText().toString());
                data.put("cantidad", bultos);
                int peso = Integer.parseInt(et_peso.getText().toString());
                calcular_mercados_centro(bultos,peso);
                calcular_centro_abastos(bultos,peso);
                calcular_san_gil(bultos,peso);
                calcular_socorro(bultos,peso);

            }
        });

        btn_escaner_manual = view.findViewById(R.id.btn_escaner_manual);
        btn_escaner_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_pregunta_1.setVisibility(View.VISIBLE);
                tv_pregunta_2.setVisibility(View.VISIBLE);
                et_bultos.setVisibility(View.VISIBLE);
                et_peso.setVisibility(View.VISIBLE);
                btn_calcular.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }


    int paso = 0;
    public void recibir_datos(String nombre, int total){
        if(paso <= 2){
            data.put(nombre,total);
            paso++;
        }else{
            data.put(nombre,total);
            db.collection("registros")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            pb_estimado.setVisibility(View.GONE);
                            Intent intent = new Intent(getContext(),ResultadoActivity.class);
                            intent.putExtra("cantidad",et_bultos.getText().toString());
                            intent.putExtra("estimado_centro",data.get("estimado_centro_abastos")+"");
                            intent.putExtra("estimado_mercado",data.get("estimado_marcados_centro")+"");
                            intent.putExtra("estimado_socorro",data.get("estimado_socorro")+"");
                            intent.putExtra("estimado_san_gil",data.get("estimado_san_gil")+"");
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void calcular_mercados_centro(int bultos, int peso){
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","Bucaramanga, Mercados del centro").orderBy("Date", Query.Direction.ASCENDING).limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        int precio_DANE = Integer.parseInt(document.getData().get("PROM_DIARIO").toString());
                        int total = bultos*peso*precio_DANE;
                        recibir_datos("estimado_marcados_centro", total);
                    }
                }
            }
        });
    }

    public void calcular_socorro(int bultos, int peso){
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","Socorro (Santander)").orderBy("Date", Query.Direction.ASCENDING).limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        int precio_DANE = Integer.parseInt(document.getData().get("PROM_DIARIO").toString());
                        int total = bultos*peso*precio_DANE;
                        recibir_datos("estimado_socorro", total);
                    }
                }
            }
        });
    }

    public void calcular_san_gil(int bultos, int peso){
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","San Gil (Santander)").orderBy("Date", Query.Direction.ASCENDING).limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        int precio_DANE = Integer.parseInt(document.getData().get("PROM_DIARIO").toString());
                        int total = bultos*peso*precio_DANE;
                        recibir_datos("estimado_san_gil", total);
                    }
                }
            }
        });
    }

    public void calcular_centro_abastos(int bultos, int peso){
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","Bucaramanga, Centroabastos").orderBy("Date", Query.Direction.ASCENDING).limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        int precio_DANE = Integer.parseInt(document.getData().get("PROM_DIARIO").toString());
                        int total = bultos*peso*precio_DANE;
                        recibir_datos("estimado_centro_abastos", total);
                    }
                }
            }
        });
    }

}