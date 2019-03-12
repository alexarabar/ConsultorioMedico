package br.com.alexandrebarboza.consultoriomedico.Domain.Relation;

import java.io.Serializable;

/**
 * Created by Alexandre on 25/02/2017.
 */

public class MedicoXEndereco implements Serializable {
    public static final String TABELA = "MedicoXEndereco";
    public static final String _MEDICO   = "_medico";
    public static final String _ENDERECO = "_endereco";

    private long   _medico;
    private long   _endereco;

    public MedicoXEndereco() {
        this._medico = 0;
        this._endereco = 0;
    }

    public long get_medico() {
        return _medico;
    }

    public void set_medico(long _medico) {
        this._medico = _medico;
    }

    public long get_endereco() {
        return _endereco;
    }

    public void set_endereco(long _endereco) {
        this._endereco = _endereco;
    }
}
