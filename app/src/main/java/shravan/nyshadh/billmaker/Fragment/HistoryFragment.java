package shravan.nyshadh.billmaker.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import shravan.nyshadh.billmaker.Adapter.InvoiceHistoryAdapter;
import shravan.nyshadh.billmaker.Modal.Invoice;
import shravan.nyshadh.billmaker.NewEntryActivity;
import shravan.nyshadh.billmaker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements InvoiceHistoryAdapter.InvoiceActionListener {
    RecyclerView rvInvoices;
    View view;
    FloatingActionButton fab;
    List<Invoice> invoiceList;
    Activity activity;
    InvoiceHistoryAdapter.InvoiceActionListener invoiceActionListener;

    public HistoryFragment(InvoiceHistoryAdapter.InvoiceActionListener invoiceActionListener) {
        this.invoiceActionListener = invoiceActionListener;
        invoiceList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            invoiceList.add(new Invoice("Bhavik", "942879844" + i, "500", "10/5/2020"));
        }
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        rvInvoices = view.findViewById(R.id.rvInvoices);
        fab = view.findViewById(R.id.fab);
        rvInvoices.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvInvoices.setHasFixedSize(true);
        rvInvoices.setAdapter(new InvoiceHistoryAdapter(getActivity(), invoiceList, this));
        rvInvoices.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NewEntryActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });

        return view;
    }

    @Override
    public void onActionSelected(String action, Invoice invoice) {
        invoiceActionListener.onActionSelected(action, invoice);
    }
}
