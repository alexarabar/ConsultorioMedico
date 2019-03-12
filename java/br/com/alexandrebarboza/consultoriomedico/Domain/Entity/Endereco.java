package br.com.alexandrebarboza.consultoriomedico.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 25/02/2017.
 */

public class Endereco implements Serializable {
    public static final String TABELA = "Enderecos";
    public static final String _ID = "_id";
    public static final String LOGRADOURO = "logradouro";
    public static final String NUMERO = "numero";
    public static final String COMPLEMENTO = "complemento";
    public static final String BAIRRO = "bairro";
    public static final String CIDADE = "cidade";
    public static final String UF = "uf";
    public static final String CEP = "cep";

    private long _id;
    private String logradouro;
    private int numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private int cep;

    public Endereco() {
        this._id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    @Override
    public String toString() {
        return logradouro + " " + numero + " " + complemento + " " + bairro + " " + cidade + " " + uf + " " + cep;
    }
}
