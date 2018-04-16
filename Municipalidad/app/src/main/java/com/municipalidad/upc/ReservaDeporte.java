package com.municipalidad.upc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.municipalidad.upc.adapter.ReservaAdapter;
import com.municipalidad.upc.entidades.Reserva;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReservaDeporte extends AppCompatActivity {

    Button btnOtrasReservas;

    RecyclerView recyclerView;
    ReservaAdapter reservaAdapter;
    ArrayList<Reserva> reservas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_deporte);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.rv_pagos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayout);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOtrasReservas = findViewById(R.id.btnOtrasReservas);

        btnOtrasReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservaDeporte.this, GrabarReservaDeporte.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarReservas();
    }

    public void cargarReservas(){
        reservas.clear();

        RequestQueue queue;
        queue = Volley.newRequestQueue(ReservaDeporte.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BuildConfig.urlReserva + "/9571716", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        deserializarReservas(response);
                        reservaAdapter = new ReservaAdapter(ReservaDeporte.this, reservas);
                        recyclerView.setAdapter(reservaAdapter);
                        reservaAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReservaDeporte.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void deserializarReservas(JSONObject jsonObject) {
        try {
            JSONArray reserva = new JSONArray(jsonObject.getString("reserva"));
            for (int i = 0; i < reserva.length(); i++) {
                JSONObject item = reserva.getJSONObject(i);
                Reserva reserva1 = new Reserva();
                reserva1.setIdHorario(item.getInt("idhorario"));
                reserva1.setDisciplina(item.getString("disciplina"));
                reserva1.setSede(item.getString("dessed"));
                reserva1.setDia(item.getString("sem"));
                reserva1.setHoraInicio(item.getString("horainicio"));
                reserva1.setHoraFin(item.getString("horafin"));
                reserva1.setEdadDesde(item.getString("edaddesde"));
                reserva1.setEdadHasta(item.getString("edadhasta"));
                reserva1.setDisponibles(item.getInt("disponible"));
                reserva1.setPrecio(item.getDouble("Costo"));
                reserva1.setIdMatricula(item.getInt("idmatricula"));
                reserva1.setIdActividad(item.getInt("idactividad"));

                reservas.add(reserva1);
            }

            reservaAdapter = new ReservaAdapter(this, reservas);
            recyclerView.setAdapter(reservaAdapter);
            reservaAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Toast.makeText(this, "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
