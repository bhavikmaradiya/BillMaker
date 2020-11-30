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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Product;
import shravan.nyshadh.billmaker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManualProductFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    TextView tvSubTotal, tvTaxPercentage, tvTaxAmount, tvDiscountAmount, tvGrandTotal;
    ImageView imgDate, imgRemoveTax;
    EditText etRemarks, etDate, etPlace;
    Button createBtn;
    View view;
    RadioGroup radioGrp;
    String date;
    Calendar now;
    Activity activity;
    boolean isVisible = false;
    CreateInvoiceListener createInvoiceListener;
    private RelativeLayout taxAmountCard, discountCard;

    public interface CreateInvoiceListener {
        void onCreateInvoice();
    }

    public ManualProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        this.createInvoiceListener = (CreateInvoiceListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manual_product, container, false);
        initView();
        setUpListener();
        return view;
    }

    private void setUpListener() {
        etDate.setOnClickListener(etDateView -> selectDate());
        imgDate.setOnClickListener(imgDateView -> selectDate());
        createBtn.setOnClickListener(createBtnView -> {
            if (createInvoiceListener != null) {
                try {
                    createInvoiceListener.onCreateInvoice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        tvTaxPercentage.setOnClickListener(tvTaxPercentageView -> {
            if (tvTaxPercentage.getText().toString().trim().equals("Apply")) {
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
            calculateValues();
        });
        createBtn.setOnClickListener(createBtnView -> {
            if (Common.SELECTED_PRODUCTS == null || Common.SELECTED_PRODUCTS.isEmpty() || Common.selectedCustomer == null) {
                Toasty.warning(activity, "Add some products to continue", Toasty.LENGTH_SHORT).show();
            } else if (etRemarks.getText().toString().trim().isEmpty()) {
                etRemarks.requestFocus();
                etRemarks.setError("Enter remarks");
            } else if (date == null || date.trim().isEmpty()) {
                etDate.requestFocus();
                etDate.setError("Enter valid date");
                Toasty.warning(activity, "Enter valid date", Toasty.LENGTH_SHORT).show();
            } else if (etPlace.getText().toString().trim().isEmpty()) {
                etPlace.requestFocus();
                etPlace.setError("Enter valid place");
            } else if (etPlace.getText().toString().trim().isEmpty()) {
                etPlace.requestFocus();
                etPlace.setError("Enter valid place");
            } else if (radioGrp.getCheckedRadioButtonId() == -1) {
                Toasty.warning(activity, "Select valid status", Toasty.LENGTH_SHORT).show();
            } else {
                StringRequest request = new StringRequest(Request.Method.POST, Common.CREATE_INVOICE, response -> {
                    if (response.equalsIgnoreCase("success")) {
                        Toasty.success(activity, "Invoice Created Successfully", Toasty.LENGTH_SHORT).show();
                        Common.selectedCustomer = null;
                        Common.TAX_PERCENT = 0;
                        Common.SELECTED_PRODUCTS = null;
                        activity.finish();
                    }
                }, error -> Toasty.error(activity, Objects.requireNonNull(error.getMessage()), Toasty.LENGTH_SHORT).show()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("cust_id", String.valueOf(Common.selectedCustomer.getCustomerId()));
                        params.put("user_id", "1");
                        params.put("product_id", Common.getProductsId());
                        params.put("quantity", Common.getProductsQuantity());
                        params.put("discount_percentage", Common.getProductsDiscount());
                        params.put("tax_percentage", String.valueOf(Common.TAX_PERCENT));
                        params.put("remarks", etRemarks.getText().toString());
                        params.put("time", date);
                        params.put("place", etPlace.getText().toString());
                        params.put("status", radioGrp.getCheckedRadioButtonId() == R.id.pending ? "Pending" : "Paid");
                        return params;
                    }
                };
                Volley.newRequestQueue(activity).add(request);
            }
        });
    }

    private void selectDate() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show(getChildFragmentManager(), "SELECT DATE");
    }

    private void initView() {
        tvSubTotal = view.findViewById(R.id.tvSubTotal);
        now = Calendar.getInstance();
        tvTaxPercentage = view.findViewById(R.id.tvTaxPercentage);
        imgRemoveTax = view.findViewById(R.id.imgRemoveTax);
        tvTaxAmount = view.findViewById(R.id.tvTaxAmount);
        createBtn = view.findViewById(R.id.createBtn);
        radioGrp = view.findViewById(R.id.radioGrp);
        tvDiscountAmount = view.findViewById(R.id.tvDiscountAmount);
        tvGrandTotal = view.findViewById(R.id.tvGrandTotal);
        taxAmountCard = view.findViewById(R.id.taxAmountCard);
        imgDate = view.findViewById(R.id.imgDate);
        etRemarks = view.findViewById(R.id.etRemarks);
        etDate = view.findViewById(R.id.etDate);
        discountCard = view.findViewById(R.id.discountCard);
        etPlace = view.findViewById(R.id.etPlace);
        etDate.setText(now.get(Calendar.DAY_OF_MONTH) + "/" + now.get(Calendar.MONTH) + "/" + now.get(Calendar.YEAR));
        date = now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DAY_OF_MONTH);
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
            tvTaxPercentage.setTextColor(Color.parseColor("#2E9B00"));
            taxAmountCard.setVisibility(View.GONE);
        } else {
            tvTaxPercentage.setText(Common.TAX_PERCENT + " %");
            imgRemoveTax.setVisibility(View.VISIBLE);
            tvTaxPercentage.setTextColor(Color.parseColor("#303030"));
            taxAmountCard.setVisibility(View.VISIBLE);
            tvTaxAmount.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(taxAmount));
        }

        if (discountAmount == 0) {
            discountCard.setVisibility(View.GONE);
        } else {
            discountCard.setVisibility(View.VISIBLE);
            tvDiscountAmount.setText(" - " + NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(discountAmount));
        }

        tvSubTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(subTotal));
        tvGrandTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(grandTotal));
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = year + "-" + monthOfYear + "-" + dayOfMonth;
        etDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
    }
}
