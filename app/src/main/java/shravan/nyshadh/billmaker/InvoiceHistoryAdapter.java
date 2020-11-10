package shravan.nyshadh.billmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvoiceHistoryAdapter extends RecyclerView.Adapter<InvoiceHistoryAdapter.InvoiceHolder> {
    Context context;
    List<Invoice> invoiceList;

    public InvoiceHistoryAdapter(Context context, List<Invoice> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;
    }

    @NonNull
    @Override
    public InvoiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvoiceHolder(LayoutInflater.from(context).inflate(R.layout.item_invoice, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceHolder holder, int position) {
        holder.price.setText("Total : " + invoiceList.get(position).price);
        holder.date.setText(invoiceList.get(position).date);
        holder.number.setText(invoiceList.get(position).number);
        holder.name.setText(invoiceList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public static class InvoiceHolder extends RecyclerView.ViewHolder {
        TextView name, number, date, price;

        public InvoiceHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);
        }
    }
}
