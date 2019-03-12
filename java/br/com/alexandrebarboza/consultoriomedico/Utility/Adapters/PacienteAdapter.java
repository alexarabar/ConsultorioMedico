package br.com.alexandrebarboza.consultoriomedico.Utility.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Paciente;
import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Glossary;

/**
 * Created by Alexandre on 12/03/2017.
 */

public class PacienteAdapter extends ArrayAdapter<Paciente> {
    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public PacienteAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context  = context;
        this.resource = resource;
        setNotifyOnChange (true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        PacienteAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new PacienteAdapter.ViewHolder();
            view = inflater.inflate(resource, parent, false);
            holder.text_cor_paciente  = (TextView) view.findViewById(R.id.text_cor_paciente);
            holder.text_cpf_paciente  = (TextView) view.findViewById(R.id.text_cpf_paciente);
            holder.text_nome_paciente = (TextView) view.findViewById(R.id.text_nome_paciente);
            view.setTag(holder);
        } else {
            holder = (PacienteAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }
        Paciente paciente =  getItem(position);
        Paciente previous = null;
        String compare   = " ";
        if (position > 0) {
            previous = getItem(position - 1);
            if (previous.getNome().length() > 0) {
                compare = previous.getNome().toUpperCase().substring(0, 1);
            }
        }
        Glossary.SetGlossary(holder.text_cor_paciente, paciente.getNome(), context, position, compare);
        holder.text_cpf_paciente.setText(paciente.getCpf());
        holder.text_nome_paciente.setText(paciente.getNome());
        return view;
    }
    static class ViewHolder {
        TextView text_cor_paciente;
        TextView text_nome_paciente;
        TextView text_cpf_paciente;
    }
}

