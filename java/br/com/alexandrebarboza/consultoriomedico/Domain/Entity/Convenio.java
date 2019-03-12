package br.com.alexandrebarboza.consultoriomedico.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 02/03/2017.
 */

public class Convenio implements Serializable {
    public static final String TABELA = "Convenios";
    public static final String _ID = "_id";
    public static final String NOME = "nome";

    private long   _id;
    private String nome;

    public Convenio() {
        this._id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
