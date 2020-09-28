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
import com.demo.employeemanagementsystemdemo.user.Departure_Request;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Departure_Adapter extends FirestoreRecyclerAdapter<Departure_Request , Departure_Adapter.DepartureHolder> {

    private Context context;
    public Departure_Adapter(@NonNull FirestoreRecyclerOptions<Departure_Request> options , Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DepartureHolder holder, int position, @NonNull Departure_Request model) {
        holder.V_D_Name.setText(context.getString(R.string.name) + ":- " + model.getName());
        holder.V_D_SDate.setText(context.getString(R.string.submit_date) + ":- " + model.getSubmit_date());
        holder.V_D_Date.setText(context.getString(R.string.departure_date) + " " + model.getDeparture_date() + " ( " + model.getFrom() + " - " + model.getTo() + " )");
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
    } // end onBindViewHolder()

    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public void updateDate (int position , String vacation_date){
        getSnapshots().getSnapshot(position).getReference().update("departure_date" , vacation_date);
    }

    public void updateTime (int position , String from , String to){
        getSnapshots().getSnapshot(position).getReference().update("from" , from);
        getSnapshots().getSnapshot(position).getReference().update("to" , to);
    }

    @NonNull
    @Override
    public DepartureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_design , parent , false);
        return new DepartureHolder(view);
    } // end onCreateViewHolder()


      class DepartureHolder extends RecyclerView.ViewHolder {
        private TextView V_D_Name , V_D_SDate , V_D_Date , V_D_Status;
        public DepartureHolder(@NonNull View itemView) {
            super(itemView);
            V_D_Name = itemView.findViewById(R.id.V_D_Name);
            V_D_SDate = itemView.findViewById(R.id.V_D_SDate);
            V_D_Date = itemView.findViewById(R.id.V_D_Date);
            V_D_Status = itemView.findViewById(R.id.V_D_Status);
        } // end DepartureViewHolder()
    } // end DepartureViewHolder()
}

