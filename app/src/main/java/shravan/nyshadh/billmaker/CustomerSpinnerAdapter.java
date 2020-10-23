package shravan.nyshadh.billmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomerSpinnerAdapter extends ArrayAdapter<Customer> {

    CustomerSpinnerAdapter(@NonNull Context context, @NonNull List<Customer> customers) {
        super(context, R.layout.item_customer, customers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, parent);
    }

    private View initView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        TextView name = view.findViewById(R.id.customer_name);
        TextView number = view.findViewById(R.id.customer_number);
        Customer customer = getItem(position);
        if (customer.getName() == null && customer.getPhoneNum() == null) {
            name.setText("Select Customer");
            number.setVisibility(View.GONE);
        } else {
            number.setVisibility(View.VISIBLE);
            name.setText(customer.getName());
            number.setText(customer.getPhoneNum());
        }
        return view;
    }
}
