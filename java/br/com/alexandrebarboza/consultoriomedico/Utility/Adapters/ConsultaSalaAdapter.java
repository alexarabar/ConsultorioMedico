package br.com.alexandrebarboza.consultoriomedico.Utility.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Sala;
import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Input;
import br.com.alexandrebarboza.consultoriomedico.Utility.Utility;

/**
 * Created by Alexandre on 18/03/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class ConsultaSalaAdapter extends ArrayAdapter<Sala> implements View.OnClickListener, View.OnScrollChangeListener, RadioGroup.OnCheckedChangeListener {
    private Context   context;
    private int      resource;
    private Activity activity;
    private Intent intent;
    private RadioGroup   radio_group;
    private RadioButton radio_button;
    private String      default_text;

    public ConsultaSalaAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context     = context;
        this.resource    = resource;
        this.activity = (Activity) context;
        this.intent = activity.getIntent();
        this.radio_group = new RadioGroup(context);
        this.radio_group.setOnCheckedChangeListener(this);
        this.default_text = intent.getStringExtra("INPUT");
        ListView list_view = (ListView) activity.findViewById(R.id.list_salas);
        list_view.setOnScrollChangeListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        ViewHolder holder;
        radio_button = new RadioButton(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_salas, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            try {
                radio_group.addView(radio_button, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Sala sala = getItem(position);
        holder.radio_sala = (RadioButton) convertView.findViewById(R.id.radio_sala_consulta);
        holder.radio_sala.setOnClickListener(this);
        holder.text_sala = (TextView) convertView.findViewById(R.id.text_sala_consulta);
        holder.text_sala.setOnClickListener(this);
        holder.text_sala.setText(sala.getDescricao());
        holder.radio_sala.setTag(holder.text_sala);
        radio_button.setTag(holder.radio_sala);
        if (!default_text.isEmpty() && holder.text_sala.getText().toString().equals(default_text)) {
            holder.radio_sala.setChecked(true);
        } else {
            holder.radio_sala.setChecked(false);
        }
        radio_button.setChecked(holder.radio_sala.isChecked());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        TextView tmp;
        String   txt;
        switch (v.getId()) {
            case R.id.radio_sala_consulta:
                tmp = (TextView) v.getTag();
                txt = tmp.getText().toString();
                intent.putExtra("INPUT", txt);
                intent.putExtra("ACTION", 0);
                intent.putExtra("OLD", "");
                RadioButton rb1 = (RadioButton) v;
                if (rb1.isChecked()) {
                    Utility.clearAllRadioButtons(radio_group);
                    rb1.setChecked(true);
                }
                RadioButton rb2 = Utility.getRadioButton(radio_group, rb1);
                if (rb2 != null) {
                    rb2.setChecked(rb1.isChecked());
                }
                break;
            case R.id.text_sala_consulta:
                tmp = (TextView) v;
                txt = tmp.getText().toString();
                intent.putExtra("ACTION", 1);
                intent.putExtra("OLD", txt);
                Input input = new Input(activity, context.getResources().getString(R.string.str_sala), txt, 20);
                input.setInput();
                break;
        }
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        String in  = intent.getStringExtra("INPUT");
        if (in != null && !in.isEmpty()) {
            Utility.clearAllRadioButtons(radio_group);
            Utility.setRadioButton(in, radio_group);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        RadioButton rb1 = (RadioButton) group.findViewById(checkedId);
        RadioButton rb2 = (RadioButton) rb1.getTag();
        TextView tv = (TextView) rb2.getTag();
        String str = tv.getText().toString();
        String in  = intent.getStringExtra("INPUT");
        if (in.isEmpty()) {
            if (rb1.isChecked()) {
                intent.putExtra("INPUT", str);
                intent.putExtra("ACTION", 0);
                intent.putExtra("OLD", "");
            }
        }
    }

    static class ViewHolder {
        RadioButton radio_sala;
        TextView text_sala;
    }
}
