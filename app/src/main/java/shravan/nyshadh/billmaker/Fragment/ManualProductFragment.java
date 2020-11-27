package shravan.nyshadh.billmaker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.NumberFormat;
import java.util.Locale;

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
    public void onResume() {
        super.onResume();
        setValues();
    }


    private void setValues() {
        if (Common.SELECTED_PRODUCTS != null && Common.SELECTED_PRODUCTS.size() > 0) {
            double discountAmount = 0, grandTotal = 0, subTotal = 0;

            for (Product product : Common.SELECTED_PRODUCTS) {
                if (product.getDiscountPercentage() > 0) {
                    discountAmount = discountAmount + product.getDiscountAmount();
                }
                if (Integer.parseInt(product.getSellprice()) > 0) {
                    subTotal = subTotal + (product.getQuantity() * Integer.parseInt(product.getSellprice()));
                }
            }
            grandTotal = subTotal - discountAmount;
            tvDiscountAmount.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(discountAmount));
            tvSubTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(subTotal));
            tvGrandTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(grandTotal));
        }
    }
}
