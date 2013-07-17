package com.example.alram2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.example.listview.ListActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AdresslistActivity extends ListActivity 
{
    private ListView lv;
    private String phone;
    private int phone2;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adresslist);
         
        lv = (ListView)findViewById(R.id.listView_adress);
         
        ArrayList<Person> m_orders = new ArrayList<Person>();
        
        Map<String, String> phone_address = ContactUtil.getAddressBook(this);
         
        @SuppressWarnings("rawtypes")
        Iterator ite = phone_address.keySet().iterator();
        while(ite.hasNext())
        {
            phone = ite.next().toString();
            String name = phone_address.get(phone).toString();
            m_orders.add(new Person(name, phone));
        }
          
        PersonAdapter m_adapter = new PersonAdapter(this, R.layout.view_fiend_list, m_orders);
        lv.setAdapter(m_adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowID) 
            {
                doSelectFriend((Person)parent.getItemAtPosition(position));
                Person person = (Person)parent.getItemAtPosition(position);
                
        		SharedPreferences pref = getSharedPreferences("setting", MODE_PRIVATE);
        		Editor edit = pref.edit();
        		edit.putString("phone", person.getNumber());
        		edit.commit();
                finish();
            }});
    }
     

    public void doSelectFriend(Person p)
    {
        Log.e("####", p.getName() + ", " + p.getNumber());
    }
     
    private class PersonAdapter extends ArrayAdapter<Person> 
    {
        private ArrayList<Person> items;
 
        public PersonAdapter(Context context, int textViewResourceId, ArrayList<Person> items) 
        {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
            View v = convertView;
            if (v == null) 
            {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.view_fiend_list, null);
            }
            Person p = items.get(position);
            if (p != null) 
            {
                    TextView tt = (TextView) v.findViewById(R.id.name);
                    TextView bt = (TextView) v.findViewById(R.id.msg);
                    if (tt != null)
                    {
                        tt.setText(p.getName());                            
                    }
                    if(bt != null)
                    {
                        bt.setText("전화번호: "+ p.getNumber());
                    }
            }
            return v;
        }
    }
     
    class Person 
    {
        private String Name;
        private String Number;
         
        public Person(String _Name, String _Number)
        {
            this.Name = _Name;
            this.Number = _Number;
        }
         
        public String getName() 
        {
            return Name;
        }
 
        public String getNumber() 
        {
            return Number;
        }
    }
}