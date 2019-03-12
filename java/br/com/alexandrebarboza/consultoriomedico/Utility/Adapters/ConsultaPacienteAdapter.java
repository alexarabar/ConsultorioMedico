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
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Paciente;
import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;

/**
 * Created by Alexandre on 18/03/2017.
 */

public class ConsultaPacienteAdapter extends ArrayAdapter<Paciente> {
    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;
    private String  str_convenio;
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

    public ConsultaPacienteAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context  = context;
        this.resource = resource;
        setNotifyOnChange (true);
        database = null;
        str_convenio = "";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ConsultaPacienteAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new ConsultaPacienteAdapter.ViewHolder();
            view = inflater.inflate(resource, parent, false);
            holder.text_nome_paciente = (TextView) view.findViewById(R.id.text_nome_paciente2);
            holder.text_convenio = (TextView) view.findViewById(R.id.text_convenio3);
            view.setTag(holder);
        } else {
            holder = (ConsultaPacienteAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }
        Paciente paciente =  getItem(position);
        holder.text_nome_paciente.setText(paciente.getNome());
        Domain domain = openDomain((Activity) context);
        if (domain != null) {
            holder.text_convenio.setText(domain.getTextConvenio((Activity) context, paciente.getConvenio()));
        } else {
            holder.text_convenio.setText("");
        }
        closeDatabase();
        TextView det4 = (TextView) view.findViewById(R.id.text_label_detalhe4);
        TextView det5 = (TextView) view.findViewById(R.id.text_label_detalhe5);
        if (holder.text_convenio.getText().toString().isEmpty()) {
            det4.setText("");
            det5.setText("");
        } else {
            det4.setText("(");
            det5.setText(")");
        }
        return view;
    }

    static class ViewHolder {
        TextView text_nome_paciente;
        TextView text_convenio;
    }
}
