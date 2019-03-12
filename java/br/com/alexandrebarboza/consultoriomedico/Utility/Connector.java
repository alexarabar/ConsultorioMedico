package br.com.alexandrebarboza.consultoriomedico.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.sql.Time;
import java.util.Date;

import br.com.alexandrebarboza.consultoriomedico.Database.Database;
import br.com.alexandrebarboza.consultoriomedico.Domain.Domain;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Consulta;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Convenio;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Email;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Endereco;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Medico;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Paciente;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Telefone;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.MedicoXConvenio;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.MedicoXEmail;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.MedicoXEndereco;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.MedicoXTelefone;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.PacienteXEmail;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.PacienteXEndereco;
import br.com.alexandrebarboza.consultoriomedico.Domain.Relation.PacienteXTelefone;
import br.com.alexandrebarboza.consultoriomedico.R;
import br.com.alexandrebarboza.consultoriomedico.Utility.Adapters.Adapters;
import br.com.alexandrebarboza.consultoriomedico.Utility.Messages.Output;

/**
 * Created by Alexandre on 24/02/2017.
 */

public class Connector {
    public static final int MEDICO = 1;
    public static final int PACIENTE = 2;

    private static boolean MedicoIsChanged(Medico medico, EditText ed_crm, Spinner sp_uf) {
        return (!medico.getCrm().equals(ed_crm.getText().toString()) || !medico.getUf().equals(sp_uf.getSelectedItem().toString()));
    }

    private static boolean PacienteIsChanged(Paciente paciente, EditText ed_cpf) {
        return (!paciente.getCpf().equals(ed_cpf.getText().toString()));
    }

    private static boolean TelefoneIsChanged(ArrayAdapter<Telefone> telefones, Spinner sp_telefone, int position) {
        String str = sp_telefone.getItemAtPosition(position).toString();
        for (int i = 0; i < telefones.getCount(); i++) {
            if (telefones.getItem(i).getNumero().equals(str)) {
                return false;
            }
        }
        return true;
    }

    private static boolean EmailIsChanged(ArrayAdapter<Email> emails, Spinner sp_email, int position) {
        String str = sp_email.getItemAtPosition(position).toString();
        for (int i = 0; i < emails.getCount(); i++) {
            if (emails.getItem(i).getEndereco().equals(str)) {
                return false;
            }
        }
        return true;
    }

    private static boolean ConvenioIsChanged(ArrayAdapter<Convenio> convenios, Spinner sp_convenio, int position) {
        String str = sp_convenio.getItemAtPosition(position).toString();
        for (int i = 0; i < convenios.getCount(); i++) {
            if (convenios.getItem(i).getNome().equals(str)) {
                return false;
            }
        }
        return true;
    }

    private static boolean ConsultaIsChanged(Consulta consulta, String dt, String t1, String t2, long id_medico, long id_paciente, long id_sala) {
        Date   dt2 = Dates.convertFromDefaultDate(consulta.getData());
        String st2 = Dates.ShortDateFromString(dt2.toString(), "en", "US");
        if (st2.equals(dt)  &&
            consulta.getInicio().toString().equals(t1)  &&
            consulta.getFim().toString().equals(t2)     &&
            consulta.get_medico() == id_medico     &&
            consulta.get_paciente() == id_paciente &&
            consulta.get_sala() == id_sala) {
            return false;
        }
        return true;
    }

    private static void errorMessageEntity(Activity activity, Domain domain, String data, String operation) {
        String msg, s = data.substring(0, data.length() - 1);
        msg = operation + " " + s + " " + activity.getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
        Output.Alert(activity, activity.getResources().getString(R.string.str_fail), msg);
    }

    private static void errorMessageRelation(Activity activity, Domain domain, String data, String operation) {
        String msg = operation + " na " + data + " " + activity.getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
        Output.Alert(activity, activity.getResources().getString(R.string.str_fail), msg);
    }

    private static boolean addMedicoTelefone(Activity activity, Domain domain, ArrayAdapter<String> ad_telefone, int i, long id_medico) {
        Telefone telefone = new Telefone();
        telefone.setNumero(ad_telefone.getItem(i).toString());
        long id_telefone = domain.addTelefone(telefone);
        boolean result;
        if (id_telefone > 0) {
            MedicoXTelefone medico_x_telefone = new MedicoXTelefone();
            medico_x_telefone.set_medico(id_medico);
            medico_x_telefone.set_telefone(id_telefone);
            if (domain.addMedicoXTelefone(medico_x_telefone) > -1) { // Operação efetuada com êxito!
                result = true;
            } else { // Erro inserindo na relação Medico X Telefone!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_med_x_tel), activity.getResources().getString(R.string.str_err_insert));
                result = false;
            }
        } else { // Erro inserindo Telefone!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_telefone), activity.getResources().getString(R.string.str_err_insert));
            result = false;
        }
        return result;
    }

    private static boolean addPacienteTelefone(Activity activity, Domain domain, ArrayAdapter<String> ad_telefone, int i, long id_paciente) {
        Telefone telefone = new Telefone();
        telefone.setNumero(ad_telefone.getItem(i).toString());
        long id_telefone = domain.addTelefone(telefone);
        boolean result;
        if (id_telefone > 0) {
            PacienteXTelefone paciente_x_telefone = new PacienteXTelefone();
            paciente_x_telefone.set_paciente(id_paciente);
            paciente_x_telefone.set_telefone(id_telefone);
            if (domain.addPacienteXTelefone(paciente_x_telefone) > -1) { // Operação efetuada com êxito!
                result = true;
            } else { // Erro inserindo na relação Paciente X Telefone!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_pac_x_tel), activity.getResources().getString(R.string.str_err_insert));
                result = false;
            }
        } else { // Erro inserindo Telefone!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_telefone), activity.getResources().getString(R.string.str_err_insert));
            result = false;
        }
        return result;
    }

    private static boolean deleteMedicoTelefone(Activity activity, Domain domain, long id) {
        if (domain.deleteTelefone(id) > -1) {
            if (domain.deleteMedicoXTelefone(id, true) > -1) { // Operação efetuada com êxito!
                return true;
            } else { // Erro excluindo a relação Médico X Telefone!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_med_x_tel), activity.getResources().getString(R.string.str_err_delete));
            }
        } else { // Erro excluindo Telefone!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_telefone), activity.getResources().getString(R.string.str_err_delete));
        }
        return false;
    }

    private static boolean deletePacienteTelefone(Activity activity, Domain domain, long id) {
        if (domain.deleteTelefone(id) > -1) {
            if (domain.deletePacienteXTelefone(id, true) > -1) { // Operação efetuada com êxito!
                return true;
            } else { // Erro excluindo a relação Paciente X Telefone!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_pac_x_tel), activity.getResources().getString(R.string.str_err_delete));
            }
        } else { // Erro excluindo Telefone!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_telefone), activity.getResources().getString(R.string.str_err_delete));
        }
        return false;
    }

    private static boolean addMedicoEmail(Activity activity, Domain domain, ArrayAdapter<String> ad_email, int i, long id_medico) {
        Email email = new Email();
        email.setEndereco(ad_email.getItem(i).toString());
        long id_email = domain.addEmail(email);
        boolean result;
        if (id_email > 0) {
            MedicoXEmail medico_x_email = new MedicoXEmail();
            medico_x_email.set_medico(id_medico);
            medico_x_email.set_email(id_email);
            if (domain.addMedicoXEmail(medico_x_email) > -1) { // Operação efetuada com êxito!
                result = true;
            } else { // Erro inserindo na relação Medico X Email!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_med_x_mai), activity.getResources().getString(R.string.str_err_insert));
                result = false;
            }
        } else { // Erro inserindo Email!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_email), activity.getResources().getString(R.string.str_err_insert));
            result = false;
        }
        return result;
    }

    private static boolean addPacienteEmail (Activity activity, Domain domain, ArrayAdapter<String> ad_email, int i, long id_paciente) {
        Email email = new Email();
        email.setEndereco(ad_email.getItem(i).toString());
        long id_email = domain.addEmail(email);
        boolean result;
        if (id_email > 0) {
            PacienteXEmail paciente_x_email = new PacienteXEmail();
            paciente_x_email.set_paciente(id_paciente);
            paciente_x_email.set_email(id_email);
            if (domain.addPacienteXEmail(paciente_x_email) > -1) { // Operação efetuada com êxito!
                result = true;
            } else { // Erro inserindo na relação Paciente X Email!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_pac_x_mai), activity.getResources().getString(R.string.str_err_insert));
                result = false;
            }
        } else { // Erro inserindo Email!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_email), activity.getResources().getString(R.string.str_err_insert));
            result = false;
        }
        return result;
    }

    private static boolean deleteMedicoEmail(Activity activity, Domain domain, long id) {
        if (domain.deleteEmail(id) > -1) {
            if (domain.deleteMedicoXEmail(id, true) > -1) { // Operação efetuada com êxito!
                return true;
            } else { // Erro excluindo a relação Médico X Email!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_med_x_mai), activity.getResources().getString(R.string.str_err_delete));
            }
        } else { // Erro excluindo Email!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_email), activity.getResources().getString(R.string.str_err_delete));
        }
        return false;
    }

    private static boolean deletePacienteEmail(Activity activity, Domain domain, long id) {
        if (domain.deleteEmail(id) > -1) {
            if (domain.deletePacienteXEmail(id, true) > -1) { // Operação efetuada com êxito!
                return true;
            } else { // Erro excluindo a relação Médico X Email!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_med_x_mai), activity.getResources().getString(R.string.str_err_delete));
            }
        } else { // Erro excluindo Email!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_email), activity.getResources().getString(R.string.str_err_delete));
        }
        return false;
    }

    private static Endereco getEndereco(Endereco src) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(src.getLogradouro());
        endereco.setNumero(src.getNumero());
        endereco.setComplemento(src.getComplemento());
        endereco.setBairro(src.getBairro());
        endereco.setCidade(src.getCidade());
        endereco.setUf(src.getUf());
        endereco.setCep(src.getCep());
        return endereco;
    }

    private static boolean addMedicoEndereco(Activity activity, Domain domain, ArrayAdapter<Endereco> enderecos, int i, long id_medico) {
        Endereco endereco = getEndereco(enderecos.getItem(i));
        long id_endereco = domain.addEndereco(endereco);
        boolean result;
        if (id_endereco > 0) {
            MedicoXEndereco medico_x_end = new MedicoXEndereco();
            medico_x_end.set_medico(id_medico);
            medico_x_end.set_endereco(id_endereco);
            if (domain.addMedicoXEndereco(medico_x_end) > -1) { // Operação efetuada com êxito!
                result = true;
            } else { // Erro inserindo na relação Medico X Endereço!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_med_x_end), activity.getResources().getString(R.string.str_err_insert));
                result = false;
            }
        } else { // Erro inserindo Endereço!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_endereco), activity.getResources().getString(R.string.str_err_insert));
            result = false;
        }
        return result;
    }

    private static boolean addPacienteEndereco(Activity activity, Domain domain, ArrayAdapter<Endereco> enderecos, int i, long id_paciente) {
        Endereco endereco = getEndereco(enderecos.getItem(i));
        long id_endereco = domain.addEndereco(endereco);
        boolean result;
        if (id_endereco > 0) {
            PacienteXEndereco paciente_x_end = new PacienteXEndereco();
            paciente_x_end.set_paciente(id_paciente);
            paciente_x_end.set_endereco(id_endereco);
            if (domain.addPacienteXEndereco(paciente_x_end) > -1) { // Operação efetuada com êxito!
                result = true;
            } else { // Erro inserindo na relação Paciente X Endereço!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_pac_x_end), activity.getResources().getString(R.string.str_err_insert));
                result = false;
            }
        } else { // Erro inserindo Endereço!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_endereco), activity.getResources().getString(R.string.str_err_insert));
            result = false;
        }
        return result;
    }

    private static boolean deleteMedicoConvenio(Activity activity, Domain domain, long id) {
        if (domain.deleteMedicoXConvenio(id, true) > -1) { // Operação efetuada com êxito!
            return true;
        } // Erro excluindo a relação Médico X Convênio!
        errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_med_x_conv), activity.getResources().getString(R.string.str_err_delete));
        return false;
    }

    private static boolean addMedicoConvenio(Activity activity, Domain domain, ArrayAdapter<String> ad_convenio, int i, long id_medico) {
        Convenio convenio = new Convenio();
        convenio.setNome(ad_convenio.getItem(i).toString());
        long id_convenio = domain.getIdConvenio(activity, convenio.getNome());
        boolean result;
        if (id_convenio > 0) {
            MedicoXConvenio medico_x_conv = new MedicoXConvenio();
            medico_x_conv.set_medico(id_medico);
            medico_x_conv.set_convenio(id_convenio);
            if (domain.addMedicoXConvenio(medico_x_conv) > -1) { // Operação efetuada com êxito!
                result = true;
            } else { // Erro inserindo na relação Medico X Convênio!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_med_x_conv), activity.getResources().getString(R.string.str_err_insert));
                result = false;
            }
        } else { // Erro buscando convenio!
            if (domain.getError() != null) {
                errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_convenio), activity.getResources().getString(R.string.str_err_find));
            } else {
                String msg, s = activity.getResources().getString(R.string.str_convenio);
                s = s.substring(0, s.length() - 1);
                msg = s + " " + convenio.getNome() + " " + activity.getResources().getString(R.string.str_not_found);
                Output.Alert(activity, activity.getResources().getString(R.string.str_fail), msg);
            }
            result = false;
        }
        return result;
    }

    private static boolean foundMedicoConvenio(Activity activity, Domain domain, ArrayAdapter<String> ad_convenio, int i, long id_medico) {
        long id_convenio = domain.getIdConvenio(activity, ad_convenio.getItem(i).toString());
        int result = domain.checkUniqueKeyForMedicoXConvenio(id_medico, id_convenio);
        if (result == 1) {
            return true;
        }
        if (result == -1) {
            errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_med_x_conv), activity.getResources().getString(R.string.str_err_find));
        }
        return false;
    }

    private static boolean updateEndereco(Activity activity, Domain domain, Endereco endereco) {
        if (domain.updateEndereco(endereco) > -1) { // Operação efetuada com êxito!
            return true;
        } // Erro alterando Endereço!
        errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_endereco), activity.getResources().getString(R.string.str_err_update));
        return false;
    }

    private static boolean deleteMedicoEndereco(Activity activity, Domain domain, long id) {
        if (domain.deleteEndereco(id) > -1) {
            if (domain.deleteMedicoXEndereco(id, true) > -1) { // Operação efetuada com êxito!
                return true;
            } else { // Erro excluindo a relação Médico X Endereço!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_med_x_end), activity.getResources().getString(R.string.str_err_delete));
            }
        } else { // Erro excluindo Endereço!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_endereco), activity.getResources().getString(R.string.str_err_delete));
        }
        return false;
    }

    private static boolean deletePacienteEndereco(Activity activity, Domain domain, long id) {
        if (domain.deleteEndereco(id) > -1) {
            if (domain.deletePacienteXEndereco(id, true) > -1) { // Operação efetuada com êxito!
                return true;
            } else { // Erro excluindo a relação Médico X Endereço!
                errorMessageRelation(activity, domain, activity.getResources().getString(R.string.str_med_x_end), activity.getResources().getString(R.string.str_err_delete));
            }
        } else { // Erro excluindo Endereço!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_endereco), activity.getResources().getString(R.string.str_err_delete));
        }
        return false;
    }

    private static boolean addAllTelefones(Activity activity, Domain domain, Spinner sp_telefone, ArrayAdapter<String> ad_telefone, ArrayAdapter<Telefone> telefones, boolean update, int src, long id, int[] vec) {
        boolean flag = true, result;
        for (int i = 0; i < ad_telefone.getCount(); i++) {
             if (update == true) {
                 result = TelefoneIsChanged(telefones, sp_telefone, i);   // Telefone mudou?
             } else {
                 result = true;
             }
             if (result == true) {
                 if (src == MEDICO) {
                     flag = addMedicoTelefone(activity, domain, ad_telefone, i, id);  // Incluir telefone de médico!
                 } else if (src == PACIENTE) {
                     flag = addPacienteTelefone(activity, domain, ad_telefone, i, id);  // Incluir telefone de paciente!
                 }
                 vec[0] = 1;
            }
            if (!flag) {
                vec[0] = 0;
                break;
            }
        }
        return flag;
    }

    private static boolean addAllEmails(Activity activity, Domain domain, Spinner sp_email, ArrayAdapter<String> ad_email, ArrayAdapter<Email> emails, boolean update, int src, long id, int[] vec) {
        boolean flag = true, result;
        for (int i = 0; i < ad_email.getCount(); i++) { // Incluir email!
            if (update == true) {
                result = EmailIsChanged(emails, sp_email, i);
            } else {
                result = true;
            }
            if (result == true) {
                if (src == MEDICO) {
                    flag = addMedicoEmail(activity, domain, ad_email, i, id);
                } else if (src == PACIENTE) {
                    flag = addPacienteEmail(activity, domain, ad_email, i, id);
                }
                vec[0] = 1;
            }
            if (!flag) {
                vec[0] = 0;
                break;
            }
        }
        return flag;
    }

    private static boolean addAllEnderecos(Activity activity, Domain domain, ArrayAdapter<Endereco> enderecos, boolean update, int src, long id, int[] vec) {
        boolean add, flag = true;
        Endereco end1, end2;
        for (int i = 0; i < enderecos.getCount(); i++) {
            if (update == true) {
                end1 = enderecos.getItem(i);
                long endereco_id = end1.get_id();
                if (endereco_id > 0) {
                    end2 = domain.selectEndereco(endereco_id);
                    if (!Utility.enderecoComparator(end1, end2)) {
                        flag = updateEndereco(activity, domain, end1); // Alterar endereço!
                        vec[0] = 1;
                    }
                    add = false; // Não incluir endereço!
                } else {
                    add = true;
                }
            } else {
                add = true;
            }
            if (add == true) {
                if (src == PACIENTE) {
                    flag = addPacienteEndereco(activity, domain, enderecos, i, id);
                } else if (src == MEDICO) {
                    flag = addMedicoEndereco(activity, domain, enderecos, i, id);
                }
                vec[0] = 1;
            }
            if (!flag) {
                vec[0] = 0;
                break;
            }
        }
        return flag;
    }

    private static boolean addAllConvenios(Activity activity, Domain domain, Spinner sp_convenio, ArrayAdapter<String> ad_convenio, ArrayAdapter<Convenio> convenios, boolean update, long id, int[] vec) {
        boolean flag = true;
        for (int i = 0; i < ad_convenio.getCount(); i++) {
            if (update == false) {
                flag = addMedicoConvenio(activity, domain, ad_convenio, i, id);
            } else {
                if (ConvenioIsChanged(convenios, sp_convenio, i)) {
                    if (!foundMedicoConvenio(activity, domain, ad_convenio, i, id)) {
                        flag = addMedicoConvenio(activity, domain, ad_convenio, i, id);
                        vec[0] = 1;
                    }
                }
            }
            if (!flag) {
                vec[0] = 0;
                break;
            }
        }
        return flag;
    }

    private static boolean removeAllTelefones(Activity activity, Domain domain, ArrayAdapter <String> ad_telefone, ArrayAdapter <Telefone> telefones, int src, int[] vec) {
        boolean flag = true;
        for (int i = 0; i < telefones.getCount(); i++) {
            if (!Adapters.DataFound(telefones.getItem(i).getNumero(), ad_telefone)) { // Excluir telefone!
                if (src == PACIENTE) {
                    flag = deletePacienteTelefone(activity, domain, telefones.getItem(i).get_id());
                } else if (src == MEDICO) {
                    flag = deleteMedicoTelefone(activity, domain, telefones.getItem(i).get_id());
                }
                if (!flag) {
                    vec[0] = 0;
                    break;
                } else {
                    vec[0] = 1;
                }
            }
        }
        return flag;
    }

    private static boolean removeAllEmails(Activity activity, Domain domain, ArrayAdapter<String> ad_email, ArrayAdapter<Email> emails, int src, int[] vec) {
        boolean flag = true;
        for (int i = 0; i < emails.getCount(); i++) {
            if (!Adapters.DataFound(emails.getItem(i).getEndereco(), ad_email)) { // Excluir email!
                if (src == PACIENTE) {
                    flag = deletePacienteEmail(activity, domain, emails.getItem(i).get_id());
                } else if (src == MEDICO) {
                    flag = deleteMedicoEmail(activity, domain, emails.getItem(i).get_id());
                }
                if (!flag) {
                    vec[0] = 0;
                    break;
                } else {
                    vec[0] = 1;
                }
            }
        }
        return flag;
    }

    private static boolean removeAllEnderecos(Activity activity, Domain domain, ArrayAdapter<Endereco> enderecos, int src, long id, int[] vec) {
        boolean flag = true;
        String[] array = {"", "", "", "", "", "", ""};
        ArrayAdapter<Endereco> enderecos2 = domain.selectEnderecos(activity, id, src);;
        if (enderecos2 != null) {
            for (int i = 0; i < enderecos2.getCount(); i++) {
                 Utility.enderecoToArray(array, enderecos2.getItem(i));
                 if (!Utility.enderecoExists(enderecos, array)) {     // Excluir endereço!
                     if (src == PACIENTE) {
                         flag = deletePacienteEndereco(activity, domain, enderecos2.getItem(i).get_id());
                     } else if (src == MEDICO) {
                         flag = deleteMedicoEndereco(activity, domain, enderecos2.getItem(i).get_id());
                     }
                     if (!flag) {
                         vec[0] = 0;
                         break;
                     } else {
                         vec[0] = 1;
                     }
                 }
            }
        }
        return flag;
    }

    private static boolean removeAllConvenios(Activity activity, Domain domain, ArrayAdapter<String> ad_convenio, ArrayAdapter<Convenio> convenios, int[] vec) {
        boolean flag = true;
        for (int i = 0; i < convenios.getCount(); i++) {
            if (!Adapters.DataFound(convenios.getItem(i).getNome(), ad_convenio)) { // Excluir convênio!
                flag = deleteMedicoConvenio(activity, domain, convenios.getItem(i).get_id());
                if (!flag) {
                    vec[0] = 0;
                    break;
                } else {
                    vec[0] = 1;
                }
            }
        }
        return flag;
    }

    private static int checkTelefone(Domain domain, Spinner sp_telefone, ArrayAdapter<Telefone> telefones, int[] vec, boolean changed) {
        int x, flag = 1;
        boolean control;
        for (int i = 0; i < sp_telefone.getCount(); i++) {
            if (changed == true) {
                if (TelefoneIsChanged(telefones, sp_telefone, i) == true) {
                    control = true;
                } else {
                    control = false;
                }
            } else {
                control = true;
            }
            if (control == true) {

                // Verificar se o numero do telefone ja existe!
                x = domain.checkUniqueKeyForTelefone(sp_telefone.getItemAtPosition(i).toString());
                if (x == -1) {
                    flag = -1;
                    break;
                } else if (x == 0) {
                    flag = 1;
                } else if (x == 1) {
                    flag = 3;
                    vec[0] = i;
                    break;
                }
            } else {
                flag = 1;
            }
        }
        return flag;
    }

    private static int checkEmail(Domain domain, Spinner sp_email, ArrayAdapter<Email> emails, int[] vec, boolean changed) {
        int x, flag = 1;
        boolean control;
        for (int i = 0; i < sp_email.getCount(); i++) {
            if (changed == true) {
                if (EmailIsChanged(emails, sp_email, i) == true) {
                    control = true;
                } else {
                    control = false;
                }
            } else {
                control = true;
            }
            if (control == true) {

                // Verificar se o endereço de email ja existe!
                x = domain.checkUniqueKeyForEmail(sp_email.getItemAtPosition(i).toString());

                if (x == -1) {
                    flag = -1;
                    break;
                } else if (x == 0) {
                    flag = 1;
                } else if (x == 1) {
                    flag = 4;
                    vec[0] = i;
                    break;
                }
            } else {
                flag = 1;
            }
        }
        return flag;
    }

    private static int checkEndereco(Domain domain, ArrayAdapter<Endereco> enderecos, int[] vec) {
        int x, flag = 1;
        for (int i = 0; i < enderecos.getCount(); i++) {

            // Verificar se o endereço já existe!
            x = domain.checkAllFieldsForEndereco(enderecos.getItem(i));

            if (x == -1) {
                flag = -1;
                break;
            } else if (x == 0) {
                flag = 1;
            } else {
                flag = 5;
                vec[0] = i;
                break;
            }
        }
        return flag;
    }

    public static boolean OpenDatabase(Resources resources, Context context, Database database, Domain domain, boolean flag) {
        String error = "";
        if (flag) {
            if (database.setWritable()) {
                // Banco de Dados aberto para escrita!
            } else {
                error = resources.getString(R.string.str_write);
            }
        } else {
            if (database.setReadable()) {
                // Banco de Dados aberto para leitura!
            } else {
                error = resources.getString(R.string.str_read);
            }
        }
        if (!error.isEmpty()) {
            Output.Alert(context, resources.getString(R.string.str_fail), resources.getString(R.string.str_err_sql_open) + " " + error + "!" + resources.getString(R.string.str_msg_plus) + "\n" + database.getError());
            return false;
        }
        domain.setConnection(database.getConnection());
        return true;
    }

    public static boolean DeleteMedico(Activity activity, Domain domain, Medico medico, ArrayAdapter <Telefone> telefones, ArrayAdapter <Email> emails, ArrayAdapter <Endereco> enderecos, String[] v) {
        boolean flag = false;
        String field, str = "";
        if (domain.deleteMedico(medico.get_id()) > -1) {
            flag = true;
        } else {
            str = activity.getResources().getString(R.string.str_medicos);
            str = str.substring(0, str.length() - 1) + " ";
        }
        if (str.isEmpty()) {
            for (int i = 0; i < telefones.getCount(); i++) {
                if (domain.deleteTelefone(telefones.getItem(i).get_id()) > -1) {
                    flag = true;
                } else {
                    field = activity.getResources().getString(R.string.str_telefone);
                    field = field.substring(0, field.length() - 1) + " ";
                    str = field;
                    break;
                }
            }
        }
        if (str.isEmpty()) {
            if (domain.deleteMedicoXTelefone(medico.get_id(), false) > -1) {
                flag = true;
            } else {
                str = activity.getResources().getString(R.string.str_med_x_tel) + " ";
            }
        }
        if (str.isEmpty()) {
            for (int i  = 0; i < emails.getCount(); i++) {
                if (domain.deleteEmail(emails.getItem(i).get_id()) > -1) {
                    flag = true;
                } else {
                    field = activity.getResources().getString(R.string.str_email);
                    field = field.substring(0, field.length() -1) + " ";
                    str = field;
                    break;
                }
            }
        }
        if (str.isEmpty()) {
            if (domain.deleteMedicoXEmail(medico.get_id(), false) > -1) {
                flag = true;
            } else {
                str = activity.getResources().getString(R.string.str_med_x_mai) + " ";
            }
        }
        if (str.isEmpty()) {
            for (int i  = 0; i < enderecos.getCount(); i++) {
                if (domain.deleteEndereco(enderecos.getItem(i).get_id()) > -1) {
                    flag = true;
                } else {
                    field = activity.getResources().getString(R.string.str_endereco);
                    field = field.substring(0, field.length() -1) + " ";
                    str = field;
                    break;
                }
            }
        }
        if (str.isEmpty()) {
            if (domain.deleteMedicoXEndereco(medico.get_id(), false) > -1) {
                flag = true;
            } else {
                str = activity.getResources().getString(R.string.str_med_x_end) + " ";
            }
        }
        if (str.isEmpty()) {
            if (domain.deleteMedicoXConvenio(medico.get_id(), false) > -1) {
                flag = true;
            } else {
                str = activity.getResources().getString(R.string.str_med_x_conv) + " ";
            }
        }
        v[0] = str;
        return flag;
    }

    public static boolean DeletePaciente(Activity activity, Domain domain, Paciente paciente, ArrayAdapter <Telefone> telefones, ArrayAdapter <Email> emails, ArrayAdapter <Endereco> enderecos, String[] v) {
        boolean flag = false;
        String field, str = "";
        if (domain.deletePaciente(paciente.get_id()) > -1) {
            flag = true;
        } else {
            str = activity.getResources().getString(R.string.str_pacientes);
            str = str.substring(0, str.length() -1)  + " ";
        }
        if (str.isEmpty()) {
            for (int i = 0; i < telefones.getCount(); i++) {
                if (domain.deleteTelefone(telefones.getItem(i).get_id()) > -1) {
                    flag = true;
                } else {
                    field = activity.getResources().getString(R.string.str_telefone);
                    field = field.substring(0, field.length() - 1) + " ";
                    str = field;
                    break;
                }
            }
        }
        if (str.isEmpty()) {
            if (domain.deletePacienteXTelefone(paciente.get_id(), false) > -1) {
                flag = true;
            } else {
                str = activity.getResources().getString(R.string.str_pac_x_tel) + " ";
            }
        }
        if (str.isEmpty()) {
            for (int i  = 0; i < emails.getCount(); i++) {
                if (domain.deleteEmail(emails.getItem(i).get_id()) > -1) {
                    flag = true;
                } else {
                    field = activity.getResources().getString(R.string.str_email);
                    field = field.substring(0, field.length() -1) + " ";
                    str = field;
                    break;
                }
            }
        }
        if (str.isEmpty()) {
            if (domain.deletePacienteXEmail(paciente.get_id(), false) > -1) {
                flag = true;
            } else {
                str = activity.getResources().getString(R.string.str_pac_x_mai) + " ";
            }
        }
        if (str.isEmpty()) {
            for (int i  = 0; i < enderecos.getCount(); i++) {
                if (domain.deleteEndereco(enderecos.getItem(i).get_id()) > -1) {
                    flag = true;
                } else {
                    field = activity.getResources().getString(R.string.str_endereco);
                    field = field.substring(0, field.length() -1) + " ";
                    str = field;
                    break;
                }
            }
        }
        if (str.isEmpty()) {
            if (domain.deletePacienteXEndereco(paciente.get_id(), false) > -1) {
                flag = true;
            } else {
                str = activity.getResources().getString(R.string.str_pac_x_end) + " ";
            }
        }
        v[0] = str;
        return flag;
    }

    public static boolean DeleteConsulta(Activity activity, Domain domain, Consulta consulta, String[] v) {
        if (domain.deleteConsulta(consulta.get_id()) > -1) {
            return true;
        }
        String str = activity.getResources().getString(R.string.str_consultas);
        str = str.substring(0, str.length() - 1) + " ";
        v[0] = str;
        return false;
    }

    public static boolean SaveAddMedico(Activity activity, Domain domain, Medico medico, ArrayAdapter<String> ad_telefone, ArrayAdapter<String> ad_email, ArrayAdapter<Endereco> enderecos, ArrayAdapter<String> ad_convenio, EditText ed_nome, EditText ed_crm, Spinner sp_uf, long id_esp) {
        boolean flag = false;
        medico.setNome(ed_nome.getText().toString());
        medico.setCrm(ed_crm.getText().toString());
        medico.setUf(sp_uf.getSelectedItem().toString());
        medico.setEspecialidade(id_esp);
        long id_medico = domain.addMedico(medico);
        int v[] = {0};
        if (id_medico > 0) {
            flag = addAllTelefones(activity, domain, null, ad_telefone, null, false, MEDICO, id_medico, v);
        } else { // Erro incluindo Médico!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_medicos), activity.getResources().getString(R.string.str_err_insert));
        }
        if (flag) {
            flag = addAllEmails(activity, domain, null, ad_email, null, false, MEDICO, id_medico, v);
        }
        if (flag) {
            flag = addAllEnderecos(activity, domain, enderecos, false, MEDICO, id_medico, v);
        }
        if (flag) {
            flag = addAllConvenios(activity, domain, null, ad_convenio, null, false, id_medico, v);
        }
        return flag;
    }

    public static boolean SaveAddPaciente(Activity activity, Domain domain, Paciente paciente, ArrayAdapter<String> ad_telefone, ArrayAdapter<String> ad_email, ArrayAdapter<Endereco> enderecos, EditText ed_nome, EditText ed_cpf, long id_conv) {
        boolean flag = false;
        paciente.setNome(ed_nome.getText().toString());
        paciente.setCpf(ed_cpf.getText().toString());
        paciente.setConvenio(id_conv);
        long id_paciente = domain.addPaciente(paciente);
        int v[] = {0};
        if (id_paciente > 0) {
            flag = addAllTelefones(activity, domain, null, ad_telefone, null, false, PACIENTE, id_paciente, v);
        } else { // Erro inserindo Paciente!
            errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_pacientes), activity.getResources().getString(R.string.str_err_insert));
        }
        if (flag) {
            flag = addAllEmails(activity, domain, null, ad_email, null, false, PACIENTE, id_paciente, v);
        }
        if (flag) {
            flag = addAllEnderecos(activity, domain, enderecos, false, PACIENTE, id_paciente, v);
        }
        return flag;
    }

    public static boolean SaveAddConsulta(Activity activity, Domain domain, Consulta consulta, String dt, String t1, String t2, long id_pac, long id_med, long id_sal) {
        consulta.setData(Dates.getSQLDate(dt, true));
        Time ini = Time.valueOf(t1);
        Time fim = Time.valueOf(t2);
        consulta.setInicio(ini);
        consulta.setFim(fim);
        consulta.set_paciente(id_pac);
        consulta.set_medico(id_med);
        consulta.set_sala(id_sal);
        long id_consulta = domain.addConsulta(consulta);
        if (id_consulta > 0) {
            return true;
        }
        errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_consultas), activity.getResources().getString(R.string.str_err_insert));
        return false;
    }

    public static boolean SaveUpdateMedico(Activity activity, Domain domain, Medico medico, ArrayAdapter<Telefone> telefones, ArrayAdapter<Email> emails, ArrayAdapter<Endereco> enderecos, ArrayAdapter<Convenio> convenios, ArrayAdapter<String> ad_telefone, ArrayAdapter<String> ad_email, ArrayAdapter<String> ad_convenio, Spinner sp_telefone, Spinner sp_email, Spinner sp_convenio, EditText ed_nome, EditText ed_crm, Spinner sp_uf, long id_esp, int[] v) {
        boolean flag;
        boolean f1, f2, f3;
        f1 = MedicoIsChanged(medico, ed_crm, sp_uf); // CRM ou UF mudaram.
        f2 = !medico.getNome().equals(ed_nome.getText().toString()); // NOme mudou.
        f3 = medico.getEspecialidade() != id_esp; // Especialidade mudou!
        if (f1 || f2 || f3) {
            if (f1) {
                medico.setCrm(ed_crm.getText().toString());
                medico.setUf(sp_uf.getSelectedItem().toString());
            }
            if (f2) {
                medico.setNome(ed_nome.getText().toString());
            }
            if (f3) {
                medico.setEspecialidade(id_esp);
            }
            if (domain.updateMedico(medico) > -1) {
                v[0] = 1;
                flag = true;
            } else {         // Erro Alterando Médico!
                errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_medicos), activity.getResources().getString(R.string.str_err_update));
                v[0] = 0;
                flag = false;
            }
        } else {
            flag = true;
        }
        if (flag) {
            flag = addAllTelefones(activity, domain, sp_telefone, ad_telefone, telefones, true, MEDICO, medico.get_id(), v);
        }
        if (flag) {
            flag = removeAllTelefones(activity, domain, ad_telefone, telefones, MEDICO, v);
        }
        if (flag) {
            flag = addAllEmails(activity, domain, sp_email, ad_email, emails, true, MEDICO, medico.get_id(), v);
        }
        if (flag) {
            flag = removeAllEmails(activity, domain, ad_email, emails, MEDICO, v);
        }
        if (flag) {
            flag = addAllEnderecos(activity, domain, enderecos, true, MEDICO, medico.get_id(), v);
        }
        if (flag) {
            flag = removeAllEnderecos(activity, domain, enderecos, MEDICO, medico.get_id(), v);
        }
        if (flag) {
            flag = addAllConvenios(activity, domain, sp_convenio, ad_convenio, convenios, true, medico.get_id(), v);
        }
        if (flag) {
            flag = removeAllConvenios(activity, domain, ad_convenio, convenios, v);
        }
        return flag;
    }

    public static boolean SaveUpdatePaciente(Activity activity, Domain domain, Paciente paciente, ArrayAdapter<Telefone> telefones, ArrayAdapter<Email> emails, ArrayAdapter<Endereco> enderecos, ArrayAdapter<String> ad_telefone, ArrayAdapter<String> ad_email, Spinner sp_telefone, Spinner sp_email, EditText ed_nome, EditText ed_cpf, long id_conv, int[] v) {
        boolean flag;
        boolean f1, f2, f3;
        f1 = PacienteIsChanged(paciente, ed_cpf); // CPF mudou.
        f2 = !paciente.getNome().equals(ed_nome.getText().toString()); // Nome mudou.
        f3 = paciente.getConvenio() != id_conv; // Convenio mudou!
        if (f1 || f2 || f3) {
            if (f1) {
                paciente.setCpf(ed_cpf.getText().toString());
            }
            if (f2) {
                paciente.setNome(ed_nome.getText().toString());
            }
            if (f3) {
                paciente.setConvenio(id_conv);
            }
            if (domain.updatePaciente(paciente) > -1) {
                v[0] = 1;
                flag = true;
            } else {         // Erro Alterando Paciente!
                errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_pacientes), activity.getResources().getString(R.string.str_err_update));
                v[0] = 0;
                flag = false;
            }
        } else {
            flag = true;
        }
        if (flag) {
            flag = addAllTelefones(activity, domain, sp_telefone, ad_telefone, telefones, true, PACIENTE, paciente.get_id(), v);
        }
        if (flag) {
            flag = removeAllTelefones(activity, domain, ad_telefone, telefones, PACIENTE, v);
        }
        if (flag) {
            flag = addAllEmails(activity, domain, sp_email, ad_email, emails, true, PACIENTE, paciente.get_id(), v);
        }
        if (flag) {
            flag = removeAllEmails(activity, domain, ad_email, emails, PACIENTE, v);
        }
        if (flag) {
            flag = addAllEnderecos(activity, domain, enderecos, true, PACIENTE, paciente.get_id(), v);
        }
        if (flag) {
            flag = removeAllEnderecos(activity, domain, enderecos, PACIENTE, paciente.get_id(), v);
        }
        return flag;
    }

    public static boolean SaveUpdateConsulta(Activity activity, Domain domain, Consulta consulta, String dt, String t1, String t2, long id_pac, long id_med, long id_sal) {
        consulta.setData(Dates.getSQLDate(dt, true));
        Time ini = Time.valueOf(t1);
        Time fim = Time.valueOf(t2);
        consulta.setInicio(ini);
        consulta.setFim(fim);
        consulta.set_paciente(id_pac);
        consulta.set_medico(id_med);
        consulta.set_sala(id_sal);
        long id_consulta = domain.updateConsulta(consulta);
        if (id_consulta > 0) {
            return true;
        }
        errorMessageEntity(activity, domain, activity.getResources().getString(R.string.str_consultas), activity.getResources().getString(R.string.str_err_update));
        return false;
    }

    public static int LoadAddMedico(Domain domain, Spinner sp_telefone, Spinner sp_email, ArrayAdapter <Endereco> enderecos, EditText ed_crm, Spinner sp_uf, int[] vec) {
        int x, flag = 0;

        // Verificar se a chave (crm + uf) já existe!
        x = domain.checkUniqueKeyForMedico(ed_crm.getText().toString(), sp_uf.getSelectedItem().toString());

        if (x == -1) {
            flag = -1;
        } else if (x == 0) {
            flag = 1;
        } else if (x == 1) {
            flag = 2;
        }
        if (flag == 1) {
            flag = checkTelefone(domain, sp_telefone, null, vec, false);
        }
        if (flag == 1) {
            flag = checkEmail(domain, sp_email, null, vec, false);
        }
        if (flag == 1) {
            flag = checkEndereco(domain, enderecos, vec);
        }
        return flag;
    }

    public static int LoadUpdateMedico(Domain domain, Medico medico, ArrayAdapter <Telefone> telefones, ArrayAdapter <Email> emails, ArrayAdapter <Endereco> enderecos, Spinner sp_telefone, Spinner sp_email, EditText ed_crm, Spinner sp_uf, int[] vet) {
        int x, flag = 0;
        boolean medico_changed = MedicoIsChanged(medico, ed_crm, sp_uf);
        if (medico_changed) {

            // Verificar Se a chave (crm + uf) já existe!
            x = domain.checkUniqueKeyForMedico(ed_crm.getText().toString(), sp_uf.getSelectedItem().toString());

        } else {
            x = 0;
        }
        if (x == -1) {
            flag = -1;
        } else if (x == 0) {
            flag = 1;
        } else if (x == 1) {
            flag = 2;
        }
        if (flag == 1) {
            flag = checkTelefone(domain, sp_telefone, telefones, vet, true);
        }
        if (flag == 1) {
            flag = checkEmail(domain, sp_email, emails, vet, true);
        }
        if (flag == 1) {
            flag = checkEndereco(domain, enderecos, vet);
        }
        return flag;
    }

    public static int LoadAddPaciente(Domain domain, Spinner sp_telefone, Spinner sp_email, ArrayAdapter <Endereco> enderecos, EditText ed_cpf,  int[] vec) {
        int x, flag = 0;

        // Verificar se a chave CPF já existe!
        x = domain.checkUniqueKeyForPaciente(ed_cpf.getText().toString());

        if (x == -1) {
            flag = -1;
        } else if (x == 0) {
            flag = 1;
        } else if (x == 1) {
            flag = 2;
        }
        if (flag == 1) {
            flag = checkTelefone(domain, sp_telefone, null, vec, false);
        }
        if (flag == 1) {
            flag = checkEmail(domain, sp_email, null, vec, false);
        }
        if (flag == 1) {
            flag = checkEndereco(domain, enderecos, vec);
        }
        return flag;
    }

    public static int LoadUpdatePaciente(Domain domain, Paciente paciente, ArrayAdapter <Telefone> telefones, ArrayAdapter <Email> emails, ArrayAdapter <Endereco> enderecos, Spinner sp_telefone, Spinner sp_email, EditText ed_cpf, int[] vec) {
        int x, flag = 0;
        boolean paciente_changed = PacienteIsChanged(paciente, ed_cpf);
        if (paciente_changed) {

            // Verificar Se a chave CPF já existe!
            x = domain.checkUniqueKeyForPaciente(ed_cpf.getText().toString());

        } else {
            x = 0;
        }
        if (x == -1) {
            flag = -1;
        } else if (x == 0) {
            flag = 1;
        } else if (x == 1) {
            flag = 2;
        }
        if (flag == 1) {
            flag = checkTelefone(domain, sp_telefone, telefones, vec, true);
        }
        if (flag == 1) {
            flag = checkEmail(domain, sp_email, emails, vec, true);
        }
        if (flag == 1) {
            flag = checkEndereco(domain, enderecos, vec);
        }
        return flag;
    }

    public static int LoadAddConsulta(Activity activity, Domain domain, String data, EditText ed_hora_ini, EditText ed_min_ini, EditText ed_hora_fim, EditText ed_min_fim, EditText ed_medico, EditText ed_paciente, EditText ed_sala, long id) {
        String s1 = Dates.getTime(ed_hora_ini.getText().toString(), ed_min_ini.getText().toString());
        String s2 = Dates.getTime(ed_hora_fim.getText().toString(), ed_min_fim.getText().toString());
        try {
            Time t1 = Time.valueOf(s1);
            Time t2 = Time.valueOf(s2);
            if (t1.compareTo(t2) >= 0) {
                return 2;
            }
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            return -2;
        }
        data = Dates.getSQLDate(data, true).toString();

        // Verificar se existe uma data igual com horario inicial >= h1 && <= h2 || horario final <= h2 && >= h1 || horario inicial <= h1 e horario final >= h2

        switch (domain.selectDataTimeForConsulta(activity, data, s1, s2, ed_medico.getText().toString(), ed_paciente.getText().toString(), ed_sala.getText().toString(), id)) {
            case -1: // Erro na consulta!
                return -1;
            case -2: // Médico ocupado neste horário!
                return 3;
            case -3: // Paciente ocupado neste horário!
                return 4;
            case -4: // Sala ocupada neste horário
                return 5;
        }
        return 1;
    }

    public static int LoadUpdateConsulta(Activity activity, Domain domain, Consulta consulta, String data, EditText ed_hora_ini, EditText ed_min_ini, EditText ed_hora_fim, EditText ed_min_fim, EditText ed_medico, EditText ed_paciente, EditText ed_sala) {
        String s1 = Dates.getTime(ed_hora_ini.getText().toString(), ed_min_ini.getText().toString());
        String s2 = Dates.getTime(ed_hora_fim.getText().toString(), ed_min_fim.getText().toString());

        long id_medico   = domain.getIdMedico(activity, ed_medico.getText().toString());
        long id_paciente = domain.getIdPaciente(activity, ed_paciente.getText().toString());
        long id_sala     = domain.getIdSala(activity, ed_sala.getText().toString());

        if (ConsultaIsChanged(consulta, data, s1, s2, id_paciente, id_medico, id_sala)) {
            return LoadAddConsulta(activity, domain, data, ed_hora_ini, ed_min_ini, ed_hora_fim, ed_min_fim, ed_medico, ed_paciente, ed_sala, consulta.get_id());
        }
        return 0;
    }

    public static long getIdEspecialidade(Activity activity, Domain domain, String text) {
        return domain.getIdEspecialidade(activity, text);
    }

    public static long getIdConvenio(Activity activity, Domain domain, String text) {
        return domain.getIdConvenio(activity, text);
    }

}
