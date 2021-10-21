package org.tensorflow.lite.examples.detection;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreciosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreciosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PreciosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreciosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreciosFragment newInstance(String param1, String param2) {
        PreciosFragment fragment = new PreciosFragment();
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

    Button btn_opcion_1, btn_opcion_2, btn_opcion_3;

    LineChartView ch_socorro;
    LineChartView ch_san_gil;
    LineChartView ch_mercados_centro;
    LineChartView ch_centro_abasto;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DateFormat formateadorFechaMedia = DateFormat.getDateInstance(DateFormat.MEDIUM);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_precios, container, false);

        ch_socorro = view.findViewById(R.id.ch_socorro);
        ch_san_gil = view.findViewById(R.id.ch_san_gil);
        ch_centro_abasto = view.findViewById(R.id.ch_centro_abastos);
        ch_mercados_centro = view.findViewById(R.id.ch_mercado_centro);

        btn_opcion_1 = view.findViewById(R.id.btn_opcion_1);
        btn_opcion_1.setTextColor(Color.parseColor("#FFFFFFFF"));
        btn_opcion_1.setBackgroundColor(Color.parseColor("#1274FF"));
        btn_opcion_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_opcion_1.setTextColor(Color.parseColor("#FFFFFFFF"));
                btn_opcion_1.setBackgroundColor(Color.parseColor("#1274FF"));
                btn_opcion_2.setTextColor(Color.parseColor("#FF000000"));
                btn_opcion_2.setBackgroundColor(Color.parseColor("#C9EDFF"));
                btn_opcion_3.setTextColor(Color.parseColor("#FF000000"));
                btn_opcion_3.setBackgroundColor(Color.parseColor("#C9EDFF"));
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH,-3);
                grafico_san_gil(c);
                grafico_socorro(c);
                grafico_centro_abastos(c);
                grafico_mercados_centro(c);
            }
        });

        btn_opcion_2 = view.findViewById(R.id.btn_opcion_2);
        btn_opcion_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_opcion_1.setTextColor(Color.parseColor("#FF000000"));
                btn_opcion_1.setBackgroundColor(Color.parseColor("#C9EDFF"));
                btn_opcion_2.setTextColor(Color.parseColor("#FFFFFFFF"));
                btn_opcion_2.setBackgroundColor(Color.parseColor("#1274FF"));
                btn_opcion_3.setTextColor(Color.parseColor("#FF000000"));
                btn_opcion_3.setBackgroundColor(Color.parseColor("#C9EDFF"));
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH,-6);
                grafico_san_gil(c);
                grafico_socorro(c);
                grafico_centro_abastos(c);
                grafico_mercados_centro(c);
            }
        });

        btn_opcion_3 = view.findViewById(R.id.btn_opcion_3);
        btn_opcion_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_opcion_1.setTextColor(Color.parseColor("#FF000000"));
                btn_opcion_1.setBackgroundColor(Color.parseColor("#C9EDFF"));
                btn_opcion_2.setTextColor(Color.parseColor("#FF000000"));
                btn_opcion_2.setBackgroundColor(Color.parseColor("#C9EDFF"));
                btn_opcion_3.setTextColor(Color.parseColor("#FFFFFFFF"));
                btn_opcion_3.setBackgroundColor(Color.parseColor("#1274FF"));
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH,-12);
                grafico_san_gil(c);
                grafico_socorro(c);
                grafico_centro_abastos(c);
                grafico_mercados_centro(c);
            }
        });

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH,-3);
        grafico_san_gil(c);
        grafico_socorro(c);
        grafico_centro_abastos(c);
        grafico_mercados_centro(c);

        return view;
    }

    public void grafico_socorro(Calendar c){
        Timestamp timestamp_user = new Timestamp(c.getTime());
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","Socorro (Santander)").whereGreaterThanOrEqualTo("Date",timestamp_user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<PointValue> puntos_socorro = new ArrayList<PointValue>();
                    int contador = 0;
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Timestamp timestamp_bd = (Timestamp) document.getData().get("Date");
                        puntos_socorro.add(new PointValue(contador,Integer.parseInt(document.getData().get("PROM_DIARIO").toString())).setLabel("Fecha: " + formateadorFechaMedia.format(timestamp_bd.toDate()) + " Valor: " + document.getData().get("PROM_DIARIO").toString()));
                        contador++;
                    }
                    Line line = new Line(puntos_socorro).setColor(Color.RED).setCubic(true).setHasLabelsOnlyForSelected(true).setPointColor(Color.RED).setPointRadius(7);
                    List<Line> lines = new ArrayList<Line>();
                    lines.add(line);
                    LineChartData data = new LineChartData();
                    data.setValueLabelTextSize(17);
                    data.setLines(lines);
                    ch_socorro.setLineChartData(data);
                }
            }
        });
    }

    public void grafico_san_gil(Calendar c){
        Timestamp timestamp_user = new Timestamp(c.getTime());
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","San Gil (Santander)").whereGreaterThanOrEqualTo("Date",timestamp_user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<PointValue> puntos_san_gil = new ArrayList<PointValue>();
                    int contador = 0;
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Timestamp timestamp_bd = (Timestamp) document.getData().get("Date");
                        puntos_san_gil.add(new PointValue(contador,Integer.parseInt(document.getData().get("PROM_DIARIO").toString())).setLabel("Fecha: " + formateadorFechaMedia.format(timestamp_bd.toDate()) + " Valor: " + document.getData().get("PROM_DIARIO").toString()));
                        contador++;
                    }
                    Line line = new Line(puntos_san_gil).setColor(Color.parseColor("#FFA500")).setCubic(true).setHasLabelsOnlyForSelected(true).setPointColor(Color.parseColor("#FFA500")).setPointRadius(7);
                    List<Line> lines = new ArrayList<Line>();
                    lines.add(line);
                    LineChartData data = new LineChartData();
                    data.setValueLabelTextSize(17);
                    data.setLines(lines);
                    ch_san_gil.setLineChartData(data);
                }
            }
        });
    }

    public void grafico_centro_abastos(Calendar c){
        Timestamp timestamp_user = new Timestamp(c.getTime());
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","Bucaramanga, Centroabastos").whereGreaterThanOrEqualTo("Date",timestamp_user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<PointValue> puntos_centro_abastos = new ArrayList<PointValue>();
                    int contador = 0;
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Timestamp timestamp_bd = (Timestamp) document.getData().get("Date");
                        puntos_centro_abastos.add(new PointValue(contador,Integer.parseInt(document.getData().get("PROM_DIARIO").toString())).setLabel("Fecha: " + formateadorFechaMedia.format(timestamp_bd.toDate()) + " Valor: " + document.getData().get("PROM_DIARIO").toString()));
                        contador++;
                    }
                    Line line = new Line(puntos_centro_abastos).setColor(Color.BLACK).setCubic(true).setHasLabelsOnlyForSelected(true).setPointColor(Color.BLACK).setPointRadius(7);
                    List<Line> lines = new ArrayList<Line>();
                    lines.add(line);
                    LineChartData data = new LineChartData();
                    data.setValueLabelTextSize(17);
                    data.setLines(lines);
                    ch_centro_abasto.setLineChartData(data);
                }
            }
        });
    }

    public void grafico_mercados_centro(Calendar c){
        Timestamp timestamp_user = new Timestamp(c.getTime());
        db.collection("precios_DANE").whereEqualTo("NOM_ABASTO","Bucaramanga, Mercados del centro").whereGreaterThanOrEqualTo("Date",timestamp_user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<PointValue> puntos_mercados_centro = new ArrayList<PointValue>();
                    int contador = 0;
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Timestamp timestamp_bd = (Timestamp) document.getData().get("Date");
                        puntos_mercados_centro.add(new PointValue(contador,Integer.parseInt(document.getData().get("PROM_DIARIO").toString())).setLabel("Fecha: " + formateadorFechaMedia.format(timestamp_bd.toDate()) + " Valor: " + document.getData().get("PROM_DIARIO").toString()));
                        contador++;
                    }
                    Line line = new Line(puntos_mercados_centro).setColor(Color.GRAY).setCubic(true).setHasLabelsOnlyForSelected(true).setPointColor(Color.GRAY).setPointRadius(7);
                    List<Line> lines = new ArrayList<Line>();
                    lines.add(line);
                    LineChartData data = new LineChartData();
                    data.setValueLabelTextSize(17);
                    data.setLines(lines);
                    ch_mercados_centro.setLineChartData(data);
                }
            }
        });
    }

}