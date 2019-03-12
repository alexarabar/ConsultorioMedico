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
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Medico;
import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;

/**
 * Created by Alexandre on 17/03/2017.
 */

public class ConsultaMedicoAdapter extends ArrayAdapter<Medico> {
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

    public ConsultaMedicoAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context  = context;
        this.resource = resource;
        setNotifyOnChange (true);
        database = null;
        str_convenio = "";
    }

    public void setConvenio(long id_conv) {
        Domain domain = openDomain((Activity) context);
        if (domain != null) {
            str_convenio = domain.getTextConvenio((Activity) context, id_conv);
        }
        closeDatabase();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ConsultaMedicoAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new ConsultaMedicoAdapter.ViewHolder();
            view = inflater.inflate(resource, parent, false);
            holder.text_nome_medico = (TextView) view.findViewById(R.id.text_nome_medico2);
            holder.text_especialidade = (TextView) view.findViewById(R.id.text_especialidade2);
            holder.text_convenio = (TextView) view.findViewById(R.id.text_convenio2);
            view.setTag(holder);
        } else {
            holder = (ConsultaMedicoAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }
        Medico medico =  getItem(position);
        holder.text_nome_medico.setText(medico.getNome());
        Domain domain = openDomain((Activity) context);
        if (domain != null) {
            holder.text_especialidade.setText(domain.getTextEspecialidade((Activity) context, medico.getEspecialidade()));
        } else {
            holder.text_especialidade.setText("");
        }
        closeDatabase();
        TextView det2 = (TextView) view.findViewById(R.id.text_label_detalhe2);
        if (str_convenio.isEmpty()) {
            det2.setText("");
            holder.text_convenio.setText("");
        } else {
            if (holder.text_especialidade.getText().toString().isEmpty()) {
                det2.setText("");
            } else {
                det2.setText("/");
            }
            holder.text_convenio.setText(str_convenio);
        }
        TextView det1 = (TextView) view.findViewById(R.id.text_label_detalhe1);
        TextView det3 = (TextView) view.findViewById(R.id.text_label_detalhe3);
        if (holder.text_especialidade.getText().toString().isEmpty() && holder.text_convenio.getText().toString().isEmpty()) {
            det1.setText("");
            det3.setText("");
        } else {
            det1.setText("(");
            det3.setText(" )");
        }
        return view;
    }

    static class ViewHolder {
        TextView text_nome_medico;
        TextView text_especialidade;
        TextView text_convenio;
    }
}
