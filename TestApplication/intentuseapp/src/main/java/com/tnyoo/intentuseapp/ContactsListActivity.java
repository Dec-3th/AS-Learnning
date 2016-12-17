package com.tnyoo.intentuseapp;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsListActivity extends ListActivity {

    private List<String> contacts = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_contacts_list);
        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        contacts = getAllContacts();

        // Create an array adapter for the list view, using the Articles headlines array
        setListAdapter(new ArrayAdapter<String>(this, layout, contacts));
    }


    public List<String> getAllContacts() {
        Toast.makeText(this, "获取所有联系人信息...", Toast.LENGTH_SHORT).show();

        List<String> contacts = new ArrayList<String>();

        Cursor cursor = this.getBaseContext().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int contactIdIntex = 0;
        int contactNameIndex = 0;
        String contactId;
        String contactName = "";
        String contactPhone = "";

        if (cursor.getCount() > 0) {
            contactIdIntex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            contactNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            contactId = cursor.getString(contactIdIntex);
            contactName = cursor.getString(contactNameIndex);
            contactPhone = "";

            //由于一个联系人可能会有多个PhoneNumber，故再查找一次该联系人的phone信息
            Cursor phones = this.getBaseContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null,
                    null);

            int phoneIndex = 0;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }

            while (phones.moveToNext()) {
                if (!phones.isLast())
                    contactPhone += phones.getString(phoneIndex) + ", ";
                else
                    contactPhone += phones.getString(phoneIndex);
            }
            contacts.add(contactName + ":\n\t\t" +contactPhone);

//            /*
//             * 查找该联系人的email信息
//             */
//            Cursor emails = this.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
//                    null, null);
//            int emailIndex = 0;
//            if(emails.getCount() > 0) {
//                emailIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
//            }
//            while(emails.moveToNext()) {
//                String email = emails.getString(emailIndex);
//                Log.i(TAG, email);
//            }
        }
        return contacts;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //通知容器activity有条目被选中，与activity进行交互。
//        mheadlineSelectListener.onArticleSelected(position);
        Toast.makeText(this, "选中第" + position + "个电话号码.", Toast.LENGTH_SHORT).show();

        //高亮选中条目
        getListView().setItemChecked(position, true);
    }
}
