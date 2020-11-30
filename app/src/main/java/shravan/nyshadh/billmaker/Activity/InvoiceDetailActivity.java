package shravan.nyshadh.billmaker.Activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Invoice;
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
        invoice = (Invoice) getIntent().getSerializableExtra(Common.ACTION_INVOICE_DETAIL);
        webView.getSettings().setJavaScriptEnabled(true);
        dialog = new ProgressDialog(InvoiceDetailActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        webView.setWebChromeClient(new WebChromeClient());


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
        webView.loadUrl(invoice != null ? "https://eyeplus.xtechsoftsolution.com/invoice_pdf.php?cust_id=2&last_id=9" : "");

        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse("https://eyeplus.xtechsoftsolution.com/invoice_pdf.php?cust_id=2&last_id=9"));

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "INVOICE" + invoice.getInvoiceId());
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                    Toast.LENGTH_LONG).show();

        });
    }
}