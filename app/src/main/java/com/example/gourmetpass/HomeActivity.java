package com.example.gourmetpass;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout containerPasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        containerPasses = findViewById(R.id.containerPasses);

        // Arreglo de datos que se generarán dinámicamente
        String[] restaurantes = {
                "2x1 en Platos Principales - Bistro Italia",
                "30% OFF en Menú Ejecutivo - Sushi Gourmet",
                "Postre Gratis con tu Reserva - Burger Craft",
                "2x1 en Coctelería - La Cava Bar",
                "Descuento del 20% - Cafetería Central"
        };

        // Creación dinámica de botones desde Java (Requisito del Parcial)
        for (final String promo : restaurantes) {
            Button btnPromo = new Button(this);
            btnPromo.setText(promo);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 12, 0, 12);
            btnPromo.setLayoutParams(params);

            btnPromo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HomeActivity.this, "Canjeaste: " + promo, Toast.LENGTH_LONG).show();
                }
            });

            containerPasses.addView(btnPromo);
        }
    }
}