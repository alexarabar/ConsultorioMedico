package br.com.alexandrebarboza.consultoriomedico.Utility.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Convenio;
import br.com.alexandrebarboza.consultoriomedico.R;

/**
 * Created by Alexandre on 18/03/2017.
 */

public class ConsultaConvenioAdapter extends ArrayAdapter<Convenio> {
    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public ConsultaConvenioAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context  = context;
        this.resource = resource;
        setNotifyOnChange (true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ConsultaConvenioAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new ConsultaConvenioAdapter.ViewHolder();
            view = inflater.inflate(resource, parent, false);
            holder.text_nome = (TextView) view.findViewById(R.id.text_convenio_nome);
            view.setTag(holder);
        } else {
            holder = (ConsultaConvenioAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }
        Convenio convenio = getItem(position);
        holder.text_nome.setText(convenio.getNome());
        return view;
    }

    static class ViewHolder {
        TextView text_nome;
    }
}
