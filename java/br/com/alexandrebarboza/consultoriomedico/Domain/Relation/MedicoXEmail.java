package br.com.alexandrebarboza.consultoriomedico.Domain.Relation;

import java.io.Serializable;

/**
 * Created by Alexandre on 24/02/2017.
 */

public class MedicoXEmail  implements Serializable {
    public static final String TABELA  = "MedicoXEmail";
    public static final String _MEDICO = "_medico";
    public static final String _EMAIL  = "_email";

    private long   _medico;
    private long   _email;

    public MedicoXEmail() {
        this._medico = 0;
        this._email  = 0;
    }

    public long get_medico() {
        return _medico;
    }

    public void set_medico(long _medico) {
        this._medico = _medico;
    }

    public long get_email() {
        return _email;
    }

    public void set_email(long _email) {
        this._email = _email;
    }
}
