package com.municipalidad.upc.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.municipalidad.upc.entidades.Horario;

import java.util.List;

import com.municipalidad.upc.R;


public class HorarioActividadAdapter extends RecyclerView.Adapter<HorarioActividadAdapter.HorarioViewHolder> {

    private List<Horario> horarios;
    private Activity activity;
    private int mSelectedItem = -1;

    public HorarioActividadAdapter(Activity activity, List<Horario> horarios) {
        this.activity = activity;
        this.horarios = horarios;
    }

    @Override
    public HorarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.horarios_actividades, parent, false);
        return new HorarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HorarioViewHolder holder, final int position) {
        holder.lblHorario.setText(horarios.get(position).getDia() + " " +
                horarios.get(position).getHoraInicio() + "-" + horarios.get(position).getHoraFin() + " - " +
                horarios.get(position).getEdadDesde() + " a " + horarios.get(position).getEdadHasta() + " años");
        holder.lblDeporte.setText(horarios.get(position).getDisciplina() + " - " + horarios.get(position).getSede());
        if(position == mSelectedItem) {
            holder.imgEstrella.setImageResource(R.drawable.select_star);
            horarios.get(position).setSeleccionado("1");
        } else {
            holder.imgEstrella.setImageResource(R.drawable.unselect_star);
            horarios.get(position).setSeleccionado("0");
        }

        if(horarios.get(position).getIdActividad() == 1)
            holder.imgDeporte.setImageResource(R.drawable.futbol);
        else if(horarios.get(position).getIdActividad() == 2)
            holder.imgDeporte.setImageResource(R.drawable.voley);
        else if(horarios.get(position).getIdActividad() == 3)
            holder.imgDeporte.setImageResource(R.drawable.basketball);
        else if(horarios.get(position).getIdActividad() == 4)
            holder.imgDeporte.setImageResource(R.drawable.capoeira);
        else if(horarios.get(position).getIdActividad() == 5)
            holder.imgDeporte.setImageResource(R.drawable.esgrima);
        else if(horarios.get(position).getIdActividad() == 6)
            holder.imgDeporte.setImageResource(R.drawable.futbol);
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    public class HorarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgDeporte, imgEstrella;
        private TextView lblDeporte, lblHorario;
        private RelativeLayout rlv;

        HorarioViewHolder(final View itemView) {
            super(itemView);

            imgDeporte = itemView.findViewById(R.id.imgDeporte2);
            imgEstrella = itemView.findViewById(R.id.imgEstrella);
            lblDeporte = itemView.findViewById(R.id.lblDeporte2);
            lblHorario = itemView.findViewById(R.id.lblHorario);
            rlv = itemView.findViewById(R.id.rlv2);
            imgEstrella.setOnClickListener(this);
            imgDeporte.setOnClickListener(this);
            lblDeporte.setOnClickListener(this);
            lblHorario.setOnClickListener(this);
            rlv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mSelectedItem = getAdapterPosition();
            notifyDataSetChanged();
        }
    }
}
