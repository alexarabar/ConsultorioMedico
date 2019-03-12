package br.com.alexandrebarboza.consultoriomedico.Database.Script;

/**
 * Created by Alexandre on 17/02/2017.
 */

public class IndexCreate {
    public static String id_crm_uf() {
        return "CREATE UNIQUE INDEX IF NOT EXISTS id_crm_uf ON Medicos (crm COLLATE NOCASE, uf COLLATE NOCASE);";
    }
    public static String id_cpf() {
        return "CREATE UNIQUE INDEX IF NOT EXISTS id_cpf ON Pacientes (cpf COLLATE NOCASE);";
    }
    public static String id_especialidade() {
        return "CREATE UNIQUE INDEX id_especialidade ON Especialidades (titulo COLLATE NOCASE);";
    }
    public static String id_sala() {
        return "CREATE UNIQUE INDEX id_sala ON Salas (descricao COLLATE NOCASE);";
    }
    public static String id_medico_x_convenio() {
        return "CREATE UNIQUE INDEX IF NOT EXISTS id_medico_x_convenio ON MedicoXConvenio (_medico COLLATE NOCASE, _convenio COLLATE NOCASE);";
    }
    public static String id_medico_x_email() {
        return "CREATE UNIQUE INDEX IF NOT EXISTS id_medico_x_email ON MedicoXEmail (_medico COLLATE NOCASE, _email COLLATE NOCASE);";
    }
    public static String id_medico_x_endereco() {
        return "CREATE UNIQUE INDEX IF NOT EXISTS id_medico_x_endereco ON MedicoXEndereco (_medico COLLATE NOCASE, _endereco COLLATE NOCASE);";
    }
    public static String id_medico_x_telefone() {
        return "CREATE UNIQUE INDEX IF NOT EXISTS id_medico_x_telefone ON MedicoXTelefone (_medico COLLATE NOCASE, _telefone COLLATE NOCASE);";
    }
    public static String id_paciente_x_email() {
        return "CREATE UNIQUE INDEX IF NOT EXISTS id_paciente_x_email ON PacienteXEmail (_paciente COLLATE NOCASE, _email COLLATE NOCASE);";
    }
    public static String id_paciente_x_endereco() {
        return "CREATE UNIQUE INDEX IF NOT EXISTS id_paciente_x_endereco ON PacienteXEndereco (_paciente COLLATE NOCASE, _endereco COLLATE NOCASE);";
    }
    public static String id_paciente_x_telefone() {
        return "CREATE UNIQUE INDEX IF NOT EXISTS id_paciente_x_telefone ON PacienteXTelefone (_paciente COLLATE NOCASE, _telefone COLLATE NOCASE);";

    }
}
