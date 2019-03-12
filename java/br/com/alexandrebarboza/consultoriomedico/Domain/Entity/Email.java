package br.com.alexandrebarboza.consultoriomedico.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 24/02/2017.
 */

public class Email implements Serializable {
    public static final String TABELA = "Emails";
    public static final String _ID = "_id";
    public static final String ENDERECO = "endereco";

    private long   _id;
    private String endereco;

    public Email() {
        this._id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return endereco;
    }
}
