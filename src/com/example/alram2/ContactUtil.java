package com.example.alram2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
 
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
 
public class ContactUtil {
 
    public static String myPhoneNumber(Context context, boolean isIDD)
    {
        TelephonyManager phone = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (isIDD)
            return getIDD(phone.getLine1Number());
        else
            return phone.getLine1Number();
    }
    /**
     * �� ��ȭ��ȣ ��������
     * @param context
     * @return ��ȭ��ȣ
     */
    public static String myPhoneNumber(Context context)
    {
        return myPhoneNumber(context, false);
    }
     
      
    /**
     * �ּҷϿ� �ִ� ��ȭ��ȣ ��� ��������
     * @param context
     * @param isIDD ������ȭ �԰� ��� ����
     * @return �ּҷ��� ��ȭ��ȣ
     */
    public static ArrayList<String> contactPhoneNumber(Context context, boolean isIDD)
    {
        ArrayList<String> phone = new ArrayList<String>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext()) 
        {
            int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String s = cursor.getString(index);
             
            if (isIDD)
                phone.add(getIDD(s));
            else
                phone.add(s);
        }
         
        return phone;
    }
    /**
     * �ּҷϿ� �ִ� ��ȭ��ȣ ��� ��������
     * @param context
     * @return �ּҷ��� ��ȭ��ȣ
     */
    public static ArrayList<String> contactPhoneNumber(Context context)
    {
        return contactPhoneNumber(context, false);
    }
 
     
    /**
     * �ּҷ��� �̸�, ��ȭ��ȣ ���� �����´�
     * @param context
     * @param isIDD ������ȭ �԰� ��� ����
     * @return �̸�, ��ȭ��ȣ map
     */
    public static Map<String, String> getAddressBook(Context context, boolean isIDD)
    {
        Map<String, String> result = new HashMap<String, String>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext()) 
        {
            int phone_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int name_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String phone = cursor.getString(phone_idx);
            String name = cursor.getString(name_idx);
             
            if (isIDD)
                result.put(getIDD(phone), name);
            else
                result.put(phone, name);
             
//          Log.e("####getAddressBook", name + " : "+phone);
        }
         
        return result;
    }
    /**
     * �ּҷ��� �̸�, ��ȭ��ȣ ���� �����´�
     * @param context
     * @return �̸�, ��ȭ��ȣ map
     */
    public static Map<String, String> getAddressBook(Context context)
    {
        return getAddressBook(context, false);
    }
     
    /**
     * ������ȭ ������� �����Ѵ�.
     * @param phone
     * @return ������ȭ��ȣ �԰�
     */
    public static String getIDD(String phone)
    {
        String result = phone;
         
        try {
            result = "82" + Long.parseLong(phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}