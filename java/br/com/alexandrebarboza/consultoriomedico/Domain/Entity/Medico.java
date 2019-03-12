package br.com.alexandrebarboza.consultoriomedico.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 19/02/2017.
 */

public class Medico implements Serializable {
    public static final String TABELA = "Medicos";
    public static final String _ID = "_id";
    public static final String CRM = "crm";
    public static final String UF  = "uf";
    public static final String NOME = "nome";
    public static final String ESPECIALIDADE = "especialidade";

    private long   _id;
    private String crm;
    private String uf;
    private String nome;
    private long   especialidade;

    public Medico() {
        _id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(long especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return nome;
    }
}
