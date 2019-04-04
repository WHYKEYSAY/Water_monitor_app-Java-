package com.example.groupel.elecoen390_watermonitor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FilterListView extends AppCompatActivity {
  ListView list;
  String titles[] = {"Nitrate Removal | SMART Single Cartridge Countertop Water Filter System", "Nitrate Countertop Water Filter System","3MRO401", "AP-RO5500", "APUV8", "Cactus X-12"};
  int imgs[] = {R.drawable.nitrates1, R.drawable.nitrates2, R.drawable.cyanide1, R.drawable.cyanide2, R.drawable.organic1, R.drawable.organic2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list_view);

        list =findViewById(R.id.list1);

        MyAdapter adapter = new MyAdapter(this, titles, imgs);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    Intent intent = new Intent(FilterListView.this, Filter1.class);
                    startActivity(intent);
                }
                if (position==1){
                    Intent intent = new Intent(FilterListView.this, Filter2.class);
                    startActivity(intent);
                }
                if (position==2){
                    Intent intent = new Intent(FilterListView.this, Filter3.class);
                    startActivity(intent);
                }
                if (position==3){
                    Intent intent = new Intent(FilterListView.this, Filter4.class);
                    startActivity(intent);
                }
                if (position==4){
                    Intent intent = new Intent(FilterListView.this, Filter5.class);
                    startActivity(intent);
                }
                if (position==5){
                    Intent intent = new Intent(FilterListView.this, Filter6.class);
                    startActivity(intent);
                }
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String>{
     Context oontext;
     String myTitles[];
     int[] imgs;

     MyAdapter(Context c, String[] titles, int[] imgs){
         super(c,R.layout.row, R.id.text1, titles);
         this.oontext=c;
         this.imgs=imgs;
         this.myTitles=titles;
     }

        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater =(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images =row.findViewById(R.id.logo);
            TextView myTitle =row.findViewById(R.id.text1);
            images.setImageResource(imgs[position]);
            myTitle.setText(titles[position]);
            return row;
        }
    }



}
