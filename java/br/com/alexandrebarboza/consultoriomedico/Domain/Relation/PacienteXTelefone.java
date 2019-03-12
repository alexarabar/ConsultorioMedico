package br.com.alexandrebarboza.consultoriomedico.Domain.Relation;

import java.io.Serializable;

/**
 * Created by Alexandre on 11/03/2017.
 */

public class PacienteXTelefone implements Serializable {
    public static final String TABELA  = "PacienteXTelefone";
    public static final String _PACIENTE = "_paciente";
    public static final String _TELEFONE = "_telefone";

    private long   _paciente;
    private long   _telefone;

    public PacienteXTelefone() {
        this._paciente = 0;
        this._telefone = 0;
    }

    public long get_paciente() {
        return _paciente;
    }

    public void set_paciente(long _paciente) {
        this._paciente = _paciente;
    }

    public long get_telefone() {
        return _telefone;
    }

    public void set_telefone(long _telefone) {

        this._telefone = _telefone;
    }

}
