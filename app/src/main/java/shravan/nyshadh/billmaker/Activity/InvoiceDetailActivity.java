package shravan.nyshadh.billmaker.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Invoice;
import shravan.nyshadh.billmaker.Modal.JavaScriptInterface;
import shravan.nyshadh.billmaker.R;

public class InvoiceDetailActivity extends AppCompatActivity {
    WebView webView;
    Invoice invoice;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);
        webView = findViewById(R.id.webView);
        if (!getSharedPreferences(Common.KEY_LOGIN, MODE_PRIVATE).getBoolean(Common.IS_LOGGEDIN, false)) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        invoice = (Invoice) getIntent().getSerializableExtra(Common.ACTION_INVOICE_DETAIL);
        webView.getSettings().setJavaScriptEnabled(true);
        dialog = new ProgressDialog(InvoiceDetailActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        webView.setWebChromeClient(new WebChromeClient());

        if (!new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Eyeplus/").exists()) {
            new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Eyeplus/").mkdirs();
        }


        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };

        webView.setWebViewClient(client);
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        webView.loadUrl(invoice != null ? invoice.getInvoiceUrl() : "");
        webView.addJavascriptInterface(new JavaScriptInterface(InvoiceDetailActivity.this, String.valueOf(invoice.getInvoiceId())), "Android");

        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            webView.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url));
        });
    }
}