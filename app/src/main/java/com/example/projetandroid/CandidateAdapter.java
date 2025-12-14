package com.example.projetandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {

    private List<Candidat> candidats;

    public CandidateAdapter(List<Candidat> candidats) {
        this.candidats = candidats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Candidat candidat = candidats.get(position);
        holder.nom.setText(candidat.getNom());
        holder.telephone.setText(candidat.getTelephone());
        holder.offre.setText("Poste: " + candidat.getOffreMetier());
    }

    @Override
    public int getItemCount() {
        return candidats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nom, telephone, offre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.candidatNom);
            telephone = itemView.findViewById(R.id.candidatTelephone);
            offre = itemView.findViewById(R.id.candidatOffre);
        }
    }
}
