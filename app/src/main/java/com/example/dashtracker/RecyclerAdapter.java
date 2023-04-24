package com.example.dashtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList entryID, entryDate, entryDistance;
    private Activity activity;

    RecyclerAdapter(ArrayList entryID, ArrayList entryDate, ArrayList entryDistance, Context context,
                           Activity activity){
        this.context = context;
        this.activity = activity;
        this.entryID = entryID;
        this.entryDate = entryDate;
        this.entryDistance = entryDistance;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater in = LayoutInflater.from(context);
        View view = in.inflate(R.layout.sussy_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.distView.setText(String.valueOf(entryDistance.get(position)));
        holder.dttView.setText(String.valueOf(entryDate.get(position)));
        holder.idView.setText(String.valueOf(entryID.get(position)));


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete?");
                builder.setNeutralButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    String getOrderID = (holder.idView).getText().toString().trim();
                    SQLClass sql = new SQLClass(context);
                    sql.removeFromDB(getOrderID);

                    Intent menuIntent = new Intent(context, Entries.class);
                    activity.startActivity(menuIntent);
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
    }

    @Override
    public int getItemCount() { return entryID.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView distView;
        TextView dttView;
        TextView idView;
        Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dttView = itemView.findViewById(R.id.dateView2);
            distView = itemView.findViewById(R.id.distanceView2);
            idView = itemView.findViewById(R.id.idView2);
            delete = itemView.findViewById(R.id.delButton);
        }
    }
}
