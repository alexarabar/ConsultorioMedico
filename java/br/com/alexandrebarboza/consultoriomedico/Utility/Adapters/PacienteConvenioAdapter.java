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

import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Convenio;
import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Input;
import br.com.alexandrebarboza.consultoriomedico.Utility.Utility;

/**
 * Created by Alexandre on 13/03/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class PacienteConvenioAdapter extends ArrayAdapter<Convenio> implements View.OnClickListener, View.OnScrollChangeListener, RadioGroup.OnCheckedChangeListener {
    private Context   context;
    private int      resource;
    private boolean  checked;
    private Activity activity;
    private Intent intent;
    private RadioGroup   radio_group;
    private RadioButton radio_button;
    private String     default_text;

    public PacienteConvenioAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);

        System.out.println("Construindo Convênio Adapter");

        this.context     = context;
        this.resource    = resource;
        this.activity = (Activity) context;
        this.intent = activity.getIntent();
        this.radio_group = new RadioGroup(context);
        this.radio_group.setOnCheckedChangeListener(this);
        this.default_text = intent.getStringExtra("INPUT");
        this.checked = false;
        ListView list_view = (ListView) activity.findViewById(R.id.list_paciente_convenios);
        list_view.setOnScrollChangeListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        PacienteConvenioAdapter.ViewHolder holder;
        radio_button = new RadioButton(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_paciente_convenios, parent, false);
            holder = new PacienteConvenioAdapter.ViewHolder();
            convertView.setTag(holder);
            try {
                radio_group.addView(radio_button, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder = (PacienteConvenioAdapter.ViewHolder) convertView.getTag();
        }
        Convenio convenio = getItem(position);
        holder.radio_convenio = (RadioButton) convertView.findViewById(R.id.radio_paciente_convenio);
        holder.radio_convenio.setOnClickListener(this);
        holder.text_convenio = (TextView) convertView.findViewById(R.id.text_paciente_convenio);
        holder.text_convenio.setOnClickListener(this);
        holder.text_convenio.setText(convenio.getNome());
        holder.radio_convenio.setTag(holder.text_convenio);
        radio_button.setTag(holder.radio_convenio);
        if (!default_text.isEmpty() && holder.text_convenio.getText().toString().equals(default_text)) {
            holder.radio_convenio.setChecked(true);
            checked = true;
        } else {
            holder.radio_convenio.setChecked(false);
        }
        radio_button.setChecked(holder.radio_convenio.isChecked());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        TextView tmp;
        String   txt, in;
        switch (v.getId()) {
            case R.id.radio_paciente_convenio:
                intent.putExtra("ACTION", 0);
                intent.putExtra("OLD", "");
                RadioButton rb1 = (RadioButton) v;
                RadioButton rb2 = Utility.getRadioButton(radio_group, rb1);
                tmp = (TextView) v.getTag();
                txt = tmp.getText().toString();
                in  = intent.getStringExtra("INPUT");
                if (rb1.isChecked()) {
                    boolean flag = false;
                    if (txt.equals(in)) {
                        if (checked == false) {
                            flag = true;
                        }
                    } else {
                        flag = true;
                    }
                    if (flag) {
                        Utility.clearAllRadioButtons(radio_group);
                        rb1.setChecked(true);
                        intent.putExtra("INPUT", txt);
                        checked = true;
                    } else {
                        rb1.setChecked(false);
                        intent.putExtra("INPUT", "");
                        checked = false;
                    }
                }
                rb2.setChecked(rb1.isChecked());
                break;
            case R.id.text_paciente_convenio:
                tmp = (TextView) v;
                txt = tmp.getText().toString();
                intent.putExtra("ACTION", 1);
                intent.putExtra("OLD", txt);
                Input input = new Input(activity, context.getResources().getString(R.string.str_convenio), txt, 45);
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

        /*
        RadioButton rb1 = (RadioButton) group.findViewById(checkedId);
        RadioButton rb2 = (RadioButton) rb1.getTag();
        TextView tv = (TextView) rb2.getTag();
        String str = tv.getText().toString();
        String in  = intent.getStringExtra("INPUT");
        */

    }

    static class ViewHolder {
        RadioButton radio_convenio;
        TextView text_convenio;
    }

}
