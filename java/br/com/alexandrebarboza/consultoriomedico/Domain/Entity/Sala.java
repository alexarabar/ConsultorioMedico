package br.com.alexandrebarboza.consultoriomedico.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 18/03/2017.
 */

public class Sala implements Serializable {
    public static final String TABELA = "Salas";
    public static final String _ID = "_id";
    public static final String DESCRICAO = "descricao";

    private long   _id;
    private String descricao;

    public Sala() {
        this._id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
