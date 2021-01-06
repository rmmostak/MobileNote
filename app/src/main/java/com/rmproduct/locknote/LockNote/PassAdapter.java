package com.rmproduct.locknote.LockNote;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rmproduct.locknote.R;

import java.util.List;

public class PassAdapter extends RecyclerView.Adapter<PassAdapter.MyViewHolder> {

    private Context context;
    private List<PassModel> modelList;

    public PassAdapter(Context context, List<PassModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dateTv, timeTv, titleTv;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            dateTv = itemView.findViewById(R.id.passDate);
            timeTv = itemView.findViewById(R.id.passTime);
            titleTv = itemView.findViewById(R.id.passTitle);

        }
    }


    @NonNull
    @Override
    public PassAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_layout, parent, false);
        return new PassAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassAdapter.MyViewHolder holder, int position) {

        final PassModel model=modelList.get(position);


        holder.dateTv.setText(model.getDate());
        holder.timeTv.setText(model.getTime());
        holder.titleTv.setText(model.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PassView.class);
                intent.putExtra("id", model.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}

