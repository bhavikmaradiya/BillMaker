package shravan.nyshadh.billmaker.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import shravan.nyshadh.billmaker.Adapter.InvoiceHistoryAdapter;
import shravan.nyshadh.billmaker.Modal.Common;
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
    SwipeRefreshLayout swipeRefreshLayout;
    Activity activity;
    InvoiceHistoryAdapter adapter;
    InvoiceHistoryAdapter.InvoiceActionListener invoiceActionListener;


    public HistoryFragment() {
        invoiceList = new ArrayList<>();
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        this.invoiceActionListener = (InvoiceHistoryAdapter.InvoiceActionListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.invoiceActionListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        rvInvoices = view.findViewById(R.id.rvInvoices);
        swipeRefreshLayout = view.findViewById(R.id.pullToRefresh);
        fab = view.findViewById(R.id.fab);
        rvInvoices.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvInvoices.setHasFixedSize(true);
        adapter = new InvoiceHistoryAdapter(getActivity(), invoiceList, this);
        rvInvoices.setAdapter(adapter);
        rvInvoices.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (Common.isNetworkAvailable(activity)) {
                InvoiceTask task = new InvoiceTask();
                task.execute();
            } else {
                Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

        });
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NewEntryActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });

        if (Common.isNetworkAvailable(getActivity())) {
            InvoiceTask task = new InvoiceTask();
            task.execute();
        } else {
            Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Common.isNetworkAvailable(getActivity())) {
            InvoiceTask task = new InvoiceTask();
            task.execute();
        } else {
            Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT).show();
        }
    }

    private class InvoiceTask extends AsyncTask<Void, Void, List<Invoice>> {

        @Override
        protected List<Invoice> doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Common.GET_INVOICES, response -> {
                Gson gson = new Gson();
                invoiceList.clear();
                invoiceList.addAll(gson.fromJson(response, new TypeToken<List<Invoice>>() {
                }.getType()));
                adapter.notifyDataSetChanged();
            }, error -> activity.runOnUiThread(() -> Toasty.error(activity, "Failed to load", Toasty.LENGTH_SHORT).show()));
            Volley.newRequestQueue(activity).add(request);
            return null;
        }

        @Override
        protected void onPostExecute(List<Invoice> invoices) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
            super.onPostExecute(invoices);
        }
    }

    @Override
    public void onActionSelected(String action, Invoice invoice) {
        if (invoiceActionListener != null) invoiceActionListener.onActionSelected(action, invoice);
    }
}
