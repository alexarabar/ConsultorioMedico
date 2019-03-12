package br.com.alexandrebarboza.consultoriomedico.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 11/03/2017.
 */

public class Paciente  implements Serializable {
    public static final String TABELA = "Pacientes";
    public static final String _ID = "_id";
    public static final String CPF = "cpf";
    public static final String NOME = "nome";
    public static final String CONVENIO = "convenio";

    private long   _id;
    private String cpf;
    private String nome;
    private long   convenio;

    public Paciente() {
        _id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getConvenio() {
        return convenio;
    }

    public void setConvenio(long convenio) {
        this.convenio = convenio;
    }

    @Override
    public String toString() {
        return nome;
    }
}
