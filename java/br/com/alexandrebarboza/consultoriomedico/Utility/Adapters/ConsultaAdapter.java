package br.com.alexandrebarboza.consultoriomedico.Utility.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.alexandrebarboza.consultoriomedico.Database.Database;
import br.com.alexandrebarboza.consultoriomedico.Domain.Domain;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Consulta;
import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;

/**
 * Created by Alexandre on 19/03/2017.
 */

public class ConsultaAdapter extends ArrayAdapter<Consulta> {
    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;
    private Database database;

    private Domain openDomain(Activity activity) {
        database = Database.getInstance(activity);
        Domain domain = Domain.getInstance();
        if (!Connector.OpenDatabase(activity.getResources(), activity, database, domain, false)) {
            return null;
        }
        return domain;
    }

    private void closeDatabase() {
        if (database != null) {
            database.Close();
        }
    }

    private String reduceTime(String str) {
        String s = str;
        s = s.substring(0, s.length() -3);
        return s;
    }

    private String getNomeMedico(Long id) {
        Activity activity = (Activity) context;
        Domain domain = openDomain(activity);
        String result = domain.getTextMedico(activity, id);
        closeDatabase();
        return result;
    }

    private String getNomePaciente(Long id) {
        Activity activity = (Activity) context;
        Domain domain = openDomain(activity);
        String result = domain.getTextPaciente(activity, id);
        closeDatabase();
        return result;
    }

    public ConsultaAdapter(Context context, int resource) {
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
            holder.text_inicio        = (TextView) view.findViewById(R.id.text_consulta_inicio);
            holder.text_fim           = (TextView) view.findViewById(R.id.text_consulta_fim);
            holder.text_nome_medico   = (TextView) view.findViewById(R.id.text_consulta_medico);
            holder.text_nome_paciente = (TextView) view.findViewById(R.id.text_consulta_paciente);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            view = convertView;
        }
        Consulta consulta =  getItem(position);

        //System.out.println("GRAVOU DATA? " + consulta.getData().toString());

        holder.text_inicio.setText(reduceTime(consulta.getInicio().toString()));
        holder.text_fim.setText(reduceTime(consulta.getFim().toString()));
        holder.text_nome_medico.setText(getNomeMedico(consulta.get_medico()));
        holder.text_nome_paciente.setText(getNomePaciente(consulta.get_paciente()));
        return view;
    }


    static class ViewHolder {
        TextView text_inicio;
        TextView text_fim;
        TextView text_nome_medico;
        TextView text_nome_paciente;
    }
}
