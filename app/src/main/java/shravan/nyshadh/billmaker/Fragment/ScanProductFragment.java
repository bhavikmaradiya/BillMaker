package shravan.nyshadh.billmaker.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import shravan.nyshadh.billmaker.Adapter.ProductListAdapter;
import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Product;
import shravan.nyshadh.billmaker.R;

import static com.android.volley.Request.Method.GET;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanProductFragment extends Fragment implements ZXingScannerView.ResultHandler, ProductListAdapter.ProductOptionsListener {
    private ZXingScannerView mScannerView;
    private RecyclerView productListRecyclerView;
    private RelativeLayout placeholder_view;
    private ProductListAdapter productListAdapter;
    private Activity activity;
    boolean isVisible = false;

    public ScanProductFragment() {
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
        View view = inflater.inflate(R.layout.fragment_scan_product, container, false);
        productListRecyclerView = view.findViewById(R.id.productList);
        productListRecyclerView.setHasFixedSize(true);
        productListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FrameLayout scannerFragment = view.findViewById(R.id.qrcodeFragment);
        placeholder_view = view.findViewById(R.id.placeholder_view);
        productListAdapter = new ProductListAdapter(activity == null ? getActivity() : activity, this);
        productListRecyclerView.setAdapter(productListAdapter);
        mScannerView = new ZXingScannerView(activity == null ? getActivity() : activity);
        scannerFragment.addView(mScannerView);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 552 && grantResults.length > 0) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        if (productListAdapter != null) {
            if (productListAdapter.hasProducts()) {
                productListRecyclerView.setVisibility(View.VISIBLE);
                placeholder_view.setVisibility(View.GONE);
            } else {
                productListRecyclerView.setVisibility(View.GONE);
                placeholder_view.setVisibility(View.VISIBLE);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        552);
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (productListAdapter != null) {
            if (isVisible) {
                mScannerView.stopCamera();
            }
            StringRequest request = new StringRequest(GET, Common.GET_PRODUCT + Integer.parseInt(rawResult.getText()), response -> {
                Dialog dialog = new Dialog(activity);
                View view = LayoutInflater.from(activity).inflate(R.layout.apply_discount, null, false);
                EditText etDiscountPercentage = view.findViewById(R.id.etDiscountPercentage);
                Button cancel = view.findViewById(R.id.cancelBtn);
                Button apply = view.findViewById(R.id.applyBtn);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(view);
                try {
                    if (isVisible && !dialog.isShowing() && new JSONArray(response != null ? response : "[]").length() > 0) {
                        dialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                apply.setOnClickListener(applyView -> {
                    if (etDiscountPercentage.getText().toString().trim().length() > 0 && Double.parseDouble(etDiscountPercentage.getText().toString()) != 0) {
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            Product product = new Product();
                            product.setProductId(object.getInt("product_id"));
                            product.setName(object.getString("name"));
                            product.setSellprice(object.getString("sellprice"));
                            product.setUnitprice(object.getString("unitprice"));
                            product.setBrand(object.getString("brand"));
                            product.setColor(object.getString("color"));
                            product.setType(object.getString("type"));
                            product.setDiscountPercentage(Double.parseDouble(etDiscountPercentage.getText().toString()));
                            product.setTotalprice(object.getString("totalprice"));
                            productListAdapter.addProduct(product);
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (productListAdapter.hasProducts()) {
                                productListRecyclerView.setVisibility(View.VISIBLE);
                                placeholder_view.setVisibility(View.GONE);
                            } else {
                                productListRecyclerView.setVisibility(View.GONE);
                                placeholder_view.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toasty.error(activity, "Something went wrong!", Toasty.LENGTH_SHORT).show();
                            mScannerView.startCamera();
                            mScannerView.resumeCameraPreview(this);
                            e.printStackTrace();
                        }
                    } else {
                        Toasty.warning(activity, "Enter valid value", Toasty.LENGTH_SHORT).show();
                    }
                });
                cancel.setOnClickListener(cancelView -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        JSONObject object = array.getJSONObject(0);
                        Product product = new Product();
                        product.setProductId(object.getInt("product_id"));
                        product.setName(object.getString("name"));
                        product.setSellprice(object.getString("sellprice"));
                        product.setUnitprice(object.getString("unitprice"));
                        product.setBrand(object.getString("brand"));
                        product.setColor(object.getString("color"));
                        product.setType(object.getString("type"));
                        product.setTotalprice(object.getString("totalprice"));
                        productListAdapter.addProduct(product);
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (productListAdapter.hasProducts()) {
                            productListRecyclerView.setVisibility(View.VISIBLE);
                            placeholder_view.setVisibility(View.GONE);
                        } else {
                            productListRecyclerView.setVisibility(View.GONE);
                            placeholder_view.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toasty.error(activity, "Something went wrong!", Toasty.LENGTH_SHORT).show();
                        mScannerView.startCamera();
                        mScannerView.resumeCameraPreview(this);
                        e.printStackTrace();
                    }
                });

            }, error -> {
                Toasty.error(activity, "Something went wrong!", Toasty.LENGTH_SHORT).show();
                mScannerView.startCamera();
                mScannerView.resumeCameraPreview(this);
            });
            request.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            try {
                Integer.parseInt(rawResult.getText());
                if (isVisible && !productListAdapter.isAdded(Integer.parseInt(rawResult.getText()))) {
                    Volley.newRequestQueue(activity).add(request);
                } else if (!isVisible) {
                    mScannerView.startCamera();
                    mScannerView.resumeCameraPreview(this);
                }
            } catch (Exception e) {
                if (isVisible) {
                    Toasty.error(activity, "Something went wrong!", Toasty.LENGTH_SHORT).show();
                    mScannerView.startCamera();
                    mScannerView.resumeCameraPreview(this);
                }
                e.printStackTrace();
            }


        }


    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onApplyDiscount(int position, Product product) {
        Dialog dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.apply_discount, null, false);
        EditText etDiscountPercentage = view.findViewById(R.id.etDiscountPercentage);
        Button cancel = view.findViewById(R.id.cancelBtn);
        Button apply = view.findViewById(R.id.applyBtn);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        etDiscountPercentage.setText(product.getDiscountPercentage() == 0 ? "" : String.valueOf(product.getDiscountPercentage()));
        try {
            if (isVisible && !dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        apply.setOnClickListener(applyView -> {
            if (etDiscountPercentage.getText().toString().trim().length() > 0) {
                if (productListAdapter != null)
                    productListAdapter.applyDiscount(position, Double.parseDouble(etDiscountPercentage.getText().toString()));
                if (dialog.isShowing()) dialog.dismiss();
            } else {
                Toasty.warning(activity, "Enter valid value", Toasty.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(cancelView -> {
            if (productListAdapter != null)
                productListAdapter.applyDiscount(position, 0);
            if (dialog.isShowing()) dialog.dismiss();
        });
    }

    @Override
    public void onProductAdded() {
        mScannerView.startCamera();
        mScannerView.resumeCameraPreview(this);
        if (productListAdapter.hasProducts()) {
            productListRecyclerView.setVisibility(View.VISIBLE);
            placeholder_view.setVisibility(View.GONE);
        } else {
            productListRecyclerView.setVisibility(View.GONE);
            placeholder_view.setVisibility(View.VISIBLE);
        }
    }
}
