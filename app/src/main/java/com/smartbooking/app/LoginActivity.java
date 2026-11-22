package com.smartbooking.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.smartbooking.app.data.api.ApiClient;
import com.smartbooking.app.data.model.LoginRequest;
import com.smartbooking.app.data.model.LoginResponse;
import com.smartbooking.app.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tokenManager = new TokenManager(this);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> intentarLogin());
    }

    private void intentarLogin() {
        String email = etEmail.getText() == null ? "" : etEmail.getText().toString().trim();
        String password = etPassword.getText() == null ? "" : etPassword.getText().toString();

        if (!validar(email, password)) {
            return;
        }

        btnLogin.setEnabled(false);

        ApiClient.getService().login(new LoginRequest(email, password))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        btnLogin.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse body = response.body();
                            tokenManager.guardarSesion(body.getAccess(), body.getRefresh(),
                                    body.getRol(), body.getNombre());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else if (response.code() == 400) {
                            mostrarMensaje(R.string.login_error_400);
                        } else if (response.code() == 401) {
                            mostrarMensaje(R.string.login_error_401);
                        } else {
                            mostrarMensaje(R.string.login_error_servidor);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        btnLogin.setEnabled(true);
                        mostrarMensaje(R.string.login_error_red);
                    }
                });
    }

    private boolean validar(String email, String password) {
        tilEmail.setError(null);
        tilPassword.setError(null);
        boolean ok = true;

        if (email.isEmpty()) {
            tilEmail.setError(getString(R.string.login_error_email_vacio));
            ok = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.login_error_email_invalido));
            ok = false;
        }
        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.login_error_password_vacia));
            ok = false;
        }
        return ok;
    }

    private void mostrarMensaje(int idTexto) {
        Toast.makeText(this, idTexto, Toast.LENGTH_LONG).show();
    }
}