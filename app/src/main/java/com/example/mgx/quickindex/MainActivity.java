package com.example.mgx.quickindex;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private QuickIndexBar mQib;
    private ListView mListViewv;
    private ArrayList<PersonUtils> mList = new ArrayList<>();
    private PersonAdapter mPersonAdapter;
    private TextView mToast;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListViewv = (ListView) findViewById(R.id.lv);
        mToast = (TextView) findViewById(R.id.tv_toast);

        mQib = (QuickIndexBar) findViewById(R.id.qib);
        for (int i = 0; i < ResUtils.NAMES.length; i++) {
            PersonUtils personUtils = new PersonUtils();
            personUtils.personName = ResUtils.NAMES[i];
            personUtils.firstName = PinyinUtils.getPinyin(ResUtils.NAMES[i]).substring(0,1);
            mList.add(personUtils);
        }
        Collections.sort(mList, new Comparator<PersonUtils>() {
            @Override
            public int compare(PersonUtils lhs, PersonUtils rhs) {
                return lhs.firstName.compareTo(rhs.firstName);
            }
        });
        mPersonAdapter = new PersonAdapter();
        mListViewv.setAdapter(mPersonAdapter);

        mQib.setOnLetterIndexListener(new QuickIndexBar.onLetterIndexListener() {
            @Override
            public void onLetterIndex(String letter) {
                mToast.setText(letter);
                mToast.setVisibility(View.VISIBLE);
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mToast.setVisibility(View.GONE);
                    }
                },1000);
                for (int i = 0; i<mList.size(); i++) {
                    PersonUtils personUtils = mList.get(i);
                    if (personUtils.firstName.equals(letter)) {
                        mListViewv.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    public class PersonAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView ==null) {
                convertView = View.inflate(getApplicationContext(),R.layout.item_person,null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.tv_name);
            TextView index = (TextView) convertView.findViewById(R.id.tv_index);

            PersonUtils personUtils = mList.get(position);
            if (position == 0 || !personUtils.firstName.equals(mList.get(position-1).firstName)){
                index.setText(personUtils.firstName);
                index.setVisibility(View.VISIBLE);
            }else {
                index.setVisibility(View.GONE);
            }
            name.setText(personUtils.personName);

            return convertView;
        }
    }
}
