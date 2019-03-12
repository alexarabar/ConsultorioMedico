package br.com.alexandrebarboza.consultoriomedico.Utility.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Medico;
import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Glossary;

/**
 * Created by Alexandre on 21/02/2017.
 */

public class MedicoAdapter extends ArrayAdapter <Medico> {
    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public MedicoAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context  = context;
        this.resource = resource;
        setNotifyOnChange (true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            view = inflater.inflate(resource, parent, false);
            holder.text_cor_medico = (TextView) view.findViewById(R.id.text_cor_medico);
            holder.text_crm_medico = (TextView) view.findViewById(R.id.text_crm_medico);
            holder.text_uf_medico = (TextView) view.findViewById(R.id.text_uf_medicos);
            holder.text_nome_medico = (TextView) view.findViewById(R.id.text_nome_medico);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            view = convertView;
        }
        Medico medico =  getItem(position);
        Medico previous = null;
        String compare   = " ";
        if (position > 0) {
            previous = getItem(position - 1);
            if (previous.getNome().length() > 0) {
                compare = previous.getNome().toUpperCase().substring(0, 1);
            }
        }
        Glossary.SetGlossary(holder.text_cor_medico, medico.getNome(), context, position, compare);

        holder.text_crm_medico.setText(medico.getCrm());
        holder.text_uf_medico.setText(medico.getUf());
        holder.text_nome_medico.setText(medico.getNome());
        return view;
    }

    static class ViewHolder {
        TextView text_cor_medico;
        TextView text_nome_medico;
        TextView text_crm_medico;
        TextView text_uf_medico;
    }
}

