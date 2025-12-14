package com.example.projetandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class OffreAdapter extends ArrayAdapter<Offre> {

    public OffreAdapter(Context context, ArrayList<Offre> offres) {
        super(context, 0, offres);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Offre offre = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_offre, parent, false);
        }

        // Lookup view for data population
        TextView txtEntreprise = convertView.findViewById(R.id.txtEntreprise);
        TextView txtPoste = convertView.findViewById(R.id.txtPoste);
        TextView txtVue = convertView.findViewById(R.id.txtVue);
        TextView txtPrix = convertView.findViewById(R.id.txtPrix);

        // Populate the data into the template view using the data object
        if (offre != null) {
            txtEntreprise.setText(offre.getEntreprise());
            txtPoste.setText(offre.getPoste());
            txtVue.setText(String.valueOf(offre.getVue()));
            txtPrix.setText("Offre: " + offre.getPrix());
        }

        // The click listener is handled in the activity, not here.
        // This was the source of the bug.

        // Return the completed view to render on screen
        return convertView;
    }
}
