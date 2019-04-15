package com.example.zachary.midtronicsassesment;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ResourceBundle;

public class CountryActivity extends AppCompatActivity implements CountryDetailsFragment.OnFragmentInteractionListener {

    ListView listView;
    Button returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_country);
        Resources res = getResources();
        String[] countries = res.getStringArray(R.array.countries_array);

        returnButton = (Button)findViewById(R.id.button3);


        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.listview,countries);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tView = (TextView)view;
                String text = tView.getText().toString();

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                String url = "https://restcountries.eu/rest/v1/name/" + text;

                StringRequest stringRequest = new StringRequest
                        (Request.Method.GET, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    String formattedString = response.substring(1, response.length() - 1);
                                    JSONObject jsonObject = new JSONObject(formattedString);
                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                                    CountryDetailsFragment f = new CountryDetailsFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", jsonObject.getString("name"));
                                    bundle.putString("capital", jsonObject.getString("capital"));
                                    bundle.putString("population", jsonObject.getString("population"));
                                    bundle.putString("area", jsonObject.getString("area"));
                                    bundle.putString("region", jsonObject.getString("region"));
                                    bundle.putString("subregion", jsonObject.getString("subregion"));
                                    f.setArguments(bundle);

                                    ft.replace(R.id.your_placeholder, f);
                                    ft.addToBackStack(null);

                                    ft.commit();
                                    listView.setVisibility(View.INVISIBLE);
                                    returnButton.setVisibility(View.INVISIBLE);}
                                catch(JSONException e)
                                {

                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error

                            }
                        });

                requestQueue.add(stringRequest);

            }
        });
    }

    @Override
    public void onFragmentInteraction() {
        this.getSupportFragmentManager().popBackStackImmediate();
        listView.setVisibility(View.VISIBLE);
        returnButton.setVisibility(View.VISIBLE);

    }

    public void returnMain(View v)
    {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
    }
}
