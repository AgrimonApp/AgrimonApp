package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ResultadoActivity extends AppCompatActivity {

    TextView tv_cantidad_bultos,tv_estimado_centro_result,tv_estimado_mercado_result,tv_estimado_socorro_result,tv_estimado_san_gil_result;
    Button btn_volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        tv_cantidad_bultos = findViewById(R.id.tv_cantidad_bultos);
        tv_estimado_centro_result = findViewById(R.id.tv_estimado_centro_result);
        tv_estimado_mercado_result = findViewById(R.id.tv_estimado_mercado_result);
        tv_estimado_socorro_result = findViewById(R.id.tv_estimado_socorro_result);
        tv_estimado_san_gil_result = findViewById(R.id.tv_estimado_san_gil_result);

        String cantidad = getIntent().getStringExtra("cantidad");
        tv_cantidad_bultos.setText(cantidad);

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");

        String estimado_centro_abastos = getIntent().getStringExtra("estimado_centro");
        tv_estimado_centro_result.setText("Centro Abastos: $" + decimalFormat.format(Integer.parseInt(estimado_centro_abastos)) + " COP");
        String estimado_marcados_centro = getIntent().getStringExtra("estimado_mercado");
        tv_estimado_mercado_result.setText("Mercados del Centro: $" + decimalFormat.format(Integer.parseInt(estimado_marcados_centro)) + " COP");
        String estimado_socorro = getIntent().getStringExtra("estimado_socorro");
        tv_estimado_socorro_result.setText("Socorro: $" + decimalFormat.format(Integer.parseInt(estimado_socorro)) + " COP");
        String estimado_san_gil = getIntent().getStringExtra("estimado_san_gil");
        tv_estimado_san_gil_result.setText("San Gil: $" + decimalFormat.format(Integer.parseInt(estimado_san_gil)) + " COP");

        btn_volver = findViewById(R.id.btn_volver);
        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultadoActivity.this,InicioActivity.class));
                finish();
            }
        });

    }
}