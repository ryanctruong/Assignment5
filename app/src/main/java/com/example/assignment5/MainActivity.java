package com.example.assignment5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    TextView tvPokeNum, tvPokeWei, tvPokeHei, tvPokeBXP, tvPokeAbility,pokeName, pokeNum, pokeWei,
    pokeHei,pokeBaseXP, pokeAbility;

    Button searchBut;
    EditText searchB;
    ImageView pokemonImg;
    ListView lv;

    private final View.OnClickListener localListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            TextView tickerTextView = (TextView) findViewById(R.id.searchBar);
            String ticker = tickerTextView.getText().toString();
            getInfo(ticker);
        }
    };

    FirebaseDatabase fb;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        fb = FirebaseDatabase.getInstance();
        db = fb.getReference();

        tvPokeNum = findViewById(R.id.tv_pokeNum);
        tvPokeWei = findViewById(R.id.tv_pokeWei);
        tvPokeHei = findViewById(R.id.tv_pokeHei);
        tvPokeBXP = findViewById(R.id.tv_pokeBXP);
        tvPokeAbility = findViewById(R.id.tv_pokeAbility);

        pokeName = findViewById(R.id.pokemonName);
        pokeNum = findViewById(R.id.pokeNumber);
        pokeWei = findViewById(R.id.pokeWeight);
        pokeHei = findViewById(R.id.pokeHeight);
        pokeBaseXP = findViewById(R.id.pokeBaseXP);
        pokeAbility = findViewById(R.id.pokeAbility);

        searchBut = findViewById(R.id.searchButton);
        searchB = findViewById(R.id.searchBar);
        pokemonImg = findViewById(R.id.pokemonImage);
        lv = findViewById(R.id.listView);

        searchBut.setOnClickListener(localListener);

    }

    private void getInfo(String ticker){
        ANRequest req = AndroidNetworking.get("https://pokeapi.co/api/v2/pokemon/{ticker}")
                .addPathParameter("ticker", ticker)
                .setPriority(Priority.LOW)
                .build();

        req.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    String imageUrl = response.getString("id");
                    tvPokeNum.setText(imageUrl);
                    String image_url = response.getJSONObject("sprites").
                            getJSONObject("other").getJSONObject("official-artwork").getString("front_default");
                    Picasso.get().load(image_url).into(pokemonImg);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(getApplicationContext(),"Error on getting data ", Toast.LENGTH_LONG).show();
            }
        });
    }
}