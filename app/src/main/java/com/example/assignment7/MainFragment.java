package com.example.assignment7;


import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(FetchWeatherService.ACTION_RETRIEVE_WEATHER_DATA)) {
                String[] data = intent.getStringArrayExtra(FetchWeatherService.EXTRA_WEATHER_DATA);
                mForecastAdapter.clear();
                for(String dayForecastStr : data) {
                    mForecastAdapter.add(dayForecastStr);
                }
            }
        }
    };

    public MainFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);
    }

    @Override
    public boolean onOptionItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refreshWeatherData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void refreshWeatherData() {
        Intent intent = new Intent(getActivity(), FetchWeatherService.class);
        intent.setAction(FetchWeatherService.ACTION_RETRIEVE_WEATHER_DATA);
        getActivity().startService(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] data = {
                "Mon 6/23 - Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 6/29 - Sunny - 20/7"
        };

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));

        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_forecast,
                        R.id.list_item_forecast_textview,
                        weekForecast);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String forecast = mForecastAdapter.getItem(position);
                Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("data", forecast);
            }
        });
        IntentFilter intentFilter = new IntentFilter(FetchWeatherService.ACTION_RETRIEVE_WEATHER_DATA);
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(mBroadcastReceiver);
        super.onDestroyView();
    }
}