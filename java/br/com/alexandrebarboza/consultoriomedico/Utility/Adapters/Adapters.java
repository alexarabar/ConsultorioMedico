package br.com.alexandrebarboza.consultoriomedico.Utility.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Comparator;

/**
 * Created by Alexandre on 22/02/2017.
 */

public class Adapters {
    public static ArrayAdapter <String> GetSpinner(Context context, Spinner spinner) {
        ArrayAdapter <String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return adapter;
    }

    public static boolean DataFound(String data, ArrayAdapter adapter) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(data)) {
                return true;
            }
        }
        return false;
    }

    public static void adapterSort(ArrayAdapter <String> adapter) {
        adapter.sort(new Comparator <String> () {

            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
    }
}
