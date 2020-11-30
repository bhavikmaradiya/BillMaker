package shravan.nyshadh.billmaker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Customer;
import shravan.nyshadh.billmaker.R;

public class SelectCustomerAdapter extends RecyclerView.Adapter<SelectCustomerAdapter.CustomerHolder> {
    Context context;
    List<Customer> customerList;
    CustomerListener customerListener;

    public SelectCustomerAdapter(Context context, List<Customer> customerList, CustomerListener customerListener) {
        this.context = context;
        this.customerList = customerList;
        this.customerListener = customerListener;
    }

    @NonNull
    @Override
    public CustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerHolder(LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false));
    }

    public void updateList(List<Customer> list) {
        this.customerList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHolder holder, int position) {
        holder.imgCheck.setVisibility(Common.selectedCustomer != null && Common.selectedCustomer.getCustomerId() == customerList.get(position).getCustomerId() ? View.VISIBLE : View.INVISIBLE);
        holder.customerName.setText(customerList.get(position).getCustomerName());
        holder.customerNumber.setText(customerList.get(position).getCustomerPhone());

        holder.itemView.setOnClickListener(view -> {
            Common.selectedCustomer = customerList.get(position);
            customerListener.onCustomerSelect(customerList.get(position));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerHolder extends RecyclerView.ViewHolder {
        TextView customerName, customerNumber;
        ImageView imgCheck;

        public CustomerHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name);
            customerNumber = itemView.findViewById(R.id.customer_number);
            imgCheck = itemView.findViewById(R.id.imgCheck);
        }
    }

    public interface CustomerListener {
        void onCustomerSelect(Customer customer);
    }
}
