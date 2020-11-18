package shravan.nyshadh.billmaker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import shravan.nyshadh.billmaker.Modal.Customer;
import shravan.nyshadh.billmaker.NewEntryActivity;
import shravan.nyshadh.billmaker.R;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.CustomerHolder> {
    Context context;
    List<Customer> customerList;

    public CustomerListAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerHolder(LayoutInflater.from(context).inflate(R.layout.item_customer_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHolder holder, int position) {
        holder.customerName.setText(customerList.get(position).getName());
        holder.customerNumber.setText(customerList.get(position).getPhoneNum());
        holder.imgCreateInvoice.setOnClickListener(view -> context.startActivity(new Intent(context, NewEntryActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerHolder extends RecyclerView.ViewHolder {
        TextView customerName, customerNumber;
        ImageView imgCreateInvoice;

        public CustomerHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name);
            customerNumber = itemView.findViewById(R.id.customer_number);
            imgCreateInvoice = itemView.findViewById(R.id.imgCreateInvoice);
        }
    }
}
