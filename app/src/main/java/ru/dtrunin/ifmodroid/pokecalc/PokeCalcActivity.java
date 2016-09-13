package ru.dtrunin.ifmodroid.pokecalc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;

public class PokeCalcActivity extends AppCompatActivity {

    ImageView pokemonImageView;
    Spinner pokemonSpinnerView;
    EditText powerUpCostView;
    EditText cpView;
    EditText hpView;
    Button doCalcButton;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_calc);

        pokemonImageView = (ImageView) findViewById(R.id.pokemon_img);
        pokemonSpinnerView = (Spinner) findViewById(R.id.pokemon_spinner);
        powerUpCostView = (EditText) findViewById(R.id.powerup_cost_text);
        cpView = (EditText) findViewById(R.id.cp_text);
        hpView = (EditText) findViewById(R.id.hp_text);
        doCalcButton = (Button) findViewById(R.id.do_calc_btn);
        scrollView = (ScrollView) findViewById(R.id.scrol_view);
    }

}
