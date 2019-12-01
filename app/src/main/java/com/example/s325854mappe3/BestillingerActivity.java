package com.example.s325854mappe3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class BestillingerActivity extends AppCompatActivity{

    ArrayList<Rombestilling> bestillinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestillinger);
        bestillinger = (ArrayList<Rombestilling>) getIntent().getSerializableExtra("Arraylist");
        System.out.println("size "+bestillinger.size());
        System.out.println(bestillinger.get(0).getFra());

        ListView listView = (ListView) findViewById(R.id.listview);

        CustomAdapter customAdapter = new CustomAdapter();

        listView.setAdapter(customAdapter);

    }

    class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return bestillinger.size();
        }

        @Override
        public Object getItem(int position) {
            return bestillinger.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            view = getLayoutInflater().inflate(R.layout.customlayout,null);

            TextView fra = (TextView)view.findViewById(R.id.textViewfra);
            TextView til = (TextView)view.findViewById(R.id.textViewtil);



            System.out.println("Position "+position);
            System.out.println(bestillinger.get(position).getFra());
            fra.setText("Bestilling Fra: "+bestillinger.get(position).getFra());
            til.setText("Til: "+bestillinger.get(position).getTil());


            return view;
        }
    }





}
