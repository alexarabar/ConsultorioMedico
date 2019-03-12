package br.com.alexandrebarboza.consultoriomedico.Domain.Relation;

import java.io.Serializable;

/**
 * Created by Alexandre on 19/02/2017.
 */

public class MedicoXTelefone implements Serializable {
    public static final String TABELA    = "MedicoXTelefone";
    public static final String _MEDICO   = "_medico";
    public static final String _TELEFONE = "_telefone";

    private long   _medico;
    private long   _telefone;

    public MedicoXTelefone() {
        this._medico = 0;
        this._telefone = 0;
    }

    public long get_medico() {
        return _medico;
    }

    public void set_medico(long _medico) {
        this._medico = _medico;
    }

    public long get_telefone() {
        return _telefone;
    }

    public void set_telefone(long _telefone) {

        this._telefone = _telefone;
    }
}
