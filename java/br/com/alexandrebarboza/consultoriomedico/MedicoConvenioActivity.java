package br.com.alexandrebarboza.consultoriomedico;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

import br.com.alexandrebarboza.consultoriomedico.Database.Database;
import br.com.alexandrebarboza.consultoriomedico.Domain.Domain;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Convenio;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Input;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Output;

public class MedicoConvenioActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private Database database;
    private Domain domain;
    private Button bt_ok, bt_add, bt_del, bt_cancel;
    private ListView ls_convenios;
    private ArrayAdapter<Convenio> adapter;
    private ArrayList <String> list;

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

    private void Listar() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        adapter = domain.findMedicoConvenios(this);
        ls_convenios.setAdapter(adapter);
        ls_convenios.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        database.Close();
    }

    private void Gravar(boolean flag) {
        Intent intent =  getIntent();
        if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
            return;
        }
        String s, msg;
        s = getResources().getString(R.string.str_convenio);
        s = s.substring(0, s.length() - 1).toLowerCase();
        if (flag) { // Inclusão.
            Convenio convenio = new Convenio();
            convenio.setNome(intent.getStringExtra("INPUT"));
            if (domain.addConvenio(convenio) < 0) {
                msg = getResources().getString(R.string.str_err_insert) + " " + s + " " + getResources().getString(R.string.str_msg_plus) + "\n";
                Output.Alert(this, getResources().getString(R.string.str_fail), msg + domain.getError());
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
                Listar();
            }
        } else { // Alteração.
            Convenio convenio = getConvenio(intent.getStringExtra("OLD"));
            convenio.setNome(intent.getStringExtra("INPUT"));
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

    private void Excluir() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
            return;
        }
        Intent intent = this.getIntent();
        this.list = intent.getStringArrayListExtra("LIST");
        boolean f1 = false;
        boolean f2 = false;
        int c = list.size();
        for (int i = c -1; i >= 0; i--) {
            Convenio convenio = getConvenio(list.get(i));
            if (domain.deleteConvenio(convenio.get_id()) < 0) {
                f1 = false;
                break;
            } else {
                if (domain.deleteMedicoXConvenio(intent.getLongExtra("ID", 0), convenio.get_id()) < 0) {
                    f1 = false;
                    f2 = true;
                    break;
                }
                list.remove(i);
                f1 = true;
            }
        }
        if (f1 == true) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
            Listar();
        } else {
            String s, msg;
            if (f2 == true) {
                s = getResources().getString(R.string.str_med_x_conv);
                msg = getResources().getString(R.string.str_err_delete) + " " + s + " " + getResources().getString(R.string.str_msg_plus) + "\n";
            } else {
                s = getResources().getString(R.string.str_convenio);
                s = s.substring(0, s.length() - 1).toLowerCase();
                msg = getResources().getString(R.string.str_err_delete) + " " + s + " " + getResources().getString(R.string.str_msg_plus) + "\n";
            }
            Output.Alert(this, getResources().getString(R.string.str_fail), msg + domain.getError());
        }
        database.Close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_convenio);
        Intent intent = this.getIntent();
        this.list = intent.getStringArrayListExtra("LIST");
        String t = getResources().getString(R.string.str_convenio);
        t = t.substring(0, t.length() - 1) + "s";
        setTitle(t);
        bt_ok = (Button) findViewById(R.id.button_medico_convenio_ok);
        bt_ok.setOnClickListener(this);
        bt_add = (Button) findViewById(R.id.button_medico_convenio_add);
        bt_add.setOnClickListener(this);
        bt_del = (Button) findViewById(R.id.button_medico_convenio_del);
        bt_del.setOnClickListener(this);
        bt_cancel = (Button) findViewById(R.id.button_medico_convenio_cancel);
        bt_cancel.setOnClickListener(this);
        ls_convenios = (ListView)   findViewById(R.id.list_medico_convenios);
        domain = domain.getInstance();
        database = database.getInstance(this);
        Listar();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Intent intent = getIntent();
        String text = intent.getStringExtra("INPUT");
        String old  = intent.getStringExtra("OLD");
        if (intent.getIntExtra("DEL", 0) == 1) {
            Excluir();
            intent.putExtra("DEL", 0);
            intent.putStringArrayListExtra("LIST", null);
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
                intent.putExtra("INPUT", text);
                int result = intent.getIntExtra("ACTION", 0);
                if (result == 1) {
                    Gravar(false);          // UPDATE!
                    intent.putExtra("ACTION", 0);
                } else {
                    Gravar(true);           // INSERT!
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = this.getIntent();
        Input input;
        switch (v.getId()) {
            case R.id.button_medico_convenio_ok:
                intent.putExtra("LIST", list);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.button_medico_convenio_add:
                input = new Input(this, getResources().getString(R.string.str_convenio), "", 45);
                input.setInput();
                break;
            case R.id.button_medico_convenio_del: // Excluir todos os registros marcados.
                if (list.size() == 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_none), Toast.LENGTH_SHORT).show();
                } else {
                    String st1, st2, st3, st4;
                    st1 = getResources().getString(R.string.str_delete) + " ";
                    st2 = getResources().getString(R.string.str_all) + " os ";
                    st3 = getResources().getString(R.string.str_convenio);
                    st3 = st3.substring(0, st3.length() -1).toLowerCase() + "s ";
                    st4 = getResources().getString(R.string.str_select) + "?";
                    Output.Question(this, st1 + st2 + st3 + st4, getResources().getString(R.string.str_remove));
                    intent.putExtra("DEL", 1);
                    intent.putExtra("LIST", list);
                }
                break;
            case R.id.button_medico_convenio_cancel:
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                break;
        }
    }
}
