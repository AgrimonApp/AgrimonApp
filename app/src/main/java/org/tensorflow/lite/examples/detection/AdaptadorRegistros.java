package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;

public class AdaptadorRegistros extends RecyclerView.Adapter<AdaptadorRegistros.MyViewHolder>{

    Context context;
    ArrayList<Registro> registroArrayList;

    public AdaptadorRegistros(Context context, ArrayList<Registro> registroArrayList) {
        this.context = context;
        this.registroArrayList = registroArrayList;
    }

    @NonNull
    @Override
    public AdaptadorRegistros.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_registros,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorRegistros.MyViewHolder holder, int position) {
        Registro registro = registroArrayList.get(position);

        DateFormat formateadorFechaMedia = DateFormat.getDateInstance(DateFormat.MEDIUM);

        holder.tv_fecha.setText(formateadorFechaMedia.format(registro.fecha));
        holder.tv_cantidad.setText(registro.cantidad + "");
        holder.tv_estimado_centro.setText(registro.estimado_centro_abastos + "");
        holder.tv_estimado_mercados.setText(registro.estimado_marcados_centro + "");
        holder.tv_estimado_san_gil.setText(registro.estimado_san_gil + "");
        holder.tv_estimado_socorro.setText(registro.estimado_socorro + "");
    }

    @Override
    public int getItemCount() {
        return registroArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_fecha,tv_cantidad,tv_estimado_centro,tv_estimado_mercados,tv_estimado_san_gil,tv_estimado_socorro;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_fecha = itemView.findViewById(R.id.tv_fecha);
            tv_cantidad = itemView.findViewById(R.id.tv_cantidad);
            tv_estimado_centro = itemView.findViewById(R.id.tv_estimado_centro);
            tv_estimado_mercados = itemView.findViewById(R.id.tv_estimado_mercados);
            tv_estimado_san_gil = itemView.findViewById(R.id.tv_estimado_san_gil);
            tv_estimado_socorro = itemView.findViewById(R.id.tv_estimado_socorro);

        }
    }

}
