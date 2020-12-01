package shravan.nyshadh.billmaker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.R;

import static com.android.volley.Request.Method.POST;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        if (getSharedPreferences(Common.KEY_LOGIN, MODE_PRIVATE).getBoolean(Common.IS_LOGGEDIN, false)) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(view -> {
            if (TextUtils.isEmpty(etUsername.getText().toString().trim()) || TextUtils.isEmpty(etPassword.getText().toString().trim())) {
                Toasty.warning(getApplicationContext(), "Enter valid username and password", Toasty.LENGTH_SHORT).show();
            } else {
                StringRequest request = new StringRequest(POST, Common.LOGIN, response -> {
                    if (!response.equalsIgnoreCase("error")) {
                        Toasty.success(getApplicationContext(), "Login Successfully!", Toasty.LENGTH_SHORT).show();
                        getSharedPreferences(Common.KEY_LOGIN, MODE_PRIVATE).edit().putBoolean(Common.IS_LOGGEDIN, true).putString(Common.USERID, response.trim()).apply();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toasty.error(getApplicationContext(), "Enter valid username and password", Toasty.LENGTH_SHORT).show();
                        getSharedPreferences(Common.KEY_LOGIN, MODE_PRIVATE).edit().putBoolean(Common.IS_LOGGEDIN, false).putString(Common.USERID, response.trim()).apply();
                    }
                }, error -> Toasty.error(getApplicationContext(), "Something went wrong!", Toasty.LENGTH_SHORT).show()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("uname", etUsername.getText().toString());
                        params.put("pass", etPassword.getText().toString());
                        return params;
                    }
                };
                if (!getSharedPreferences(Common.KEY_LOGIN, MODE_PRIVATE).getBoolean(Common.IS_LOGGEDIN, false)) {
                    Volley.newRequestQueue(getApplicationContext()).add(request);
                } else {
                    Toasty.warning(getApplicationContext(), "Already logged in", Toasty.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }
}