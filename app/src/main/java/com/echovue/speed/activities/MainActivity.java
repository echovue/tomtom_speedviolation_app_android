package com.echovue.speed.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.echovue.speed.R;
import com.google.common.base.Strings;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.api.SearchError;
import com.tomtom.online.sdk.search.api.revgeo.RevGeoSearchResultListener;
import com.tomtom.online.sdk.search.data.common.Address;
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderFullAddress;
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchQuery;
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchQueryBuilder;
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchResponse;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnInformation;
    private TextView txtStreet;
    private TextView txtCity;
    private TextView txtState;
    private TextView txtZipCode;
    private Location currentLocation;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInformation = findViewById(R.id.btnGetInformation);
        txtStreet = findViewById(R.id.txtStreet);
        txtCity = findViewById(R.id.txtCity);
        txtZipCode = findViewById(R.id.txtZipCode);
        txtState = findViewById(R.id.txtState);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
        }


/*        btnCheckSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speed = inputSpeed.getText().toString() + ' ' +
                        getApplicationContext().getString(R.string.text_speed_unit);
                txtCurrentSpeed.setVisibility(View.VISIBLE);
                txtCurrentSpeed.setText(speed);

                txtSlowDown.setVisibility(View.VISIBLE);

                reverseGeocode(new Double(inputLatitude.getText().toString()),
                               new Double(inputLongitude.getText().toString()));

            }
        });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
            }
        }
    }

    protected void reverseGeocode(final Location location) {
        reverseGeocode(location.getLatitude(), location.getLongitude());
    }

    protected void reverseGeocode(final double latitude, final double longitude) {
        SearchApi searchAPI = createSearchAPI();
        ReverseGeocoderSearchQuery reverseGeocoderQuery =
                createReverseGeocoderQuery(latitude, longitude);

        searchAPI.reverseGeocoding(reverseGeocoderQuery, new RevGeoSearchResultListener() {
            @Override
            public void onSearchResult(ReverseGeocoderSearchResponse response) {
                String address = getAddressFromResponse(response);
                Log.d("Address: ", address);
                List<ReverseGeocoderFullAddress> addresses = response.getAddresses();
                if (addresses.size() > 0) {
                    for (ReverseGeocoderFullAddress add : addresses) {
                        Log.d("Address: ", add.toString());
                    }
                }
                //balloon.setText(address);
                //marker.select();
            }

            @Override
            public void onSearchError(SearchError error) {
                Log.d("Address: ", getApplicationContext().getString(R.string.reverse_geocoding_error));
                //balloon.setText(context.getString(R.string.reverse_geocoding_error));
                //marker.select();
            }
        });
    }

    protected SearchApi createSearchAPI() {
        SearchApi searchApi = OnlineSearchApi.create(getApplicationContext());
        return searchApi;
    }

    protected ReverseGeocoderSearchQuery createReverseGeocoderQuery(double latitude, double longitude) {
        return ReverseGeocoderSearchQueryBuilder
                .create(latitude, longitude)
                .withReturnSpeedLimit(true)
                .build();
    }

    protected String getNoReverseGeocodingResultsMessage() {
        return getApplicationContext().getString(R.string.reverse_geocoding_no_results);
    }

    protected String getAddressFromResponse(ReverseGeocoderSearchResponse response) {

        String result = getNoReverseGeocodingResultsMessage();

        if (!response.hasResults()) {
            return result;
        }

        Address address = response.getAddresses().get(0).getAddress();
        String freeformAddress = address.getFreeformAddress();
        if (!Strings.isNullOrEmpty(freeformAddress)) {
            result = freeformAddress;
        }

        return result;
    }
}
