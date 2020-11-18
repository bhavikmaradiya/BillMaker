package shravan.nyshadh.billmaker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import shravan.nyshadh.billmaker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManualProductFragment extends Fragment {
    TextView tvSubTotal, tvTax, tvTaxAmount, tvDiscountAmount, tvGrandTotal;
    ImageView imgDate;
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
        return view;
    }
}
