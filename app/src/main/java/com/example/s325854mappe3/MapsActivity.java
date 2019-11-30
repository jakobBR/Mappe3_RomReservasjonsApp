package com.example.s325854mappe3;

import androidx.fragment.app.FragmentActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
                //TODO
                // display legg til rombestilling knapp
                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (markerFromClickExists){
                    markerFromClick.remove();
                }
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
                // Setting the position for the marker
                markerOptions.position(latLng);
                // Setting the title for the marker.
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                // Placing a marker on the touched position
                markerFromClick = mMap.addMarker(markerOptions);
                markerFromClickExists=true;
                markerFromClick.showInfoWindow();
            }
        });


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
            Log.d("----","//// --- IN onPostExecute()");
            System.out.println("//// --- IN onPostExecute() romList size"+romList.size());

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
            System.out.println("//// --- IN postData()");
            Log.d("----","//// --- IN postData()");
            System.out.println("//// --- romList size"+romList.size());
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
            System.out.println("//// --- IN postData onPostExecute()");
        }
    }
}

