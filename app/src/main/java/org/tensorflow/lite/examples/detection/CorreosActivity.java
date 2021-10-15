package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class CorreosActivity extends AppCompatActivity {

    String name,email;
    Boolean result;
    Boolean isConnection = true;

    EditText et_asunto_users, et_cuerpo_users;
    TextView tv_correo_users, tv_asunto_users, tv_cuerpo_users, tv_sin_internet_correo, et_email_users;
    Button btn_enviar_correo;
    ImageView img_sin_internet;
    ProgressBar pb_correos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correos);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Bundle datos = this.getIntent().getExtras();
        name = datos.getString("name");
        email = datos.getString("email");

        tv_correo_users = findViewById(R.id.tv_correo_users);
        tv_asunto_users = findViewById(R.id.tv_asunto_users);
        tv_cuerpo_users = findViewById(R.id.tv_cuerpo_users);
        tv_sin_internet_correo = findViewById(R.id.tv_sin_internet_correo);
        tv_sin_internet_correo.setVisibility(View.GONE);
        et_email_users = findViewById(R.id.et_email_users);
        et_asunto_users = findViewById(R.id.et_asunto_users);
        et_cuerpo_users = findViewById(R.id.et_cuerpo_users);
        btn_enviar_correo = findViewById(R.id.btn_enviar_correo);
        img_sin_internet = findViewById(R.id.img_sin_internet);
        img_sin_internet.setVisibility(View.GONE);
        pb_correos = findViewById(R.id.pb_correos);
        pb_correos.setVisibility(View.GONE);

        isConnection = checkNetworkConnection();
        if(isConnection  == false){
            tv_correo_users.setVisibility(View.GONE);
            tv_asunto_users.setVisibility(View.GONE);
            tv_cuerpo_users.setVisibility(View.GONE);
            et_email_users.setVisibility(View.GONE);
            et_asunto_users.setVisibility(View.GONE);
            et_cuerpo_users.setVisibility(View.GONE);
            btn_enviar_correo.setVisibility(View.GONE);

            img_sin_internet.setVisibility(View.VISIBLE);
            tv_sin_internet_correo.setVisibility(View.VISIBLE);
        }

        et_email_users.setText(email);
        et_asunto_users.setText(user.getDisplayName() + " quiere contactarse contigo");
        et_cuerpo_users.setText("Hola, " + name + ", el usuario " + user.getDisplayName() + " quiere contactarse contigo. Si deseas seguir la conversión, lo puedes hacer por este medio (respondiendo a todos) o enviarle un mensaje directo haciendo clic en su correo " + user.getEmail());

        btn_enviar_correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb_correos.setVisibility(View.VISIBLE);
                RequestQueue queue = Volley.newRequestQueue(CorreosActivity.this);
                String url = "https://script.google.com/macros/s/AKfycbx1YT_4gecDplROizws1yP1Zh5rr8LTXETMI9KSDYZB94KXRejjI9qBFLrFnYhC4Bj3/exec?funcion=2&recipient=" + email +  "&subject=" + et_asunto_users.getText().toString() + "&body=" + et_cuerpo_users.getText().toString().replace("\n","") + "&copy=" + user.getEmail();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    result = response.getBoolean("rta");
                                    if(result == true){
                                        pb_correos.setVisibility(View.GONE);
                                        onBackPressed();
                                        Toast.makeText(CorreosActivity.this, "¡Correo enviado con éxito!", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Toast.makeText(CorreosActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);
            }
        });

    }

    private boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }else{
            return false;
        }
    }


}