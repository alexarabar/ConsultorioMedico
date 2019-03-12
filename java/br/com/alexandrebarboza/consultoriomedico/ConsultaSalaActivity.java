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
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Sala;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Input;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Output;

public class ConsultaSalaActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private Database database;
    private Domain domain;
    private Bundle extras;
    private Button bt_ok, bt_add, bt_del, bt_cancel;
    private ListView ls_salas;
    private ArrayAdapter<Sala> adapter;

    private Sala getSala(String text) {
        Sala sala;
        for (int i = 0; i < ls_salas.getCount(); i++) {
            sala = (Sala) ls_salas.getItemAtPosition(i);
            if (sala.getDescricao().equals(text)) {
                return sala;
            }
        }
        return null;
    }

    private int Comparar(String text) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).getDescricao().equals(text)) {
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
        s = getResources().getString(R.string.str_sala);
        s = s.substring(0, s.length() - 1).toLowerCase();
        Sala sala;
        if (flag) { // Inclusão.
            sala = new Sala();
            sala.setDescricao(extras.getString("INPUT"));
            if (domain.addSala(sala) < 0) {
                msg = getResources().getString(R.string.str_err_insert) + " " + s + " " + getResources().getString(R.string.str_msg_plus) + "\n";
                Output.Alert(this, getResources().getString(R.string.str_fail), msg + domain.getError());
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
                Listar();
            }
        } else { // Alteração.
            sala = getSala(extras.getString("OLD"));
            sala.setDescricao(extras.getString("INPUT"));
            if (domain.updateSala(sala) < 0) {
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
        adapter = domain.findSalas(this);
        ls_salas.setAdapter(adapter);
        ls_salas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        database.Close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Excluir() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
            return;
        }
        String s, msg;
        s = getResources().getString(R.string.str_sala);
        s = s.substring(0, s.length() - 1).toLowerCase();
        Sala sala = getSala(extras.getString("INPUT"));
        if (domain.deleteSala(sala.get_id()) < 0) {
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
        setContentView(R.layout.activity_consulta_sala);
        String t = getResources().getString(R.string.str_sala);
        t = t.substring(0, t.length() - 1) + "s";
        setTitle(t);
        bt_ok = (Button) findViewById(R.id.button_sala_ok);
        bt_ok.setOnClickListener(this);
        bt_add = (Button) findViewById(R.id.button_sala_add);
        bt_add.setOnClickListener(this);
        bt_del = (Button) findViewById(R.id.button_sala_del);
        bt_del.setOnClickListener(this);
        bt_cancel = (Button) findViewById(R.id.button_sala_cancel);
        bt_cancel.setOnClickListener(this);
        ls_salas = (ListView)   findViewById(R.id.list_salas);
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
                    String s1 = getResources().getString(R.string.str_sala);
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
            case R.id.button_sala_ok:
                intent.putExtra("SALA", extras.getString("INPUT"));
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.button_sala_add:
                input = new Input(this, getResources().getString(R.string.str_sala), "", 20);
                input.setInput();
                break;
            case R.id.button_sala_del:
                String text = extras.getString("INPUT");
                if (text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_none), Toast.LENGTH_SHORT).show();
                } else {
                    String st1, st2;
                    st1 = getResources().getString(R.string.str_delete);
                    st2 = getResources().getString(R.string.str_sala);
                    st2 = st2.substring(0, st2.length() -1).toLowerCase();
                    Output.Question(this, st1 + " a " +  st2 + " " + text + "?", getResources().getString(R.string.str_remove));
                    extras.putInt("DEL", 1);
                    intent.putExtras(extras);
                }
                break;
            case R.id.button_sala_cancel:
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                break;
        }
    }

}
