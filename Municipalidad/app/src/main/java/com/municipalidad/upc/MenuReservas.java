package com.municipalidad.upc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MenuReservas extends AppCompatActivity {

    RelativeLayout rlvDeporte, rlvInstalacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_reservas);

        rlvDeporte = findViewById(R.id.rlvDeporte);
        rlvInstalacion = findViewById(R.id.rlvInstalacion);

        rlvDeporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuReservas.this, ReservaDeporte.class);
                startActivity(intent);
            }
        });
    }
}
