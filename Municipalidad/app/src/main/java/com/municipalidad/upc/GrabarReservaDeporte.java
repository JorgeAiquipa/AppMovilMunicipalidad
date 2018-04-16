package com.municipalidad.upc;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.municipalidad.upc.adapter.HorarioActividadAdapter;
import com.municipalidad.upc.entidades.Actividad;
import com.municipalidad.upc.entidades.Dependiente;
import com.municipalidad.upc.entidades.Horario;
import com.municipalidad.upc.entidades.Sede;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class GrabarReservaDeporte extends AppCompatActivity {

    Spinner spDeportes, spSedes, spHijos;
    Button btnGrabarDeporte;

    ArrayList<Actividad> actividades = new ArrayList<>();
    ArrayList<Sede> sedes = new ArrayList<>();
    ArrayList<Dependiente> dependientes = new ArrayList<>();
    ArrayAdapter<Actividad> adapter;
    ArrayAdapter<Sede> adapter2;
    ArrayAdapter<Dependiente> adapter3;

    RecyclerView recyclerView;
    HorarioActividadAdapter horarioActividadAdapter;
    ArrayList<Horario> horarios = new ArrayList<>();

    private RequestQueue queue;

    int horario = 0;
    double precio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabar_reserva_deporte);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spDeportes = findViewById(R.id.spDeporte);
        spSedes = findViewById(R.id.spSede);
        spHijos = findViewById(R.id.spHijos);
        btnGrabarDeporte = findViewById(R.id.btnGrabarDeporte);

        spDeportes.getBackground().setColorFilter(getResources().getColor(R.color.negro), PorterDuff.Mode.SRC_ATOP);
        spSedes.getBackground().setColorFilter(getResources().getColor(R.color.negro), PorterDuff.Mode.SRC_ATOP);
        spHijos.getBackground().setColorFilter(getResources().getColor(R.color.negro), PorterDuff.Mode.SRC_ATOP);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.rv_horarios);
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

        cargarActividades();
        cargarSedes();
        cargarDependientes();

        spDeportes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarHorarios();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSedes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarHorarios();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spHijos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarHorarios();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGrabarDeporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<horarios.size(); i++){
                    if(horarios.get(i).getSeleccionado().equals("1")){
                        horario = horarios.get(i).getIdHorario();
                        precio = horarios.get(i).getPrecio();
                        break;
                    }
                }
                try {
                    new Grabar(GrabarReservaDeporte.this).execute(new URL(BuildConfig.urlReserva + "/grabar"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void cargarActividades(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, actividades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDeportes.setAdapter(adapter);

        adapter.clear();

        queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BuildConfig.urlActividad, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        deserializarActividades(response);
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GrabarReservaDeporte.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void cargarSedes(){
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sedes);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSedes.setAdapter(adapter2);

        adapter2.clear();

        queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BuildConfig.urlSede, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        deserializarSedes(response);
                        adapter2.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GrabarReservaDeporte.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void cargarDependientes(){
        adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dependientes);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHijos.setAdapter(adapter3);

        adapter3.clear();

        queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BuildConfig.urlDependiente + "/9571716", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        deserializarDependientes(response);
                        adapter3.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GrabarReservaDeporte.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void cargarHorarios(){
        horarios.clear();

        if(spDeportes.getSelectedItemPosition() != 0 && spSedes.getSelectedItemPosition() != 0 &&
                spHijos.getSelectedItemPosition() != 0) {
            int idActividad = actividades.get(spDeportes.getSelectedItemPosition()).getId();
            int idSede = sedes.get(spSedes.getSelectedItemPosition()).getId();
            int edadHijo = dependientes.get(spHijos.getSelectedItemPosition()).getEdad();

            queue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BuildConfig.urlHorario + "/" + idActividad + "/" + idSede + "/" + edadHijo, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            deserializarHorarios(response);
                            horarioActividadAdapter = new HorarioActividadAdapter(GrabarReservaDeporte.this, horarios);
                            recyclerView.setAdapter(horarioActividadAdapter);
                            horarioActividadAdapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(GrabarReservaDeporte.this,
                                    "Error: " + error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            queue.add(jsonObjectRequest);
        }
    }

    public void deserializarActividades(JSONObject jsonObject) {
        try {
            Actividad actividad1 = new Actividad();
            actividad1.setId(0);
            actividad1.setDescripcion("Seleccione la actividad");
            actividades.add(actividad1);

            JSONArray actividad = new JSONArray(jsonObject.getString("actividad"));
            for (int i = 0; i < actividad.length(); i++) {
                JSONObject item = actividad.getJSONObject(i);
                actividad1 = new Actividad();
                actividad1.setId(item.getInt("IDActividad"));
                actividad1.setDescripcion(item.getString("Descripcion"));

                actividades.add(actividad1);
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void deserializarSedes(JSONObject jsonObject) {
        try {
            Sede sede1 = new Sede();
            sede1.setId(0);
            sede1.setDescripcion("Seleccione la sede");
            sede1.setAbreviado("");
            sede1.setDireccion("");
            sede1.setTelefono("");
            sede1.setEstado("");
            sedes.add(sede1);

            JSONArray sede = new JSONArray(jsonObject.getString("sede"));
            for (int i = 0; i < sede.length(); i++) {
                JSONObject item = sede.getJSONObject(i);
                sede1 = new Sede();
                sede1.setId(item.getInt("IdSede"));
                sede1.setDescripcion(item.getString("Descripcion"));
                sede1.setAbreviado(item.getString("Abreviado"));
                sede1.setDireccion(item.getString("Direccion"));
                sede1.setTelefono(item.getString("Telefono"));
                sede1.setEstado(item.getString("Estado"));

                sedes.add(sede1);
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void deserializarDependientes(JSONObject jsonObject) {
        try {
            Dependiente dependiente1 = new Dependiente();
            dependiente1.setDniDependiente("");
            dependiente1.setDniCiudadano("");
            dependiente1.setNombre("Seleccione a su hijo");
            dependiente1.setEdad(99);
            dependiente1.setSexo("");
            dependiente1.setEstado("");
            dependientes.add(dependiente1);

            JSONArray dependiente = new JSONArray(jsonObject.getString("dependiente"));
            for (int i = 0; i < dependiente.length(); i++) {
                JSONObject item = dependiente.getJSONObject(i);
                dependiente1 = new Dependiente();
                dependiente1.setDniDependiente(item.getString("DniDependiente"));
                dependiente1.setDniCiudadano(item.getString("DniCiudadano"));
                dependiente1.setNombre(item.getString("Nombre"));
                dependiente1.setEdad(item.getInt("Edad"));
                dependiente1.setSexo(item.getString("Sexo"));
                dependiente1.setEstado(item.getString("Estado"));

                dependientes.add(dependiente1);
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void deserializarHorarios(JSONObject jsonObject) {
        try {
            JSONArray dependiente = new JSONArray(jsonObject.getString("horario"));
            for (int i = 0; i < dependiente.length(); i++) {
                JSONObject item = dependiente.getJSONObject(i);
                Horario horario1 = new Horario();
                horario1.setIdHorario(item.getInt("idhorario"));
                horario1.setDisciplina(item.getString("disciplina"));
                horario1.setSede(item.getString("dessed"));
                horario1.setDia(item.getString("sem"));
                horario1.setHoraInicio(item.getString("horainicio"));
                horario1.setHoraFin(item.getString("horafin"));
                horario1.setEdadDesde(item.getString("edaddesde"));
                horario1.setEdadHasta(item.getString("edadhasta"));
                horario1.setDisponibles(item.getInt("disponible"));
                horario1.setPrecio(item.getDouble("Costo"));
                horario1.setIdActividad(item.getInt("idactividad"));
                horario1.setSeleccionado("0");

                horarios.add(horario1);
            }

            horarioActividadAdapter = new HorarioActividadAdapter(this, horarios);
            recyclerView.setAdapter(horarioActividadAdapter);
            horarioActividadAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Toast.makeText(this, "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private static class Grabar extends AsyncTask<URL, Void, String> {

        private WeakReference<GrabarReservaDeporte> mRef;

        private ProgressDialog dialog = null;

        Grabar(GrabarReservaDeporte activity) {
            mRef = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            GrabarReservaDeporte myActivity = mRef.get();

            dialog = new ProgressDialog(myActivity);
            dialog.setMessage("Grabando...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(URL... params) {
            HttpURLConnection connection;
            GrabarReservaDeporte myActivity = mRef.get();

            try {
                JSONObject postDataParams = new JSONObject();
                try {
                    postDataParams.put("actividad", myActivity.actividades.get(myActivity.spDeportes.getSelectedItemPosition()).getId());
                    postDataParams.put("programa", 1);
                    postDataParams.put("horario", myActivity.horario);
                    postDataParams.put("ciudadano", "9571716");
                    postDataParams.put("dependiente", myActivity.dependientes.get(myActivity.spHijos.getSelectedItemPosition()).getDniDependiente());
                    postDataParams.put("precio", myActivity.precio);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                connection = (HttpURLConnection) params[0].openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                try {
                    writer.write(getPostDataString(postDataParams));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                writer.flush();
                writer.close();
                os.close();

                final int responseCode = connection.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    return in.readLine();
                } else {
                    Log.i("ERROR", String.valueOf(responseCode));
                    return "ERROR : " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);

            GrabarReservaDeporte myActivity = mRef.get();

            if (dialog.isShowing()) dialog.dismiss();

            Toast.makeText(myActivity, "Gracias por reservar", Toast.LENGTH_LONG).show();

            myActivity.finish();
        }

        String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while(itr.hasNext()){

                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }
}
