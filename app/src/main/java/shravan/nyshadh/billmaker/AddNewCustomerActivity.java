package shravan.nyshadh.billmaker;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Customer;
import shravan.nyshadh.billmaker.Modal.Prescriber;

import static shravan.nyshadh.billmaker.Modal.Common.GET_PRESCRIBERS;
import static shravan.nyshadh.billmaker.Modal.Common.IS_NEW;

public class AddNewCustomerActivity extends AppCompatActivity {
    RadioGroup radioGrp;
    EditText etFullName, etPhoneNumber, etSecondaryPhoneNumber, etEmail, etAge, etAddress, etRemarks, etRightIPD, etLeftIPD;
    Spinner spPrescribers;
    Button btnSave;
    List<Prescriber> prescriberList;
    boolean isNew;
    PrescriberSpinnerAdapter prescriberAdapter;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        initView();
        if (!isNew) setValues();

        btnSave.setOnClickListener(view -> {
            if (TextUtils.isEmpty(etFullName.getText().toString().trim())) {
                etFullName.requestFocus();
                etFullName.setError("Enter your name");
            } else if (TextUtils.isEmpty(etPhoneNumber.getText().toString().trim())) {
                etPhoneNumber.requestFocus();
                etPhoneNumber.setError("Enter your phone number");
            } else if (TextUtils.isEmpty(etSecondaryPhoneNumber.getText().toString().trim())) {
                etSecondaryPhoneNumber.requestFocus();
                etSecondaryPhoneNumber.setError("Enter your second phone number");
            } else if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                etEmail.requestFocus();
                etEmail.setError("Enter your email address");
            } else if (TextUtils.isEmpty(etAge.getText().toString().trim())) {
                etAge.requestFocus();
                etAge.setError("Enter your age");
            } else if (radioGrp.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Select your gender", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
                etAddress.requestFocus();
                etAddress.setError("Enter your address");
            } else if (TextUtils.isEmpty(etRemarks.getText().toString().trim())) {
                etRemarks.requestFocus();
                etRemarks.setError("Enter your remarks");
            } else if (TextUtils.isEmpty(etRightIPD.getText().toString().trim())) {
                etRightIPD.requestFocus();
                etRightIPD.setError("Enter right IPD");
            } else if (TextUtils.isEmpty(etLeftIPD.getText().toString().trim())) {
                etLeftIPD.requestFocus();
                etLeftIPD.setError("Enter left IPD");
            } else {
                if (isNew) {
                    StringRequest request = new StringRequest(Common.ADD_CUSTOMER, response -> {
                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                    }, error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show()) {
                        protected Map<String, String> getParams() {
                            Map<String, String> map = new HashMap<>();
                            map.put("cname", etFullName.getText().toString());
                            map.put("cphone", etPhoneNumber.getText().toString());
                            map.put("cphone2", etSecondaryPhoneNumber.getText().toString());
                            map.put("cemail", etEmail.getText().toString());
                            map.put("cage", etAge.getText().toString());
                            map.put("cgender", radioGrp.getCheckedRadioButtonId() == R.id.male ? "male" : "female");
                            map.put("caddress", etAddress.getText().toString());
                            map.put("cremarks", etRemarks.getText().toString());
                            map.put("right_IPD", etRightIPD.getText().toString());
                            map.put("left_IPD", etLeftIPD.getText().toString());
                            if (spPrescribers.getSelectedItem() != null) {
                                map.put("prescriber_id", String.valueOf((Integer) spPrescribers.getSelectedItem()));
                            }
                            return map;
                        }
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(request);
                }
            }
        });

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
        prescriberList = new ArrayList<>();
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
        getPrecriberList();
    }

    private void getPrecriberList() {
        StringRequest prescriberRequest = new StringRequest(GET_PRESCRIBERS, response -> {
            try {
                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Prescriber prescriber = new Prescriber();
                    prescriber.setPrescriberName(object.getString("prescriber_name"));
                    prescriber.setPrescriberId(object.getInt("prescriber_id"));
                    prescriber.setPrescriberEmail(object.getString("prescriber_email"));
                    prescriber.setPrescriberAddress(object.getString("prescriber_address"));
                    prescriber.setPrescriberPhone(object.getString("prescriber_phone"));
                    prescriberList.add(prescriber);
                }
                if (prescriberAdapter == null) {
                    prescriberAdapter = new PrescriberSpinnerAdapter(getApplicationContext(), prescriberList);
                    spPrescribers.setAdapter(prescriberAdapter);
                    prescriberAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(getApplicationContext()).add(prescriberRequest);
    }

    private static class PrescriberSpinnerAdapter extends BaseAdapter {

        private Context context;
        private List<Prescriber> prescriberList;

        public PrescriberSpinnerAdapter(Context context, List<Prescriber> prescriberList) {
            this.context = context;
            this.prescriberList = new ArrayList<>();
            this.prescriberList.add(0, new Prescriber(null, "Select"));
            this.prescriberList.addAll(prescriberList);
        }

        @Override
        public int getCount() {
            return prescriberList.size();
        }

        @Override
        public Integer getItem(int position) {
            return prescriberList.get(position).getPrescriberId();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false);
            TextView spinner_text = view.findViewById(R.id.spinner_text);
            spinner_text.setText(prescriberList.get(position).getPrescriberName());
            return view;
        }
    }
}
