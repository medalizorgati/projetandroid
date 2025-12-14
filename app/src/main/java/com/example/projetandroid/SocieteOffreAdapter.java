package com.example.projetandroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SocieteOffreAdapter extends RecyclerView.Adapter<SocieteOffreAdapter.ViewHolder> {

    private List<Offre> offres;

    public SocieteOffreAdapter(List<Offre> offres) {
        this.offres = offres;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_offre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Offre offre = offres.get(position);
        holder.txtEntreprise.setText(offre.getEntreprise());
        holder.txtPoste.setText(offre.getPoste());
        holder.txtPrix.setText("Offre: " + offre.getPrix());
        holder.txtVue.setText("0"); // Placeholder

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, OffreDescriptionActivity.class);
            intent.putExtra("OFFRE_ID", offre.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return offres.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtEntreprise, txtPoste, txtPrix, txtVue;

        public ViewHolder(View itemView) {
            super(itemView);
            txtEntreprise = itemView.findViewById(R.id.txtEntreprise);
            txtPoste = itemView.findViewById(R.id.txtPoste);
            txtPrix = itemView.findViewById(R.id.txtPrix);
            txtVue = itemView.findViewById(R.id.txtVue);
        }
    }
}
