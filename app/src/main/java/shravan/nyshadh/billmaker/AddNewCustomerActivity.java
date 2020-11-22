package shravan.nyshadh.billmaker;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Customer;
import shravan.nyshadh.billmaker.Modal.Presciber;

import static shravan.nyshadh.billmaker.Modal.Common.IS_NEW;

public class AddNewCustomerActivity extends AppCompatActivity {
    RadioGroup radioGrp;
    RadioButton checkedGender;
    EditText etFullName, etPhoneNumber, etSecondaryPhoneNumber, etEmail, etAge, etAddress, etRemarks, etRightIPD, etLeftIPD;
    Spinner spPrescribers;
    Button btnSave;
    boolean isNew;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        initView();
        if (!isNew) setValues();


    }

    private void setValues() {
        if (customer != null) {
            etFullName.setText(customer.getCustomerName() != null && !customer.getCustomerName().isEmpty() ? customer.getCustomerName() : "");
            etPhoneNumber.setText(customer.getCustomerPhone() != null && !customer.getCustomerPhone().isEmpty() ? customer.getCustomerPhone() : "");
            etSecondaryPhoneNumber.setText(customer.getCustomerPhone2() != null && !customer.getCustomerPhone2().isEmpty() ? customer.getCustomerPhone2() : "");
            etEmail.setText(customer.getCustomerEmail() != null && !customer.getCustomerEmail().isEmpty() ? customer.getCustomerEmail() : "");
            etAge.setText(customer.getCustomerAge() != null && !customer.getCustomerAge().isEmpty() ? customer.getCustomerAge() : "");
            etAddress.setText(customer.getCustomerAddress() != null && !customer.getCustomerAddress().isEmpty() ? customer.getCustomerAddress() : "");
            etRemarks.setText(customer.getCustomerRemarks() != null && !customer.getCustomerRemarks().isEmpty() ? customer.getCustomerRemarks() : "");
            etRightIPD.setText(customer.getCustomerRightIPD() != null && !customer.getCustomerRightIPD().isEmpty() ? customer.getCustomerRightIPD() : "");
            etLeftIPD.setText(customer.getCustomerLeftIPD() != null && !customer.getCustomerLeftIPD().isEmpty() ? customer.getCustomerLeftIPD() : "");
            if (customer.getCustomerGender() != null && !customer.getCustomerGender().isEmpty()) {
                radioGrp.check(customer.getCustomerGender().equals("male") ? R.id.male : R.id.female);
            }
        }
    }

    private void initView() {
        isNew = getIntent().getBooleanExtra(IS_NEW, false);
        radioGrp = findViewById(R.id.radioGrp);
        etAddress = findViewById(R.id.etAddress);
        etFullName = findViewById(R.id.etFullName);
        etAge = findViewById(R.id.etAge);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etSecondaryPhoneNumber = findViewById(R.id.etSecondaryPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        etRemarks = findViewById(R.id.etRemarks);
        etRightIPD = findViewById(R.id.etRightIPD);
        etLeftIPD = findViewById(R.id.etLeftIPD);
        spPrescribers = findViewById(R.id.spPrescribers);
        btnSave = findViewById(R.id.btnSave);
        customer = !isNew ? (Customer) getIntent().getSerializableExtra(Common.CUSTOMER) : null;
    }

    private static class PrescriberSpinnerAdapter extends BaseAdapter {

        private Context context;
        private List<Presciber> presciberList;

        public PrescriberSpinnerAdapter(Context context, List<Presciber> presciberList) {
            this.context = context;
            this.presciberList.add(0, new Presciber(null, "Select"));
            this.presciberList.addAll(presciberList);
        }

        @Override
        public int getCount() {
            return presciberList.size();
        }

        @Override
        public Integer getItem(int position) {
            return presciberList.get(position).getPrescriberId();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
