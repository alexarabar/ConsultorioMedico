package br.com.alexandrebarboza.consultoriomedico.Utility.Messages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.Adapters;
import br.com.alexandrebarboza.consultoriomedico.Utility.Utility;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static br.com.alexandrebarboza.consultoriomedico.Utility.Utility.enderecoIsEmpty;

/**
 * Created by Alexandre on 25/02/2017.
 */

public class InputEndereco implements DialogInterface.OnClickListener, Button.OnClickListener {
    private Activity activity;
    private View view;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private EditText edit_logradouro;
    private EditText edit_numero;
    private EditText edit_complemento;
    private EditText edit_bairro;
    private EditText edit_cidade;
    private Spinner  spinner_uf;
    private ArrayAdapter <String> adapter_uf;
    private EditText edit_cep;

    private String    title;

    public InputEndereco(Activity activity, String title, String[] input) {
        this.activity = activity;
        this.title    = title;
        this.inflater = LayoutInflater.from(activity);
        this.view = inflater.inflate(R.layout.message_input_endereco, null);
        this.edit_logradouro = (EditText) view.findViewById(R.id.edit_logradouro);
        this.edit_numero = (EditText) view.findViewById(R.id.edit_numero);
        this.edit_complemento = (EditText) view.findViewById(R.id.edit_complemento);
        this.edit_bairro= (EditText) view.findViewById(R.id.edit_bairro);
        this.edit_cidade = (EditText) view.findViewById(R.id.edit_cidade);
        this.spinner_uf  = (Spinner)  view.findViewById(R.id.spinner_uf_endereco);
        this.adapter_uf = Adapters.GetSpinner(activity, spinner_uf);
        Utility.setUF(adapter_uf);
        this.edit_cep = (EditText) view.findViewById(R.id.edit_cep);
        if (input != null) {
            try {
                this.edit_logradouro.setText(input[0]);
                this.edit_numero.setText(input[1]);
                this.edit_complemento.setText(input[2]);
                this.edit_bairro.setText(input[3]);
                this.edit_cidade.setText(input[4]);
                this.spinner_uf.setSelection(adapter_uf.getPosition(input[5]));
                this.edit_cep.setText(input[6]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setInputEndereco() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setTitle(title);
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
        String[] endereco = {edit_logradouro.getText().toString(), edit_numero.getText().toString(), edit_complemento.getText().toString(), edit_bairro.getText().toString(), edit_cidade.getText().toString(), spinner_uf.getSelectedItem().toString(), edit_cep.getText().toString()};
        if (!enderecoIsEmpty(endereco)) {
            Intent it = activity.getIntent();
            it.putExtra("LOGRADOURO", edit_logradouro.getText().toString());
            it.putExtra("NUMERO", edit_numero.getText().toString());
            it.putExtra("COMPLEMENTO", edit_complemento.getText().toString());
            it.putExtra("BAIRRO", edit_bairro.getText().toString());
            it.putExtra("CIDADE", edit_cidade.getText().toString());
            it.putExtra("UF", spinner_uf.getSelectedItem().toString());
            it.putExtra("CEP", edit_cep.getText().toString());
            dialog.dismiss();
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.str_full), Toast.LENGTH_SHORT).show();
        }
    }
}
