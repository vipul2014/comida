package gbpec.comida;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import gbpec.comida.donor_module.Donor_NavigationMainActivity;

//import android.content.Intent;

public class FoodItems extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private LinearLayout layout, layout2;
    private EditText item, quantity, newaddress, pickup_address_et;
    public static EditText items[] = new EditText[100];
    public static EditText quantities[] = new EditText[100];
    public static TextView serial[] = new TextView[100], pickup_address_tv;
    private int k = 0, text = 2, addressPick = 1, timePick = 1;
    SessionManager session;
    //DatePicker simpleDatePicker;
    EditText simpleDatePicker_et, timepicker_from, timepicker_to, timepicker_upto;
    AutoCompleteTextView t_address, t_city, t_pin;
    Spinner t_state;
    private int pickt1_h, pickt2_h;
    private String address, user, user_name, Address;
    String foodlattitude = new String();
    String foodlongitude = new String();
    final StringBuilder sb = new StringBuilder(1000);
    String pickt_from, pickt_to, valid_date, valid_time;

    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_items);
        //layout=(LinearLayout)findViewById(R.id.itemview);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tab_share_food);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setTitle("Share Surplus Food");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user1 = session.getUserDetails();
        user = user1.get(SessionManager.USER_CONTACT);
        user_name = user1.get(SessionManager.USER_NAME);
        Toast.makeText(getApplicationContext(), user, Toast.LENGTH_LONG).show();

        // Intent i=getIntent();
        //  user=i.getStringExtra("user");
        //  Toast.makeText(getApplicationContext(), user, Toast.LENGTH_LONG).show();

        layout2 = (LinearLayout) findViewById(R.id.view2);
        layout2.setOrientation(LinearLayout.VERTICAL);
        item = (EditText) findViewById(R.id.itemname);
        quantity = (EditText) findViewById(R.id.quantity);
        simpleDatePicker_et = (EditText) findViewById(R.id.simpleDatePicker_et);
        timepicker_from = (EditText) findViewById(R.id.timepicker_from);
        timepicker_to = (EditText) findViewById(R.id.timepicker_to);
        timepicker_upto = (EditText) findViewById(R.id.timepicker_upto);
//        pickup_address_tv=(TextView)findViewById(R.id.pickup_address_tv);
//        pickup_address_et=(EditText)findViewById(R.id.pickup_address_et);
        t_address = (AutoCompleteTextView) findViewById(R.id.tv_adress);
        t_city = (AutoCompleteTextView) findViewById(R.id.t_city);
        t_pin = findViewById(R.id.t_pin);
        t_state = (Spinner) findViewById(R.id.spin_state);

        t_state.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.state_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        t_state.setAdapter(adapter);

        String URL = "http://vipul.hol.es/getProfile.php?contactno=" + user;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   loading.dismiss();
                // Toast.makeText(getContext(),"thhis--"+response,Toast.LENGTH_LONG).show();
                //WORKING CORRECTLY NAD GETTING DATA
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    // Toast.makeText(getContext(),"1",Toast.LENGTH_LONG).show();
                    JSONArray result = jsonObject1.getJSONArray("result");
                    //  Toast.makeText(getContext(),"2",Toast.LENGTH_LONG).show();
                    JSONObject businessData = result.getJSONObject(0);

                    Address = businessData.getString("address");
                    //setting values
//                    pickup_address_tv.setText(Address);
                    //
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //   Toast.makeText(getContext(),"data-",Toast.LENGTH_LONG).show();
                // showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        //  Toast.makeText(getContext(),"execute",Toast.LENGTH_LONG).show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //  Toast.makeText(getContext(),"exe",Toast.LENGTH_LONG).show();
        requestQueue.add(stringRequest);


        timepicker_from.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(FoodItems.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String am_pm;
                        int hour;
                        if (selectedHour > 12) {
                            am_pm = "PM";
                            hour = selectedHour - 12;
                        } else {
                            am_pm = "AM";
                            hour = selectedHour;
                        }
                        timepicker_from.setText(hour + ":" + selectedMinute + " - " + am_pm);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        timepicker_to.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(FoodItems.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String am_pm;
                        int hour;
                        if (selectedHour > 12) {
                            am_pm = "PM";
                            hour = selectedHour - 12;
                        } else {
                            am_pm = "AM";
                            hour = selectedHour;
                        }
                        timepicker_to.setText(hour + ":" + selectedMinute + " - " + am_pm);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        timepicker_upto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(FoodItems.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String am_pm;
                        int hour;
                        if (selectedHour > 12) {
                            am_pm = "PM";
                            hour = selectedHour - 12;
                        } else {
                            am_pm = "AM";
                            hour = selectedHour;
                        }
                        timepicker_upto.setText(hour + ":" + selectedMinute + " - " + am_pm);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                simpleDatePicker_et.setText(sdf.format(myCalendar.getTime()));
            }


        };

        simpleDatePicker_et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FoodItems.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//
//        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
//        checkLocation();

//pick up time values
//        TimePicker simpleTimePicker1=(TimePicker) findViewById(R.id.simpleTimePicker1);
////        simpleTimePicker1.setIs24HourView(true);
//
//        TimePicker simpleTimePicker2=(TimePicker) findViewById(R.id.simpleTimePicker2);
////        simpleTimePicker2.setIs24HourView(true);
//
//        simpleTimePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                pickt1_h=hourOfDay;
//                pickt1_m=minute;
//            }
//        });
//
//        simpleTimePicker2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                 pickt2_h=hourOfDay;
//                pickt2_m=minute;
//            }
//        });
//pick up time end

        //Valid upto details
//         simpleDatePicker=(DatePicker)findViewById(R.id.simpleDatePicker);
//        TimePicker simpleTimePicker3=(TimePicker) findViewById(R.id.simpleTimePicker3);
////        simpleTimePicker3.setIs24HourView(true);
//        simpleTimePicker3.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                validt_h=hourOfDay;
//                validt_m=minute;
//            }
//        });
//Valid upto details time limit end

//        newaddress = (EditText) findViewById(R.id.address_new);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(this, Donor_NavigationMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void editAddress(View v) {
        pickup_address_tv.setVisibility(View.GONE);
        pickup_address_et.setVisibility(View.VISIBLE);
        pickup_address_et.setText(pickup_address_tv.getText().toString());
        addressPick = 2;
    }

    //Radio button for addressonRadioButtonClicked
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.business_time:
                if (checked) {
                    LinearLayout layotpick = (LinearLayout) findViewById(R.id.picktime);
                    layotpick.setVisibility(View.GONE);
                    timePick = 1;
                }
                // Pirates are the best
                //    newaddress.setVisibility(View.GONE);
                //address="Business Address";
                //address_check=0;
                break;
            case R.id.new_time:
                if (checked) {
                    timePick = 2;
                    LinearLayout layotpick = (LinearLayout) findViewById(R.id.picktime);
                    layotpick.setVisibility(View.VISIBLE);
                    // Ninjas rule
                    //    address = "New address";
                    //    newaddress.setVisibility(View.VISIBLE);
                    //   address_check=1;
                    break;
                }
        }
    }

    //Radio button end
    public void addmore(View v) {
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        layout.setLayoutParams(params);


        items[k] = new EditText(FoodItems.this);
        items[k].setHint("Item");
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        params1.setMargins(10, 0, 0, 0);
        items[k].setLayoutParams(params1);
        // items[k].setLayoutParams(params);

        quantities[k] = new EditText(FoodItems.this);
        quantities[k].setHint("Quantity");
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        params3.setMargins(15, 0, 10, 0);
        // quantities[k].setWidth(100);
        quantities[k].setLayoutParams(params3);
        // quantities[k].setLayoutParams(params);

        String a = Integer.toString(text);
        TextView t = new TextView(FoodItems.this);
        // serial[k].setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
        t.setId(k);
        t.setTextSize(18);
        t.setText(a + ") ");
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins(0, 0, 0, 0);
        t.setLayoutParams(params2);

        text = Integer.parseInt(a);
        text++;

        layout.addView(t);
        layout.addView(items[k]);
        layout.addView(quantities[k]);
        //layout.removeAllViews();
        layout2.addView(layout);
        k++;
    }

    public void submit(View v) {
//converting food items in single string


        sb.append("1) ");
        sb.append(item.getText().toString() + "-");
        sb.append(quantity.getText().toString() + "&");
        for (int i = 0; i < k; i++) {
            sb.append(i + 2 + ") ");
            sb.append(items[i].getText().toString() + "-");
            sb.append(quantities[i].getText().toString() + "&");
        }
//        Toast.makeText(FoodItems.this, foodlongitude+"/n"+foodlongitude, Toast.LENGTH_LONG).show();
        //end string builder
        //valid dya and vaid_month will be the upto  date for collection of food
//        final int valid_day=simpleDatePicker.getDayOfMonth();
//        int valid_mo=simpleDatePicker.getMonth();
//        String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
//        final String valid_month=MONTHS[valid_mo];
        //valid day end

        //checking wether it is new address or not
//        if(addressPick==1){
//            address=Address;
//        }
//        else{
//            address=pickup_address_et.getText().toString();
//        }
//        //


        final StringBuilder add = new StringBuilder(1000);
        add.append(t_address.getText().toString());
        add.append("#");
        add.append(t_city.getText().toString());
        add.append("#");
        add.append(state);
        add.append("#");
        add.append(t_pin.getText().toString());

        address = String.valueOf(add);


        if (timePick == 1) {
            pickt_from = "9am";
            pickt_to = "9pm";
        } else {
            pickt_from = timepicker_from.getText().toString();
            pickt_to = timepicker_to.getText().toString();
        }
        valid_date = simpleDatePicker_et.getText().toString();
        valid_time = timepicker_upto.getText().toString();
        if (foodlattitude.isEmpty() || foodlongitude.isEmpty()) {
            getlocationAttributes();
//            startLocationUpdates();
        }
    }

    void makeRequest() {

        String REGISTER_URL = "http://vipul.hol.es/fooditems.php";

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        // progressDialog.dismiss();

                        // Showing Echo Response Message Coming From Server.
                        Toast.makeText(FoodItems.this, ServerResponse, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        //  progressDialog.dismiss();

                        // Showing error message if something goes wrong.
                        Toast.makeText(FoodItems.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                //params.put("mnumber",bnumber);
                params.put("fuser", user);
                params.put("fname", user_name);
                params.put("fDetails", String.valueOf(sb));
                params.put("fRequestDate", valid_date);//valid date
                params.put("fRequestTime", valid_time);//valid time
                params.put("fPickupTime", pickt_from + "to" + pickt_to);
                params.put("fPickupAddress", address);
                params.put("fFoodLatitude", foodlattitude);
                params.put("fFoodLongitude", foodlongitude);
                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(FoodItems.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
        // Toast.makeText(getApplicationContext(), "Address::"+address+Integer.toString(address_check), Toast.LENGTH_LONG).show();


  /*      Toast.makeText(getApplicationContext(), sb, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "Pick up time::"+pickt1_h+":"+pickt1_m+"\nto\n"+pickt2_h+":"+pickt2_m, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "Valid upto::"+valid_month+"/"+valid_day+"--"+validt_h+":"+validt_m, Toast.LENGTH_LONG).show();

*/

//    private boolean checkLocation() {
//        if(!isLocationEnabled())
//            showAlert();
//        return isLocationEnabled();
//    }
//
//    private void showAlert() {
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Enable Location")
//                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
//                        "use this app")
//                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//
//                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(myIntent);
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//
//                    }
//                });
//        dialog.show();
//    }
//    private void startLocationUpdates() {
//        // Create the location request
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(UPDATE_INTERVAL)
//                .setFastestInterval(FASTEST_INTERVAL);
//        // Request location updates
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, this);
//        Log.d("reque", "--->>>>");
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        startLocationUpdates();
//
//        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//
//        if(mLocation == null){
//            startLocationUpdates();
//        }
//        if (mLocation != null) {
//
//            foodlattitude=String.valueOf(mLocation.getLatitude());
//            foodlongitude=String.valueOf(mLocation.getLongitude());
////            String location=ngolattitude+ngolongitude;
////            testing.setText(location);
//        } else {
//            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i("LOCATION:", "Connection Suspended");
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    public boolean isLocationEnabled() {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||//make it or later
//                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//    }

}

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==R.id.spin_state){


            String item = parent.getItemAtPosition(position).toString();
            if(item.equals("State")){
               t_state.setAutofillHints("enter state");
            }
            else{

            }
            state=item;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getlocationAttributes() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://maps.googleapis.com/maps/api/geocode/json?address="+t_pin.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = obj.getJSONArray("results");

                            //now looping through all the elements of the json array

                            foodlattitude=jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
                            foodlongitude=jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();


                            Toast.makeText(FoodItems.this, foodlattitude+"/n"+foodlongitude, Toast.LENGTH_LONG).show();

                            makeRequest();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
    }

