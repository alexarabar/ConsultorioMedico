package br.com.alexandrebarboza.consultoriomedico;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import br.com.alexandrebarboza.consultoriomedico.Database.Database;
import br.com.alexandrebarboza.consultoriomedico.Domain.Domain;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Paciente;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Output;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.ProgressPacientes;
import br.com.alexandrebarboza.consultoriomedico.Utility.Utility;

public class PacienteActivity extends AppCompatActivity  implements View.OnClickListener, View.OnFocusChangeListener, AdapterView.OnItemClickListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private Database database;
    private Domain domain;
    private DataFilter filter;
    private EditText ed_name;
    private ImageButton bt_add;
    private ListView ls_pacientes;
    private ArrayAdapter<Paciente> adapter;
    private ProgressPacientes progress;
    public static final String p_paciente = "PACIENTE";

    private class DataFilter implements TextWatcher {
        private ArrayAdapter <Paciente> adapter;

        private DataFilter(ArrayAdapter <Paciente> adapter) {
            this.adapter = adapter;
        }

        public void setAdapter(ArrayAdapter <Paciente> adapter) {
            this.adapter = adapter;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            adapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);
        bt_add = (ImageButton) findViewById(R.id.button_adicionar_paciente);
        bt_add.setOnClickListener(this);
        ed_name = (EditText) findViewById(R.id.edit_nome_paciente1);
        ls_pacientes = (ListView) findViewById(R.id.list_pacientes);
        ls_pacientes.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
        progress = null;
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_pacientes));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        adapter = domain.findPacientes(this);
        ls_pacientes.setAdapter(adapter);
        filter = new DataFilter(adapter);
        ed_name.addTextChangedListener(filter);
        database.Close();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        ed_name.setText("");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (progress != null && progress.getStatus() == AsyncTask.Status.RUNNING) {
            progress.cancel(false);
            progress = null;
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this, PacienteEditActivity.class);
        it.putExtra("PACIENTE_TEXT", ed_name.getText().toString());
        startActivityForResult(it, 0);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Paciente paciente = adapter.getItem(position);
        Intent it = new Intent(this, PacienteEditActivity.class);
        it.putExtra(p_paciente, paciente);
        startActivityForResult(it, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(true);
        menu.getItem(5).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_voltar:
                setResult(Activity.RESULT_CANCELED, null);
                finish();
                break;
            case R.id.m_exportar:
                int i = ls_pacientes.getCount();
                if (i == 0) {
                    break;
                }
                String msg = getResources().getString(R.string.str_export);
                String s = getResources().getString(R.string.str_pacientes).toLowerCase();
                if (i == 1) {
                    s = s.substring(0, s.length() -1);
                } else {
                    msg += " os";
                }
                msg += " (" + i + ") " + s + " " + getResources().getString(R.string.str_contacts) + "?";
                Output.Question(this, msg, getResources().getString(R.string.str_exp_title));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }

        ProgressDialog status = Utility.createProgressDialog(this, this.getResources().getString(R.string.str_contact_create), ls_pacientes.getCount());
        progress = new ProgressPacientes(database, domain, this, status, ls_pacientes);
        progress.execute();

        /*
        for (int i = 0; i < ls_pacientes.getCount(); i++) {
            String nome = ls_pacientes.getItemAtPosition(i).toString();
            String[] tels = null;
            String[] mails = null;
            long id = domain.getIdPaciente(this, nome);
            int c1 = domain.getCountPacienteXTelefones(id);
            int c2 = domain.getCountPacienteXEmails(id);
            if (c1 > 0) {
                tels = new String[c1];
            }
            if (c2 > 0) {
                mails = new String[c2];
            }
            domain.setArrayPacienteTelefones(tels, id);
            domain.setArrayPacienteEmails(mails, id);
            ArrayAdapter<Endereco> enderecos = domain.selectEnderecos(this, id, Connector.PACIENTE);
            String res = Utility.exportContacts(this, this.getResources().getString(R.string.str_pacientes), nome, tels, mails, enderecos);

        }
        */
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
