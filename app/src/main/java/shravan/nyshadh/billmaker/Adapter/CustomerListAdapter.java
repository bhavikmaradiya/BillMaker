package shravan.nyshadh.billmaker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import shravan.nyshadh.billmaker.Modal.Customer;
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
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerHolder extends RecyclerView.ViewHolder {
        TextView customerName, customerNumber;

        public CustomerHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name);
            customerNumber = itemView.findViewById(R.id.customer_number);
        }
    }
}
