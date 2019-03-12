package br.com.alexandrebarboza.consultoriomedico;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ListView;

import br.com.alexandrebarboza.consultoriomedico.Database.Database;
import br.com.alexandrebarboza.consultoriomedico.Domain.Domain;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Paciente;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.ConsultaPacienteAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;

public class ConsultaPacienteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Database database;
    private Domain domain;
    private ConsultaPacienteActivity.DataFilter filter;
    private EditText ed_name;
    private ListView ls_pacientes;
    private ArrayAdapter<Paciente> adapter;

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
        setContentView(R.layout.activity_consulta_paciente);
        ed_name = (EditText) findViewById(R.id.edit_nome_paciente3);
        ls_pacientes = (ListView) findViewById(R.id.list_consulta_pacientes);
        ls_pacientes.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_pacientes));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        Intent it = getIntent();
        String conv = it.getStringExtra("CONVENIO");
        long   id_conv = 0;
        if (conv != null && !conv.isEmpty()) {
            id_conv = domain.getIdConvenio(this, conv);
        }

        adapter = domain.findPacientes(this, id_conv);
        ls_pacientes.setAdapter(adapter);
        filter = new ConsultaPacienteActivity.DataFilter(adapter);
        ed_name.addTextChangedListener(filter);
        database.Close();
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ConsultaPacienteAdapter cpa  = (ConsultaPacienteAdapter) adapter;
        Intent it = getIntent();
        it.putExtra("PACIENTE", cpa.getItem(position).getNome());
        it.putExtra("CONVENIO", cpa.getItem(position).getConvenio());
        setResult(Activity.RESULT_OK, it);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_voltar:
                setResult(Activity.RESULT_CANCELED, null);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
