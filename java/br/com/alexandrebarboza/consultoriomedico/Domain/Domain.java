package br.com.alexandrebarboza.consultoriomedico.Domain;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ArrayAdapter;

import java.sql.Time;

import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Consulta;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Convenio;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Email;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Endereco;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Especialidade;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Medico;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Paciente;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Sala;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Telefone;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.MedicoXConvenio;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.MedicoXEmail;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.MedicoXEndereco;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.MedicoXTelefone;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.PacienteXEmail;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.PacienteXEndereco;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.PacienteXTelefone;
import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.ConsultaAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.ConsultaConvenioAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.ConsultaEspecialidadeAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.ConsultaMedicoAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.ConsultaPacienteAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.ConsultaSalaAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.EspecialidadeAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.MedicoAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.MedicoConvenioAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.PacienteAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.PacienteConvenioAdapter;
import br.com.alexandrebarboza.consultoriomedico.Utility.Connector;
import br.com.alexandrebarboza.consultoriomedico.Utility.Dates;
import br.com.alexandrebarboza.consultoriomedico.Utility.Utility;

/**
 * Created by Alexandre on 19/02/2017.
 */

public class Domain {
    private static Domain instance = null;
    private SQLiteDatabase connection = null;
    private String error;

    private Domain() { // Singleton
    }

    private ContentValues putConsulta(Consulta consulta) {
        ContentValues values = new ContentValues();
        values.put(Consulta.DATA, consulta.getData().toString());
        values.put(Consulta.INICIO, consulta.getInicio().toString());
        values.put(Consulta.FIM, consulta.getFim().toString());
        values.put(Consulta._MEDICO, consulta.get_medico());
        values.put(Consulta._PACIENTE, consulta.get_paciente());
        values.put(Consulta._SALA, consulta.get_sala());
        return values;
    }

    private ContentValues putMedico(Medico medico) {
        ContentValues values = new ContentValues();
        values.put(Medico.CRM, medico.getCrm());
        values.put(Medico.UF, medico.getUf());
        values.put(Medico.NOME, medico.getNome());
        values.put(Medico.ESPECIALIDADE, medico.getEspecialidade());
        return values;
    }

    private ContentValues putPaciente(Paciente paciente) {
        ContentValues values = new ContentValues();
        values.put(Paciente.CPF, paciente.getCpf());
        values.put(Paciente.NOME, paciente.getNome());
        values.put(Paciente.CONVENIO, paciente.getConvenio());
        return values;
    }

    private ContentValues putTelefone(Telefone telefone) {
        ContentValues values = new ContentValues();
        values.put(Telefone.NUMERO, telefone.getNumero());
        return values;
    }

    private ContentValues putEmail(Email email) {
        ContentValues values = new ContentValues();
        values.put(Email.ENDERECO, email.getEndereco());
        return values;
    }

    private ContentValues putEndereco(Endereco endereco) {
        ContentValues values = new ContentValues();
        values.put(Endereco.LOGRADOURO, endereco.getLogradouro());
        values.put(Endereco.NUMERO, endereco.getNumero());
        values.put(Endereco.COMPLEMENTO, endereco.getComplemento());
        values.put(Endereco.BAIRRO, endereco.getBairro());
        values.put(Endereco.CIDADE, endereco.getCidade());
        values.put(Endereco.UF, endereco.getUf());
        values.put(Endereco.CEP, endereco.getCep());
        return values;
    }

    private ContentValues putConvenio(Convenio convenio) {
        ContentValues values = new ContentValues();
        values.put(Convenio.NOME, convenio.getNome());
        return values;
    }

    private ContentValues putSala(Sala sala) {
        ContentValues values = new ContentValues();
        values.put(Sala.DESCRICAO, sala.getDescricao());
        return values;
    }

    private ContentValues putMedicoXTelefone(MedicoXTelefone medico_x_telefone) {
        ContentValues values = new ContentValues();
        values.put(MedicoXTelefone._MEDICO, medico_x_telefone.get_medico());
        values.put(MedicoXTelefone._TELEFONE, medico_x_telefone.get_telefone());
        return values;
    }

    private ContentValues putMedicoXEmail(MedicoXEmail medico_x_email) {
        ContentValues values = new ContentValues();
        values.put(MedicoXEmail._MEDICO, medico_x_email.get_medico());
        values.put(MedicoXEmail._EMAIL, medico_x_email.get_email());
        return values;
    }

    private ContentValues putMedicoXEndereco(MedicoXEndereco medico_x_endereco) {
        ContentValues values = new ContentValues();
        values.put(MedicoXEndereco._MEDICO, medico_x_endereco.get_medico());
        values.put(MedicoXEndereco._ENDERECO, medico_x_endereco.get_endereco());
        return values;
    }

    private ContentValues putMedicoXConvenio(MedicoXConvenio medico_x_convenio) {
        ContentValues values = new ContentValues();
        values.put(MedicoXConvenio._MEDICO, medico_x_convenio.get_medico());
        values.put(MedicoXConvenio._CONVENIO, medico_x_convenio.get_convenio());
        return values;
    }

    private ContentValues putMedicoEspecialidade(Especialidade especialidade) {
        ContentValues values = new ContentValues();
        values.put(especialidade.TITULO, especialidade.getTitulo());
        return values;
    }

    private ContentValues putPacienteXTelefone (PacienteXTelefone paciente_x_telefone) {
        ContentValues values = new ContentValues();
        values.put(PacienteXTelefone._PACIENTE, paciente_x_telefone.get_paciente());
        values.put(PacienteXTelefone._TELEFONE, paciente_x_telefone.get_telefone());
        return values;
    }

    private ContentValues putPacienteXEmail (PacienteXEmail paciente_x_email) {
        ContentValues values = new ContentValues();
        values.put(PacienteXEmail._PACIENTE, paciente_x_email.get_paciente());
        values.put(PacienteXEmail._EMAIL,    paciente_x_email.get_email());
        return values;
    }

    private ContentValues putPacienteXEndereco (PacienteXEndereco paciente_x_endereco) {
        ContentValues values = new ContentValues();
        values.put(PacienteXEndereco._PACIENTE, paciente_x_endereco.get_paciente());
        values.put(PacienteXEndereco._ENDERECO, paciente_x_endereco.get_endereco());
        return values;
    }

    private long insertRecord(String tabela, ContentValues values) {
        long result = -1;
        try {
            result = connection.insertOrThrow(tabela, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }

        System.out.println("Resultado da Inclusão na tabela: " + tabela + " foi: " + result);

        return result;
    }

    private int updateRecord(String tabela, ContentValues values, long id) {
        int result = -1;
        try {
            result = connection.update(tabela, values, "_id = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }

        System.out.println("Resultado da Atualização na tabela: " + tabela + " foi: " + result);

        return result;
    }

    private int deleteRecord(String tabela, long id) {
        int result = -1;
        try {
            result = connection.delete(tabela, "_id = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }

        System.out.println("Resultado da Exclusão na tabela: " + tabela + " foi: " + result);

        return result;
    }

    private int deleteRecord(String tabela, String key1, String key2, long value1, long value2) {
        String where = key1 + " = '" + String.valueOf(value1) + "' ";
        where += "AND " + key2 + " = '" + String.valueOf(value2) + "'";
        int result = -1;
        try {
            result = connection.delete(tabela, where, null);
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }

        System.out.println("Resultado da Exclusão na tabela: " + tabela + " foi: " + result);

        return result;
    }

    private int deleteRecord(String tabela, long id, String key) {
        int result = -1;
        try {
            result = connection.delete(tabela, key + " = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }

        System.out.println("Resultado da Exclusão na tabela: " + tabela + " foi: " + result);

        return result;
    }

    private Cursor getRecords(String tabela, String columns[], String where, String[] args, String order) {
        try {
            Cursor c = connection.query(true, tabela, columns, where, args, null, null, order, null);
            return c;
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
            return null;
        }
    }

    private int getRecordFound(String tabela, String columns[], String where) {
        try {
            Cursor c = connection.query(tabela, columns, where, null, null, null, null);
            if (c.getCount() > 0)
                return 1;
            else
                return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
            return -1;
        }
    }

    private long getRecordId(String tabela, String columns[], String where) {
        try {
            long res = 0;
            Cursor c = connection.query(tabela, columns, where, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                res = c.getLong(0);
            }
            c.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
            return -1;
        }
    }

    private String[] getConvenioID(String[] str) {
        Cursor cursor = connection.query(MedicoXConvenio.TABELA, null, "_convenio = ?", str, null, null, null);
        long   medico_id;
        String[] result = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = new String[cursor.getCount()];
            int i = 0;
            do {
                medico_id = cursor.getLong(cursor.getColumnIndex(MedicoXConvenio._MEDICO));
                result[i] = String.valueOf(medico_id);
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    private String makePlaceholders(int len) {
        if (len < 1) {
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    private Endereco getEndereco(Cursor cursor, boolean flag) {
        int i = 0;
        Endereco endereco = new Endereco();
        if (flag == true) {
            endereco.set_id(cursor.getLong(i++));
        }
        endereco.setLogradouro(cursor.getString(i++));
        endereco.setNumero(cursor.getInt(i++));
        endereco.setComplemento(cursor.getString(i++));
        endereco.setBairro(cursor.getString(i++));
        endereco.setCidade(cursor.getString(i++));
        endereco.setUf(cursor.getString(i++));
        endereco.setCep(cursor.getInt(i++));
        return endereco;
    }

    private boolean setEnderecos(ArrayAdapter <Endereco> enderecos, ArrayAdapter adapter, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"_id", "logradouro", "numero", "complemento", "bairro", "cidade", "uf", "cep"};
        Cursor cursor = getRecords(Endereco.TABELA, col, where, null, "logradouro COLLATE NOCASE ASC, numero COLLATE NOCASE ASC, complemento COLLATE NOCASE ASC");   // BUG: ORDER BY
        if (cursor == null) {
            return false;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Endereco endereco = getEndereco(cursor, true);
                enderecos.add(endereco);
                adapter.add(Utility.enderecoReduce(endereco));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return true;
    }

    private void setEmails(ArrayAdapter <Email> emails, ArrayAdapter adapter, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"_id", "endereco"};
        Cursor cursor = getRecords(Email.TABELA, col, where, null, "endereco COLLATE NOCASE ASC");
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Email mail = new Email();
                    mail.set_id(cursor.getLong(0));
                    mail.setEndereco(cursor.getString(1));
                    emails.add(mail);
                    adapter.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private void setTelefones(ArrayAdapter <Telefone> telefones, ArrayAdapter adapter, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"_id", "numero"};
        Cursor cursor = getRecords(Telefone.TABELA, col, where, null, "numero COLLATE NOCASE ASC");
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Telefone tel = new Telefone();
                    tel.set_id(cursor.getLong(0));
                    tel.setNumero(cursor.getString(1));
                    telefones.add(tel);
                    adapter.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private void setArrayTelefone(String[] array, long id_telefone, int i) {
        String where = "_id = '" + id_telefone + "'";
        String col[] = {"_id", "numero"};
        Cursor cursor = getRecords(Telefone.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                array[i] = cursor.getString(1);
            }
            cursor.close();
        }
    }

    private void setArrayEmail(String[] array, long id_email, int i) {
        String where = "_id = '" + id_email + "'";
        String col[] = {"_id", "endereco"};
        Cursor cursor = getRecords(Email.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                array[i] = cursor.getString(1);
            }
            cursor.close();
        }
    }

    public static Domain getInstance() {
        if (instance == null) {
            instance = new Domain();
        }
        return instance;
    }

    public void setConnection(SQLiteDatabase connection) {
        this.connection = connection;
    }

    public String getError() {
        return error;
    }

    public long addConsulta(Consulta consulta) {
        ContentValues values = putConsulta(consulta);
        return insertRecord(Consulta.TABELA, values);
    }

    public long addMedico(Medico medico) {
        ContentValues values = putMedico(medico);
        return insertRecord(Medico.TABELA, values);
    }

    public long addPaciente(Paciente paciente) {
        ContentValues values = putPaciente(paciente);
        return insertRecord(Paciente.TABELA, values);
    }

    public long addTelefone(Telefone telefone) {
        ContentValues values = putTelefone(telefone);
        return insertRecord(Telefone.TABELA, values);
    }

    public long addEmail(Email email) {
        ContentValues values = putEmail(email);
        return insertRecord(Email.TABELA, values);
    }

    public long addEndereco(Endereco endereco) {
        ContentValues values = putEndereco(endereco);
        return insertRecord(Endereco.TABELA, values);
    }

    public long addConvenio(Convenio convenio) {
        ContentValues values = putConvenio(convenio);
        return insertRecord(Convenio.TABELA, values);
    }

    public long addMedicoXTelefone(MedicoXTelefone medico_x_telefone) {
        ContentValues values = putMedicoXTelefone(medico_x_telefone);
        return insertRecord(MedicoXTelefone.TABELA, values);
    }

    public long addMedicoXEmail(MedicoXEmail medico_x_email) {
        ContentValues values = putMedicoXEmail(medico_x_email);
        return insertRecord(MedicoXEmail.TABELA, values);
    }

    public long addMedicoXEndereco(MedicoXEndereco medico_x_endereco) {
        ContentValues values = putMedicoXEndereco(medico_x_endereco);
        return insertRecord(MedicoXEndereco.TABELA, values);
    }

    public long addMedicoXConvenio(MedicoXConvenio medico_x_convenio) {
        ContentValues values = putMedicoXConvenio(medico_x_convenio);
        return insertRecord(MedicoXConvenio.TABELA, values);
    }

    public long addMedicoEspecialidade(Especialidade especialidade) {
        ContentValues values = putMedicoEspecialidade(especialidade);
        return insertRecord(Especialidade.TABELA, values);
    }

    public long addPacienteXTelefone(PacienteXTelefone paciente_x_telefone) {
        ContentValues values = putPacienteXTelefone(paciente_x_telefone);
        return insertRecord(PacienteXTelefone.TABELA, values);
    }

    public long addPacienteXEmail(PacienteXEmail paciente_x_email) {
        ContentValues values = putPacienteXEmail(paciente_x_email);
        return insertRecord(PacienteXEmail.TABELA, values);
    }

    public long addPacienteXEndereco(PacienteXEndereco paciente_x_endereco) {
        ContentValues values = putPacienteXEndereco(paciente_x_endereco);
        return insertRecord(PacienteXEndereco.TABELA, values);
    }

    public long addSala(Sala sala) {
        ContentValues values = putSala(sala);
        return insertRecord(Sala.TABELA, values);
    }

    public int updateMedico(Medico medico) {
        ContentValues values = putMedico(medico);
        return updateRecord(Medico.TABELA, values, medico.get_id());
    }

    public int updatePaciente(Paciente paciente) {
        ContentValues values = putPaciente(paciente);
        return updateRecord(Paciente.TABELA, values, paciente.get_id());
    }

    public int updateConsulta(Consulta consulta) {
        ContentValues values = putConsulta(consulta);
        return updateRecord(Consulta.TABELA, values, consulta.get_id());
    }

    public int updateEndereco(Endereco endereco) {
        ContentValues values = putEndereco(endereco);
        return updateRecord(Endereco.TABELA, values, endereco.get_id());
    }

    public int updateMedicoEspecialidade(Especialidade especialidade) {
        ContentValues values = putMedicoEspecialidade(especialidade);
        return updateRecord(Especialidade.TABELA, values, especialidade.get_id());
    }

    public int updateConvenio(Convenio convenio) {
        ContentValues values = putConvenio(convenio);
        return updateRecord(Convenio.TABELA, values, convenio.get_id());
    }

    public int updateSala(Sala sala) {
        ContentValues values = putSala(sala);
        return updateRecord(Sala.TABELA, values, sala.get_id());
    }

    public int deleteMedico(long id) {
        return deleteRecord(Medico.TABELA, id);
    }

    public int deletePaciente(long id) {
        return deleteRecord(Paciente.TABELA, id);
    }

    public int deleteConsulta(long id) {
        return deleteRecord(Consulta.TABELA, id);
    }

    public int deleteTelefone(long id) {
        return deleteRecord(Telefone.TABELA, id);
    }

    public int deleteEmail(long id) {
        return deleteRecord(Email.TABELA, id);
    }

    public int deleteEndereco(long id) {
        return deleteRecord(Endereco.TABELA, id);
    }

    public int deleteConvenio(long id) {
        return deleteRecord(Convenio.TABELA, id);
    }

    public int deleteEspecialidade(long id) {
        return deleteRecord(Especialidade.TABELA, id);
    }

    public int deleteSala(long id) {
        return deleteRecord(Sala.TABELA, id);
    }

    public int deleteMedicoXTelefone(long id, boolean key) {
        String s;
        if (!key) {
           s = "_medico";
        } else {
           s = "_telefone";
        }
        return deleteRecord(MedicoXTelefone.TABELA, id, s);
    }

    public int deletePacienteXTelefone(long id, boolean key) {
        String s;
        if (!key) {
            s = "_paciente";
        } else {
            s = "_telefone";
        }
        return deleteRecord(PacienteXTelefone.TABELA, id, s);
    }

    public int deleteMedicoXEmail(long id, boolean key) {
        String s;
        if (!key) {
            s = "_medico";
        } else {
            s = "_email";
        }
        return deleteRecord(MedicoXEmail.TABELA, id, s);
    }

    public int deletePacienteXEmail(long id, boolean key) {
        String s;
        if (!key) {
            s = "_paciente";
        } else {
            s = "_email";
        }
        return deleteRecord(PacienteXEmail.TABELA, id, s);
    }

    public int deleteMedicoXEndereco(long id, boolean key) {
        String s;
        if (!key) {
            s = "_medico";
        } else {
            s = "_endereco";
        }
        return deleteRecord(MedicoXEndereco.TABELA, id, s);
    }

    public int deletePacienteXEndereco(long id, boolean key) {
        String s;
        if (!key) {
            s = "_paciente";
        } else {
            s = "_endereco";
        }
        return deleteRecord(PacienteXEndereco.TABELA, id, s);
    }

    public int deleteMedicoXConvenio(long id, boolean key) {
        String s;
        if (!key) {
            s = "_medico";
        } else {
            s = "_convenio";
        }
        return deleteRecord(MedicoXConvenio.TABELA, id, s);
    }

    public int deleteMedicoXConvenio(long medico_id, long convenio_id) {
        return deleteRecord(MedicoXConvenio.TABELA, "_medico", "_convenio", medico_id, convenio_id);
    }

    public int checkUniqueKeyForMedico(String crm, String uf) {
        String where = "crm = '" + crm + "' and uf = '" + uf + "'";
        String id[] = {"_id"};
        return getRecordFound(Medico.TABELA, id, where);
    }

    public int checkUniqueKeyForPaciente(String cpf) {
        String where = "cpf = '" + cpf + "'";
        String id[] = {"_id"};
        return getRecordFound(Paciente.TABELA, id, where);
    }

    public int checkUniqueKeyForTelefone(String numero) {
        String where = "numero = '" + numero + "'";
        String id[] = {"_id"};
        return getRecordFound(Telefone.TABELA, id, where);
    }

    public int checkUniqueKeyForEmail(String endereco) {
        String where = "endereco = '" + endereco + "'";
        String id[] = {"_id"};
        return getRecordFound(Email.TABELA, id, where);
    }

    public int checkUniqueKeyForMedicoXConvenio(long medico_id, long convenio_id) {
        String where = "_medico = '" + medico_id + "' AND _convenio = '" + convenio_id + "'";
        String id[] = {"_medico", "_convenio"};
        return getRecordFound(MedicoXConvenio.TABELA, id, where);
    }

    public int checkAllFieldsForEndereco(Endereco endereco) {
        String where = "logradouro = '" + endereco.getLogradouro() + "' AND ";
        where += "numero = '" + endereco.getNumero() + "' AND ";
        where += "complemento = '" + endereco.getComplemento() + "' AND ";
        where += "bairro = '" + endereco.getBairro() + "' AND ";
        where += "cidade = '" + endereco.getCidade() + "' AND ";
        where += "uf = '" + endereco.getUf() + "' AND ";
        where += "cep = '" + endereco.getCep() + "'";
        String _id[] = {"_id"};
        long result = getRecordId(Endereco.TABELA, _id, where);
        if (result > 0 && endereco.get_id() == result) {
            return 0;
        } else {
            return (int) result;
        }
    }

    public ConsultaAdapter findConsultas(Context context) {
        ConsultaAdapter adapter = new ConsultaAdapter(context, R.layout.list_consultas);
        Cursor cursor = connection.query(Consulta.TABELA, null, null, null, null, null, "data COLLATE NOCASE ASC, inicio COLLATE NOCASE ASC, fim COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Consulta consulta = new Consulta();
                consulta.set_id(cursor.getLong(cursor.getColumnIndex(Consulta._ID)));
                consulta.set_paciente(cursor.getLong(cursor.getColumnIndex(Consulta._PACIENTE)));
                consulta.set_medico(cursor.getLong(cursor.getColumnIndex(Consulta._MEDICO)));
                consulta.set_sala(cursor.getLong(cursor.getColumnIndex(Consulta._SALA)));
                String dt = cursor.getString(cursor.getColumnIndex(Consulta.DATA));
                String t1 = cursor.getString(cursor.getColumnIndex(Consulta.INICIO));
                String t2 = cursor.getString(cursor.getColumnIndex(Consulta.FIM));
                consulta.setData(Dates.getSQLDate(dt, false));
                consulta.setInicio(Time.valueOf(t1));
                consulta.setFim(Time.valueOf(t2));
                adapter.add(consulta);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public MedicoAdapter findMedicos(Context context) {
        MedicoAdapter adapter = new MedicoAdapter(context, R.layout.list_medicos);
        Cursor cursor = connection.query(Medico.TABELA, null, null, null, null, null, "nome COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Medico medico = new Medico();
                medico.set_id(cursor.getLong(cursor.getColumnIndex(Medico._ID)));
                medico.setCrm(cursor.getString(cursor.getColumnIndex(Medico.CRM)));
                medico.setUf(cursor.getString(cursor.getColumnIndex(Medico.UF)));
                medico.setNome(cursor.getString(cursor.getColumnIndex(Medico.NOME)));
                medico.setEspecialidade(cursor.getLong(cursor.getColumnIndex(Medico.ESPECIALIDADE)));
                adapter.add(medico);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public boolean medicoHasConvenio(long id_med, long id_conv) {
        String[] tmp = new String[] {String.valueOf(id_med), String.valueOf(id_conv)};
        Cursor cursor = connection.query(MedicoXConvenio.TABELA, null, "_medico = ? AND _convenio = ?", tmp, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public ConsultaMedicoAdapter findMedicos(Context context, long id_esp, long id_conv) {
        ConsultaMedicoAdapter adapter = new ConsultaMedicoAdapter(context, R.layout.list_consulta_medicos);
        Cursor temp, cursor = null;
        String[] tmp, str = new String[]{""};
        String order = "nome COLLATE NOCASE ASC";
        long medico_id;
        if (id_esp == 0 && id_conv == 0) {        // passar nenhuma.
            cursor = connection.query(Medico.TABELA, null, null, null, null, null, order);
        } else if (id_conv == 0) {                // passar só id_esp.
            str[0] = String.valueOf(id_esp);
            cursor = connection.query(Medico.TABELA, null, "especialidade = ?", str, null, null, order);
        } else if (id_esp == 0) {                // passar só id_conv.
            str[0] = String.valueOf(id_conv);
            tmp = getConvenioID(str);
            if (tmp != null) {
                cursor = connection.query(Medico.TABELA, null, "_id IN (" + makePlaceholders(tmp.length) + ")", tmp, null, null, order);
            }
        } else {                                  // passar as duas.
            str[0] = String.valueOf(id_conv);
            tmp = getConvenioID(str);
            if (tmp != null) {
                cursor = connection.query(Medico.TABELA, null, "especialidade = '" + String.valueOf(id_esp) + "' AND _id IN (" + makePlaceholders(tmp.length) + ")", tmp, null, null, order);
            }
        }
        if (cursor != null) {
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Medico medico = new Medico();
                    medico.set_id(cursor.getLong(cursor.getColumnIndex(Medico._ID)));
                    medico.setCrm(cursor.getString(cursor.getColumnIndex(Medico.CRM)));
                    medico.setUf(cursor.getString(cursor.getColumnIndex(Medico.UF)));
                    medico.setNome(cursor.getString(cursor.getColumnIndex(Medico.NOME)));
                    medico.setEspecialidade(cursor.getLong(cursor.getColumnIndex(Medico.ESPECIALIDADE)));
                    adapter.add(medico);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        adapter.setConvenio(id_conv);
        return adapter;
    }

    public PacienteAdapter findPacientes(Context context) {
        PacienteAdapter adapter = new PacienteAdapter(context, R.layout.list_pacientes);
        Cursor cursor = connection.query(Paciente.TABELA, null, null, null, null, null, "nome COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Paciente paciente = new Paciente();
                paciente.set_id(cursor.getLong(cursor.getColumnIndex(Paciente._ID)));
                paciente.setCpf(cursor.getString(cursor.getColumnIndex(Paciente.CPF)));
                paciente.setNome(cursor.getString(cursor.getColumnIndex(Paciente.NOME)));
                paciente.setConvenio(cursor.getLong(cursor.getColumnIndex(Paciente.CONVENIO)));
                adapter.add(paciente);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public ConsultaPacienteAdapter findPacientes(Context context, long id_conv) {
        ConsultaPacienteAdapter adapter = new ConsultaPacienteAdapter(context, R.layout.list_consulta_pacientes);
        String[] tmp = {""};
        String where;
        if (id_conv == 0 ) {
            tmp   = null;
            where = null;
        } else {
            tmp[0] = String.valueOf(id_conv);
            where = "convenio = ?";
        }
        Cursor cursor = connection.query(Paciente.TABELA, null, where, tmp, null, null, "nome COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Paciente paciente = new Paciente();
                paciente.set_id(cursor.getLong(cursor.getColumnIndex(Paciente._ID)));
                paciente.setCpf(cursor.getString(cursor.getColumnIndex(Paciente.CPF)));
                paciente.setNome(cursor.getString(cursor.getColumnIndex(Paciente.NOME)));
                paciente.setConvenio(cursor.getLong(cursor.getColumnIndex(Paciente.CONVENIO)));
                adapter.add(paciente);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public EspecialidadeAdapter findEspecialidades(Context context) {
        EspecialidadeAdapter adapter = new EspecialidadeAdapter(context, R.layout.list_especialidades);
        Cursor cursor = connection.query(Especialidade.TABELA, null, null, null, null, null, "titulo COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Especialidade especialidade = new Especialidade();
                especialidade.set_id(cursor.getLong(cursor.getColumnIndex(Especialidade._ID)));
                especialidade.setTitulo(cursor.getString(cursor.getColumnIndex(Especialidade.TITULO)));
                adapter.add(especialidade);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ConsultaSalaAdapter findSalas(Context context) {
        ConsultaSalaAdapter adapter = new ConsultaSalaAdapter(context, R.layout.list_salas);
        Cursor cursor = connection.query(Sala.TABELA, null, null, null, null, null, "descricao COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Sala sala = new Sala();
                sala.set_id(cursor.getLong(cursor.getColumnIndex(Sala._ID)));
                sala.setDescricao(cursor.getString(cursor.getColumnIndex(Sala.DESCRICAO)));
                adapter.add(sala);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public ConsultaEspecialidadeAdapter findConsultaEspecialidades(Context context) {
        ConsultaEspecialidadeAdapter adapter = new ConsultaEspecialidadeAdapter(context, R.layout.list_consulta_especialidades);
        Cursor cursor = connection.query(Especialidade.TABELA, null, null, null, null, null, "titulo COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Especialidade especialidade = new Especialidade();
                especialidade.set_id(cursor.getLong(cursor.getColumnIndex(Especialidade._ID)));
                especialidade.setTitulo(cursor.getString(cursor.getColumnIndex(Especialidade.TITULO)));
                adapter.add(especialidade);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public MedicoConvenioAdapter findMedicoConvenios(Context context) {
        MedicoConvenioAdapter adapter = new MedicoConvenioAdapter(context, R.layout.list_medico_convenios);
        Cursor cursor = connection.query(Convenio.TABELA, null, null, null, null, null, "nome COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Convenio convenio = new Convenio();
                convenio.set_id(cursor.getLong(cursor.getColumnIndex(Convenio._ID)));
                convenio.setNome(cursor.getString(cursor.getColumnIndex(Convenio.NOME)));
                adapter.add(convenio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public ConsultaConvenioAdapter findConsultaConvenios(Context context) {
        ConsultaConvenioAdapter adapter = new ConsultaConvenioAdapter(context, R.layout.list_consulta_convenios);
        Cursor cursor = connection.query(Convenio.TABELA, null, null, null, null, null, "nome COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Convenio convenio = new Convenio();
                convenio.set_id(cursor.getLong(cursor.getColumnIndex(Convenio._ID)));
                convenio.setNome(cursor.getString(cursor.getColumnIndex(Convenio.NOME)));
                adapter.add(convenio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public PacienteConvenioAdapter findPacienteConvenios(Context context) {
        PacienteConvenioAdapter adapter = new PacienteConvenioAdapter(context, R.layout.list_paciente_convenios);
        Cursor cursor = connection.query(Convenio.TABELA, null, null, null, null, null, "nome COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Convenio convenio = new Convenio();
                convenio.set_id(cursor.getLong(cursor.getColumnIndex(Convenio._ID)));
                convenio.setNome(cursor.getString(cursor.getColumnIndex(Convenio.NOME)));
                adapter.add(convenio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public boolean setMedicoTelefones(ArrayAdapter <Telefone> telefones, ArrayAdapter <MedicoXTelefone> medico_x_telefone, ArrayAdapter adapter, long id) {
        String where = "_medico = '" + id + "'";
        String col[] = {"_medico", "_telefone"};
        Cursor cursor = getRecords(MedicoXTelefone.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    long id2 = cursor.getLong(1);
                    MedicoXTelefone m_x_t = new MedicoXTelefone();
                    m_x_t.set_medico(cursor.getLong(0));
                    m_x_t.set_telefone(id2);
                    medico_x_telefone.add(m_x_t);
                    setTelefones(telefones, adapter, id2);
                } while (cursor.moveToNext());
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public boolean setPacienteTelefones(ArrayAdapter <Telefone> telefones, ArrayAdapter <PacienteXTelefone> paciente_x_telefone, ArrayAdapter adapter, long id) {
        String where = "_paciente = '" + id + "'";
        String col[] = {"_paciente", "_telefone"};
        Cursor cursor = getRecords(PacienteXTelefone.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    long id2 = cursor.getLong(1);
                    PacienteXTelefone p_x_t = new PacienteXTelefone();
                    p_x_t.set_paciente(cursor.getLong(0));
                    p_x_t.set_telefone(id2);
                    paciente_x_telefone.add(p_x_t);
                    setTelefones(telefones, adapter, id2);
                } while (cursor.moveToNext());
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public boolean setMedicoEmails(ArrayAdapter <Email> emails, ArrayAdapter <MedicoXEmail> medico_x_email, ArrayAdapter adapter, long id) {
        String where = "_medico = '" + id + "'";
        String col[] = {"_medico", "_email"};
        Cursor cursor = getRecords(MedicoXEmail.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    long id2 = cursor.getLong(1);
                    MedicoXEmail m_x_e = new MedicoXEmail();
                    m_x_e.set_medico(cursor.getLong(0));
                    m_x_e.set_email(id2);
                    medico_x_email.add(m_x_e);
                    setEmails(emails, adapter, id2);
                } while (cursor.moveToNext());
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public boolean setPacienteEmails(ArrayAdapter <Email> emails, ArrayAdapter <PacienteXEmail> paciente_x_email, ArrayAdapter adapter, long id) {
        String where = "_paciente = '" + id + "'";
        String col[] = {"_paciente", "_email"};
        Cursor cursor = getRecords(PacienteXEmail.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    long id2 = cursor.getLong(1);
                    PacienteXEmail p_x_e = new PacienteXEmail();
                    p_x_e.set_paciente(cursor.getLong(0));
                    p_x_e.set_email(id2);
                    paciente_x_email.add(p_x_e);
                    setEmails(emails, adapter, id2);
                } while (cursor.moveToNext());
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public boolean setMedicoEnderecos(ArrayAdapter <Endereco> enderecos, ArrayAdapter <MedicoXEndereco> medico_x_endereco, ArrayAdapter adapter, long id) {
        String where = "_medico = '" + id + "'";
        String col[] = {"_medico", "_endereco"};
        Cursor cursor = getRecords(MedicoXEndereco.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    long id2 = cursor.getLong(1);
                    MedicoXEndereco m_x_e = new MedicoXEndereco();
                    m_x_e.set_medico(cursor.getLong(0));
                    m_x_e.set_endereco(id2);
                    medico_x_endereco.add(m_x_e);
                    if (setEnderecos(enderecos, adapter, id2) == false) {
                        cursor.close();
                        return false;
                    }
                } while (cursor.moveToNext());
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public boolean setPacienteEnderecos(ArrayAdapter <Endereco> enderecos, ArrayAdapter <PacienteXEndereco> paciente_x_endereco, ArrayAdapter adapter, long id) {
        String where = "_paciente = '" + id + "'";
        String col[] = {"_paciente", "_endereco"};
        Cursor cursor = getRecords(PacienteXEndereco.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    long id2 = cursor.getLong(1);
                    PacienteXEndereco p_x_e = new PacienteXEndereco();
                    p_x_e.set_paciente(cursor.getLong(0));
                    p_x_e.set_endereco(id2);
                    paciente_x_endereco.add(p_x_e);
                    if (setEnderecos(enderecos, adapter, id2) == false) {
                        cursor.close();
                        return false;
                    }
                } while (cursor.moveToNext());
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public boolean setConvenios(ArrayAdapter <Convenio> convenios, ArrayAdapter <MedicoXConvenio>  medico_x_convenio, ArrayAdapter adapter, long id) {
        String where1 = "_medico = '" + id + "'";
        String col1[] = {"_medico", "_convenio"};
        Cursor cursor1 = getRecords(MedicoXConvenio.TABELA, col1, where1, null, null);
        boolean flag = false;
        if (cursor1 != null) {
            if (cursor1.getCount() > 0) {
                cursor1.moveToFirst();
                do {
                    long id2 = cursor1.getLong(1);
                    MedicoXConvenio m_x_c = new MedicoXConvenio();
                    m_x_c.set_medico(cursor1.getLong(0));
                    m_x_c.set_convenio(id2);
                    medico_x_convenio.add(m_x_c);
                    String where2 = "_id = '" + id2 + "'";
                    String col2[] = {"_id", "nome"};
                    Cursor cursor2 = getRecords(Convenio.TABELA, col2, where2, null, "nome COLLATE NOCASE ASC");
                    if (cursor2 != null) {
                        if(cursor2.getCount() > 0) {
                            cursor2.moveToFirst();
                            do {
                                Convenio convenio = new Convenio();
                                convenio.set_id(cursor2.getLong(0));
                                convenio.setNome(cursor2.getString(1));
                                convenios.add(convenio);
                                adapter.add(cursor2.getString(1));
                            } while (cursor2.moveToNext());
                        }
                        cursor2.close();
                    }
                } while (cursor1.moveToNext());
                flag = true;
            }
            cursor1.close();
        }
        return flag;
    }

    public Endereco selectEndereco(long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"logradouro", "numero", "complemento", "bairro", "cidade", "uf", "cep"};
        Cursor cursor = getRecords(Endereco.TABELA, col, where, null, null);
        Endereco endereco = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                endereco = getEndereco(cursor, false);
            }
            cursor.close();
        }
        return endereco;
    }

    public ArrayAdapter <Endereco> selectEnderecos(Activity activity, long id, int src) {
        ArrayAdapter <Endereco> enderecos = new ArrayAdapter <Endereco> (activity, 0);
        String table = "", where1, col1[] = {"", ""};
        if (src == Connector.MEDICO) {
            col1[0] = "_medico";
            table = MedicoXEndereco.TABELA;
        } else if (src == Connector.PACIENTE) {
            col1[0] = "_paciente";
            table = PacienteXEndereco.TABELA;
        }
        where1 = col1[0] + " = '" + id + "'";
        col1[1] = "_endereco";
        Cursor cursor1 = getRecords(table, col1, where1, null, null);
        if (cursor1 != null) {
            if (cursor1.getCount() > 0) {
                cursor1.moveToFirst();
                do {
                    long id2 = cursor1.getLong(1);
                    String where2 = "_id = '" + id2 + "'";
                    String col2[] = {"_id", "logradouro", "numero", "complemento", "bairro", "cidade", "uf", "cep"};
                    Cursor cursor2 = getRecords(Endereco.TABELA, col2, where2, null, null);
                    if (cursor2 != null) {
                        if (cursor2.getCount() > 0) {
                            cursor2.moveToFirst();
                            do {
                                Endereco endereco = getEndereco(cursor2, true);
                                enderecos.add(endereco);
                            } while (cursor2.moveToNext());
                        }
                        cursor2.close();
                    }
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
        if (enderecos.getCount() > 0)
            return enderecos;
        return null;
    }

    public int selectDataTimeForConsulta(Activity activity, String dt, String t1, String t2, String medico, String paciente, String sala, long id) {
        int flag = 0;
        long id_pac = getIdPaciente(activity, paciente);
        long id_med = getIdMedico(activity, medico);
        long id_sal = getIdSala(activity, sala);
        String where = "data = '" + dt + "' AND (('" + t1 + "' >= inicio AND '" + t1 + "' <= fim) OR ('" + t2 + "' <= fim AND '" + t2 + "' >= inicio) OR ('" + t1 + "' <= inicio AND '" + t2 + "' >= fim))";
        String[] cols = {"_id", "_paciente", "_medico", "_sala", "data", "inicio", "fim"};
        Cursor cursor = getRecords(Consulta.TABELA, cols, where, null, null);
        if (cursor == null) {
            return -1;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                if (cursor.getLong(0) == id) {
                    continue;
                }
                if (cursor.getLong(3) == id_sal) {
                    flag = -4;
                } else if (cursor.getLong(2) == id_med) {
                    flag = -2;
                } else if (cursor.getLong(1) == id_pac) {
                    flag = -3;
                }
                if (flag < 0){
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return flag;
    }

    public long getIdMedico(Activity activity, String text) {
        String where = "nome = '" + text + "'";
        String col[] = {"_id"};
        long result = 0;
        Cursor cursor = getRecords(Medico.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getLong(0);
            }
            cursor.close();
        }
        return result;
    }

    public long getIdPaciente(Activity activity, String text) {
        String where = "nome = '" + text + "'";
        String col[] = {"_id"};
        long result = 0;
        Cursor cursor = getRecords(Paciente.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getLong(0);
            }
            cursor.close();
        }
        return result;
    }

    public long getIdSala(Activity activity, String text) {
        String where = "descricao = '" + text + "'";
        String col[] = {"_id"};
        long result = 0;
        Cursor cursor = getRecords(Sala.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getLong(0);
            }
            cursor.close();
        }
        return result;
    }

    public long getIdEspecialidade(Activity activity, String text) {
        String where = "titulo = '" + text + "'";
        String col[] = {"_id"};
        long result = 0;
        Cursor cursor = getRecords(Especialidade.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getLong(0);
            }
            cursor.close();
        }
        return result;
    }

    public String getTextMedico(Activity activity, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"nome"};
        String result = "";
        Cursor cursor = getRecords(Medico.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getString(0);
            }
            cursor.close();
        }
        return result;
    }

    public String getTextPaciente(Activity activity, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"nome"};
        String result = "";
        Cursor cursor = getRecords(Paciente.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getString(0);
            }
            cursor.close();
        }
        return result;
    }

    public String getTextEspecialidade(Activity activity, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"titulo"};
        String result = "";
        Cursor cursor = getRecords(Especialidade.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getString(0);
            }
            cursor.close();
        }
        return result;
    }

    public String getTextConvenio(Activity activity, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"nome"};
        String result = "";
        Cursor cursor = getRecords(Convenio.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getString(0);
            }
            cursor.close();
        }
        return result;
    }

    public String getTextSala(Activity activity, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"descricao"};
        String result = "";
        Cursor cursor = getRecords(Sala.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getString(0);
            }
            cursor.close();
        }
        return result;
    }

    public String getTextEspecialidadeMedico(Activity activity, long id_medico) {
        String result = "";
        String where = "_id = '" + id_medico + "'";
        String col[] = {"especialidade"};
        Cursor cursor = getRecords(Medico.TABELA, col, where, null, null);
        long id_esp = 0;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id_esp = cursor.getLong(0);
            }
            cursor.close();
        }
        if (id_esp > 0) {
            where = "_id = '" + id_esp + "'";
            col[0] = "titulo";
            cursor = getRecords(Especialidade.TABELA, col, where, null, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    result = cursor.getString(0);
                }
                cursor.close();
            }
        }
        return result;
    }

    public String getTextConvenioPaciente(Activity activity, long id_paciente) {
        String result = "";
        String where = "_id = '" + id_paciente + "'";
        String col[] = {"convenio"};
        Cursor cursor = getRecords(Paciente.TABELA, col, where, null, null);
        long id_con = 0;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id_con = cursor.getLong(0);
            }
            cursor.close();
        }
        if (id_con > 0) {
            where = "_id = '" + id_con + "'";
            col[0] = "nome";
            cursor = getRecords(Convenio.TABELA, col, where, null, null);
            if (cursor != null) {
                if(cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    result = cursor.getString(0);
                }
                cursor.close();
            }
        }
        return result;
    }

    public long getIdConvenio(Activity activity, String text) {
        String where = "nome = '" + text + "'";
        String col[] = {"_id"};
        long result = 0;
        Cursor cursor = getRecords(Convenio.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getLong(0);
            }
            cursor.close();
        }
        return result;
    }

    public int getCountMedicoXTelefones(long id) {
        int result = 0;
        String where = "_medico = '" + id + "'";
        String col[] = {"_medico", "_telefone"};
        Cursor cursor = getRecords(MedicoXTelefone.TABELA, col, where, null, null);
        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        return result;
    }

    public int getCountPacienteXTelefones(long id) {
        int result = 0;
        String where = "_paciente = '" + id + "'";
        String col[] = {"_paciente", "_telefone"};
        Cursor cursor = getRecords(PacienteXTelefone.TABELA, col, where, null, null);
        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        return result;
    }

    public int getCountMedicoXEmails(long id) {
        int result = 0;
        String where = "_medico = '" + id + "'";
        String col[] = {"_medico", "_email"};
        Cursor cursor = getRecords(MedicoXEmail.TABELA, col, where, null, null);
        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        return result;
    }

    public int getCountPacienteXEmails(long id) {
        int result = 0;
        String where = "_paciente = '" + id + "'";
        String col[] = {"_paciente", "_email"};
        Cursor cursor = getRecords(PacienteXEmail.TABELA, col, where, null, null);
        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        return result;
    }

    public void setArrayMedicoTelefones(String[] array, long id_medico) {
        String where = "_medico = '" + id_medico + "'";
        String col[] = {"_medico", "_telefone"};
        Cursor cursor = getRecords(MedicoXTelefone.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int i = 0;
                do {
                    long id_telefone = cursor.getLong(1);
                    setArrayTelefone(array, id_telefone, i);
                    i++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public void setArrayMedicoEmails(String[] array, long id_medico) {
        String where = "_medico = '" + id_medico + "'";
        String col[] = {"_medico", "_email"};
        Cursor cursor = getRecords(MedicoXEmail.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int i = 0;
                do {
                    long id_email = cursor.getLong(1);
                    setArrayEmail(array, id_email, i);
                    i++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public void setArrayPacienteTelefones(String[] array, long id_paciente) {
        String where = "_paciente = '" + id_paciente + "'";
        String col[] = {"_paciente", "_telefone"};
        Cursor cursor = getRecords(PacienteXTelefone.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int i = 0;
                do {
                    long id_telefone = cursor.getLong(1);
                    setArrayTelefone(array, id_telefone, i);
                    i++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public void setArrayPacienteEmails(String[] array, long id_paciente) {
        String where = "_paciente = '" + id_paciente + "'";
        String col[] = {"_paciente", "_email"};
        Cursor cursor = getRecords(PacienteXEmail.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int i = 0;
                do {
                    long id_email = cursor.getLong(1);
                    setArrayEmail(array, id_email, i);
                    i++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }
}
