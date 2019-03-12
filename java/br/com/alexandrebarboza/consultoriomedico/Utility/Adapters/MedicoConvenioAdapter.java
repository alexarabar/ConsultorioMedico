package br.com.alexandrebarboza.consultoriomedico.Utility.Adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;

import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Convenio;
import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Input;

/**
 * Created by Alexandre on 06/03/2017.
 */

public class MedicoConvenioAdapter extends ArrayAdapter<Convenio> implements View.OnClickListener {
    private ArrayList <String> list;
    private Activity activity;
    private Intent     intent;

    private boolean compareToList(String text) {
        if (list == null) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
             if (list.get(i).equals(text)) {
                 return true;
             }
        }
        return false;
    }

    private int getPositionOfList(String text) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(text)) {
                return i;
            }
        }
        return -1;
    }

    public MedicoConvenioAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        activity = (Activity) context;
        intent = activity.getIntent();
        this.list = intent.getStringArrayListExtra("LIST");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        MedicoConvenioAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_medico_convenios, parent, false);
            holder = new MedicoConvenioAdapter.ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (MedicoConvenioAdapter.ViewHolder) convertView.getTag();
        }
        Convenio convenio = getItem(position);
        holder.check_convenio = (CheckBox) convertView.findViewById(R.id.check_medico_convenio);
        holder.check_convenio.setOnClickListener(this);
        holder.text_convenio = (TextView) convertView.findViewById(R.id.text_medico_convenio);
        holder.text_convenio.setOnClickListener(this);
        holder.text_convenio.setText(convenio.getNome());
        holder.check_convenio.setTag(holder.text_convenio);
        holder.text_convenio.setTag(holder.check_convenio);
        holder.check_convenio.setChecked(compareToList(holder.text_convenio.getText().toString()));
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_medico_convenio:
                CheckBox check = (CheckBox) v;
                TextView text = (TextView) check.getTag();
                if (check.isChecked()) {                 // if (list != null && text != null)
                    list.add(text.getText().toString()); // TODO java.lang.NullPointerException - 22/02/2018
                } else {
                    list.remove(getPositionOfList(text.getText().toString()));
                }
                break;
            case R.id.text_medico_convenio:
                TextView tmp;
                String   txt;
                tmp = (TextView) v;
                txt = tmp.getText().toString();
                intent.putExtra("ACTION", 1);
                intent.putExtra("OLD", txt);
                CheckBox cb = (CheckBox) tmp.getTag();
                if (cb.isChecked()) {
                    list.remove(getPositionOfList(tmp.getText().toString()));
                    cb.setChecked(false);
                }
                Input input = new Input(activity, activity.getResources().getString(R.string.str_convenio), txt, 45);
                input.setInput();
                break;
        }
    }

    static class ViewHolder {
        CheckBox check_convenio;
        TextView text_convenio;
    }
}

