package br.com.alexandrebarboza.consultoriomedico.Database.Script;

/**
 * Created by Alexandre on 17/02/2017.
 */

public class TableCreate {
    public static String Consultas() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Consultas ( ");
        sb.append("_id INTEGER CONSTRAINT consulta_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_paciente INTEGER CONSTRAINT x_paciente_id REFERENCES Pacientes (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_medico INTEGER CONSTRAINT x_medico_id REFERENCES Medicos (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_sala INTEGER CONSTRAINT x_sala_id REFERENCES Salas (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("data DATE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("inicio TIME NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("fim TIME NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Convenios() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Convenios ( ");
        sb.append("_id INTEGER CONSTRAINT convenio_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("nome VARCHAR (45) NOT NULL ON CONFLICT FAIL CONSTRAINT uk_convenio_nome UNIQUE ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Emails() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Emails ( ");
        sb.append("_id INTEGER CONSTRAINT email_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("endereco VARCHAR (65) CONSTRAINT uk_email_endereco UNIQUE ON CONFLICT FAIL NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Enderecos() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Enderecos ( ");
        sb.append("_id INTEGER CONSTRAINT endereco_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("logradouro VARCHAR (50) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("numero INTEGER NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("complemento VARCHAR (15) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("bairro VARCHAR (30) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("cidade VARCHAR (40) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("uf VARCHAR (2) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("cep INTEGER (8) NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Especialidades() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Especialidades ( ");
        sb.append("_id INTEGER CONSTRAINT especialidade_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("titulo VARCHAR (40) CONSTRAINT uk_especialidade_titulo UNIQUE ON CONFLICT FAIL NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Medicos() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Medicos ( ");
        sb.append("_id INTEGER CONSTRAINT medico_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("crm VARCHAR (10) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("uf VARCHAR (2) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("nome VARCHAR (45) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("especialidade INTEGER CONSTRAINT fk_medico_especialidade REFERENCES Especialidades (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String MedicoXConvenio() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS MedicoXConvenio ( ");
        sb.append("_medico INTEGER CONSTRAINT x_medico_id REFERENCES Medicos (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_convenio INTEGER CONSTRAINT x_convenio_id REFERENCES Convenios (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String MedicoXEmail() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS MedicoXEmail ( ");
        sb.append("_medico INTEGER CONSTRAINT x_medico_id REFERENCES Medicos (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_email INTEGER CONSTRAINT x_email_id REFERENCES Emails (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String MedicoXEndereco() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS MedicoXEndereco ( ");
        sb.append("_medico INTEGER CONSTRAINT x_medico_id REFERENCES Medicos (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_endereco INTEGER CONSTRAINT x_endereco_id REFERENCES Enderecos (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String MedicoXTelefone() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS MedicoXTelefone ( ");
        sb.append("_medico INTEGER CONSTRAINT x_medico_id REFERENCES Medicos (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_telefone INTEGER CONSTRAINT x_telefone_id REFERENCES Telefones (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Pacientes() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Pacientes ( ");
        sb.append("_id INTEGER CONSTRAINT paciente_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("cpf VARCHAR (11) CONSTRAINT uk_cpf_paciente UNIQUE ON CONFLICT FAIL NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("nome VARCHAR (45) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("convenio INTEGER CONSTRAINT fk_paciente_convenio REFERENCES Convenios (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String PacienteXEmail() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS PacienteXEmail ( ");
        sb.append("_paciente INTEGER CONSTRAINT x_paciente_id REFERENCES Pacientes (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_email INTEGER CONSTRAINT x_email_id REFERENCES Emails (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String PacienteXEndereco() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS PacienteXEndereco ( ");
        sb.append("_paciente INTEGER CONSTRAINT x_paciente_id REFERENCES Pacientes (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_endereco INTEGER CONSTRAINT x_endereco_id REFERENCES Enderecos (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String PacienteXTelefone() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS PacienteXTelefone ( ");
        sb.append("_paciente INTEGER CONSTRAINT x_paciente_id REFERENCES Pacientes (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_telefone INTEGER CONSTRAINT x_telefone_id REFERENCES Telefones (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Salas() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Salas ( ");
        sb.append("_id INTEGER CONSTRAINT sala_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("descricao VARCHAR (20) NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Telefones () {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Telefones ( ");
        sb.append("_id INTEGER CONSTRAINT telefone_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE,");
        sb.append("numero VARCHAR (15) NOT NULL ON CONFLICT FAIL CONSTRAINT uk_telefone_numero UNIQUE COLLATE NOCASE);");
        return sb.toString();
    }
}