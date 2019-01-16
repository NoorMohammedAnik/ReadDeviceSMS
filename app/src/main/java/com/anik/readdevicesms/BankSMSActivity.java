package com.anik.readdevicesms;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BankSMSActivity extends AppCompatActivity {

    private static BankSMSActivity inst;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;

    Button btnRetrieveData;
    public static BankSMSActivity instance() {
        return inst;
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        inst = this;
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_sms);

        smsListView =  findViewById(R.id.SMSList2);
        btnRetrieveData=findViewById(R.id.btn_data);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        smsListView.setAdapter(arrayAdapter);


        // Add SMS Read Permision At Runtime
        // Todo : If Permission Is Not GRANTED
        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

            // Todo : If Permission Granted Then Show SMS
            refreshSmsInbox();

        } else {
            // Todo : Then Set Permission
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(BankSMSActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }


//        btnRetrieveData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                refreshSmsInboxFull();
//            }
//        });


        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                try {
                    String[] smsMessages = smsMessagesList.get(pos).split("\n");
                    String address = smsMessages[0];
                    String smsMessage = "";
                    for (int i = 1; i < smsMessages.length; ++i) {
                        smsMessage += smsMessages[i];
                    }

                    String smsMessageStr = address + "\n";
                    smsMessageStr += smsMessage;
                    Toast.makeText(BankSMSActivity.this, smsMessageStr, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }
    public void refreshSmsInboxFull() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int date = smsInboxCursor.getColumnIndex("date");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String number=smsInboxCursor.getString(indexAddress);
            String getDate=millisToDate(date);

//                String FormattedDate;
//
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",Locale.US);
//                FormattedDate = sdf.format(getDate);

                /*
                1. Check specific number is matched or not

                2. If need to show all sms removed condition
                 */
            if(number.equals("+8801680709603")) {

                String info =smsInboxCursor.getString(indexBody);
                Log.d("SMS",info);



                arrayAdapter.add(info);
            }

        } while (smsInboxCursor.moveToNext());
    }


    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int date = smsInboxCursor.getColumnIndex("date");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String number=smsInboxCursor.getString(indexAddress);
            String getDate=millisToDate(date);

//                String FormattedDate;
//
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",Locale.US);
//                FormattedDate = sdf.format(getDate);

                /*
                1. Check specific number is matched or not

                2. If need to show all sms removed condition
                +8801680709603
                 */
            if(number.equals("+8801680709603")) {

                String info =smsInboxCursor.getString(indexBody);
                Log.d("SMS",info);


                //count per word-->> to next word BDT
                String value2[]=info.split(" ")[3].split("BDT");
                // System.out.println(value2[0]);
                //System.out.println(value2[1]);

                String value3[]=info.split(" ")[11].split("BDT");
                //System.out.println(value3[0]);

                String str="Withdraw Amount Tk : "+value2[0]+"\nAvaiable Balance Tk : "+value3[0];


                arrayAdapter.add(str);
            }

        } while (smsInboxCursor.moveToNext());
    }



    public static String millisToDate(long currentTime) {
        String finalDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        Date date = calendar.getTime();
        finalDate = date.toString();
        return finalDate;
    }

    public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }



}