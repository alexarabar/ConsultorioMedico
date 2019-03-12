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
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Medico;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Output;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.ProgressMedicos;
import br.com.alexandrebarboza.consultoriomedico.Utility.Utility;

public class MedicoActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, AdapterView.OnItemClickListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private Database database;
    private Domain domain;
    private DataFilter filter;
    private EditText ed_name;
    private ImageButton bt_add;
    private ListView ls_medicos;
    private ArrayAdapter<Medico> adapter;
    ProgressMedicos progress;
    public static final String p_medico = "MEDICO";

    private class DataFilter implements TextWatcher {
        private ArrayAdapter<Medico> adapter;

        private DataFilter(ArrayAdapter<Medico> adapter) {
            this.adapter = adapter;
        }

        public void setAdapter(ArrayAdapter<Medico> adapter) {
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
        setContentView(R.layout.activity_medico);
        bt_add = (ImageButton) findViewById(R.id.button_adicionar_medico);
        bt_add.setOnClickListener(this);
        ed_name = (EditText) findViewById(R.id.edit_nome_medico1);
        ls_medicos = (ListView) findViewById(R.id.list_medicos);
        ls_medicos.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
        progress = null;
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_medicos));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        adapter = domain.findMedicos(this);
        ls_medicos.setAdapter(adapter);
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
        Intent it = new Intent(this, MedicoEditActivity.class);
        it.putExtra("MEDICO_TEXT", ed_name.getText().toString());
        startActivityForResult(it, 0);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Medico medico = adapter.getItem(position);
        Intent it = new Intent(this, MedicoEditActivity.class);
        it.putExtra(p_medico, medico);
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
                int i = ls_medicos.getCount();
                if (i == 0) {
                    break;
                }
                String msg = getResources().getString(R.string.str_export);
                String s = getResources().getString(R.string.str_medicos).toLowerCase();
                if (i == 1) {
                    s = s.substring(0, s.length() - 1);
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
        ProgressDialog status = Utility.createProgressDialog(this, this.getResources().getString(R.string.str_contact_create), ls_medicos.getCount());
        progress = new ProgressMedicos(database, domain, this, status, ls_medicos);
        progress.execute();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}