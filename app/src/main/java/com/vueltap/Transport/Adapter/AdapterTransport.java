package com.vueltap.Transport.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vueltap.Transport.Model.ModelTransport;
import com.vueltap.R;

import java.util.ArrayList;

public class AdapterTransport extends RecyclerView.Adapter<AdapterTransport.ViewHolder> {

    private ArrayList<ModelTransport> arrayList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_type_transport,parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(arrayList.get(position).getName());
        holder.tvDescription.setText(arrayList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName,tvDescription;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.textViewRowName);
            tvDescription=itemView.findViewById(R.id.textViewRowDescription);
        }
    }
}
