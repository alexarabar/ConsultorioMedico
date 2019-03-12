package br.com.alexandrebarboza.consultoriomedico.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 27/02/2017.
 */

public class Especialidade implements Serializable {
    public static final String TABELA = "Especialidades";
    public static final String _ID = "_id";
    public static final String TITULO = "titulo";

    private long   _id;
    private String titulo;

    public Especialidade() {
        this._id = 0;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
