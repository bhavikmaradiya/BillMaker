package shravan.nyshadh.billmaker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Product;
import shravan.nyshadh.billmaker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManualProductFragment extends Fragment {
    TextView tvSubTotal, tvTax, tvTaxAmount, tvDiscountAmount, tvGrandTotal;
    ImageView imgDate;
    double discountPercentage;
    EditText etRemarks, etDate, etPlace;

    public ManualProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manual_product, container, false);
        tvSubTotal = view.findViewById(R.id.tvSubTotal);
        tvTax = view.findViewById(R.id.tvTax);
        tvTaxAmount = view.findViewById(R.id.tvTaxAmount);
        tvDiscountAmount = view.findViewById(R.id.tvDiscountAmount);
        tvGrandTotal = view.findViewById(R.id.tvGrandTotal);
        imgDate = view.findViewById(R.id.imgDate);
        etRemarks = view.findViewById(R.id.etRemarks);
        etDate = view.findViewById(R.id.etDate);
        etPlace = view.findViewById(R.id.etPlace);
        setValues();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            setValues();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setValues();
    }


    private void setValues() {
        Toast.makeText(getActivity(), "setValues", Toast.LENGTH_SHORT).show();
        if (Common.SELECTED_PRODUCTS != null && Common.SELECTED_PRODUCTS.size() > 0) {
            double discountAmount = 0;
            double totalPrice = 0;

            for (Product product : Common.SELECTED_PRODUCTS) {
                if (product.getDiscountPercentage() > 0) {
                    discountAmount = discountAmount + Integer.parseInt(product.getSellprice()) % product.getDiscountPercentage();
                }
                if (Double.parseDouble(product.getSellprice()) > 0) {
                    totalPrice = totalPrice + product.getQuantity() * Integer.parseInt(product.getSellprice());
                }
            }
            totalPrice = totalPrice - discountAmount;
            tvDiscountAmount.setText(String.valueOf(discountAmount));
            tvSubTotal.setText(String.valueOf(totalPrice));
            tvGrandTotal.setText(String.valueOf(totalPrice));
        }
    }
}
