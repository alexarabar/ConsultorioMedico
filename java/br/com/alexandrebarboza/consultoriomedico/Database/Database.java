package br.com.alexandrebarboza.consultoriomedico.Database;

import br.com.alexandrebarboza.consultoriomedico.Database.Script.*;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alexandre on 11/02/2017.
 */

public class Database extends SQLiteOpenHelper {
    private static final String NOME_BANCO =  "Agenda";
    private static final int VERSAO =  9;
    private static Database instance = null;
    private SQLiteDatabase connection;
    private String error;

    private Database(Context context) { // Singleton
        super(context, NOME_BANCO, null, VERSAO);
        connection = null;
        error = "";
    }

    public static Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context);
        }
        return instance;
    }

    public SQLiteDatabase getConnection() {
        return connection;
    }

    public String getError(){
        return error;
    }

    public boolean setReadable() {
        try {
            connection = getReadableDatabase();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
            return false;
        }
    }

    public boolean setWritable() {
        try {
            connection = getWritableDatabase();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
            return false;
        }
    }

    public void Close() {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TableCreate.Telefones());
            db.execSQL(TableCreate.Emails());
            db.execSQL(TableCreate.Enderecos());
            db.execSQL(TableCreate.Convenios());
            db.execSQL(TableCreate.Especialidades());
            db.execSQL(TableCreate.Medicos());
            db.execSQL(TableCreate.Pacientes());
            db.execSQL(TableCreate.Salas());
            db.execSQL(TableCreate.Consultas());
            db.execSQL(TableCreate.MedicoXTelefone());
            db.execSQL(TableCreate.MedicoXEmail());
            db.execSQL(TableCreate.MedicoXEndereco());
            db.execSQL(TableCreate.MedicoXConvenio());
            db.execSQL(TableCreate.PacienteXTelefone());
            db.execSQL(TableCreate.PacienteXEmail());
            db.execSQL(TableCreate.PacienteXEndereco());
            db.execSQL(IndexCreate.id_crm_uf());
            db.execSQL(IndexCreate.id_cpf());
            db.execSQL(IndexCreate.id_especialidade());
            db.execSQL(IndexCreate.id_sala());
            db.execSQL(IndexCreate.id_medico_x_telefone());
            db.execSQL(IndexCreate.id_medico_x_email());
            db.execSQL(IndexCreate.id_medico_x_endereco());
            db.execSQL(IndexCreate.id_medico_x_convenio());
            db.execSQL(IndexCreate.id_paciente_x_telefone());
            db.execSQL(IndexCreate.id_paciente_x_email());
            db.execSQL(IndexCreate.id_paciente_x_endereco());
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(IndexDrop.id_paciente_x_telefone());
            db.execSQL(IndexDrop.id_paciente_x_endereco());
            db.execSQL(IndexDrop.id_paciente_x_email());
            db.execSQL(IndexDrop.id_medico_x_telefone());
            db.execSQL(IndexDrop.id_medico_x_endereco());
            db.execSQL(IndexDrop.id_medico_x_email());
            db.execSQL(IndexDrop.id_medico_x_convenio());
            db.execSQL(IndexDrop.id_crm_uf());
            db.execSQL(TableDrop.PacienteXTelefone());
            db.execSQL(TableDrop.PacienteXEndereco());
            db.execSQL(TableDrop.PacienteXEmail());
            db.execSQL(TableDrop.MedicoXTelefone());
            db.execSQL(TableDrop.MedicoXEndereco());
            db.execSQL(TableDrop.MedicoXEmail());
            db.execSQL(TableDrop.MedicoXConvenio());
            db.execSQL(TableDrop.Consultas());
            db.execSQL(TableDrop.Telefones());
            db.execSQL(TableDrop.Salas());
            db.execSQL(TableDrop.Pacientes());
            db.execSQL(TableDrop.Convenios());
            db.execSQL(TableDrop.Medicos());
            db.execSQL(TableDrop.Especialidades());
            db.execSQL(TableDrop.Enderecos());
            db.execSQL(TableDrop.Emails());
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }
    }
}