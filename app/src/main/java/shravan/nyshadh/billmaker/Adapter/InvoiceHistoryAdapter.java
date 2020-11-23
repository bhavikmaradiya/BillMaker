package shravan.nyshadh.billmaker.Adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Invoice;
import shravan.nyshadh.billmaker.R;

public class InvoiceHistoryAdapter extends RecyclerView.Adapter<InvoiceHistoryAdapter.InvoiceHolder> {
    private Context context;
    private List<Invoice> invoiceList;
    private InvoiceActionListener invoiceActionListener;

    public InvoiceHistoryAdapter(Context context, List<Invoice> invoiceList, InvoiceActionListener invoiceActionListener) {
        this.context = context;
        this.invoiceList = invoiceList;
        this.invoiceActionListener = invoiceActionListener;
    }

    @NonNull
    @Override
    public InvoiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvoiceHolder(LayoutInflater.from(context).inflate(R.layout.item_invoice, parent, false));
    }

    private float dipToPixels() {
        DisplayMetrics metrics = this.context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 3, metrics);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceHolder holder, int position) {
        Invoice invoice = invoiceList.get(holder.getAdapterPosition());

        holder.actionLayout.setVisibility(invoice.isExpanded() ? View.VISIBLE : View.GONE);
        holder.price.setText(String.format("Total : %s", "50"));
        holder.date.setText(invoice.getTime().replaceAll("-", "/"));
        holder.number.setText(invoice.getCustomer() != null ? invoice.getCustomer().getCustomerPhone() : "");
        holder.name.setText(invoice.getCustomer() != null ? invoice.getCustomer().getCustomerName() : "");

        holder.itemView.setOnClickListener(view -> {
            invoice.setExpanded(!invoice.isExpanded());
            notifyItemChanged(position);
        });

        holder.imgCall.setOnClickListener(view -> invoiceActionListener.onActionSelected(Common.ACTION_INVOICE_CALL, invoice));
        holder.imgWhatsapp.setOnClickListener(view -> invoiceActionListener.onActionSelected(Common.ACTION_INVOICE_WHATSAPP, invoice));
        holder.imgDetail.setOnClickListener(view -> invoiceActionListener.onActionSelected(Common.ACTION_INVOICE_DETAIL, invoice));
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public static class InvoiceHolder extends RecyclerView.ViewHolder {
        TextView name, number, date, price;
        LinearLayout actionLayout;
        ImageView imgCall, imgWhatsapp, imgDetail;
        CardView cardView;

        public InvoiceHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);
            imgCall = itemView.findViewById(R.id.imgCall);
            imgDetail = itemView.findViewById(R.id.imgDetail);
            cardView = itemView.findViewById(R.id.cardView);
            imgWhatsapp = itemView.findViewById(R.id.imgWhatsapp);
            actionLayout = itemView.findViewById(R.id.actionLayout);
        }
    }

    public interface InvoiceActionListener {
        void onActionSelected(String action, Invoice invoice);
    }
}
