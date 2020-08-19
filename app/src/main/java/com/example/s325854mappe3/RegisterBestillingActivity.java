package com.example.s325854mappe3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterBestillingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    ConstraintLayout oc;
    EditText hourFra;
    EditText minuteFra;
    EditText hourTil;
    EditText minuteTil;
    TextView datoFra;
    TextView datoTil;
    MaterialButton btn;

    Rombestilling rombestilling;
    Calendar calendarFra;
    Calendar calendarTil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerbestilling);

        getSupportFragmentManager().beginTransaction().replace(
                R.id.order_reg_fragment_container,
                new ToolbarFragment("Fyll Inn Bestilling Info")
        ).commit();

        oc = (ConstraintLayout) findViewById(R.id.order_constraint);

        ////////View input felt fra
        datoFra = (TextView) findViewById(R.id.reg_order_date_fra);

        datoFra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DateFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        hourFra = (EditText) findViewById(R.id.order_hour_fra);
        minuteFra = (EditText) findViewById(R.id.order_minute_fra);

        ///////view inputfelt til

        hourTil = (EditText) findViewById(R.id.order_hour_til);
        minuteTil = (EditText) findViewById(R.id.order_minute_til);

        btn = (MaterialButton) findViewById(R.id.input_bestilling_add_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addBestilling()) {
                    Toast.makeText(getBaseContext(), "Ny Rombestilling lagret!",

                            Toast.LENGTH_LONG).show();

                    int resultCode = 200;
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Rombestilling", rombestilling);
                    setResult(resultCode, resultIntent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Fyll ut alle felt",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public boolean addBestilling() {
        Log.d("tag", "addBestilling: "+datoFra.getText());
        if
        (
                TextUtils.isEmpty(hourFra.getText()) ||
                TextUtils.isEmpty(minuteFra.getText()) ||
                TextUtils.isEmpty(hourTil.getText()) ||
                TextUtils.isEmpty(minuteTil.getText()) ||
                calendarFra == null

        )
        {
            return false;
        }
        calendarFra.set(Calendar.HOUR_OF_DAY,Integer.valueOf(hourFra.getText().toString()));
        calendarFra.set(Calendar.MINUTE,Integer.valueOf(minuteFra.getText().toString()));
        rombestilling = (Rombestilling) getIntent().getSerializableExtra("Rombestilling");
        Log.d("tag", "addBestilling: calendatfra after "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendarFra.getTime()));
        rombestilling.fra = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendarFra.getTime());

        Calendar calendarTil = calendarFra;
        calendarTil.add(Calendar.HOUR_OF_DAY,Integer.valueOf(hourTil.getText().toString()));
        calendarTil.add(Calendar.MINUTE,Integer.valueOf(minuteTil.getText().toString()));
        Log.d("tag", "addBestilling: calendar til after "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendarTil.getTime()));
        rombestilling.til = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendarFra.getTime());
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendarFra = Calendar.getInstance();
        calendarFra.set(Calendar.YEAR, year);
        calendarFra.set(Calendar.MONTH, month);
        calendarFra.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendarFra.getTime());
        datoFra.setText(currentDateString);
    }
}
