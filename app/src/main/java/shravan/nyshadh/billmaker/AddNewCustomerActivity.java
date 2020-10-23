package shravan.nyshadh.billmaker;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class AddNewCustomerActivity extends AppCompatActivity {
    RadioGroup radioGrp;
    RadioButton male, female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        radioGrp = findViewById(R.id.radioGrp);
        male = findViewById(R.id.male);
//        male.setOnCheckedChangeListener(((buttonView, isChecked) -> {
//            radioGrp.clearCheck();
//        }));


    }
}
