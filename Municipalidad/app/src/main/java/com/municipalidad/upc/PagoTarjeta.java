package com.municipalidad.upc;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class PagoTarjeta extends AppCompatActivity {

    int idMatricula;
    double monto;

    EditText txtNroTarjeta, txtVenc, txtCVV, txtNombre, txtApellido, txtCorreo;
    String nroTarjeta, venc, cvv, nombre, apellido, correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_tarjeta);

        Bundle b = getIntent().getExtras();
        idMatricula = b.getInt("matricula");
        monto = b.getDouble("monto");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnPagar;

        txtNroTarjeta = findViewById(R.id.txtNroTarjeta);
        txtVenc = findViewById(R.id.txtVenc);
        txtCVV = findViewById(R.id.txtCVV);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtCorreo = findViewById(R.id.txtCorreo);
        btnPagar = findViewById(R.id.btnPagar);

        btnPagar.setText(getResources().getString(R.string.boton_pagar, String.valueOf(monto)));

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nroTarjeta = txtNroTarjeta.getText().toString();
                venc = txtVenc.getText().toString();
                cvv = txtCVV.getText().toString();
                nombre = txtNombre.getText().toString();
                apellido = txtApellido.getText().toString();
                correo = txtCorreo.getText().toString();

                if(!nroTarjeta.equals("") && !venc.equals("") && !cvv.equals("") && !nombre.equals("") &&
                        !apellido.equals("") && !correo.equals("")){
                    try {
                        new Grabar(PagoTarjeta.this).execute(new URL(BuildConfig.urlReserva + "/pagar"));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(PagoTarjeta.this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private static class Grabar extends AsyncTask<URL, Void, String> {

        private WeakReference<PagoTarjeta> mRef;

        private ProgressDialog dialog = null;

        Grabar(PagoTarjeta activity) {
            mRef = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PagoTarjeta myActivity = mRef.get();

            dialog = new ProgressDialog(myActivity);
            dialog.setMessage("Grabando...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(URL... params) {
            HttpURLConnection connection;
            PagoTarjeta myActivity = mRef.get();

            try {
                JSONObject postDataParams = new JSONObject();
                try {
                    postDataParams.put("matricula", myActivity.idMatricula);
                    postDataParams.put("monto", myActivity.monto);
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

            PagoTarjeta myActivity = mRef.get();

            if (dialog.isShowing()) dialog.dismiss();

            Toast.makeText(myActivity, "Gracias por pagar", Toast.LENGTH_LONG).show();

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
