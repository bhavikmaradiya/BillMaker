package shravan.nyshadh.billmaker.Modal;

import java.util.List;

public class Common {
    public static final String ACTION_INVOICE_CALL = "actionCall";
    public static final String ACTION_INVOICE_WHATSAPP = "actionWhatsapp";
    public static final String ACTION_INVOICE_DETAIL = "actionDetails";
    public static final String CUSTOMER = "customer";
    public static final String IS_NEW = "isNew";
    public static List<Product> SELECTED_PRODUCTS;
    public static Customer selectedCustomer;

    private static final String BASE_URL = "http://192.168.1.6:8000/";
    private static final String API = BASE_URL + "api/";
    public static final String GET_CUSTOMERS = API + "get-customers";
    public static final String GET_PRESCRIBERS = API + "get-prescribers";

}
