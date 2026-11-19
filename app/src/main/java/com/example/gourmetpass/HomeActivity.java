package com.example.gourmetpass;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private ImageView ivBannerGourmet;
    private LinearLayout containerPromos;
    private LinearLayout containerRedeemed;
    private Button btnLimpiarHistorial;

    private ExecutorService executorService;
    private AppDatabase db;
    private String usuarioActual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        ivBannerGourmet = findViewById(R.id.ivBannerGourmet);
        containerPromos = findViewById(R.id.containerPromos);
        containerRedeemed = findViewById(R.id.containerRedeemed);
        btnLimpiarHistorial = findViewById(R.id.btnLimpiarHistorial);

        // Inicializar ExecutorService y Room Database
        executorService = Executors.newSingleThreadExecutor();
        db = AppDatabase.getInstance(this);

        // Descargar imagen desde URL usando Glide
        String urlImagen = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800";
        if (ivBannerGourmet != null) {
            Glide.with(this)
                    .load(urlImagen)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(ivBannerGourmet);
        }

        // Recibir nombre de usuario enviado desde MainActivity
        String usuario = getIntent().getStringExtra("USUARIO_NOMBRE");
        if (usuario != null && !usuario.isEmpty()) {
            usuarioActual = usuario;
            tvWelcome.setText("¡Bienvenido, " + usuario + "!");
        }

        // Cargar pases canjeados guardados previamente en Room
        cargarPasesGuardados();

        if (btnLimpiarHistorial != null) {
            btnLimpiarHistorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    limpiarHistorialEnDb();
                }
            });
        }

        String[] restaurantes = {
                "2x1 en Platos Principales - Bistro Italia",
                "30% OFF en Menú Ejecutivo - Sushi Gourmet",
                "Postre Gratis con tu Reserva - Burger Craft",
                "2x1 en Coctelería - La Cava Bar",
                "Descuento del 20% - Cafetería Central"
        };

        for (final String promo : restaurantes) {
            Button btnPromo = new Button(this);
            btnPromo.setText(promo);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 12);
            btnPromo.setLayoutParams(params);

            btnPromo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HomeActivity.this, "Canjeaste: " + promo, Toast.LENGTH_SHORT).show();

                    // Agregar dinámicamente a la UI y persistir en Room en segundo plano
                    agregarPaseEnUi(promo);
                    guardarPaseEnDb(promo, usuarioActual);
                }
            });

            containerPromos.addView(btnPromo);
        }
    }

    private void agregarPaseEnUi(String promo) {
        TextView tvPaseCanjeado = new TextView(HomeActivity.this);
        tvPaseCanjeado.setText("✓ " + promo);
        tvPaseCanjeado.setPadding(24, 20, 24, 20);
        tvPaseCanjeado.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);

        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        itemParams.setMargins(0, 0, 0, 10);
        tvPaseCanjeado.setLayoutParams(itemParams);

        containerRedeemed.addView(tvPaseCanjeado);
    }

    private void guardarPaseEnDb(String descripcion, String usuario) {
        executorService.execute(() -> {
            PaseCanjeado pase = new PaseCanjeado(descripcion, usuario);
            db.paseCanjeadoDao().insertar(pase);
        });
    }

    private void cargarPasesGuardados() {
        executorService.execute(() -> {
            List<PaseCanjeado> pases = db.paseCanjeadoDao().obtenerTodos();
            runOnUiThread(() -> {
                for (PaseCanjeado pase : pases) {
                    agregarPaseEnUi(pase.descripcion);
                }
            });
        });
    }

    private void limpiarHistorialEnDb() {
        executorService.execute(() -> {
            db.paseCanjeadoDao().borrarTodos();
            runOnUiThread(() -> {
                containerRedeemed.removeAllViews();
                Toast.makeText(HomeActivity.this, "Historial limpiado", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}