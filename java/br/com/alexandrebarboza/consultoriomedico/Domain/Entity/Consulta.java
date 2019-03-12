package br.com.alexandrebarboza.consultoriomedico.Domain.Entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;

import br.com.alexandrebarboza.consultoriomedico.Utility.Dates;

/**
 * Created by Alexandre on 16/03/2017.
 */

public class Consulta implements Serializable {
    public static final String TABELA = "Consultas";
    public static final String _ID = "_id";
    public static final String _PACIENTE = "_paciente";
    public static final String _MEDICO   = "_medico";
    public static final String _SALA     = "_sala";
    public static final String DATA   = "data";
    public static final String INICIO = "inicio";
    public static final String FIM    = "fim";

    private long _id;
    private long _paciente;
    private long _medico;
    private long _sala;
    private Date data;
    private Time inicio;
    private Time fim;

    public Consulta() {
        this._id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_paciente() {
        return _paciente;
    }

    public void set_paciente(long _paciente) {
        this._paciente = _paciente;
    }

    public long get_medico() {
        return _medico;
    }

    public void set_medico(long _medico) {
        this._medico = _medico;
    }

    public long get_sala() {
        return _sala;
    }

    public void set_sala(long _sala) {
        this._sala = _sala;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Time getInicio() {
        return inicio;
    }

    public void setInicio(Time inicio) {
        this.inicio = inicio;
    }

    public Time getFim() {
        return fim;
    }

    public void setFim(Time fim) {
        this.fim = fim;
    }

    @Override
    public String toString() {
        if (data != null) {
            java.util.Date dt = Dates.convertFromDefaultDate(data);
            return Dates.DateToString(dt, DateFormat.LONG);
        }
        return null;
    }
}
