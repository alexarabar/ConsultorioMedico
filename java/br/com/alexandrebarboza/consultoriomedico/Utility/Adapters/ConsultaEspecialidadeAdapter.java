package br.com.alexandrebarboza.consultoriomedico.Utility.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Especialidade;
import br.com.alexandrebarboza.consultoriomedico.R;

/**
 * Created by Alexandre on 17/03/2017.
 */

public class ConsultaEspecialidadeAdapter extends ArrayAdapter<Especialidade> {
    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public ConsultaEspecialidadeAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context  = context;
        this.resource = resource;
        setNotifyOnChange (true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ConsultaEspecialidadeAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new ConsultaEspecialidadeAdapter.ViewHolder();
            view = inflater.inflate(resource, parent, false);
            holder.text_titulo = (TextView) view.findViewById(R.id.text_especialidade_titulo);
            view.setTag(holder);
        } else {
            holder = (ConsultaEspecialidadeAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }
        Especialidade especialidade =  getItem(position);
        holder.text_titulo.setText(especialidade.getTitulo());
        return view;
    }

    static class ViewHolder {
        TextView text_titulo;
    }
}
