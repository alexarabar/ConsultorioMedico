package br.com.alexandrebarboza.consultoriomedico.Utility;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import br.com.alexandrebarboza.consultoriomedico.R;

/**
 * Created by Alexandre on 29/03/2017.
 */

public class Receiver extends BroadcastReceiver {
    public static final String SENT = "SMS_SENT";
    public static final String DELIVERED = "SMS_DELIVERED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SENT)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, context.getResources().getString(R.string.str_sms_send), Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, context.getResources().getString(R.string.str_sms_fail), Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(context, context.getResources().getString((R.string.str_sms_no_serv)), Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(context, context.getResources().getString(R.string.str_sms_no_pdu), Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(context, context.getResources().getString(R.string.str_sms_off), Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (intent.getAction().equals(DELIVERED)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, context.getResources().getString(R.string.str_sms_ok), Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, context.getResources().getString(R.string.str_sms_error), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
