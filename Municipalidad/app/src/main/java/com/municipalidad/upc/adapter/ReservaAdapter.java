package com.municipalidad.upc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import com.municipalidad.upc.PagoTarjeta;
import com.municipalidad.upc.R;
import com.municipalidad.upc.entidades.Reserva;


public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.PagoViewHolder> {

    private List<Reserva> reservas;
    private Activity activity;

    public ReservaAdapter(Activity activity, List<Reserva> reservas) {
        this.activity = activity;
        this.reservas = reservas;
    }

    @Override
    public PagoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.pagos_actividades, parent, false);
        return new PagoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PagoViewHolder holder, final int position) {
        holder.lblHorario.setText(reservas.get(position).getDia() + " " +
                reservas.get(position).getHoraInicio() + "-" + reservas.get(position).getHoraFin() + " - " +
                reservas.get(position).getEdadDesde() + " a " + reservas.get(position).getEdadHasta() + " años");
        holder.lblDeporte.setText(reservas.get(position).getDisciplina() + " - " + reservas.get(position).getSede());

        if(reservas.get(position).getIdActividad() == 1)
            holder.imgInstalacion.setImageResource(R.drawable.futbol);
        else if(reservas.get(position).getIdActividad() == 2)
            holder.imgInstalacion.setImageResource(R.drawable.voley);
        else if(reservas.get(position).getIdActividad() == 3)
            holder.imgInstalacion.setImageResource(R.drawable.basketball);
        else if(reservas.get(position).getIdActividad() == 4)
            holder.imgInstalacion.setImageResource(R.drawable.capoeira);
        else if(reservas.get(position).getIdActividad() == 5)
            holder.imgInstalacion.setImageResource(R.drawable.esgrima);
        else if(reservas.get(position).getIdActividad() == 6)
            holder.imgInstalacion.setImageResource(R.drawable.futbol);
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public class PagoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgInstalacion, imgVisa;
        private TextView lblDeporte, lblHorario;
        private RelativeLayout rlv;

        PagoViewHolder(final View itemView) {
            super(itemView);

            imgInstalacion = itemView.findViewById(R.id.imgInstalacion);
            imgVisa = itemView.findViewById(R.id.imgVisa);
            lblDeporte = itemView.findViewById(R.id.lblDeporte2);
            lblHorario = itemView.findViewById(R.id.lblHorario);
            rlv = itemView.findViewById(R.id.rlv2);
            imgVisa.setOnClickListener(this);
            imgInstalacion.setOnClickListener(this);
            lblDeporte.setOnClickListener(this);
            lblHorario.setOnClickListener(this);
            rlv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, PagoTarjeta.class);
            Bundle b = new Bundle();
            b.putInt("matricula", reservas.get(getAdapterPosition()).getIdMatricula());
            b.putDouble("monto", reservas.get(getAdapterPosition()).getPrecio());
            intent.putExtras(b);
            activity.startActivity(intent);
        }
    }
}
