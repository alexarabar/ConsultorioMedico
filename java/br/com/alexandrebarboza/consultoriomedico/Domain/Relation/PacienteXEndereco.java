package br.com.alexandrebarboza.consultoriomedico.Domain.Relation;

import java.io.Serializable;

/**
 * Created by Alexandre on 11/03/2017.
 */

public class PacienteXEndereco implements Serializable {
    public static final String TABELA  = "PacienteXEndereco";
    public static final String _PACIENTE = "_paciente";
    public static final String _ENDERECO = "_endereco";

    private long   _paciente;
    private long   _endereco;

    public PacienteXEndereco() {
        this._paciente = 0;
        this._endereco = 0;
    }

    public long get_paciente() {
        return _paciente;
    }

    public void set_paciente(long _paciente) {
        this._paciente = _paciente;
    }

    public long get_endereco() {
        return _endereco;
    }

    public void set_endereco(long _endereco) {
        this._endereco = _endereco;
    }
}
