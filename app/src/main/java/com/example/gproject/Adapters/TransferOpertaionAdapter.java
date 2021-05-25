package com.example.gproject.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.Models.TransferOperationModel;
import com.example.gproject.R;

import java.util.ArrayList;

public class TransferOpertaionAdapter extends RecyclerView.Adapter<TransferOpertaionAdapter.ViewHolder> {


    // variable for our array list and context.
    private ArrayList<TransferOperationModel> transferOperationModelArrayList;
    private Context context;

    // creating a constructor.
    public TransferOpertaionAdapter(ArrayList<TransferOperationModel> userModalArrayList, Context context) {
        this.transferOperationModelArrayList = userModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.operation_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // getting data from our array list in our modal class.
        TransferOperationModel transferOperationModel = transferOperationModelArrayList.get(position);

        // on below line we are setting data to our text view.
        holder.mobileNumberTV.setText(transferOperationModel.getMobile_number());
        holder.categoryTV.setText(transferOperationModel.getCategory());
        holder.dateTV.setText(transferOperationModel.getDate());

        // on below line we are loading our image
        // from url in our image view using picasso.
        if(transferOperationModel.getSim_type() == TransferOperationModel.MTN_STRING){
            holder.simType.setImageResource(R.drawable.ic_mtn);
        }else{
            holder.simType.setImageResource(R.drawable.ic_syriatel_logo);
        }

        if(transferOperationModel.isStatus()){
            holder.statusView.setBackgroundColor(0xFF00FF00);
        }else{
            holder.statusView.setBackgroundColor(0xFFFF0000);
        }
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return transferOperationModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        private TextView mobileNumberTV, categoryTV, dateTV;
        private View statusView;
        private ImageView simType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
            mobileNumberTV = itemView.findViewById(R.id.idTVMobileNumber);
            categoryTV = itemView.findViewById(R.id.idTVCategory);
            dateTV = itemView.findViewById(R.id.idTVDate);
            statusView = itemView.findViewById(R.id.idStatusView);
            simType = itemView.findViewById(R.id.idIVSim);
        }
    }
}