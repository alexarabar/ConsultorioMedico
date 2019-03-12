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
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Medico;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.ConsultaMedicoAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;

public class ConsultaMedicoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Database database;
    private Domain domain;
    private ConsultaMedicoActivity.DataFilter filter;
    private EditText ed_name;
    private ListView ls_medicos;
    private ArrayAdapter<Medico> adapter;

    private class DataFilter implements TextWatcher {
        private ArrayAdapter <Medico> adapter;

        private DataFilter(ArrayAdapter <Medico> adapter) {
            this.adapter = adapter;
        }

        public void setAdapter(ArrayAdapter <Medico> adapter) {
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
        setContentView(R.layout.activity_consulta_medico);
        ed_name = (EditText) findViewById(R.id.edit_nome_medico3);
        ls_medicos = (ListView) findViewById(R.id.list_consulta_medicos);
        ls_medicos.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_medicos));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        Intent it = getIntent();
        long   id_esp, id_conv;
        String esp, conv;
        esp  = it.getStringExtra("ESPECIALIDADE");
        conv = it.getStringExtra("CONVENIO");
        id_esp = id_conv = 0;
        if (!esp.isEmpty()) {
            id_esp = domain.getIdEspecialidade(this, esp);
        }
        if (!conv.isEmpty()) {
            id_conv = domain.getIdConvenio(this, conv);
        }
        adapter = domain.findMedicos(this, id_esp, id_conv);
        ls_medicos.setAdapter(adapter);
        filter = new ConsultaMedicoActivity.DataFilter(adapter);
        ed_name.addTextChangedListener(filter);
        database.Close();
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ConsultaMedicoAdapter cma  = (ConsultaMedicoAdapter) adapter;
        Intent it = getIntent();
        it.putExtra("MEDICO", cma.getItem(position).getNome());
        it.putExtra("ESPECIALIDADE", cma.getItem(position).getEspecialidade());
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
