package shravan.nyshadh.billmaker.Fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import shravan.nyshadh.billmaker.Adapter.SelectCustomerAdapter;
import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Customer;
import shravan.nyshadh.billmaker.R;

import static shravan.nyshadh.billmaker.Modal.Common.GET_CUSTOMERS;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCustomerFragment extends Fragment {

    RecyclerView rvCustomers;
    SelectCustomerAdapter adapter;
    List<Customer> customerList;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText searchBar;
    Activity activity;

    public SelectCustomerFragment() {
        customerList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_customer, container, false);
        rvCustomers = view.findViewById(R.id.rvCustomers);
        searchBar = view.findViewById(R.id.searchBar);
        swipeRefreshLayout = view.findViewById(R.id.pullToRefresh);
        rvCustomers.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCustomers.setHasFixedSize(true);
        adapter = new SelectCustomerAdapter(getActivity(), customerList, customer -> {
            Common.selectedCustomer = customer;
        });
        rvCustomers.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (Common.isNetworkAvailable(getActivity())) {
                CustomerTask task = new CustomerTask();
                task.execute();
            } else {
                Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!searchBar.getText().toString().trim().isEmpty()) {
                    filter(searchBar.getText().toString().toLowerCase());
                } else {
                    if (Common.isNetworkAvailable(getActivity())) {
                        CustomerTask task = new CustomerTask();
                        task.execute();
                    } else {
                        Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (Common.isNetworkAvailable(getActivity())) {
            CustomerTask task = new CustomerTask();
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
            CustomerTask task = new CustomerTask();
            task.execute();
        } else {
            Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT).show();
        }
    }

    private void filter(String text) {
        List<Customer> temp = new ArrayList<>();
        for (Customer customer : customerList) {

            if (customer.getCustomerName().toLowerCase().startsWith(text)) {
                temp.add(customer);
            } else if (customer.getCustomerPhone().toLowerCase().startsWith(text)) {
                temp.add(customer);
            } else if (customer.getCustomerPhone2().toLowerCase().startsWith(text)) {
                temp.add(customer);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private class CustomerTask extends AsyncTask<Void, Void, List<Customer>> {
        @Override
        protected List<Customer> doInBackground(Void... voids) {
            StringRequest request = new StringRequest(GET_CUSTOMERS, response -> {
                try {
                    JSONArray array = new JSONArray(response);
                    customerList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Customer customer = new Customer();
                        customer.setCustomerId(object.getInt("cust_id"));
                        customer.setCustomerName(object.getString("cust_name"));
                        customer.setCustomerPhone(object.getString("cust_phone"));
                        customer.setCustomerPhone2(object.getString("cust_phone2"));
                        customer.setCustomerEmail(object.getString("cust_email"));
                        customer.setCustomerAge(object.getString("cust_age"));
                        customer.setCustomerGender(object.getString("cust_gender"));
                        customer.setCustomerAddress(object.getString("cust_address"));
                        customer.setCustomerRemarks(object.getString("cust_remarks"));
                        customer.setCustomerRightIPD(object.getString("cust_right_IPD"));
                        customer.setCustomerLeftIPD(object.getString("cust_left_IPD"));
                        customer.setPrescriberId(object.getInt("prescriber_id"));
                        customerList.add(customer);
                        adapter.updateList(customerList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }, error -> getActivity().runOnUiThread(() -> Toasty.error(activity, "Failed to load!", Toasty.LENGTH_SHORT).show()));
            Volley.newRequestQueue(activity).add(request);
            return customerList;
        }

        @Override
        protected void onPostExecute(List<Customer> customers) {
            super.onPostExecute(customers);
            adapter.updateList(customerList);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
