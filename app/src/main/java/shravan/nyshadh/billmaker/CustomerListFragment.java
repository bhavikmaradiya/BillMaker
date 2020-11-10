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
public class CustomerListFragment extends Fragment {
    View view;
    RecyclerView rvCustomersList;
    List<Customer> customerList;

    public CustomerListFragment() {
        customerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            customerList.add(new Customer("Bhavik", "+91 94289844" + String.valueOf(i)));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        rvCustomersList = view.findViewById(R.id.rvCustomersList);
        rvCustomersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCustomersList.setHasFixedSize(true);
        rvCustomersList.setAdapter(new CustomerListAdapter(getActivity(), customerList));

        return view;
    }
}
