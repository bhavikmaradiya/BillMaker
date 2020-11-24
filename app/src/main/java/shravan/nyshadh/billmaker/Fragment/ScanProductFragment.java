package shravan.nyshadh.billmaker.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import shravan.nyshadh.billmaker.Modal.Product;
import shravan.nyshadh.billmaker.Adapter.ProductListAdapter;
import shravan.nyshadh.billmaker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanProductFragment extends Fragment implements ZXingScannerView.ResultHandler, ProductListAdapter.ProductOptionsListener {
    private ZXingScannerView mScannerView;
    private RecyclerView productListRecyclerView;
    private RelativeLayout placeholder_view;
    private ProductListAdapter productListAdapter;

    public ScanProductFragment() {
        // Required empty public constructor
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
        productListAdapter = new ProductListAdapter(getActivity(), this);
        productListRecyclerView.setAdapter(productListAdapter);
        mScannerView = new ZXingScannerView(getActivity());
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
        if (productListAdapter.hasProducts()) {
            productListRecyclerView.setVisibility(View.VISIBLE);
            placeholder_view.setVisibility(View.GONE);
        } else {
            productListRecyclerView.setVisibility(View.GONE);
            placeholder_view.setVisibility(View.VISIBLE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    552);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (productListAdapter != null) {
            mScannerView.stopCamera();
            if (!productListAdapter.isAdded(1)) {
                Product product = new Product();
                product.setName(rawResult.getText());
                product.setQuantity(1);
                product.setProductId(1);
                product.setTotalprice("500");
                productListAdapter.addProduct(product);
            }
            if (productListAdapter.hasProducts()) {
                productListRecyclerView.setVisibility(View.VISIBLE);
                placeholder_view.setVisibility(View.GONE);
            } else {
                productListRecyclerView.setVisibility(View.GONE);
                placeholder_view.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onProductAdded() {
        mScannerView.startCamera();
        mScannerView.resumeCameraPreview(this);
    }
}
