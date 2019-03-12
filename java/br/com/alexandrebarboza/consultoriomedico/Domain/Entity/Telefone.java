package br.com.alexandrebarboza.consultoriomedico.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 19/02/2017.
 */

public class Telefone implements Serializable {
    public static final String TABELA = "Telefones";
    public static final String _ID = "_id";
    public static final String NUMERO = "numero";

    private long   _id;
    private String numero;

    public Telefone() {
        this._id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return numero;
    }
}
