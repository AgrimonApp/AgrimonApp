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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                Map<String, Object> data = new HashMap<>();
                data.put("cantidad", 5);
                data.put("estimado_centro_abastos", 1200);
                data.put("estimado_marcados_centro", 1500);
                data.put("estimado_san_gil", 1800);
                data.put("estimado_socorro", 1400);
                data.put("fecha",new Date());
                data.put("user",user.getUid());

                db.collection("registros")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(), "Guarda con el id " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error " + e, Toast.LENGTH_SHORT).show();
                            }
                        });
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
}