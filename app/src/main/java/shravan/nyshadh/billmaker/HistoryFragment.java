package shravan.nyshadh.billmaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements InvoiceHistoryAdapter.InvoiceActionListener {
    RecyclerView rvInvoices;
    View view;
    List<Invoice> invoiceList;
    InvoiceHistoryAdapter.InvoiceActionListener invoiceActionListener;

    public HistoryFragment(InvoiceHistoryAdapter.InvoiceActionListener invoiceActionListener) {
        this.invoiceActionListener = invoiceActionListener;
        invoiceList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            invoiceList.add(new Invoice("Bhavik", "942879844" + i, "500", "10/5/2020"));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        rvInvoices = view.findViewById(R.id.rvInvoices);
        rvInvoices.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvInvoices.setHasFixedSize(true);
        rvInvoices.setAdapter(new InvoiceHistoryAdapter(getActivity(), invoiceList, this));
        return view;
    }

    @Override
    public void onActionSelected(String action, Invoice invoice) {
        invoiceActionListener.onActionSelected(action, invoice);
    }
}
