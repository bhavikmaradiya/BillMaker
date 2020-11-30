package shravan.nyshadh.billmaker.Modal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

public class Common {
    public static final String ACTION_INVOICE_CALL = "actionCall";
    public static final String ACTION_INVOICE_WHATSAPP = "actionWhatsapp";
    public static final String ACTION_INVOICE_DETAIL = "actionDetails";
    public static final String CUSTOMER = "customer";
    public static final String IS_NEW = "isNew";
    public static final String IS_SELECTED = "isSelected";
    public static double TAX_PERCENT = 0;
    public static List<Product> SELECTED_PRODUCTS;
    public static Customer selectedCustomer;

    private static final String BASE_URL = "http://192.168.1.6:8000/";
    private static final String API = BASE_URL + "api/";
    public static final String GET_CUSTOMERS = API + "get-customers";
    public static final String GET_PRESCRIBERS = API + "get-prescribers";
    public static final String ADD_CUSTOMER = API + "add-customers";
    public static final String UPDATE_CUSTOMER = API + "update-customers/";
    public static final String GET_PRODUCT = API + "product-detail/";
    public static final String GET_INVOICES = API + "get-invoice";
    public static final String CREATE_INVOICE = API + "create-invoice";


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getProductsId() {
        if (SELECTED_PRODUCTS != null && !SELECTED_PRODUCTS.isEmpty()) {
            StringBuilder productsId = new StringBuilder();
            for (int i = 0; i < SELECTED_PRODUCTS.size(); i++) {
                productsId.append(String.valueOf(SELECTED_PRODUCTS.get(i).getProductId()));
                if (i != SELECTED_PRODUCTS.size() - 1) {
                    productsId.append(",");
                }
            }
            return productsId.toString();
        }
        return "";
    }

    public static String getProductsQuantity() {
        if (SELECTED_PRODUCTS != null && !SELECTED_PRODUCTS.isEmpty()) {
            StringBuilder productsQuantity = new StringBuilder();
            for (int i = 0; i < SELECTED_PRODUCTS.size(); i++) {
                productsQuantity.append(String.valueOf(SELECTED_PRODUCTS.get(i).getQuantity()));
                if (i != SELECTED_PRODUCTS.size() - 1) {
                    productsQuantity.append(",");
                }
            }
            return productsQuantity.toString();
        }
        return "";
    }

    public static String getProductsDiscount() {
        if (SELECTED_PRODUCTS != null && !SELECTED_PRODUCTS.isEmpty()) {
            StringBuilder productsDiscount = new StringBuilder();
            for (int i = 0; i < SELECTED_PRODUCTS.size(); i++) {
                productsDiscount.append(String.valueOf(SELECTED_PRODUCTS.get(i).getDiscountPercentage()));
                if (i != SELECTED_PRODUCTS.size() - 1) {
                    productsDiscount.append(",");
                }
            }
            return productsDiscount.toString();
        }
        return "";
    }


}
