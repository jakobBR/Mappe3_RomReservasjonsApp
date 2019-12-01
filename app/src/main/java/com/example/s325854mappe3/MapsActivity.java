package com.example.s325854mappe3;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker markerFromClick;
    private boolean markerFromClickExists;
    private List<Marker> romMarkers = new ArrayList<>();
    private List<Rom> romList = new ArrayList<>();
    MaterialButton addRom;
    MaterialButton addBestilling;
    ConstraintLayout parentLayout;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        parentLayout = (ConstraintLayout) findViewById(R.id.mapsConstraint);
        addRom =(MaterialButton) findViewById(R.id.rom_add_btn);
        addBestilling =(MaterialButton) findViewById(R.id.bestilling_add_btn);

        addRom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng pos = markerFromClick.getPosition();
                Intent romIntent = new Intent(view.getContext(), RegisterRomActivity.class);
                romIntent.putExtra("Rom",new Rom(String.valueOf(pos.latitude),String.valueOf(pos.longitude)));
                startActivityForResult(romIntent,100);
            }
        });
        addBestilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bestillingIntent = new Intent(view.getContext(), RegisterBestillingActivity.class);
                bestillingIntent.putExtra("Rombestilling",new Rombestilling());
                startActivityForResult(bestillingIntent,200);
            }
        });

        parentLayout.removeView(addRom);
        parentLayout.removeView(addBestilling);



    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        getJSON task = new getJSON();
        task.execute("http://student.cs.hioa.no/~s325854/jsonromout.php");
        Log.d("--","//// --- IN onMapReady()");
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // mapActionListerners
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("onMapReady", "onMarkerClick: ");
                marker.showInfoWindow();
                if (addRom.getParent() != null) parentLayout.removeView(addRom);
                displayAddBestilling();
                markerFromClick=marker;

                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (addBestilling.getParent() != null)parentLayout.removeView(addBestilling);
                if (markerFromClickExists){
                    markerFromClick.remove();
                    markerFromClickExists=false;
                    markerFromClick=null;
                    if (addRom.getParent() != null) parentLayout.removeView(addRom);
                } else {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    markerFromClick = mMap.addMarker(markerOptions);
                    markerFromClickExists = true;
                    markerFromClick.showInfoWindow();
                    displayAddRom();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100) {
            System.out.println("Roooooooooooooooooooooom");
        }

        if(requestCode == 200) {
            System.out.println("Roooooooooooooooooooooombestilling");
        }
    }


    private void displayAddRom() {
        if(addRom.getParent()==null){
            parentLayout.addView(addRom);
        }
    }

    private void displayAddBestilling() {
        if(addBestilling.getParent()==null){
            parentLayout.addView(addBestilling);
        }
    }

    private class getJSON extends AsyncTask<String, Void,String> {

        @Override
        protected String doInBackground(String... url) {
            String retur = "";
            String romData = "";
            String romBestillingData = "";

            try {
                romData = getRomData(url[0]);
               // romBestillingData = getRomData(url[1]);
                try {
                    createRomModels(romData);
                    return retur;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                return "Noe gikk feil";
            }
            return retur;
        }

        private String getRomData(String url) throws Exception {
            String line = "";
            String data="";
            URL urlen = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)
                    urlen.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept",
                    "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            System.out.println("Output from Server .... \n");
            while ((line = br.readLine()) != null) {
                data = data + line;
            }
            conn.disconnect();
            return data;
        }


        private List<Rom> createRomModels(String output) throws JSONException {
            System.out.println("---------------------- "+output);
            JSONArray mat = new JSONArray(output);
            for (int i = 0; i < mat.length(); i++) {
                JSONObject jsonobject = mat.getJSONObject(i);
                String navn = jsonobject.getString("navn");
                String beskrivelse = jsonobject.getString("beskrivelse");
                String latitude = jsonobject.getString("latitude");
                String longitude = jsonobject.getString("longitude");
                Rom rom = new Rom(navn,beskrivelse, latitude, longitude);
                romList.add(rom);
            }
            return romList;
        }

        @Override
        protected void onPostExecute(String ss) {
            Log.d("----","//// --- IN onPostExecute() romList size"+romList.size());
            for (int i = 0 ; i < romList.size(); i++ ){
                Rom r = romList.get(i);
                LatLng pos = new LatLng(Double. parseDouble(r.latitude),Double.parseDouble(r.longitude));
                romMarkers.add(mMap.addMarker(new MarkerOptions().position(pos).title(r.navn)));
            }
            //Setting camera zoom and position
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15)) ;
            LatLng cameraPos = new LatLng(Double.parseDouble(romList.get(0).latitude),Double.parseDouble(romList.get(0).longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPos));
            //new postData().execute("0");
            //System.out.println("--- Markers ---"+romMarkers.size());
        }
    }

    private class postData extends AsyncTask<String, Void,String> {

        @Override
        protected String doInBackground(String... url) {
            Log.d("----","//// --- IN postData()--- romList size"+romList.size());
            String retur = "";
            try {
                Rom r = romList.get(Integer.valueOf(url[0]));            System.out.println("romListe:  "+r.latitude);
                String params = "navn="+r.navn+"&beskrivelse="+r.beskrivelse+"&latitude="+r.latitude+"&longitude="+r.longitude;
                params = params.replaceAll(" ","%20");
                params = params.replaceAll("\n","");
                //String params = "Name=nytest2&col2=col3&dato=2019-11-26%2010:10:52";
                URL urlen = new URL("http://student.cs.hioa.no/~s325854/jsonromin.php/?"+params);
                HttpURLConnection conn = (HttpURLConnection)
                        urlen.openConnection();
                conn.setRequestMethod("POST");
                System.out.println("RESPONSE IN POST "+conn.getResponseCode());
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println(response.toString());
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return retur;
        }
        @Override
        protected void onPostExecute(String ss) {
            Log.d("----","//// --- IN postData onPostExecute()");
        }
    }
}

