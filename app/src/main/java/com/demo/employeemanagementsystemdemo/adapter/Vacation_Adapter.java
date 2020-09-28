package com.demo.employeemanagementsystemdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.employeemanagementsystemdemo.R;
import com.demo.employeemanagementsystemdemo.user.Vacation_Request;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Vacation_Adapter extends FirestoreRecyclerAdapter<Vacation_Request , Vacation_Adapter.VacationHolder> {
    private Context context;
    public Vacation_Adapter(@NonNull FirestoreRecyclerOptions<Vacation_Request> options , Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull VacationHolder holder, int position, @NonNull Vacation_Request model) {
        holder.V_D_Name.setText(context.getString(R.string.name) + ":- " + model.getName());
        holder.V_D_SDate.setText(context.getString(R.string.submit_date) + ":- " + model.getSubmit_date());
        holder.V_D_Date.setText(context.getString(R.string.vacation_date) + " " + model.getVacation_date());

        if (model.getStatus().equalsIgnoreCase(context.getString(R.string.under_review)))
        {
            holder.V_D_Status.setTextColor(Color.YELLOW);
            holder.V_D_Status.setText(model.getStatus());
        } else if (model.getStatus().equalsIgnoreCase("Approved"))
        {
            holder.V_D_Status.setTextColor(Color.BLUE);
            holder.V_D_Status.setText( model.getStatus());
        } else if (model.getStatus().equalsIgnoreCase("rejected"))
        {
            holder.V_D_Status.setTextColor(Color.RED);
            holder.V_D_Status.setText(model.getStatus());
        }

    }

    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public void updateDate (int position , String vacation_date){
        getSnapshots().getSnapshot(position).getReference().update("vacation_date" , vacation_date);
    }


    @NonNull
    @Override
    public VacationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_design , parent , false);
        return new VacationHolder(view);

    }

    class VacationHolder extends RecyclerView.ViewHolder {
        private TextView V_D_Name , V_D_SDate , V_D_Date , V_D_Status;
        public VacationHolder(@NonNull View itemView) {
            super(itemView);
            V_D_Name = itemView.findViewById(R.id.V_D_Name);
            V_D_SDate = itemView.findViewById(R.id.V_D_SDate);
            V_D_Date = itemView.findViewById(R.id.V_D_Date);
            V_D_Status = itemView.findViewById(R.id.V_D_Status);

        }
    }
}
