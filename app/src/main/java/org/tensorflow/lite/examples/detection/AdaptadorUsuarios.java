package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.MyViewHolder>{

    Context context;
    ArrayList<User> userArrayList;

    public AdaptadorUsuarios(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public AdaptadorUsuarios.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_usuarios,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorUsuarios.MyViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.tv_displayName_users.setText(user.displayName);
        holder.tv_email_users.setText(user.email);
        Picasso.get().load(user.photo).into(holder.img_perfil_users);
        holder.btn_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,CorreosActivity.class);
                i.putExtra("name",user.displayName);
                i.putExtra("email",user.email);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_displayName_users, tv_email_users;
        ImageView img_perfil_users;
        Button btn_contacto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_displayName_users = itemView.findViewById(R.id.tv_displayName_users);
            tv_email_users = itemView.findViewById(R.id.tv_email_users);
            img_perfil_users = itemView.findViewById(R.id.img_perfil_users);
            btn_contacto = itemView.findViewById(R.id.btn_contacto);

        }
    }

}
