package com.example.s325854mappe3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;

public class RegisterRomActivity extends AppCompatActivity {


    ConstraintLayout oc;
    EditText romNavn;
    EditText romBeskrivelse;
    Rom rom;

    MaterialButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerrom);

        getSupportFragmentManager().beginTransaction().replace(
                R.id.rom_reg_fragment_container,
                new ToolbarFragment("Fyll Inn Rom Info")
        ).commit();
        /*
        orderTimeInn = (EditText) findViewById(R.id.orderTimeInn);
        restaurantInn =  findViewById(R.id.restaurantInn);
        */
        oc = (ConstraintLayout) findViewById(R.id.rom_constraint);

        btn = (MaterialButton) findViewById(R.id.input_rom_add_btn);

        romNavn = (EditText) findViewById(R.id.rom_inn_name);
        romBeskrivelse = (EditText) findViewById(R.id.rom_inn_beskrivelse);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addRom()) {
                    Toast.makeText(getBaseContext(), "Nytt Rom Registrert!",
                            Toast.LENGTH_LONG).show();
                    int resultCode = 100;
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Rom", rom);
                    setResult(resultCode, resultIntent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Fyll ut alle felt",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean addRom(){
        if (TextUtils.isEmpty(romNavn.getText()) || TextUtils.isEmpty(romBeskrivelse.getText())) {
            return false;
        }
        rom = (Rom) getIntent().getSerializableExtra("Rom");
        Log.d("tag", "addrom:   "+rom.getLatitude()+rom.getLongitude());

        rom.setNavn(romNavn.getText().toString());
        rom.setBeskrivelse(romBeskrivelse.getText().toString());
        return true;
    }








}
