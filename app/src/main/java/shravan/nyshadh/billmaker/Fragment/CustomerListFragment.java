package shravan.nyshadh.billmaker.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shravan.nyshadh.billmaker.Adapter.CustomerListAdapter;
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

    public CustomerListFragment() {
        customerList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        rvCustomersList = view.findViewById(R.id.rvCustomersList);
        rvCustomersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCustomersList.setHasFixedSize(true);
        CustomerListAdapter adapter = new CustomerListAdapter(getActivity(), customerList);
        rvCustomersList.setAdapter(adapter);

        StringRequest request = new StringRequest(GET_CUSTOMERS, response -> {
            try {
                JSONArray array = new JSONArray(response);
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
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }, error -> {
            Toast.makeText(getActivity(), "error " + error.getMessage(), Toast.LENGTH_LONG).show();
        });
        Volley.newRequestQueue(getActivity()).add(request);
        return view;
    }
}
