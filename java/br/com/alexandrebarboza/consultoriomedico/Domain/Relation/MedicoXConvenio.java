package br.com.alexandrebarboza.consultoriomedico.Domain.Relation;

import java.io.Serializable;

/**
 * Created by Alexandre on 02/03/2017.
 */

public class MedicoXConvenio implements Serializable {
    public static final String TABELA    = "MedicoXConvenio";
    public static final String _MEDICO   = "_medico";
    public static final String _CONVENIO = "_convenio";

    private long   _medico;
    private long   _convenio;

    public MedicoXConvenio() {
        this._medico = 0;
        this._convenio = 0;
    }

    public long get_medico() {
        return _medico;
    }

    public void set_medico(long _medico) {
        this._medico = _medico;
    }

    public long get_convenio() {
        return _convenio;
    }

    public void set_convenio(long _convenio) {
        this._convenio = _convenio;
    }
}
