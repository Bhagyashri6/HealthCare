package com.example.healthcareapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.healthcareapp.R;
import com.example.healthcareapp.activity.UserDetailActivity;
import com.example.healthcareapp.model.User;

import java.util.ArrayList;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    Context context;
    ArrayList<User> row;
    int i ;



    public UserAdapter(Context context, ArrayList<User> row) {
        this.context = context;
        this.row = row;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = null;
        itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_list, viewGroup, false);
        context = viewGroup.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = row.get(i);

        try {
            Glide.with(context)
                    .load(user.getAvatar())
                    .override(600, 200)
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
                    .into(viewHolder.iv_labimg);
        }catch (Exception e){
            e.printStackTrace();
        }

        viewHolder.tv_name.setText(user.getFirst_name());

        viewHolder.cv_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UserDetailActivity.class);
                i.putExtra("email",user.getEmail());
                i.putExtra("first_name",user.getFirst_name());
                i.putExtra("last_name",user.getLast_name());
                i.putExtra("avatar",user.getAvatar());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return row.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_labimg;
        CardView cv_row;
        TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_labimg =(ImageView)itemView.findViewById(R.id.iv_labimg) ;
            tv_name=(TextView)itemView.findViewById(R.id.tv_name);
            cv_row =(CardView)itemView.findViewById(R.id.cv_row);


        }
    }
}
