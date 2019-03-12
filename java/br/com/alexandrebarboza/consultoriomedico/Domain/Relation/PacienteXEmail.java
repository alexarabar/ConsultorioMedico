package br.com.alexandrebarboza.consultoriomedico.Domain.Relation;

import java.io.Serializable;

/**
 * Created by Alexandre on 11/03/2017.
 */

public class PacienteXEmail implements Serializable {
    public static final String TABELA  = "PacienteXEmail";
    public static final String _PACIENTE = "_paciente";
    public static final String _EMAIL  = "_email";

    private long   _paciente;
    private long   _email;

    public PacienteXEmail() {
        this._paciente = 0;
        this._email  = 0;
    }

    public long get_paciente() {
        return _paciente;
    }

    public void set_paciente(long _paciente) {
        this._paciente = _paciente;
    }

    public long get_email() {
        return _email;
    }

    public void set_email(long _email) {
        this._email = _email;
    }

}
