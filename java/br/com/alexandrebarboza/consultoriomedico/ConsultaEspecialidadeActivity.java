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
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Especialidade;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.ConsultaEspecialidadeAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;

public class ConsultaEspecialidadeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Database database;
    private Domain domain;
    private DataFilter filter;
    private EditText ed_titulo;
    private ListView ls_especialidades;
    private ArrayAdapter<Especialidade> adapter;

    private class DataFilter implements TextWatcher {
        private ArrayAdapter <Especialidade> adapter;

        private DataFilter(ArrayAdapter <Especialidade> adapter) {
            this.adapter = adapter;
        }

        public void setAdapter(ArrayAdapter <Especialidade> adapter) {
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
        setContentView(R.layout.activity_consulta_especialidade);
        ed_titulo = (EditText) findViewById(R.id.edit_especialidade_titulo);
        ls_especialidades = (ListView) findViewById(R.id.list_consulta_especialidades);
        ls_especialidades.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_especialidades));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        adapter = domain.findConsultaEspecialidades(this);
        ls_especialidades.setAdapter(adapter);
        filter = new ConsultaEspecialidadeActivity.DataFilter(adapter);
        ed_titulo.addTextChangedListener(filter);
        database.Close();
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ConsultaEspecialidadeAdapter cea  = (ConsultaEspecialidadeAdapter) adapter;
        Intent it = getIntent();
        it.putExtra("ESPECIALIDADE", cea.getItem(position).getTitulo());
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
