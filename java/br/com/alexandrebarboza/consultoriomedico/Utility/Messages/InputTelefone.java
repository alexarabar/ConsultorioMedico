package br.com.alexandrebarboza.consultoriomedico.Utility.Messages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.alexandrebarboza.consultoriomedico.BlankActivity;
import br.com.alexandrebarboza.consultoriomedico.Database.Database;
import br.com.alexandrebarboza.consultoriomedico.Domain.Domain;
import br.com.alexandrebarboza.consultoriomedico.MedicoActivity;
import br.com.alexandrebarboza.consultoriomedico.PacienteActivity;
import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.Adapters;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;
import br.com.alexandrebarboza.consultoriomedico.Utility.Utility;

import static android.content.DialogInterface.BUTTON_NEGATIVE;

/**
 * Created by Alexandre on 28/03/2017.
 */

public class InputTelefone implements DialogInterface.OnClickListener, Button.OnClickListener {
    private Activity activity;
    private View view;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private EditText edit_message;
    private Spinner  spinner_telefone;
    private ArrayAdapter adapter_telefone;
    private Database database;
    private Domain   domain;

    public InputTelefone(Activity activity, String paciente, String medico, String par, String date, String time) {
        long id   = 0;
        int count = 0;
        String[] array = null;
        String msg = "", s1= "", s2 = "", s3 = "", s4 = "", s5 = "";
        database = Database.getInstance(activity);
        domain = domain.getInstance();
        if (!Connector.OpenDatabase(activity.getResources(), activity, database, domain, false)) {
            return;
        }
        if (par.equals(MedicoActivity.p_medico)) {
            id = domain.getIdMedico(activity, medico);
            s1 = activity.getResources().getString(R.string.str_dr);
            s2 = activity.getResources().getString(R.string.str_medico);
            s3 = activity.getResources().getString(R.string.str_paciente);
            s4 = medico;
            s5 = paciente;
            if (id > 0) {
                count = domain.getCountMedicoXTelefones(id);
                array = new String[count];
                domain.setArrayMedicoTelefones(array, id);
            }
        } else if (par.equals(PacienteActivity.p_paciente)) {
            id = domain.getIdPaciente(activity, paciente);
            s1 = activity.getResources().getString(R.string.str_sr);
            s2 = activity.getResources().getString(R.string.str_paciente);
            s3 = activity.getResources().getString(R.string.str_medico);
            s4 = paciente;
            s5 = medico;
            if (id > 0) {
                count = domain.getCountPacienteXTelefones(id);
                array = new String[count];
                domain.setArrayPacienteTelefones(array, id);
            }
        }
        database.Close();
        s2 = s2.substring(0, s2.length() -1).toLowerCase();
        s3 = s3.substring(0, s3.length() -1).toLowerCase();
        if (id == 0) {
            msg = activity.getResources().getString(R.string.str_err_sql_query) + " " + activity.getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
            Output.Alert(activity, activity.getResources().getString(R.string.str_fail), msg);
        } else {
            if (array == null || array.length == 0) {
                msg = "O " + s2 + " " + s4 + " " + activity.getResources().getString(R.string.str_no_tel);
                Output.Info(activity, activity.getResources().getString(R.string.str_send_sms), msg);
            } else {
                this.activity = activity;
                this.inflater = LayoutInflater.from(activity);
                this.view = inflater.inflate(R.layout.message_input_telefone, null);
                msg = s1 + ". " + s4 + ", " + activity.getString(R.string.str_confirm) + " " + date + " de " + time;
                msg += " " + "com o " + s3 + " " + s5 + ".";
                this.edit_message = (EditText) view.findViewById(R.id.edit_message);
                this.edit_message.setText(msg);
                this.spinner_telefone  = (Spinner)  view.findViewById(R.id.spinner_telefone);
                this.adapter_telefone = Adapters.GetSpinner(activity, spinner_telefone);
                this.adapter_telefone.setNotifyOnChange(true);
                for (int i = 0; i < array.length; i++) {
                    this.adapter_telefone.add(array[i]);
                }
                this.adapter_telefone.notifyDataSetChanged();
                Intent it = activity.getIntent();
                it.putExtra("SMS_OK", true);
            }
        }
    }
    public void setInputTelefone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setTitle(activity.getResources().getString(R.string.str_send_sms));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.str_ok, this);
        builder.setNegativeButton(R.string.str_cancel, this);
        dialog = builder.create();
        dialog.setOnDismissListener((DialogInterface.OnDismissListener) activity);
        dialog.setOnCancelListener((DialogInterface.OnCancelListener) activity);
        dialog.show();
        Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_NEGATIVE:
                AlertDialog ad = (AlertDialog) dialog;
                ad.setOnDismissListener(null);
                dialog.cancel();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        String telefone = spinner_telefone.getSelectedItem().toString();
        String mensagem = edit_message.getText().toString();

        // UPDATE 02/02/2019
        //Intent it = new Intent(activity, BlankActivity.class);
        //activity.startActivity(it);
        //activity.getIntent().putExtras(it);

        // Log.w("STRING: ", mensagem);

        if (Utility.sendSMS(telefone, mensagem)) {
            Toast.makeText(activity.getApplicationContext(), "SMS Enviado!",
            Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity.getApplicationContext(), "SMS Falhou!",
            Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }
}
