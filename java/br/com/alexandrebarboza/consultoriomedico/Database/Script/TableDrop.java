package br.com.alexandrebarboza.consultoriomedico.Database.Script;

/**
 * Created by Alexandre on 17/02/2017.
 */

public class TableDrop {
    private static String Drop(String table) {
        return "DROP TABLE IF EXISTS " + table + ";";
    }
    public static String Consultas() {
        return Drop("Consultas");
    }
    public static String Convenios() {
        return Drop("Convenios");
    }
    public static String Emails() {
        return Drop("Emails");
    }
    public static String Enderecos() {
        return Drop("Enderecos");
    }
    public static String Especialidades() {
        return Drop("Especialidades");
    }
    public static String Medicos() {
        return Drop("Medicos");
    }
    public static String MedicoXConvenio() {
        return Drop("MedicoXConvenio");
    }
    public static String MedicoXEmail() {
        return Drop("MedicoXEmail");
    }
    public static String MedicoXEndereco() {
        return Drop("MedicoXEndereco");
    }
    public static String MedicoXTelefone() {
        return Drop("MedicoXTelefone");
    }
    public static String Pacientes() {
        return Drop("Pacientes");
    }
    public static String PacienteXEmail() {
        return Drop("PacienteXEmail");
    }
    public static String PacienteXEndereco() {
        return Drop("PacienteXEndereco");
    }
    public static String PacienteXTelefone() {
        return Drop("PacienteXTelefone");
    }
    public static String Salas() {
        return Drop("Salas");
    }
    public static String Telefones () {
        return Drop("Telefones");
    }
}
