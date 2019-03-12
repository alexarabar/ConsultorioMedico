package br.com.alexandrebarboza.consultoriomedico;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.alexandrebarboza.consultoriomedico.Database.Database;
import br.com.alexandrebarboza.consultoriomedico.Domain.Domain;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Email;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Endereco;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Paciente;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Telefone;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.PacienteXEmail;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.PacienteXEndereco;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.PacienteXTelefone;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.Adapters;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Input;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.InputEndereco;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Output;
import br.com.alexandrebarboza.consultoriomedico.Utility.Utility;

public class PacienteEditActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private final short DELETE_PACIENTE   = 1;
    private final short DELETE_TELEFONE = 2;
    private final short UPDATE_TELEFONE = 3;
    private final short INSERT_TELEFONE = 4;
    private final short DELETE_EMAIL = 5;
    private final short UPDATE_EMAIL = 6;
    private final short INSERT_EMAIL = 7;
    private final short DELETE_ENDERECO = 8;
    private final short UPDATE_ENDERECO = 9;
    private final short INSERT_ENDERECO = 10;

    private short operator;
    private Paciente paciente;

    private Database database;
    private Domain domain;

    private Spinner                     sp_telefone;
    private ArrayAdapter<String>        ad_telefone;
    private ArrayAdapter <Telefone>     telefones;
    private ArrayAdapter <PacienteXTelefone> paciente_x_telefone;

    private Spinner                     sp_email;
    private ArrayAdapter <String>       ad_email;
    private ArrayAdapter <Email>        emails;
    private ArrayAdapter <PacienteXEmail> paciente_x_email;

    private Spinner                     sp_endereco;
    private ArrayAdapter <String>       ad_endereco;
    private ArrayAdapter <Endereco>     enderecos;
    private ArrayAdapter <PacienteXEndereco> paciente_x_endereco;

    private TextView    tv_telefone;
    private TextView    tv_email;

    private ImageButton ib_insert_telefone;
    private ImageButton ib_update_telefone;
    private ImageButton ib_delete_telefone;

    private ImageButton ib_insert_email;
    private ImageButton ib_update_email;
    private ImageButton ib_delete_email;

    private ImageButton ib_insert_endereco;
    private ImageButton ib_update_endereco;
    private ImageButton ib_delete_endereco;

    private EditText ed_cpf;
    private EditText ed_nome;
    private EditText ed_convenio;

    private ImageButton ib_paciente_convenio;
    private Intent      it_convenio;

    private void Preencher() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        ed_nome.setText(paciente.getNome());
        ed_cpf.setText(paciente.getCpf());
        ed_convenio.setText(domain.getTextConvenio(this, paciente.getConvenio()));
        if (domain.setPacienteTelefones(telefones, paciente_x_telefone, ad_telefone, paciente.get_id())) {
            Utility.resetImages(true, ib_update_telefone, ib_delete_telefone);
            Utility.telefoneSort(telefones);
            Adapters.adapterSort(ad_telefone);
        } else {
            Utility.resetImages(false, ib_update_telefone, ib_delete_telefone);
        }
        if (domain.setPacienteEmails(emails, paciente_x_email, ad_email, paciente.get_id())) {
            Utility.resetImages(true, ib_update_email, ib_delete_email);
            Utility.emailSort(emails);
            Adapters.adapterSort(ad_email);
        } else {
            Utility.resetImages(false, ib_update_email, ib_delete_email);
        }
        if (domain.setPacienteEnderecos(enderecos, paciente_x_endereco, ad_endereco, paciente.get_id())) {
            Utility.resetImages(true, ib_update_endereco, ib_delete_endereco);
            Utility.enderecoSort(enderecos);
            Adapters.adapterSort(ad_endereco);
        } else {
            Utility.resetImages(false, ib_update_endereco, ib_delete_endereco);
        }
        database.Close();
    }

    private void Salvar() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
            return;
        }
        boolean result  = false;
        int[] control   = {0};
        long id_conv     = Connector.getIdConvenio(this, domain, ed_convenio.getText().toString());
        if (paciente.get_id() == 0) {   // Modo de Inclusão.
            result = Connector.SaveAddPaciente(this, domain, paciente, ad_telefone, ad_email, enderecos, ed_nome, ed_cpf, id_conv);
            control[0] = 1;
        } else {                      // Modo de Alteração.
            result = Connector.SaveUpdatePaciente(this, domain, paciente, telefones, emails, enderecos, ad_telefone, ad_email, sp_telefone, sp_email, ed_nome, ed_cpf, id_conv, control);
        }
        if (result) {
            if (control[0] == 1) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        database.Close();
    }

    private void Excluir() {
        operator = DELETE_PACIENTE;
        String st1, st2, st3;
        st1 = getResources().getString(R.string.str_delete);
        st2 = getResources().getString(R.string.str_pacientes);
        st2 = st2.substring(0, st2.length() -1);
        st2 = st2.toLowerCase();
        st3 = ed_nome.getText().toString();
        Output.Question(this, st1 + " o " + st2 + " " + st3, getResources().getString(R.string.str_remove));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getResources().getString(R.string.str_paciente_editar));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        operator = 0; // nenhuma operação
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_edit);

        ed_cpf  = (EditText) findViewById(R.id.edit_cpf_paciente);
        ed_nome = (EditText) findViewById(R.id.edit_nome_paciente2);
        ed_convenio = (EditText) findViewById(R.id.edit_convenio_paciente);
        ed_convenio.setKeyListener(null); // Desabilita digitação.

        sp_telefone = (Spinner) findViewById(R.id.spinner_telefone_paciente);
        ad_telefone = Adapters.GetSpinner(this, sp_telefone);
        ad_telefone.setNotifyOnChange(true);

        sp_email = (Spinner) findViewById(R.id.spinner_email_paciente);
        ad_email = Adapters.GetSpinner(this, sp_email);
        ad_email.setNotifyOnChange(true);

        sp_endereco = (Spinner) findViewById(R.id.spinner_endereco_paciente);
        ad_endereco = Adapters.GetSpinner(this, sp_endereco);
        ad_endereco.setNotifyOnChange(true);

        tv_telefone = (TextView) findViewById(R.id.label_telefone_paciente);
        tv_telefone.setOnClickListener(this);

        tv_email = (TextView) findViewById(R.id.label_email_paciente);
        tv_email.setOnClickListener(this);

        ib_insert_telefone = (ImageButton) findViewById(R.id.button_adicionar_telefone_paciente);
        ib_update_telefone = (ImageButton) findViewById(R.id.button_alterar_telefone_paciente);
        ib_delete_telefone = (ImageButton) findViewById(R.id.button_excluir_telefone_paciente);

        ib_insert_email = (ImageButton) findViewById(R.id.button_adicionar_email_paciente);
        ib_update_email = (ImageButton) findViewById(R.id.button_alterar_email_paciente);
        ib_delete_email = (ImageButton) findViewById(R.id.button_excluir_email_paciente);

        ib_insert_endereco = (ImageButton) findViewById(R.id.button_adicionar_endereco_paciente);
        ib_update_endereco = (ImageButton) findViewById(R.id.button_alterar_endereco_paciente);
        ib_delete_endereco = (ImageButton) findViewById(R.id.button_excluir_endereco_paciente);

        ib_insert_telefone.setOnClickListener(this);
        ib_update_telefone.setOnClickListener(this);
        ib_delete_telefone.setOnClickListener(this);

        ib_insert_email.setOnClickListener(this);
        ib_update_email.setOnClickListener(this);
        ib_delete_email.setOnClickListener(this);

        ib_insert_endereco.setOnClickListener(this);
        ib_update_endereco.setOnClickListener(this);
        ib_delete_endereco.setOnClickListener(this);

        Utility.resetImages(false, ib_update_telefone, ib_delete_telefone);
        Utility.resetImages(false, ib_update_email, ib_delete_email);
        Utility.resetImages(false, ib_update_endereco, ib_delete_endereco);

        ib_paciente_convenio = (ImageButton) findViewById(R.id.button_procurar_convenio_paciente);
        ib_paciente_convenio.setOnClickListener(this);

        telefones           = new ArrayAdapter <Telefone> (this, 0);
        paciente_x_telefone = new ArrayAdapter <PacienteXTelefone> (this, 0);

        emails           = new ArrayAdapter <Email> (this, 0);
        paciente_x_email = new ArrayAdapter <PacienteXEmail> (this, 0);

        enderecos           = new ArrayAdapter <Endereco> (this, 0);
        paciente_x_endereco = new ArrayAdapter <PacienteXEndereco> (this, 0);

        database = Database.getInstance(this);
        domain = domain.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(PacienteActivity.p_paciente)) { // modo de alteração;
            paciente = (Paciente) bundle.getSerializable(PacienteActivity.p_paciente);
            Preencher();
            tv_telefone.setBackground(getResources().getDrawable(R.drawable.lb_touchable));
            tv_email.setBackground(getResources().getDrawable(R.drawable.lb_touchable));
        } else {
            paciente = new Paciente(); // modo de inclusão.
            Bundle extra = getIntent().getExtras();
            ed_nome.setText(extra.getString("PACIENTE_TEXT"));
            ed_nome.setSelection(ed_nome.getText().toString().length());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        if (paciente.get_id() > 0)
            menu.getItem(1).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_salvar:
                int flag = 0;
                int[] vet = {-1};
                String campo = "";
                if (ed_nome.getText().toString().trim().isEmpty()) {
                    campo = getResources().getString(R.string.str_nome);
                } else if (ed_cpf.getText().toString().trim().isEmpty()) {
                    campo = getResources().getString(R.string.str_cpf);
                } else {
                    if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
                        return false;
                    }
                    if (paciente.get_id() == 0) { // Inclusão.
                        flag = Connector.LoadAddPaciente(domain, sp_telefone, sp_email, enderecos, ed_cpf, vet);
                    } else {                      // Alteração.
                        flag = Connector.LoadUpdatePaciente(domain, paciente, telefones, emails, enderecos, sp_telefone, sp_email, ed_cpf, vet);
                    }
                    database.Close();
                }
                String msg;
                int pos = vet[0];
                switch (flag) {
                    case -1:  // Erro na consulta SQL.
                        msg = getResources().getString(R.string.str_err_sql_query) + " " + getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
                        Output.Alert(this, getResources().getString(R.string.str_fail), msg);
                        break;
                    case 0:   // Erro no preenchimento do Campo
                        campo = " " + campo.substring(0, campo.length() - 1) + "!";
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_fill) + campo, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Salvar();
                        break;
                    case 2: // Já existe um registro com esse CPF.
                        msg = "A " + getResources().getString(R.string.str_key_paciente) + " " + getResources().getString(R.string.str_found) + " " + getResources().getString(R.string.str_in_db);
                        Output.Info(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                    case 3: // Já existe um registro com esse numero de telefone.
                        msg = "O " + getResources().getString(R.string.str_key_telefone) + " " + sp_telefone.getItemAtPosition(pos) + " " + getResources().getString(R.string.str_found) + " " + getResources().getString(R.string.str_in_db);
                        Output.Info(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                    case 4: // Já existe um usuário com esse endereço de e-mail.
                        msg = "O " + getResources().getString(R.string.str_key_email) + " " + sp_email.getItemAtPosition(pos) + " " + getResources().getString(R.string.str_found) + " " + getResources().getString(R.string.str_in_db);
                        Output.Info(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                    case 5: // Já existe um usuário com exatamente este endereço.
                        String s = getResources().getString(R.string.str_endereco);
                        s = s.substring(0, s.length() -1).toLowerCase() + " ";
                        msg = "O " + s + sp_endereco.getItemAtPosition(pos) + " " + getResources().getString(R.string.str_found) + " " +  getResources().getString(R.string.str_in_db);
                        Output.Info(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                }
                break;
            case R.id.m_excluir:
                Excluir();
                break;
            case R.id.m_limpar:
                ad_telefone.clear();
                ad_email.clear();
                ad_endereco.clear();
                telefones.clear();
                emails.clear();
                enderecos.clear();
                if (paciente.get_id() == 0) {
                    ed_cpf.setText("");
                    ed_convenio.setText("");
                    ed_nome.setText("");
                } else {
                    Preencher();
                }
                break;
            case R.id.m_cancelar:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Intent intent = null;
        ArrayAdapter <String> updated = null;
        Spinner selected = null;
        String   found = getResources().getString(R.string.str_found) + "!";
        String   str = "";
        String[] vec = {""};
        switch (operator) {
            case DELETE_PACIENTE: // Remover Paciente
                if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
                    return;
                }
                String[] v = {""};
                boolean flag = Connector.DeletePaciente(this, domain, paciente, telefones, emails, enderecos, v);
                str = v[0];
                if (!str.isEmpty()) {
                    String msg = getResources().getString(R.string.str_err_delete) + " " + str.toLowerCase() + getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
                    Output.Alert(this, getResources().getString(R.string.str_fail), msg);
                } else {
                    if (flag) {
                        Toast.makeText(this, getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
                database.Close();
                break;
            case DELETE_TELEFONE: // Remover Telefone
                Utility.deleteData(sp_telefone, ad_telefone, ib_update_telefone, ib_delete_telefone);
                break;
            case UPDATE_TELEFONE: // Alterar Telefone
                intent = getIntent();
                str = intent.getStringExtra("INPUT");
                if (Utility.updateData(this, str, sp_telefone, ad_telefone, getResources().getString(R.string.str_telefone), found)) {
                    updated  = ad_telefone;
                    selected = sp_telefone;
                }
                break;
            case INSERT_TELEFONE: // Incluir Telefone
                intent = getIntent();
                str = intent.getStringExtra("INPUT");
                if (Utility.insertData(this, str, ad_telefone, ib_update_telefone, ib_delete_telefone, getResources().getString(R.string.str_telefone), found, getResources().getString(R.string.str_empty))) {
                    updated  = ad_telefone;
                    selected = sp_telefone;
                }
                break;
            case DELETE_EMAIL: // Excluir e-mail
                Utility.deleteData(sp_email, ad_email, ib_update_email, ib_delete_email);
                break;
            case UPDATE_EMAIL: // Alterar e-mail.
                intent = getIntent();
                str = intent.getStringExtra("INPUT");
                if (Utility.updateData(this, str, sp_email, ad_email, getResources().getString(R.string.str_email), found)) {
                    updated  = ad_email;
                    selected = sp_email;
                }
                break;
            case INSERT_EMAIL: // Incluir e-mail.
                intent = getIntent();
                str = intent.getStringExtra("INPUT");
                if (Utility.insertData(this, str, ad_email, ib_update_email, ib_delete_email, getResources().getString(R.string.str_email), found, getResources().getString(R.string.str_empty))) {
                    updated  = ad_email;
                    selected = sp_email;
                }
                break;
            case DELETE_ENDERECO: // Excluir endereço.
                Utility.deleteEndereco(sp_endereco, ad_endereco, enderecos, ib_update_endereco, ib_delete_endereco);
                break;
            case UPDATE_ENDERECO: // Alterar endereço.
                intent = getIntent();
                if (Utility.updateEndereco(this, intent, sp_endereco, ad_endereco,  enderecos, getResources().getString(R.string.str_endereco), found, vec)) {
                    str = vec[0];
                    updated = ad_endereco;
                    selected = sp_endereco;
                }
                break;
            case INSERT_ENDERECO: // Incluir endereço.
                intent = getIntent();
                if (Utility.insertEndereco(this, intent, ad_endereco, enderecos, ib_update_endereco, ib_delete_endereco, getResources().getString(R.string.str_endereco), found, vec)) {
                    str = vec[0];
                    updated  = ad_endereco;
                    selected = sp_endereco;
                }
                break;
        }
        if (updated != null) {
            if (operator == INSERT_ENDERECO || operator == UPDATE_ENDERECO) {
                Utility.enderecoSort(enderecos);
            }
            Adapters.adapterSort(updated);
            updated.notifyDataSetChanged();
            if (selected != null) {
                selected.setSelection(updated.getPosition(str));
            }
            if (intent != null) {
                if (operator == INSERT_ENDERECO || operator == UPDATE_ENDERECO) {
                    Utility.enderecoClear(intent);
                } else {
                    intent.removeExtra("INPUT");
                }
            }
        }
        operator = 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        Input input;
        InputEndereco input_endereco;
        String st1, st2, st3;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.button_adicionar_telefone_paciente:
                operator = INSERT_TELEFONE ;
                input = new Input(this, getResources().getString(R.string.str_telefone), "", 15);
                input.setInput();
                break;
            case R.id.button_alterar_telefone_paciente:
                operator = UPDATE_TELEFONE;
                input = new Input(this, getResources().getString(R.string.str_telefone), sp_telefone.getSelectedItem().toString(), 15);
                input.setInput();
                break;
            case R.id.button_excluir_telefone_paciente:
                operator = DELETE_TELEFONE;
                st1 = getResources().getString(R.string.str_delete);
                st2 = getResources().getString(R.string.str_telefone);
                st2 = st2.substring(0, st2.length() -1).toLowerCase();
                st3 = sp_telefone.getSelectedItem().toString();
                Output.Question(this, st1 + " o " +  st2 + " " + st3 + "?", getResources().getString(R.string.str_remove));
                break;
            case R.id.button_adicionar_email_paciente:
                operator = INSERT_EMAIL;
                input = new Input(this, getResources().getString(R.string.str_email), "", 65);
                input.setInput();
                break;
            case R.id.button_alterar_email_paciente:
                operator = UPDATE_EMAIL;
                input = new Input(this, getResources().getString(R.string.str_email), sp_email.getSelectedItem().toString(), 65);
                input.setInput();
                break;
            case R.id.button_excluir_email_paciente:
                operator = DELETE_EMAIL;
                st1 = getResources().getString(R.string.str_delete);
                st2 = getResources().getString(R.string.str_email);
                st2 = st2.substring(0, st2.length() -1).toLowerCase();
                st3 = sp_email.getSelectedItem().toString();
                Output.Question(this, st1 + " o " +  st2 + " " + st3 + "?", getResources().getString(R.string.str_remove));
                break;
            case R.id.button_adicionar_endereco_paciente:
                operator = INSERT_ENDERECO;
                input_endereco = new InputEndereco(this, getResources().getString(R.string.str_endereco), null);
                input_endereco.setInputEndereco();
                break;
            case R.id.button_alterar_endereco_paciente:
                operator = UPDATE_ENDERECO;
                String[] array = {"", "", "", "", "", "", "", };
                Endereco endereco = enderecos.getItem(sp_endereco.getSelectedItemPosition());
                Utility.enderecoToArray(array, endereco);
                input_endereco = new InputEndereco(this, getResources().getString(R.string.str_endereco), array);
                input_endereco.setInputEndereco();
                break;
            case R.id.button_excluir_endereco_paciente:
                operator = DELETE_ENDERECO;
                st1 = getResources().getString(R.string.str_delete);
                st2 = getResources().getString(R.string.str_endereco);
                st2 = st2.substring(0, st2.length() -1).toLowerCase();
                st3 = sp_endereco.getSelectedItem().toString();
                Output.Question(this, st1 + " o " +  st2 + " " + st3 + "?", getResources().getString(R.string.str_remove));
                break;
            case R.id.button_procurar_convenio_paciente:
                it_convenio = new Intent(this, PacienteConvenioActivity.class);
                bundle = new Bundle();
                bundle.putString("INPUT", ed_convenio.getText().toString());
                it_convenio.putExtras(bundle);
                startActivityForResult(it_convenio, 0, bundle);
                break;
            case R.id.label_telefone_paciente:
                if (sp_telefone.getCount() > 0) {
                    String txt = sp_telefone.getSelectedItem().toString();
                    Intent it = new Intent(Intent.ACTION_DIAL);
                    it.setData(Uri.parse("tel:" + Uri.encode(txt.trim())));
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                }
                break;
            case R.id.label_email_paciente:
                if (sp_email.getCount() > 0) {
                    String to = sp_email.getSelectedItem().toString();
                    Intent it = new Intent(Intent.ACTION_SENDTO);
                    it.setType("text/plain");
                    it.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    it.putExtra(Intent.EXTRA_TEXT, "Prezado(a) Sr(a): " + ed_nome.getText().toString() + ";\n");
                    it.setData(Uri.parse("mailto:" + to));
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        Intent chooser = Intent.createChooser(it, getResources().getString(R.string.str_send_mail));
                        startActivity(chooser);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, getResources().getString(R.string.str_mail_none), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String txt = bundle.getString("INPUT");
                it_convenio.replaceExtras(bundle);
                ed_convenio.setText(txt);
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (data != null) {
                if (data.hasExtra("ACTION") == true) {
                    data.removeExtra("ACTION");
                }
                if (data.hasExtra("DEL") == true) {
                    data.removeExtra("DEL");
                }
                if (data.hasExtra("OLD") == true) {
                    data.removeExtra("OLD");
                }
                if (data.hasExtra("INPUT") == true) {
                    data.removeExtra("INPUT");
                }
                it_convenio.replaceExtras(data);
            }
            long id;
            String txt;
            if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
                return;
            }
            txt = ed_convenio.getText().toString();
            id = domain.getIdConvenio(this, txt);
            if (id == 0) {
                ed_convenio.setText("");
            }
            database.Close();
        }
    }

}
