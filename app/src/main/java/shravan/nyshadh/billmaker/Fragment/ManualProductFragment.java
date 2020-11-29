package shravan.nyshadh.billmaker.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Product;
import shravan.nyshadh.billmaker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManualProductFragment extends Fragment {
    TextView tvSubTotal, tvTaxPercentage, tvTaxAmount, tvDiscountAmount, tvGrandTotal;
    ImageView imgDate, imgRemoveTax;
    EditText etRemarks, etDate, etPlace;
    Activity activity;
    boolean isVisible = false;
    private RelativeLayout taxAmountCard;


    public ManualProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manual_product, container, false);
        tvSubTotal = view.findViewById(R.id.tvSubTotal);
        tvTaxPercentage = view.findViewById(R.id.tvTaxPercentage);
        imgRemoveTax = view.findViewById(R.id.imgRemoveTax);
        tvTaxAmount = view.findViewById(R.id.tvTaxAmount);
        tvDiscountAmount = view.findViewById(R.id.tvDiscountAmount);
        tvGrandTotal = view.findViewById(R.id.tvGrandTotal);
        taxAmountCard = view.findViewById(R.id.taxAmountCard);
        imgDate = view.findViewById(R.id.imgDate);
        etRemarks = view.findViewById(R.id.etRemarks);
        etDate = view.findViewById(R.id.etDate);
        etPlace = view.findViewById(R.id.etPlace);

        tvTaxPercentage.setOnClickListener(tvTaxPercentageView -> {
            if (tvTaxPercentage.getText().toString().contains("Apply")) {
                Dialog dialog = new Dialog(activity == null ? getActivity() : activity);
                View taxView = LayoutInflater.from(activity).inflate(R.layout.apply_tax, null, false);
                EditText etTaxPercentage = taxView.findViewById(R.id.etTaxPercentage);
                Button cancel = taxView.findViewById(R.id.cancelBtn);
                Button apply = taxView.findViewById(R.id.applyBtn);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(taxView);

                if (isVisible && !dialog.isShowing()) {
                    dialog.show();
                }

                apply.setOnClickListener(applyView -> {
                    if (etTaxPercentage.getText().toString().trim().length() > 0 && Double.parseDouble(etTaxPercentage.getText().toString()) != 0) {
                        Common.TAX_PERCENT = Double.parseDouble(etTaxPercentage.getText().toString());
                        calculateValues();
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    } else {
                        Toasty.warning(activity, "Enter valid value", Toasty.LENGTH_SHORT).show();
                    }
                });

                cancel.setOnClickListener(cancelView -> {
                    Common.TAX_PERCENT = 0;
                    calculateValues();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                });

            }
        });

        imgRemoveTax.setOnClickListener(imgRemoveTaxView -> {
            Common.TAX_PERCENT = 0;
            tvTaxPercentage.setText("Apply");
            calculateValues();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        calculateValues();
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
    }

    private void calculateValues() {
        double grandTotal = 0;
        double taxAmount = 0;
        double subTotal = 0;
        double discountAmount = 0;

        if (Common.SELECTED_PRODUCTS != null && Common.SELECTED_PRODUCTS.size() > 0) {
            for (Product product : Common.SELECTED_PRODUCTS) {
                if (product.getDiscountPercentage() > 0) {
                    discountAmount = discountAmount + product.getDiscountAmount();
                }
                if (Integer.parseInt(product.getSellprice()) > 0) {
                    subTotal = subTotal + (product.getQuantity() * Integer.parseInt(product.getSellprice()));
                }
            }
            taxAmount = subTotal * Common.TAX_PERCENT / 100;
            grandTotal = subTotal - discountAmount + taxAmount;
        }

        if (taxAmount == 0) {
            imgRemoveTax.setVisibility(View.GONE);
            tvTaxPercentage.setText("Apply");
            taxAmountCard.setVisibility(View.GONE);
        } else {
            tvTaxPercentage.setText(String.valueOf(Common.TAX_PERCENT) + " %");
            imgRemoveTax.setVisibility(View.VISIBLE);
            taxAmountCard.setVisibility(View.VISIBLE);
            tvTaxAmount.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(taxAmount));
        }

        tvDiscountAmount.setText(" - "+NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(discountAmount));
        tvSubTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(subTotal));
        tvGrandTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(grandTotal));
    }
}
