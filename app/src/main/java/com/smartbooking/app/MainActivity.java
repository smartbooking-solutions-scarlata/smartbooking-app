package com.smartbooking.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import com.smartbooking.app.data.api.ApiClient;
import com.smartbooking.app.data.model.LogoutRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smartbooking.app.utils.TokenManager;

public class MainActivity extends AppCompatActivity {

    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenManager = new TokenManager(this);

        TextView tvBienvenida = findViewById(R.id.tvBienvenida);
        Button btnLogout = findViewById(R.id.btnLogout);

        String nombre = tokenManager.getNombre();
        if (nombre != null && !nombre.isEmpty()) {
            tvBienvenida.setText(getString(R.string.bienvenida) + ", " + nombre);
        }

        btnLogout.setOnClickListener(v -> cerrarSesion());
    }

    private void cerrarSesion() {
        String refresh = tokenManager.getRefresh();

        if (refresh == null || refresh.isEmpty()) {
            irAlLogin();
            return;
        }

        ApiClient.getService(this).logout(new LogoutRequest(refresh))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        irAlLogin();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        irAlLogin();
                    }
                });
    }

    private void irAlLogin() {
        tokenManager.borrarSesion();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}