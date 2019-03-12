package br.com.alexandrebarboza.consultoriomedico;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.alexandrebarboza.consultoriomedico.Database.Database;
import br.com.alexandrebarboza.consultoriomedico.Domain.Domain;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Consulta;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;
import br.com.alexandrebarboza.consultoriomedico.Utility.Dates;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, AdapterView.OnItemClickListener {
    private Database database;
    private Domain domain;
    private MainActivity.DataFilter filter;
    private EditText ed_data;
    private ImageButton bt_add;
    private ListView ls_consultas;
    private ArrayAdapter<Consulta> adapter;
    private Consulta consulta;
    public static final String p_consulta = "CONSULTA";

    private class DataFilter implements TextWatcher {
        private ArrayAdapter <Consulta> adapter;

        private DataFilter(ArrayAdapter <Consulta> adapter) {
            this.adapter = adapter;
        }

        public void setAdapter(ArrayAdapter <Consulta> adapter) {
            this.adapter = adapter;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            adapter.getFilter().filter(s);
        }
    }

    private class selecionaDataListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            ed_data.setText(Dates.DateToString(date, DateFormat.LONG));
            String str = Dates.ShortDateFromString(date.toString(), "en", "US");
            getIntent().putExtra("DATA", str);
        }
    }

    private void exibeData(Date dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day   = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new selecionaDataListener(), year, month, day);
        InputMethodManager im = (InputMethodManager) ed_data.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(ed_data.getWindowToken(), 0); // Oculta teclado.
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_add = (ImageButton) findViewById(R.id.button_adicionar_consulta);
        bt_add.setOnClickListener(this);
        ed_data = (EditText) findViewById(R.id.edit_data_consulta);
        ed_data.setOnClickListener(this);
        ed_data.setKeyListener(null);
        ls_consultas = (ListView) findViewById(R.id.list_consultas);
        ls_consultas.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_consultas));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        adapter = domain.findConsultas(this);
        ls_consultas.setAdapter(adapter);
        filter = new DataFilter(adapter);
        ed_data.addTextChangedListener(filter);
        database.Close();
        Date date;
        if (!getIntent().hasExtra("DATA")) {
            date = new Date();
        } else {
            date = Dates.StringToDate(getIntent().getStringExtra("DATA"), "en", "US", false);
        }
        ed_data.setText(Dates.DateToString(date, DateFormat.LONG));
        String str = Dates.ShortDateFromString(date.toString(), "en", "US");
        getIntent().putExtra("DATA", str);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it = null;
        if(item.getItemId() == R.id.m_medicos) {
            it = new Intent(this, MedicoActivity.class);
        } else if (item.getItemId() == R.id.m_pacientes) {
            it = new Intent(this, PacienteActivity.class);
        } else if (item.getItemId() == R.id.m_sair) {
            finish();
        }
        if (it != null) {
            startActivityForResult(it, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_adicionar_consulta:
                Intent it = new Intent(this, ConsultaEditActivity.class);
                it.putExtra("DATA", getIntent().getStringExtra("DATA"));
                startActivityForResult(it, 0);
                break;
            case R.id.edit_data_consulta:
                Date dt = Dates.StringToDate(getIntent().getStringExtra("DATA"), "en", "US", false);
                exibeData(dt);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Consulta consulta = adapter.getItem(position);
        Intent it = new Intent(this, ConsultaEditActivity.class);
        it.putExtra("DATA", getIntent().getStringExtra("DATA"));
        it.putExtra(p_consulta, consulta);
        startActivityForResult(it, 0);

    }
}
