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
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Convenio;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.ConsultaConvenioAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;

public class ConsultaConvenioActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Database database;
    private Domain domain;
    private DataFilter filter;
    private EditText ed_nome;
    private ListView ls_convenios;
    private ArrayAdapter<Convenio> adapter;

    private class DataFilter implements TextWatcher {
        private ArrayAdapter<Convenio> adapter;

        private DataFilter(ArrayAdapter<Convenio> adapter) {
            this.adapter = adapter;
        }

        public void setAdapter(ArrayAdapter<Convenio> adapter) {
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
        setContentView(R.layout.activity_consulta_convenio);
        ed_nome = (EditText) findViewById(R.id.edit_convenio_nome);
        ls_convenios = (ListView) findViewById(R.id.list_consulta_convenios);
        ls_convenios.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_convenios));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        adapter = domain.findConsultaConvenios(this);
        ls_convenios.setAdapter(adapter);
        filter = new ConsultaConvenioActivity.DataFilter(adapter);
        ed_nome.addTextChangedListener(filter);
        database.Close();
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ConsultaConvenioAdapter cca = (ConsultaConvenioAdapter) adapter;
        Intent it = getIntent();
        it.putExtra("CONVENIO", cca.getItem(position).getNome());
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
