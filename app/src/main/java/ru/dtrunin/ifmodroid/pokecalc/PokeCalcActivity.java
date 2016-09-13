package ru.dtrunin.ifmodroid.pokecalc;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class PokeCalcActivity extends AppCompatActivity implements
        Spinner.OnItemSelectedListener,
        View.OnClickListener {

    ImageView pokemonImageView;
    Spinner pokemonSpinnerView;
    EditText powerUpCostView;
    EditText cpView;
    EditText hpView;
    Button doCalcButton;
    ScrollView scrollView;
    TextView outputTextView;

    TypedArray pokemonImageIds;

    CharSequence outputText;

    /**
     * Key for saving/restoring the output text as CharSequence
     */
    private static final String KEY_OUTPUT_TEXT = "output_text";

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
        outputTextView = (TextView) findViewById(R.id.text_output);

        doCalcButton.setOnClickListener(this);
        pokemonSpinnerView.setOnItemSelectedListener(this);

        pokemonImageIds = getResources().obtainTypedArray(R.array.pokemon_images);

        if (savedInstanceState != null) {
            outputText = savedInstanceState.getCharSequence(KEY_OUTPUT_TEXT, null);
            if (outputText != null) {
                outputTextView.setVisibility(View.VISIBLE);
                outputTextView.setText(outputText);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        final int imageId = pokemonImageIds.getResourceId(pos, 0);
        pokemonImageView.setImageResource(imageId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        pokemonImageView.setImageResource(0);
    }

    @Override
    public void onClick(View v) {
        if (v == doCalcButton) {
            calculateIV();
        }
    }

    private void calculateIV() {
        final int pokemonIndex = pokemonSpinnerView.getSelectedItemPosition();

        final int baseStamina = getResources().getIntArray(R.array.pokemon_stamina)[pokemonIndex];
        final int baseAttack = getResources().getIntArray(R.array.pokemon_attack)[pokemonIndex];
        final int baseDefense = getResources().getIntArray(R.array.pokemon_defense)[pokemonIndex];

        final int cp = getInt(cpView, 0);
        final int hp = getInt(hpView, 0);
        final int powerUpCost = getInt(powerUpCostView, 0);

        final StringBuilder text = new StringBuilder();
        text.append("Base stamina: ").append(baseStamina).append('\n');
        text.append("Base attack: ").append(baseAttack).append('\n');
        text.append("Base defense: ").append(baseDefense).append('\n');
        text.append('\n');

        if (PokeMath.isAllowedPowerUpStardustCost(powerUpCost)) {
            int minLevel = PokeMath.getMinLevelForPowerUpStardustCost(powerUpCost);
            int maxLevel = PokeMath.getMaxLevelForPowerUpStardustCost(powerUpCost);

            text.append("Min possible level: ").append(minLevel).append('\n');
            text.append("Max possible level: ").append(maxLevel).append('\n');
            text.append('\n');

            text.append("Possible values of (level, attack, defense, stamina):").append('\n');
            text.append('\n');

            int totalCount = 0;

            for (int level = minLevel; level <= maxLevel; level++) {
                for (int ivStamina = 0; ivStamina <= 15; ivStamina++) {
                    final int stamina = baseStamina + ivStamina;
                    final int calculatedHp = PokeMath.getHp(level, stamina);


                    if (calculatedHp != hp) {
                        continue;
                    }

                    for (int ivAttack = 0; ivAttack <= 15; ivAttack++) {
                        final int attack = baseAttack + ivAttack;

                        for (int ivDefense = 0; ivDefense <= 15; ivDefense++) {
                            final int defense = baseDefense + ivDefense;
                            final int calculatedCp =
                                    PokeMath.getCp(level, stamina, attack, defense);

                            if (calculatedCp == cp) {
                                totalCount++;
                                text.append("(").append(level);
                                text.append(", ").append(ivAttack);
                                text.append(", ").append(ivDefense);
                                text.append(", ").append(ivStamina);
                                text.append(")\n");
                            }
                        }
                    }
                }
            }
            text.append("\nTotal: ").append(totalCount).append(" variants").append('\n');

        } else {
            text.append("Not allowed value of power up stardust cost: ")
                    .append(powerUpCost)
                    .append('\n');
        }

        this.outputText = text;

        outputTextView.setVisibility(View.VISIBLE);
        outputTextView.setText(text);
        outputTextView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, outputTextView.getTop()
                        + ((View) outputTextView.getParent()).getTop());

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(KEY_OUTPUT_TEXT, outputText);
    }

    private int getInt(TextView textView, int defaultValue) {
        final CharSequence textValue = textView.getText();

        try {
            return Integer.parseInt(textValue.toString());
        } catch (NumberFormatException e) {
            Log.w(TAG, "Failed to parse integer value: " + textValue);
        }
        return defaultValue;
    }

    private static final String TAG = "PokeCalc";

}
