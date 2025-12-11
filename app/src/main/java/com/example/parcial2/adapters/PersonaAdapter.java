// com/example/parcial2/adapters/PersonaAdapter.java
package com.example.parcial2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial2.R;
import com.example.parcial2.modelos.Persona;

import java.util.ArrayList;
import java.util.List;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder> implements Filterable {

    private List<Persona> listaOriginal;
    private List<Persona> listaFiltrada;

    public PersonaAdapter(List<Persona> lista) {
        this.listaOriginal = new ArrayList<>(lista);
        this.listaFiltrada = new ArrayList<>(lista);
    }

    @NonNull
    @Override
    public PersonaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_persona, parent, false);
        return new PersonaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonaViewHolder holder, int position) {
        Persona p = listaFiltrada.get(position);
        holder.txtNombre.setText(p.getNombre());
        holder.txtProfesion.setText(p.getProfesion());
        holder.txtEstudios.setText(p.getEstudios());
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public static class PersonaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtProfesion, txtEstudios;

        public PersonaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtProfesion = itemView.findViewById(R.id.txtProfesion);
            txtEstudios = itemView.findViewById(R.id.txtEstudios);
        }
    }

    @Override
    public Filter getFilter() {
        return filtro;
    }

    private final Filter filtro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Persona> listaFiltradaLocal = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                listaFiltradaLocal.addAll(listaOriginal);
            } else {
                String patron = constraint.toString().toLowerCase().trim();
                for (Persona p : listaOriginal) {
                    if (p.getNombre().toLowerCase().contains(patron)
                            || p.getProfesion().toLowerCase().contains(patron)
                            || p.getEstudios().toLowerCase().contains(patron)) {
                        listaFiltradaLocal.add(p);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = listaFiltradaLocal;
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listaFiltrada.clear();
            listaFiltrada.addAll((List<Persona>) results.values);
            notifyDataSetChanged();
        }
    };
}
