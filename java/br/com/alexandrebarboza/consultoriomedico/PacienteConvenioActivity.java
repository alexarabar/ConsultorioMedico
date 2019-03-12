package br.com.alexandrebarboza.consultoriomedico;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import br.com.alexandrebarboza.consultoriomedico.Database.Database;
import br.com.alexandrebarboza.consultoriomedico.Domain.Domain;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Convenio;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Input;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Output;

public class PacienteConvenioActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener{
    private Database database;
    private Domain domain;
    private Bundle extras;
    private Button bt_ok, bt_add, bt_del, bt_cancel;
    private ListView ls_convenios;
    private ArrayAdapter<Convenio> adapter;

    private Convenio getConvenio(String text) {
        Convenio conv;
        for (int i = 0; i < ls_convenios.getCount(); i++) {
            conv = (Convenio) ls_convenios.getItemAtPosition(i);
            if (conv.getNome().equals(text)) {
                return conv;
            }
        }
        return null;
    }

    private int Comparar(String text) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).getNome().equals(text)) {
                return 1;
            }
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Gravar(boolean flag) {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
            return;
        }
        String s, msg;
        s = getResources().getString(R.string.str_convenio);
        s = s.substring(0, s.length() - 1).toLowerCase();
        Convenio convenio;
        if (flag) { // Inclusão.
            convenio = new Convenio();
            convenio.setNome(extras.getString("INPUT"));
            if (domain.addConvenio(convenio) < 0) {
                msg = getResources().getString(R.string.str_err_insert) + " " + s + " " + getResources().getString(R.string.str_msg_plus) + "\n";
                Output.Alert(this, getResources().getString(R.string.str_fail), msg + domain.getError());
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
                Listar();
            }
        } else { // Alteração.
            convenio = getConvenio(extras.getString("OLD"));
            convenio.setNome(extras.getString("INPUT"));
            if (domain.updateConvenio(convenio) < 0) {
                msg = getResources().getString(R.string.str_err_update) + " " + s + " " + getResources().getString(R.string.str_msg_plus) + "\n";
                Output.Alert(this, getResources().getString(R.string.str_fail), msg + domain.getError());
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
                Listar();
            }
        }
        database.Close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Listar() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        adapter = domain.findPacienteConvenios(this);
        ls_convenios.setAdapter(adapter);
        ls_convenios.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        database.Close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Excluir() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
            return;
        }
        String s, msg;
        s = getResources().getString(R.string.str_convenio);
        s = s.substring(0, s.length() - 1).toLowerCase();
        Convenio convenio = getConvenio(extras.getString("INPUT"));
        if (domain.deleteConvenio(convenio.get_id()) < 0) {
            msg = getResources().getString(R.string.str_err_delete) + " " + s + " " + getResources().getString(R.string.str_msg_plus) + "\n";
            Output.Alert(this, getResources().getString(R.string.str_fail), msg + domain.getError());
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
            Listar();
        }
        database.Close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_convenio);
        String t = getResources().getString(R.string.str_convenio);
        t = t.substring(0, t.length() - 1) + "s";
        setTitle(t);
        bt_ok = (Button) findViewById(R.id.button_paciente_convenio_ok);
        bt_ok.setOnClickListener(this);
        bt_add = (Button) findViewById(R.id.button_paciente_convenio_add);
        bt_add.setOnClickListener(this);
        bt_del = (Button) findViewById(R.id.button_paciente_convenio_del);
        bt_del.setOnClickListener(this);
        bt_cancel = (Button) findViewById(R.id.button_paciente_convenio_cancel);
        bt_cancel.setOnClickListener(this);
        ls_convenios = (ListView)   findViewById(R.id.list_paciente_convenios);
        domain = domain.getInstance();
        database = database.getInstance(this);
        Listar();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onDismiss(DialogInterface dialog) {
        extras = getIntent().getExtras();
        String text = extras.getString("INPUT");
        String old  = extras.getString("OLD");
        if (extras.getInt("DEL", 0) == 1) {
            Excluir();
            extras.putInt("DEL", 0);
            extras.putString("INPUT", "");
        } else {
            if (text.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_empty), Toast.LENGTH_SHORT).show();
            } else if (Comparar(text) == 1) {
                if (!text.equals(old)) {
                    String s1 = getResources().getString(R.string.str_convenio);
                    String s2 = getResources().getString(R.string.str_found);
                    s1 = s1.substring(0, s1.length() - 1) + " ";
                    s2 = s1 + s2 + "!";
                    Toast.makeText(getApplicationContext(), s2, Toast.LENGTH_SHORT).show();
                }
            } else {
                extras.putString("INPUT", text);
                int result = extras.getInt("ACTION", 0);
                if (result == 1) {
                    Gravar(false);          // UPDATE!
                    extras.putInt("ACTION", 0);
                } else {
                    Gravar(true);           // INSERT!
                }
            }
        }
        getIntent().putExtras(extras);
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        extras = intent.getExtras();
        Input input;
        switch (v.getId()) {
            case R.id.button_paciente_convenio_ok:
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.button_paciente_convenio_add:
                input = new Input(this, getResources().getString(R.string.str_convenio), "", 45);
                input.setInput();
                break;
            case R.id.button_paciente_convenio_del:
                String text = extras.getString("INPUT");
                if (text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_none), Toast.LENGTH_SHORT).show();
                } else {
                    String st1, st2;
                    st1 = getResources().getString(R.string.str_delete);
                    st2 = getResources().getString(R.string.str_convenio);
                    st2 = st2.substring(0, st2.length() -1).toLowerCase();
                    Output.Question(this, st1 + " o " +  st2 + " " + text + "?", getResources().getString(R.string.str_remove));
                    extras.putInt("DEL", 1);
                    intent.putExtras(extras);
                }
                break;
            case R.id.button_paciente_convenio_cancel:
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                break;
        }
    }
}
