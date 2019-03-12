package br.com.alexandrebarboza.consultoriomedico;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.alexandrebarboza.consultoriomedico.Database.Database;
import br.com.alexandrebarboza.consultoriomedico.Domain.Domain;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Consulta;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;
import br.com.alexandrebarboza.consultoriomedico.Utility.Dates;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.InputTelefone;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Output;
import br.com.alexandrebarboza.consultoriomedico.Utility.Utility;

public class ConsultaEditActivity extends AppCompatActivity implements View.OnClickListener, EditText.OnFocusChangeListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private final int FIND_MEDICO        = 1;
    private final int FIND_ESPECIALIDADE = 2;
    private final int FIND_PACIENTE      = 3;
    private final int FIND_CONVENIO      = 4;
    private final int FIND_SALA          = 5;
    private final int DELETE_CONSULTA    = 6;
    private final int SEND_SMS           = 7;
    private int dismiss;
    private TextView tv_data;
    private TextView tv_medico;
    private TextView tv_paciente;
    private EditText ed_hora_ini;
    private EditText ed_min_ini;
    private EditText ed_hora_fim;
    private EditText ed_min_fim;
    private EditText ed_medico;
    private EditText ed_especialidade;
    private EditText ed_paciente;
    private EditText ed_convenio;
    private EditText ed_sala;
    private ImageButton ib_medico;
    private ImageButton ib_especialidade;
    private ImageButton ib_paciente;
    private ImageButton ib_convenio;
    private ImageButton ib_sala;
    private Database database;
    private Domain   domain;
    private Consulta consulta;

    private class selecionaDataListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            tv_data.setText(Dates.DateToString(date, DateFormat.FULL));
            String str = Dates.ShortDateFromString(date.toString(), "en", "US");
            getIntent().putExtra("OLD", getIntent().getStringExtra("DATA"));
            getIntent().putExtra("DATA", str);
        }
    }

    private class DataFilter implements TextWatcher {
        private EditText edit;
        private int     limit;

        private DataFilter(EditText edit, int limit) {
            this.edit  = edit;
            this.limit = limit;
        }

        public void setEdit(EditText edit) {
            this.edit = edit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int value = 0;
            String str = edit.getText().toString();
            try {
                value = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                if (str.isEmpty()) {
                    return;
                } else {
                    e.printStackTrace();
                }
            }
            if (value > this.limit || value < 0) {
                this.edit.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (this.edit.getText().toString().isEmpty()) {
                return;
            }
            if (s.length() == 2) {
                if (this.edit.getId() == ed_hora_ini.getId()) {
                    ed_min_ini.requestFocus();
                } else if (this.edit.getId() == ed_hora_fim.getId()) {
                    ed_min_fim.requestFocus();
                } else if (this.edit.getId() == ed_min_ini.getId()) {
                    ed_hora_fim.requestFocus();
                } else if (this.edit.getId() == ed_min_fim.getId()) {
                    ed_min_fim.clearFocus();
                    InputMethodManager im = (InputMethodManager) ed_min_fim.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(ed_min_fim.getWindowToken(), 0); // Oculta teclado.
                }
            }
        }
    }

    private void exibeData(Date dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day   = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new selecionaDataListener(), year, month, day);
        InputMethodManager im = (InputMethodManager) tv_data.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(tv_data.getWindowToken(), 0); // Oculta teclado.
        dialog.show();
    }

    private void setTime(EditText ed_hora, EditText ed_min, Time t) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(t);
        String h1 = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String m1 = String.valueOf(calendar.get(Calendar.MINUTE));
        ed_hora.setText(Utility.ZeroFill(h1));
        ed_min.setText(Utility.ZeroFill(m1));
    }

    private void Preencher() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        Time t1 = consulta.getInicio();
        Time t2 = consulta.getFim();
        setTime(ed_hora_ini, ed_min_ini, t1);
        setTime(ed_hora_fim, ed_min_fim, t2);
        String med = domain.getTextMedico(this, consulta.get_medico());
        String pac = domain.getTextPaciente(this, consulta.get_paciente());
        String sal = domain.getTextSala(this, consulta.get_sala());
        String esp = domain.getTextEspecialidadeMedico(this, consulta.get_medico());
        String con = domain.getTextConvenioPaciente(this, consulta.get_paciente());
        ed_medico.setText(med);
        ed_paciente.setText(pac);
        ed_sala.setText(sal);
        ed_especialidade.setText(esp);
        ed_convenio.setText(con);
        database.Close();
    }

    private void Salvar() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
            return;
        }
        boolean result  = false;
        String dt = getIntent().getStringExtra("DATA");
        String t1 = Dates.getTime(ed_hora_ini.getText().toString(), ed_min_ini.getText().toString());
        String t2 = Dates.getTime(ed_hora_fim.getText().toString(), ed_min_fim.getText().toString());
        long id_pac = domain.getIdPaciente(this, ed_paciente.getText().toString());
        long id_med = domain.getIdMedico(this, ed_medico.getText().toString());
        long id_sal = domain.getIdSala(this, ed_sala.getText().toString());
        if (consulta.get_id() == 0) {   // Modo de Inclusão.
            result = Connector.SaveAddConsulta(this, domain, consulta, dt, t1, t2, id_pac, id_med, id_sal);
        } else {                      // Modo de Alteração.
            result = Connector.SaveUpdateConsulta(this, domain, consulta, dt, t1, t2, id_pac, id_med, id_sal);
        }
        if (result) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
            finish();
        }
        database.Close();
    }

    private void Excluir() {
        String st1, st2, st3, st4;
        st1 = getResources().getString(R.string.str_delete);
        st2 = getResources().getString(R.string.str_consultas);
        st2 = st2.substring(0, st2.length() -1);
        st2 = st2.toLowerCase();
        Date dt = Dates.convertFromDefaultDate(consulta.getData());
        st3 = " de: " + Dates.DateToString(dt, DateFormat.SHORT);
        st4 = consulta.getInicio().toString();
        st4 = " as: " + st4.substring(0, st4.length() - 3);
        dismiss = DELETE_CONSULTA;
        Output.Question(this, st1 + " a " + st2 + st3 + st4 + "?", getResources().getString(R.string.str_remove));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_edit);
        this.dismiss = 0;
        tv_data = (TextView) findViewById(R.id.text_data);
        tv_data.setOnClickListener(this);
        String str = getIntent().getStringExtra("DATA");
        Date date = Dates.StringToDate(str, "en", "US", false);
        tv_data.setText(Dates.DateToString(date, DateFormat.FULL));

        tv_medico = (TextView) findViewById(R.id.text_medico_consulta);
        tv_paciente = (TextView) findViewById(R.id.text_paciente_consulta);
        tv_medico.setOnClickListener(this);
        tv_paciente.setOnClickListener(this);

        ed_hora_ini = (EditText) findViewById(R.id.edit_horas_inicio);
        ed_min_ini =  (EditText) findViewById(R.id.edit_minutos_inicio);
        ed_hora_fim = (EditText) findViewById(R.id.edit_horas_fim);
        ed_min_fim =  (EditText) findViewById(R.id.edit_minutos_fim);

        DataFilter f1 = new DataFilter(ed_hora_ini, 23);
        DataFilter f2 = new DataFilter(ed_min_ini, 59);
        DataFilter f3 = new DataFilter(ed_hora_fim, 23);
        DataFilter f4 = new DataFilter(ed_min_fim, 59);

        ed_hora_ini.addTextChangedListener(f1);
        ed_min_ini.addTextChangedListener(f2);
        ed_hora_fim.addTextChangedListener(f3);
        ed_min_fim.addTextChangedListener(f4);

        ed_hora_ini.setOnFocusChangeListener(this);
        ed_min_ini.setOnFocusChangeListener(this);
        ed_hora_fim.setOnFocusChangeListener(this);
        ed_min_fim.setOnFocusChangeListener(this);

        ed_medico         = (EditText) findViewById(R.id.edit_medico_consulta);
        ed_especialidade  = (EditText) findViewById(R.id.edit_especialidade_consulta);
        ed_paciente       = (EditText) findViewById(R.id.edit_paciente_consulta);
        ed_convenio       = (EditText) findViewById(R.id.edit_convenio_consulta);
        ed_sala           = (EditText) findViewById(R.id.edit_sala_consulta);

        ed_medico.setKeyListener(null);
        ed_especialidade.setKeyListener(null);
        ed_paciente.setKeyListener(null);
        ed_convenio.setKeyListener(null);
        ed_sala.setKeyListener(null);

        ib_medico        = (ImageButton) findViewById(R.id.button_procurar_medico_consulta);
        ib_especialidade = (ImageButton) findViewById(R.id.button_procurar_especialidade_consulta);
        ib_paciente      = (ImageButton) findViewById(R.id.button_procurar_paciente_consulta);
        ib_convenio      = (ImageButton) findViewById(R.id.button_procurar_convenio_consulta);
        ib_sala          = (ImageButton) findViewById(R.id.button_procurar_sala_consulta);

        ib_medico.setOnClickListener(this);
        ib_especialidade.setOnClickListener(this);
        ib_paciente.setOnClickListener(this);
        ib_convenio.setOnClickListener(this);
        ib_sala.setOnClickListener(this);

        database = Database.getInstance(this);
        domain = domain.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(MainActivity.p_consulta)) { // modo de alteração;
            consulta = (Consulta) bundle.getSerializable(MainActivity.p_consulta);
            Preencher();
            tv_medico.setBackground(getResources().getDrawable(R.drawable.lb_touchable));
            tv_paciente.setBackground(getResources().getDrawable(R.drawable.lb_touchable));
        } else {
            consulta = new Consulta(); // modo de inclusão.
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getResources().getString(R.string.str_consulta_editar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        if (consulta.get_id() > 0)
            menu.getItem(1).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_salvar:
                String msg = "", s;
                boolean error = false;
                if (ed_hora_ini.getText().toString().isEmpty() || ed_min_ini.getText().toString().isEmpty() ) {
                    msg = getResources().getString(R.string.str_correct) + " " + getResources().getString(R.string.str_correct_start_time);
                    error = true;
                } else if (ed_hora_fim.getText().toString().isEmpty() || ed_min_fim.getText().toString().isEmpty()) {
                    msg = getResources().getString(R.string.str_correct) + " " + getResources().getString(R.string.str_correct_end_time);
                    error = true;
                } else if (ed_medico.getText().toString().isEmpty()) {
                    msg = getResources().getString(R.string.str_correct) + " " + getResources().getString(R.string.str_correct_medico);
                    error = true;
                } else if (ed_paciente.getText().toString().isEmpty()) {
                    msg = getResources().getString(R.string.str_correct) + " " + getResources().getString(R.string.str_correct_paciente);
                    error = true;
                } else if (ed_sala.getText().toString().isEmpty()) {
                    msg = getResources().getString(R.string.str_correct) + " " + getResources().getString(R.string.str_correct_sala);
                    error = true;
                }
                if (error == true) {
                    Toast.makeText(getApplicationContext(), msg + "!", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
                    return false;
                }
                int flag = 0;
                String dt = getIntent().getStringExtra("DATA");
                if (consulta.get_id() == 0) { // Inclusão.
                    flag = Connector.LoadAddConsulta(this, domain, dt, ed_hora_ini, ed_min_ini, ed_hora_fim, ed_min_fim, ed_medico, ed_paciente, ed_sala, 0);
                } else {                    // Alteração.
                    flag = Connector.LoadUpdateConsulta(this, domain, consulta, dt, ed_hora_ini, ed_min_ini, ed_hora_fim, ed_min_fim, ed_medico, ed_paciente, ed_sala);
                }
                database.Close();
                switch (flag) {
                    case -1:
                        msg = getResources().getString(R.string.str_err_sql_query) + " " + getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
                        Output.Alert(this, getResources().getString(R.string.str_fail), msg);
                        break;
                    case 0:
                        finish(); // Nenhum campo alterado!
                        break;
                    case 1:
                        Salvar();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_err_time), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:   // Médico!
                        s = getResources().getString(R.string.str_medico);
                        s = s.substring(0, s.length() -1).toLowerCase();
                        msg = "O " + s + " " + ed_medico.getText().toString() + " " + getResources().getString(R.string.str_in_use) + "o " + getResources().getString(R.string.str_in_time);
                        Output.Info(this, getResources().getString(R.string.str_fail), msg);
                        break;
                    case 4:   // Paciente!
                        s = getResources().getString(R.string.str_paciente);
                        s = s.substring(0, s.length() -1).toLowerCase();
                        msg = "O " + s + " " + ed_paciente.getText().toString() + " " + getResources().getString(R.string.str_in_use) + "o " + getResources().getString(R.string.str_in_time);
                        Output.Info(this, getResources().getString(R.string.str_fail), msg);
                        break;
                    case 5:   // Sala!
                        s = getResources().getString(R.string.str_sala);
                        s = s.substring(0, s.length() -1).toLowerCase();
                        msg = "A " + s + " " + ed_sala.getText().toString() + " " + getResources().getString(R.string.str_in_use) + "a " + getResources().getString(R.string.str_in_time);
                        Output.Info(this, getResources().getString(R.string.str_fail), msg);
                        break;
                }
                break;
            case R.id.m_excluir:
                Excluir();
                break;
            case R.id.m_limpar:
                if (getIntent().hasExtra("OLD")) {
                    getIntent().putExtra("DATA", getIntent().getStringExtra("OLD"));
                    String str = getIntent().getStringExtra("DATA");
                    Date date = Dates.StringToDate(str, "en", "US", false);
                    tv_data.setText(Dates.DateToString(date, DateFormat.FULL));
                }
                if (consulta.get_id() == 0) {
                    ed_convenio.setText("");
                    ed_especialidade.setText("");
                    ed_medico.setText("");
                    ed_paciente.setText("");
                    ed_sala.setText("");
                    ed_hora_ini.setText("");
                    ed_hora_fim.setText("");
                    ed_min_ini.setText("");
                    ed_min_fim.setText("");
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        Intent it = null;
        int request = 0;
        Bundle bundle = new Bundle();
        String src = "";
        switch (v.getId()) {
            case R.id.button_procurar_medico_consulta:
                request = FIND_MEDICO;
                it = new Intent(this, ConsultaMedicoActivity.class);
                bundle.putString("ESPECIALIDADE", ed_especialidade.getText().toString());
                bundle.putString("CONVENIO", ed_convenio.getText().toString());
                it.putExtras(bundle);
                break;
            case R.id.button_procurar_especialidade_consulta:
                request = FIND_ESPECIALIDADE;
                it = new Intent(this, ConsultaEspecialidadeActivity.class);
                break;
            case R.id.button_procurar_paciente_consulta:
                request = FIND_PACIENTE;
                it = new Intent(this, ConsultaPacienteActivity.class);
                bundle.putString("CONVENIO", ed_convenio.getText().toString());
                it.putExtras(bundle);
                break;
            case R.id.button_procurar_convenio_consulta:
                request = FIND_CONVENIO;
                it = new Intent(this, ConsultaConvenioActivity.class);
                break;
            case R.id.button_procurar_sala_consulta:
                request = FIND_SALA;
                it = new Intent(this, ConsultaSalaActivity.class);
                bundle.putString("INPUT", ed_sala.getText().toString());
                it.putExtras(bundle);
                break;
            case R.id.text_data:
                Date dt = Dates.StringToDate(getIntent().getStringExtra("DATA"), "en", "US", false);
                exibeData(dt);
                break;
            case R.id.text_medico_consulta:
                src = MedicoActivity.p_medico;
                break;
            case R.id.text_paciente_consulta:
                src = PacienteActivity.p_paciente;
                break;
        }
        if (!src.isEmpty()) {
            if (ed_hora_ini.getText().toString().isEmpty() || ed_min_ini.getText().toString().isEmpty() ||
                ed_hora_fim.getText().toString().isEmpty() || ed_min_fim.getText().toString().isEmpty() ||
                ed_medico.getText().toString().isEmpty() || ed_paciente.getText().toString().isEmpty()) {
                return;
            }
            int perm = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if (perm == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
                return;
            }
            String date = tv_data.getText().toString();
            String time = Utility.ZeroFill(ed_hora_ini.getText().toString()) + ":" + Utility.ZeroFill(ed_min_ini.getText().toString()) + " " + getResources().getString(R.string.str_to) + " " + Utility.ZeroFill(ed_hora_fim.getText().toString()) + ":" + Utility.ZeroFill(ed_min_fim.getText().toString());
            String paciente = ed_paciente.getText().toString();
            String medico   = ed_medico.getText().toString();
            boolean flag;
            InputTelefone input = new InputTelefone(this, paciente, medico, src, date, time);
            flag = getIntent().getBooleanExtra("SMS_OK", false);
            if (flag == true) {
                dismiss = SEND_SMS;
                input.setInputTelefone();
                getIntent().removeExtra("SMS_OK");
                //Intent it = (Intent) getIntent().getExtras();
                /*
                if (it.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(it);
                }
                */
            }
        } else {
            if (it != null) {
                startActivityForResult(it, request, bundle);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String txt;
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case FIND_MEDICO:
                        txt = domain.getTextEspecialidade(this, data.getLongExtra("ESPECIALIDADE", 0));
                        ed_medico.setText(data.getStringExtra("MEDICO"));
                        ed_especialidade.setText(txt);
                        break;
                    case FIND_ESPECIALIDADE:
                        txt = data.getStringExtra("ESPECIALIDADE");
                        if (!ed_especialidade.getText().toString().equals(txt)){
                            ed_medico.setText("");
                            ed_especialidade.setText(txt);
                        }
                        break;
                    case FIND_PACIENTE:
                        long id_conv = data.getLongExtra("CONVENIO", 0);
                        txt = domain.getTextConvenio(this, id_conv);
                        ed_paciente.setText(data.getStringExtra("PACIENTE"));
                        if (!ed_convenio.getText().toString().equals(txt)) {
                            if (!domain.medicoHasConvenio(domain.getIdMedico(this, ed_medico.getText().toString()), id_conv)) {
                                ed_medico.setText("");
                                ed_especialidade.setText("");
                            }
                            ed_convenio.setText(txt);
                        }
                        break;
                    case FIND_CONVENIO:
                        txt = data.getStringExtra("CONVENIO");
                        if (!ed_convenio.getText().toString().equals(txt)){
                            ed_paciente.setText("");
                            if (!domain.medicoHasConvenio(domain.getIdMedico(this, ed_medico.getText().toString()), domain.getIdConvenio(this, txt))) {
                                ed_medico.setText("");
                                ed_especialidade.setText("");
                            }
                            ed_convenio.setText(txt);
                        }
                        break;
                    case FIND_SALA:
                        txt = data.getStringExtra("SALA");
                        ed_sala.setText(txt);
                        break;
                }
            }
        }
        database.Close();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            EditText et = (EditText) v;
            et.setText(Utility.ZeroFill(et.getText().toString()));
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (dismiss == DELETE_CONSULTA) {
            if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
                return;
            }
            String[] v = {""};
            boolean flag = Connector.DeleteConsulta(this, domain, consulta, v);
            String str = v[0];
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
        } else if (dismiss == SEND_SMS) {

        }
        dismiss = 0;
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
