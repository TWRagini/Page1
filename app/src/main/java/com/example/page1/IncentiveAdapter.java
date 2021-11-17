package com.example.page1;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

public class IncentiveAdapter extends RecyclerView.Adapter<IncentiveAdapter.ViewHolder>{
    Context context;
    private final ClickListener listener;
    List<IncentiveModel> Incentive_List;
    DatabaseOperation databaseOperation;
    public IncentiveAdapter(Context context, ClickListener listener ,List <IncentiveModel> Incentive_List)
    {
        this.context=context;
        this.Incentive_List= Incentive_List;
        this.listener = listener;
        databaseOperation = new DatabaseOperation(context.getApplicationContext());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(Incentive_List!=null && Incentive_List.size()>0)
        {
           IncentiveModel model=Incentive_List.get(position);
            holder.txt1.setText(model.getSrNo());
            holder.txt2.setText(model.getSerialnumber());
            holder.txt3.setText(model.getModel());
            holder.txt4.setText(model.getInvoiceNumber());
            holder.txt5.setText(model.getSaleDate());
            holder.txt6.setText(model.getIncentive());
            holder.txt7.setText(model.getStatus());



        }
    }

    @Override
    public int getItemCount() {
        return Incentive_List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {   TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);
                txt1=itemView.findViewById(R.id.tv_sr);
                txt2=itemView.findViewById(R.id.tv_serial);
                txt3=itemView.findViewById(R.id.tv_model);
                txt4=itemView.findViewById(R.id.tv_invoice);
            txt5=itemView.findViewById(R.id.tv_saleDate);
            txt6=itemView.findViewById(R.id.tv_incentive);
            txt7=itemView.findViewById(R.id.tv_status);


            txt7.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {


            if (view.getId() == txt7.getId()) {

               String a =  txt2.getText().toString();
             String reason= databaseOperation.getReason(a);
//


                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Status")
                        .setMessage(reason)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                builder.create().show();
                listenerRef.get().onLongClicked(getAdapterPosition());

            } else {
                Toast.makeText(view.getContext(), "Try again = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }

            listenerRef.get().onPositionClicked(getAdapterPosition());



        }

        @Override
        public boolean onLongClick(View view) {

          return false;

        }
        }
    }

