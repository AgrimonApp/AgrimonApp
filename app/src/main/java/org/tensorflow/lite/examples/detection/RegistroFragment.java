package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroFragment newInstance(String param1, String param2) {
        RegistroFragment fragment = new RegistroFragment();
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

    ProgressBar pb_registros;
    Button btn_ir_google_sheet;

    RecyclerView rv_lista_registros;
    ArrayList<Registro> registroArrayList;
    AdaptadorRegistros adaptadorRegistros;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        pb_registros = view.findViewById(R.id.pb_registros);
        btn_ir_google_sheet = view.findViewById(R.id.btn_ir_google_sheet);

        rv_lista_registros = view.findViewById(R.id.rv_lista_registros);
        rv_lista_registros.setHasFixedSize(true);
        rv_lista_registros.setLayoutManager(new LinearLayoutManager(getContext()));

        registroArrayList = new ArrayList<Registro>();
        adaptadorRegistros = new AdaptadorRegistros(getContext(),registroArrayList);

        rv_lista_registros.setAdapter(adaptadorRegistros);

        add_registro();

        btn_ir_google_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb_registros.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(), "Actualizando datos...", Toast.LENGTH_SHORT).show();
                DocumentReference docRef = db.collection("users").document(user.getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                String url = "https://script.google.com/macros/s/AKfycbzjm5dDo2jCdRFuhEEx-FTz1bTxMmcU1wKaFhpxZ49CTV7DZRAO6Y-z0kOrAerRueif/exec?ss_id="+ document.getData().get("google_sheet_id") + "&user_uid="+user.getUid();
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    Boolean result = response.getBoolean("rta");
                                                    if(result == true){
                                                        Toast.makeText(getContext(), "¡Datos actualizados! Abriendo hoja de cálculo...", Toast.LENGTH_SHORT).show();
                                                        pb_registros.setVisibility(View.GONE);
                                                        Uri uri = Uri.parse("https://docs.google.com/spreadsheets/d/"+document.getData().get("google_sheet_id")+"/");
                                                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                                        startActivity(intent);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // TODO: Handle error
                                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                queue.add(jsonObjectRequest);
                            } else {
                                Toast.makeText(getContext(), "El usuario NO está", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        return view;
    }

    public void add_registro(){
        db.collection("registros").whereEqualTo("user",user.getUid()).orderBy("fecha", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    pb_registros.setVisibility(View.GONE);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(user.getUid().equals(document.getId()) == false){
                            registroArrayList.add(document.toObject(Registro.class));
                            adaptadorRegistros.notifyDataSetChanged();
                        }
                    }
                }else{
                    Toast.makeText(getContext(), "Error getting documents. (RegistroFragment) " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}