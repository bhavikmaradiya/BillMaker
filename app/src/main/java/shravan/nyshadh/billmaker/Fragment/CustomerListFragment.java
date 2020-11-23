package shravan.nyshadh.billmaker.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import shravan.nyshadh.billmaker.Adapter.CustomerListAdapter;
import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Customer;
import shravan.nyshadh.billmaker.R;

import static shravan.nyshadh.billmaker.Modal.Common.GET_CUSTOMERS;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerListFragment extends Fragment {
    View view;
    RecyclerView rvCustomersList;
    List<Customer> customerList;
    CustomerListAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    public CustomerListFragment() {
        customerList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        rvCustomersList = view.findViewById(R.id.rvCustomersList);
        swipeRefreshLayout = view.findViewById(R.id.pullToRefresh);
        rvCustomersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCustomersList.setHasFixedSize(true);
        adapter = new CustomerListAdapter(getActivity(), customerList);
        rvCustomersList.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (Common.isNetworkAvailable(getActivity())) {
                CustomerTask task = new CustomerTask();
                task.execute();
            }else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

        });
        if (Common.isNetworkAvailable(getActivity())) {
            CustomerTask task = new CustomerTask();
            task.execute();
        }else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Common.isNetworkAvailable(getActivity())) {
            CustomerTask task = new CustomerTask();
            task.execute();
        }else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
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
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }, error -> Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show());
            Volley.newRequestQueue(getActivity()).add(request);
            return customerList;
        }

        @Override
        protected void onPostExecute(List<Customer> customers) {
            super.onPostExecute(customers);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
