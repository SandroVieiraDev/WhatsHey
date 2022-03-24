package com.env.whatshey.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.env.whatshey.R;
import com.env.whatshey.helper.DateCustom;
import com.env.whatshey.model.Historic;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private List<Historic> historicList;
    private int intBackground = -1;
    private static final int SENDER     = 0;
    private static final int RECEIVER  = 1;
    private static final int DATE  = 2;

    public HomeAdapter(List<Historic> historicList) {
        this.historicList = historicList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyList(List<Historic> list){
        this.historicList = list;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setIntBackground(int background){
        this.intBackground = background;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder;
        if (viewType == SENDER){
            viewHolder = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_sender, parent, false);
        } else {
            viewHolder = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_receiver, parent, false);
        }


        return new MyViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Historic historic = historicList.get(position);
        holder.textNumber.setText(historic.getNumber());
        holder.textDate.setText(DateCustom.getTime(historic.getTime()));

        if (intBackground == position){
            holder.linearLayout.setBackgroundColor(0x5500D1E1);
        } else {
            holder.linearLayout.setBackgroundColor(0x00000000);
        }
    }

    @Override
    public int getItemCount() {
        return historicList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int result = position % 2;
        if (result == SENDER){
            return SENDER;
        } else{
            return RECEIVER;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView textNumber;
        private final TextView textDate;
        private final LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearSender);
            textNumber = itemView.findViewById(R.id.textNumberSender);
            textDate = itemView.findViewById(R.id.textDateSender);
        }
    }

    public class ViewHolderDate extends RecyclerView.ViewHolder {
        private final TextView textAdapterDate;

        public ViewHolderDate(@NonNull View itemView) {
            super(itemView);
            textAdapterDate = itemView.findViewById(R.id.textAdapterDate);
        }
    }


}
