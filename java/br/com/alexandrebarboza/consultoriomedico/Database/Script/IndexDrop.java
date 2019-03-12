package br.com.alexandrebarboza.consultoriomedico.Database.Script;

/**
 * Created by Alexandre on 17/02/2017.
 */

public class IndexDrop {
    private static String Drop(String index) {
        return "DROP INDEX IF EXISTS " + index + ";";
    }
    public static String id_crm_uf() {
        return Drop("id_crm_uf");
    }
    public static String id_cpf() {
        return Drop("id_cpf");
    }
    public static String id_especialidade() {
        return Drop("id_especialidade");
    }
    public static String id_sala() {
        return Drop("id_sala");
    }
    public static String id_medico_x_convenio() {
        return Drop("id_medico_x_convenio");
    }
    public static String id_medico_x_email() {
        return Drop("id_medico_x_email");
    }
    public static String id_medico_x_endereco() {
        return Drop("id_medico_x_endereco");
    }
    public static String id_medico_x_telefone() {
        return Drop("id_medico_x_telefone");
    }
    public static String id_paciente_x_email() {
        return Drop("id_paciente_x_email");
    }
    public static String id_paciente_x_endereco() {
        return Drop("id_paciente_x_endereco");
    }
    public static String id_paciente_x_telefone() {
        return Drop("id_paciente_x_telefone");
    }
}
