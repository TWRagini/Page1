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
public class Customer_invoice_adapter extends RecyclerView.Adapter<Customer_invoice_adapter.ViewHolder>
{
    Context context;
    private final ClickListener listener;
    List<InvoiceModel> Incentive_List;
    DatabaseOperation databaseOperation;
    public Customer_invoice_adapter(Context context, ClickListener listener, List<InvoiceModel> Incentive_List)
    {
        this.context=context;
        this.listener = listener;
        this.Incentive_List= Incentive_List;
        databaseOperation = new DatabaseOperation(context.getApplicationContext());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.item2_layout,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(Incentive_List!=null && Incentive_List.size()>0)
        {
            InvoiceModel model=Incentive_List.get(position);
            holder.txt1.setText(model.getSrNo());
          holder.txt2.setText(model.getProduct());
          holder.txt3.setText(model.getSubCat());
          holder.txt4.setText(model.getModel());
          holder.txt5.setText(model.getSerialNumber());
          holder.txt6.setText(model.getIncentive());
          holder.txt7.setText(model.getAct());
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
            txt1=itemView.findViewById(R.id.sr);
            txt2=itemView.findViewById(R.id.product);
            txt3=itemView.findViewById(R.id.sub_cat);
            txt4=itemView.findViewById(R.id.model);
            txt5=itemView.findViewById(R.id.sr_num);
            txt6=itemView.findViewById(R.id.incentive);
            txt7=itemView.findViewById(R.id.action);

            txt7.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {


            if (view.getId() == txt7.getId()) {
               String a =  txt5.getText().toString();
              databaseOperation.deleteRow(a);
//             if (status)
//             {
//
//             }
                Toast.makeText(view.getContext(), "ITEM PRESSED = " +a+",,," + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                

            } else {
                Toast.makeText(view.getContext(), "Try again = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }

            listenerRef.get().onPositionClicked(getAdapterPosition());



        }

        @Override
        public boolean onLongClick(View view) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Hello Dialog")
                    .setMessage("LONG CLICK DIALOG WINDOW FOR ICON " + String.valueOf(getAdapterPosition()))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            builder.create().show();
            listenerRef.get().onLongClicked(getAdapterPosition());
            return true;

        }
    }

}
