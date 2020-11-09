package shravan.nyshadh.billmaker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class AddNewCustomerActivity extends AppCompatActivity {
    RadioGroup radioGrp;
    RadioButton checkedGender;
    EditText etFullName, etPhoneNumber, etSecondaryPhoneNumber, etEmail, etAge, etAddress, etRemarks, etRightIPD, etLeftIPD;
    Spinner spPrescribers;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        initView();

    }

    private void initView() {
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
    }
}
