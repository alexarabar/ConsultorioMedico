package br.com.alexandrebarboza.consultoriomedico.Utility;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Convenio;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Email;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Endereco;
import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Telefone;
import br.com.alexandrebarboza.consultoriomedico.R;

/**
 * Created by Alexandre on 21/02/2017.
 */

public class Utility {

    public static void setUF(ArrayAdapter <String> adapter) {
        adapter.add("AC");
        adapter.add("AL");
        adapter.add("AP");
        adapter.add("AM");
        adapter.add("BA");
        adapter.add("CE");
        adapter.add("DF");
        adapter.add("ES");
        adapter.add("GO");
        adapter.add("MA");
        adapter.add("MT");
        adapter.add("MS");
        adapter.add("MG");
        adapter.add("PA");
        adapter.add("PB");
        adapter.add("PR");
        adapter.add("PE");
        adapter.add("PI");
        adapter.add("RJ");
        adapter.add("RN");
        adapter.add("RS");
        adapter.add("RO");
        adapter.add("RR");
        adapter.add("SC");
        adapter.add("SP");
        adapter.add("SE");
        adapter.add("TO");
    }

    public static void resetImages(int count, ImageButton ib_update, ImageButton ib_delete) {
        if (count == 0) {
            ib_update.setImageResource(R.drawable.ic_update_disabled);
            ib_delete.setImageResource(R.drawable.ic_delete_disabled);
            ib_update.setEnabled(false);
            ib_delete.setEnabled(false);
        } else {
            if (count == 1) {
                ib_update.setImageResource(R.drawable.ic_update);
                ib_delete.setImageResource(R.drawable.ic_delete);
                ib_update.setEnabled(true);
                ib_delete.setEnabled(true);
            }
        }
    }

    public static void resetImages(boolean flag, ImageButton ib_update, ImageButton ib_delete) {
        if (flag) {
            ib_update.setImageResource(R.drawable.ic_update);
            ib_delete.setImageResource(R.drawable.ic_delete);
        } else {
            ib_update.setImageResource(R.drawable.ic_update_disabled);
            ib_delete.setImageResource(R.drawable.ic_delete_disabled);
        }
        ib_update.setEnabled(flag);
        ib_delete.setEnabled(flag);
    }

    public static boolean enderecoIsEmpty(String[] values) {
        try {
            if (values[0].trim().isEmpty() ||
                values[1].trim().isEmpty() ||
                values[2].trim().isEmpty() ||
                values[3].trim().isEmpty() ||
                values[4].trim().isEmpty() ||
                values[5].trim().isEmpty() ||
                values[6].trim().isEmpty()) {
               return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean enderecoExists(ArrayAdapter <Endereco> enderecos, String[] values) {
        try {
            for (int i = 0; i < enderecos.getCount(); i++) {
                if (enderecos.getItem(i).getLogradouro().equals(values[0]) &&
                    String.valueOf(enderecos.getItem(i).getNumero()).equals(values[1]) &&
                    enderecos.getItem(i).getComplemento().equals(values[2]) &&
                    enderecos.getItem(i).getBairro().equals(values[3]) &&
                    enderecos.getItem(i).getCidade().equals(values[4]) &&
                    enderecos.getItem(i).getUf().equals(values[5]) &&
                    String.valueOf(enderecos.getItem(i).getCep()).equals(values[6])) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Endereco enderecoInsert(String[] values) {
        try {
             Endereco endereco = new Endereco();
             endereco.setLogradouro(values[0]);
             endereco.setNumero(Integer.parseInt(values[1]));
             endereco.setComplemento(values[2]);
             endereco.setBairro(values[3]);
             endereco.setCidade(values[4]);
             endereco.setUf(values[5]);
             endereco.setCep(Integer.parseInt(values[6]));
            return endereco;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void enderecoLoad(Intent intent, String[] values) {
        try {
            values[0] = intent.getStringExtra("LOGRADOURO");
            values[1] = intent.getStringExtra("NUMERO");
            values[2] = intent.getStringExtra("COMPLEMENTO");
            values[3] = intent.getStringExtra("BAIRRO");
            values[4] = intent.getStringExtra("CIDADE");
            values[5] = intent.getStringExtra("UF");
            values[6] = intent.getStringExtra("CEP");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void enderecoUpdate(Endereco endereco, String[] values) {
        try {
             endereco.setLogradouro(values[0]);
             endereco.setNumero(Integer.parseInt(values[1]));
             endereco.setComplemento(values[2]);
             endereco.setBairro(values[3]);
             endereco.setCidade(values[4]);
             endereco.setUf(values[5]);
             endereco.setCep(Integer.parseInt(values[6]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String enderecoReduce(Endereco endereco) {
        return (endereco.getLogradouro() + ", " + endereco.getNumero() + " - " + endereco.getComplemento());
    }

    public static String enderecoReduce(String[] array) {
        return (array[0] + ", " + array[1] + " - " + array[2]);
    }

    public static void enderecoToArray(String[] array, Endereco endereco) {
        try {
            array[0] = endereco.getLogradouro();
            array[1] = String.valueOf(endereco.getNumero());
            array[2] = endereco.getComplemento();
            array[3] = endereco.getBairro();
            array[4] = endereco.getCidade();
            array[5] = endereco.getUf();
            array[6] = String.valueOf(endereco.getCep());
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void enderecoClear(Intent intent) {
        try {
            intent.removeExtra("LOGRADOURO");
            intent.removeExtra("NUMERO");
            intent.removeExtra("COMPLEMENTO");
            intent.removeExtra("BAIRRO");
            intent.removeExtra("CIDADE");
            intent.removeExtra("UF");
            intent.removeExtra("CEP");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean enderecoComparator(Endereco end1, Endereco end2) {
        if (end1.getLogradouro().equals(end2.getLogradouro()) &&
            end1.getNumero() == end2.getNumero() &&
            end1.getComplemento().equals(end2.getComplemento()) &&
            end1.getBairro().equals(end2.getBairro()) &&
            end1.getCidade().equals(end2.getCidade()) &&
            end1.getUf().equals(end2.getUf()) &&
            end1.getCep() == end2.getCep()) {
            return true;
        }
        return false;
    }

    public static boolean enderecoComparator(Endereco endereco, String array[]) {
        if (array[0].equals(endereco.getLogradouro()) &&
            array[1].equals(String.valueOf(endereco.getNumero())) &&
            array[2].equals(endereco.getComplemento()) &&
            array[3].equals(endereco.getBairro()) &&
            array[4].equals(endereco.getCidade()) &&
            array[5].equals(endereco.getUf()) &&
            array[6].equals(String.valueOf(endereco.getCep()))) {
           return true;
        }
        return false;
    }

    public static void telefoneSort(ArrayAdapter <Telefone> telefones) {
        telefones.sort(new Comparator<Telefone>() {

            @Override
            public int compare(Telefone o1, Telefone o2) {
                 return o1.toString().compareTo(o2.toString());
            }
        });
    }

    public static void emailSort(ArrayAdapter <Email> emails) {
        emails.sort(new Comparator<Email>() {

            @Override
            public int compare(Email o1, Email o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    public static void enderecoSort(ArrayAdapter <Endereco> enderecos) {
        enderecos.sort(new Comparator<Endereco>() {

            @Override
            public int compare(Endereco o1, Endereco o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    public static void convenioSort(ArrayAdapter <Convenio> convenios) {
        convenios.sort(new Comparator<Convenio>() {

            @Override
            public int compare(Convenio o1, Convenio o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    public static void spinnerToArrayList(Spinner spinner, ArrayList<String> list) {
        for (int i = 0; i < spinner.getCount(); i++) {
            list.add(i, spinner.getItemAtPosition(i).toString());
        }
    }
    public static void arrayListToAdapter(ArrayList<String> list, ArrayAdapter<String> adapter) {
        adapter.clear();
        for (int i = 0; i < list.size(); i++) {
             adapter.add(list.get(i));
        }
        adapter.sort(new Comparator <String>() {

            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        adapter.notifyDataSetChanged();
    }

    public static void deleteData(Spinner sp_data, ArrayAdapter<String> ad_data, ImageButton ib_update, ImageButton ib_delete) {
        ad_data.remove(sp_data.getSelectedItem().toString());
        ad_data.notifyDataSetChanged();
        resetImages(ad_data.getCount(), ib_update, ib_delete);
    }

    public static boolean updateData(Activity activity, String str, Spinner sp_data, ArrayAdapter<String> ad_data, String field, String found) {
        if (ad_data.getPosition(str) == -1) {      // Não existe.
            ad_data.remove(sp_data.getSelectedItem().toString());
            ad_data.add(str);
            return true;
        }
        if (!sp_data.getSelectedItem().toString().equals(str)) {
            field = field.substring(0, field.length() - 1) + " ";
            Toast.makeText(activity, field + found, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static boolean insertData(Activity activity, String str, ArrayAdapter<String> ad_data, ImageButton ib_update, ImageButton ib_delete, String field, String found, String empty) {
        if (!str.trim().isEmpty()) {
            if (ad_data.getPosition(str) == -1) { // Não existe.
                ad_data.add(str);
                resetImages(ad_data.getCount(), ib_update, ib_delete);
                return true;
            }
            field = field.substring(0, field.length() -1) + " ";
            Toast.makeText(activity, field + found, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, empty, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static void deleteEndereco(Spinner sp_endereco, ArrayAdapter<String> ad_endereco, ArrayAdapter<Endereco> enderecos, ImageButton ib_update_endereco, ImageButton ib_delete_endereco) {
        Endereco endereco = enderecos.getItem(sp_endereco.getSelectedItemPosition());
        enderecos.remove(endereco);
        ad_endereco.remove(sp_endereco.getSelectedItem().toString());
        ad_endereco.notifyDataSetChanged();
        resetImages(ad_endereco.getCount(), ib_update_endereco, ib_delete_endereco);
    }

    public static boolean updateEndereco(Activity activity, Intent intent, Spinner sp_endereco, ArrayAdapter<String> ad_endereco, ArrayAdapter<Endereco> enderecos, String field, String found, String[] vec) {
        String   text;
        String[] array = {"", "", "", "", "", "", ""};
        Endereco endereco;
        enderecoLoad(intent, array);
        if (!enderecoIsEmpty(array)) {
            endereco = enderecos.getItem(sp_endereco.getSelectedItemPosition());
            if (!enderecoExists(enderecos, array)) {  // Não existe;
                text = enderecoReduce(array);
                enderecoUpdate(endereco, array);
                ad_endereco.remove(sp_endereco.getSelectedItem().toString());
                ad_endereco.add(text);
                vec[0] = text;
                return true;
            }
            if (!enderecoComparator(enderecos.getItem(sp_endereco.getSelectedItemPosition()), array)) {
                field = field.substring(0, field.length() - 1) + " ";
                Toast.makeText(activity, field + found, Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    public static boolean insertEndereco(Activity activity, Intent intent, ArrayAdapter <String> ad_endereco, ArrayAdapter <Endereco> enderecos, ImageButton ib_update_endereco, ImageButton ib_delete_endereco, String field, String found, String[] vec) {
        String   text;
        String[] array = {"", "", "", "", "", "", ""};
        enderecoLoad(intent, array);
        if (!enderecoIsEmpty(array)) {
            if (!enderecoExists(enderecos, array)) {  // Não existe;
                Endereco result = enderecoInsert(array);
                text = enderecoReduce(result);
                enderecos.add(result);
                ad_endereco.add(text);
                resetImages(ad_endereco.getCount(), ib_update_endereco, ib_delete_endereco);
                vec[0] = text;
                return true;
            }
            field = field.substring(0, field.length() -1) + " ";
            Toast.makeText(activity, field + found, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static String ZeroFill(String s) {
        if (s.length() == 1)
            return "0" + s;
        return s;
    }

    public static void clearAllRadioButtons(RadioGroup radio_group) {
        RadioButton rb1, rb2;
        for (int i = 0 ; i < radio_group.getChildCount(); i++) {
            rb1 = (RadioButton) radio_group.getChildAt(i);
            rb1.setChecked(false);
            rb2 = (RadioButton) rb1.getTag();
            rb2.setChecked(false);
        }
    }

    public static RadioButton getRadioButton(RadioGroup radio_group, RadioButton rb) {
        RadioButton result;
        for (int i = 0 ; i < radio_group.getChildCount(); i++) {
            result = (RadioButton) radio_group.getChildAt(i);
            if (result.getTag().equals(rb)) {
                return result;
            }
        }
        return null;
    }

    public static void setRadioButton(String text, RadioGroup radio_group) {
        RadioButton rb1, rb2;
        for (int i = 0 ; i < radio_group.getChildCount(); i++) {
            rb1 = (RadioButton) radio_group.getChildAt(i);
            rb2 = (RadioButton) rb1.getTag();
            TextView tv = (TextView) rb2.getTag();
            String str = tv.getText().toString();
            if (str.equals(text)) {
                rb1.setChecked(true);
                rb2.setChecked(true);
            }
        }
    }

    public static ProgressDialog createProgressDialog(Context context, String message, int max) {
        ProgressDialog status = new ProgressDialog(context);
        status.setMessage(message);
        status.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        status.setMax(max);
        status.setProgress(0);
        status.setCancelable(true);
        return status;
    }

    public static String exportContacts(final Activity activity, String grupo, String nome, String[] tels, String[] mails, ArrayAdapter<Endereco> enderecos) {
        Contact contact = new Contact(activity, grupo);
        if (contact.mayRequestContacts(activity)) {
            return contact.createContact(nome, tels, mails, enderecos);
        }
        return null;
    }

    // TODO https://developer.android.com/guide/components/intents-common#SendMessage - 01/02/2019

    /*
    public void composeMmsMessage(String message, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", message);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    */

    public static boolean sendSMS(final String phoneNumber, final String message) {
        // Log.w("STRING: ", message);
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> ar = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(phoneNumber, null, ar, null, null);
            return true;
        } catch (Exception e) {
            Log.w("MESSAGE: ", e.getMessage());
            return false;
        }
    }
    /*

    // UPDATE - 02/02/2019

    public static void sendSMS(final Activity activity, final String phoneNumber, final String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("sms:" + phoneNumber));
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }
    */

    // UPDATE - 01/02/2019

    /*
    public static void sendSMS(final Context context, final String phoneNumber, final String message) {
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(Receiver.SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(Receiver.DELIVERED), 0);

        final Receiver receiver = new Receiver();

        try {
             context.registerReceiver(receiver, new IntentFilter(Receiver.SENT));
             context.registerReceiver(receiver, new IntentFilter(Receiver.DELIVERED));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(message);

        ArrayList<PendingIntent> sendList = new ArrayList<>();
        sendList.add(sentPI);

        ArrayList<PendingIntent> deliverList = new ArrayList<>();
        deliverList.add(deliveredPI);

        sms.sendMultipartTextMessage(phoneNumber, null, parts, sendList, deliverList);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    context.unregisterReceiver(receiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10000);
    }
    */
}
