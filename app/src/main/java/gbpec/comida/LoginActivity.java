package gbpec.comida;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gbpec.comida.donor_module.Donor_NavigationMainActivity;
import gbpec.comida.reciever_module.Reciever_Navigation;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signUp,login;
    EditText username,password;
    TextView errorText;
    SessionManager session;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        username=(EditText)findViewById(R.id.username2);
        password=(EditText)findViewById(R.id.password);
        signUp= (Button)findViewById(R.id.signup_bttn);
        login= (Button)findViewById(R.id.btn_login);
        errorText=(TextView)findViewById(R.id.error_text);
        errorText.setVisibility(View.GONE);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);
        FirebaseApp.initializeApp(this);
    }
    private void checkLogin(String username, String password) {



        String url = "http://vipul.hol.es/login_fetch.php?user_no="+username+"&password="+password;
        Toast.makeText(this,"thhis--"+username+" "+password,Toast.LENGTH_SHORT).show();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSON(response);
                progressDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"cannot login",Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void showJSON(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("success");

            if(result.length()==0){
                errorText.setText("Invalid Login Credentials !");
                errorText.setVisibility(View.VISIBLE);
            }
            else {
                JSONObject collegedata = result.getJSONObject(0);
                String username1 = collegedata.getString("User_mobile");
                String type = collegedata.getString("User_type");
            if(result.length()!=0)
            {

                session.createLoginSession(username1,type);

              if (type.equals("business")) {

                  FirebaseMessaging.getInstance().subscribeToTopic("donor");
                 // myIntent.putExtra("user", username);
                  saveDataDonor(username1);


                }
                if (type.equals("ngo")) {

                  FirebaseMessaging.getInstance().subscribeToTopic("reciever");
                  saveDataNGO(username1);
                    Intent myIntent = new Intent(LoginActivity.this,
                            Reciever_Navigation.class);

                    //myIntent.putExtra("user", username);
                    startActivity(myIntent);
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                  finish();
                }
            }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void saveDataNGO(String username) {



        String url = "http://vipul.hol.es/sharedpref_ngo.php?user_no="+username;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showDataNGO(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"cannot login",Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void showDataNGO(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("success");


                JSONObject collegedata = result.getJSONObject(0);
                String id=collegedata.getString("id");
                String name = collegedata.getString("name");
                String image = collegedata.getString("image");
                String latitude = collegedata.getString("latitude");
                 String longitude = collegedata.getString("longitude");
                 String email = collegedata.getString("email");
              String address = collegedata.getString("address");
            Toast.makeText(LoginActivity.this,latitude+"  "+longitude,Toast.LENGTH_LONG).show();

              session.createDataSessionNGO(id,name,latitude,longitude,address,email,image);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void showDataBUSINESS(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("success");


            JSONObject collegedata = result.getJSONObject(0);
            String id=collegedata.getString("id");
            String name = collegedata.getString("name");
            String image = collegedata.getString("image");
            String email = collegedata.getString("email");
            String address = collegedata.getString("address");


            session.createDataSessionBUSINESS(id,name,address,email,image);
            Intent myIntent = new Intent(LoginActivity.this,
                    Donor_NavigationMainActivity.class);
            startActivity(myIntent);
            overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void saveDataDonor(String username)
    {

        String url = "http://vipul.hol.es/sharedpref_business.php?user_no="+username;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showDataBUSINESS(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"cannot login",Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signup_bttn) {
            Intent i = new Intent(LoginActivity.this, Registration_options.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        }
        else if (v.getId() == R.id.btn_login) {
            String user_name = username.getText().toString();
            String pass_word = password.getText().toString();
            errorText.setVisibility(View.GONE);
            if(user_name.isEmpty()||pass_word.isEmpty()){
                errorText.setText("Please Enter User Id and Password !");
                errorText.setVisibility(View.VISIBLE);
            }
            else{
                checkLogin(user_name, pass_word);
            }





        }
    }
}

