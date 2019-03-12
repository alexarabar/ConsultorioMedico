package br.com.alexandrebarboza.consultoriomedico.Utility;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import br.com.alexandrebarboza.consultoriomedico.Domain.Entity.Endereco;
import br.com.alexandrebarboza.consultoriomedico.R;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * Created by Alexandre on 31/03/2017.
 */

public class Contact {
    private static final int REQUEST_READ_CONTACTS = 0;
    private Context context;
    private String  GroupTitle;
    private boolean update;

    private String getGroupId() {
        String GroupId = ifGroup();
        if (GroupId == null) {
            ArrayList<ContentProviderOperation> opsGroup = new ArrayList<ContentProviderOperation>();
            opsGroup.add(ContentProviderOperation.newInsert(ContactsContract.Groups.CONTENT_URI)
                    .withValue(ContactsContract.Groups.TITLE, GroupTitle)
                    .withValue(ContactsContract.Groups.GROUP_VISIBLE, true)
                    .withValue(ContactsContract.Groups.ACCOUNT_NAME, GroupTitle)
                    .withValue(ContactsContract.Groups.ACCOUNT_TYPE, GroupTitle)
                    .build());
            try {
                 context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, opsGroup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ifGroup();
    }

    private String ifGroup() {
        String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
        String[] selectionArgs = { "0", "1" };
        Cursor cursor = context.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
        cursor.moveToFirst();
        int len = cursor.getCount();
        String GroupId = null;
        for (int i = 0; i < len; i++) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
            String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
            if (title.equals(GroupTitle)) {
                GroupId = id;
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return GroupId;
    }

    private void addContactToGroup(ArrayList<ContentProviderOperation> ops){
        String GroupId = getGroupId();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, GroupId)
                .build());
    }

    private String contactProvider(ArrayList<ContentProviderOperation> ops){
        String what;
        try{
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            if (update) {
                what = context.getResources().getString(R.string.str_contact_update);
            } else {
                what = context.getResources().getString(R.string.str_contact_add);
            }
        } catch (Exception e){
            e.printStackTrace();
            what = context.getResources().getString(R.string.str_contact_add_fail);
        }
        return what;
    }

    private void contactName(String name, ArrayList<ContentProviderOperation> ops) {
        Uri uri = ContactsContract.Data.CONTENT_URI;
        ContentProviderOperation.Builder builder;
        builder = ContentProviderOperation.newInsert(uri);
        builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
        ops.add(builder.build());
    }

    private void contactOrganization(String company, String title, ArrayList<ContentProviderOperation> ops){
        Uri uri = ContactsContract.Data.CONTENT_URI;
        ContentProviderOperation.Builder builder;
        builder = ContentProviderOperation.newInsert(uri);
        builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
        builder.withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company);
        builder.withValue(ContactsContract.CommonDataKinds.Organization.TITLE, title);
        ops.add(builder.build());
    }

    private void addContentValues(ArrayList<ContentValues> values, ArrayList<ContentProviderOperation> ops, String id){
        Uri uri = ContactsContract.Data.CONTENT_URI;
        ContentProviderOperation.Builder builder;
        for (int i = 0; i < values.size(); i++) {
            builder = ContentProviderOperation.newInsert(uri);
            builder.withValues(values.get(i));
            if (update) {
                builder.withValue(ContactsContract.Data.RAW_CONTACT_ID, Long.parseLong(id));
            } else {
                builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
            }
            ops.add(builder.build());
        }
    }

    private void setPhoneValues(String[] tels, ArrayList<ContentValues> arrayList) {
        if (tels != null) {
            for (int i = 0; i < tels.length; i++) {
                ContentValues tel_row = new ContentValues();
                tel_row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                tel_row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, tels[i]);
                tel_row.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                arrayList.add(tel_row);
            }
        }
    }

    private void setEmailValues(String[] mails, ArrayList<ContentValues> arrayList) {
        if (mails != null) {
            for (int i = 0; i < mails.length; i++) {
                ContentValues mail_row = new ContentValues();
                mail_row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                mail_row.put(ContactsContract.CommonDataKinds.Email.ADDRESS, mails[i]);
                mail_row.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                arrayList.add(mail_row);
            }
        }
    }

    private void setAddressValues(ArrayAdapter<Endereco> enderecos, ArrayList<ContentValues> arrayList) {
        if (enderecos != null) {
            for (int i = 0; i < enderecos.getCount(); i++) {
                String logradouro = enderecos.getItem(i).getLogradouro();
                String numero = String.valueOf(enderecos.getItem(i).getNumero());
                String complemento = enderecos.getItem(i).getComplemento();
                String bairro = enderecos.getItem(i).getBairro();
                String cep = String.valueOf(enderecos.getItem(i).getCep());
                String cidade = enderecos.getItem(i).getCidade();
                String uf = enderecos.getItem(i).getUf();
                ContentValues end_row = new ContentValues();
                end_row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
                String s = Build.VERSION.RELEASE;
                s = s.substring(0, 1);
                int v = 0;
                try {
                    v = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (v >= 7) {
                    String row = logradouro + ", " + numero + " ( " + complemento + ") - " + bairro + "\n CEP: " + cep + " " + cidade + " " + uf;
                    end_row.put(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, row);
                } else {
                    end_row.put(ContactsContract.CommonDataKinds.StructuredPostal.STREET, logradouro);
                    end_row.put(ContactsContract.CommonDataKinds.StructuredPostal.POBOX, numero);
                    end_row.put(ContactsContract.CommonDataKinds.StructuredPostal.LABEL, complemento);
                    end_row.put(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD, bairro);
                    end_row.put(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, cep);
                    end_row.put(ContactsContract.CommonDataKinds.StructuredPostal.CITY, cidade);
                    end_row.put(ContactsContract.CommonDataKinds.StructuredPostal.REGION, uf);
                    end_row.put(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, context.getResources().getString(R.string.str_br));
                }
                end_row.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK);
                arrayList.add(end_row);
            }
        }
    }

    private void clearAllPhones(ArrayList<ContentProviderOperation> ops, String id) {
        long contactId = Long.parseLong(id);
        String selectPhone = ContactsContract.Data.RAW_CONTACT_ID + " =? AND " + ContactsContract.Data.MIMETYPE + " = '"  + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";
        String[] phoneArgs = new String[]{String.valueOf(contactId)};
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI).withSelection(selectPhone, phoneArgs).build());
    }

    private void clearAllEmails(ArrayList<ContentProviderOperation> ops, String id) {
        long contactId = Long.parseLong(id);
        String selectEmail = ContactsContract.Data.RAW_CONTACT_ID + " =? AND " + ContactsContract.Data.MIMETYPE + " = '"  + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'";
        String[] emailArgs = new String[]{String.valueOf(contactId)};
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI).withSelection(selectEmail, emailArgs).build());
    }

    private void clearAllAddresses(ArrayList<ContentProviderOperation> ops, String id) {
        long contactId = Long.parseLong(id);
        String selectAddress = ContactsContract.Data.RAW_CONTACT_ID + " =? AND " + ContactsContract.Data.MIMETYPE + " = '"  + ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE + "'";
        String[] addressArgs = new String[]{String.valueOf(contactId)};
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI).withSelection(selectAddress, addressArgs).build());
    }

    private String foundContact(String name) {
        String result = "";
        String where = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " = ?" ;
        String[] params = new String[] {ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, name};
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, params, ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME);
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID));
        }
        cursor.close();
        return result;
    }

    public Contact(Context context, String GroupTitle){
        this.context    = context;
        this.GroupTitle = GroupTitle;
    }

    public boolean mayRequestContacts(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (context.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        activity.requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        return false;
    }

    public String createContact(String name, String[] phones, String[] emails, ArrayAdapter<Endereco> addresses){
        ArrayList<ContentProviderOperation> ops =  new ArrayList<ContentProviderOperation>();
        ContentProviderOperation.Builder builder;
        Uri uri = ContactsContract.RawContacts.CONTENT_URI;
        String id = foundContact(name);
        if (id.isEmpty()) {
            builder = ContentProviderOperation.newInsert(uri);
            update = false;
        } else {
            builder = ContentProviderOperation.newUpdate(uri);
             update = true;
        }
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null);
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
        if (update) {
            String select = ContactsContract.RawContacts.CONTACT_ID + " = ? ";
            String[] params = new String[] { id };
            builder.withSelection(select, params);
        }
        ops.add(builder.build());
        if (update) {
            clearAllPhones(ops, id);
            clearAllEmails(ops, id);
            clearAllAddresses(ops, id);
        } else {
            addContactToGroup(ops);
            contactName(name, ops);
            contactOrganization(context.getResources().getString(R.string.app_name), GroupTitle.substring(0, GroupTitle.length() - 1), ops);
        }
        ArrayList<ContentValues> data = new ArrayList<ContentValues>();
        setPhoneValues(phones, data);
        setEmailValues(emails, data);
        setAddressValues(addresses, data);
        addContentValues(data, ops, id);
        return contactProvider(ops);
    }
}
