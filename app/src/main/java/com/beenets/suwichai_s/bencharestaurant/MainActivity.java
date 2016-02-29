package com.beenets.suwichai_s.bencharestaurant;

import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private MyManage myManage;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        bindWidget();

        //Request Database
        myManage = new MyManage(this);

        //Test add Value
        //testAddValue();

        //Delete All SQLite
        deleteAllSQLite();

        //Synchronize JSON to SQLite
        synJSONtoSQLite();


    } // Main method

    public void clickLogin(View view) {

        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        if (userString.equals("") || passwordString.equals("")) {
            //Have space
            MyAlertDialog myAlertDialog = new MyAlertDialog();
            myAlertDialog.myDialog(MainActivity.this, "มีช่องว่าง", "กรุณากรอกให้ครบทุกช่อง");

        } else {
            //No space


        }


    } // clickLogin

    private void bindWidget() {
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);

    }

    private void synJSONtoSQLite() {

        //Connected http
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        int intTimes = 0;
        while (intTimes <= 1) {

            //1. Create InputStream
            InputStream inputStream = null;
            String[] urlJSON = new String[2];
            urlJSON[0] = "http://www.swiftcodingthai.com/29feb/php_get_user_deer.php";
            urlJSON[1] = "http://www.swiftcodingthai.com/29feb/php_get_food_deer.php";
            HttpPost httpPost = null;

            try {

                HttpClient httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(urlJSON[intTimes]);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

            } catch (Exception e) {
                Log.d("bencha", "InputeStream ==>" + e.toString());
            }


            //2. Create JSON String
            String strJSON = null;
            try {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String strLine = null;

                while ((strLine = bufferedReader.readLine()) != null) {

                    stringBuilder.append(strLine);

                }
                inputStream.close();
                strJSON = stringBuilder.toString();



            } catch (Exception e) {

                Log.d("bencha", "JSON String ==>" + e.toString());
            }


            //3. Update to SQLite
            try {

                JSONArray objJsonArray = new JSONArray(strJSON);

                for (int i = 0; i < objJsonArray.length(); i++ ) {
                    JSONObject jsonObject = objJsonArray.getJSONObject(i);

                    switch (intTimes) {
                        case 0:
                            //For UserTABLE
                            String strUser = jsonObject.getString(MyManage.column_user);
                            String strPassword = jsonObject.getString(MyManage.column_pass);
                            String strName = jsonObject.getString(MyManage.column_name);

                            myManage.addUser(strUser, strPassword, strName);
                            break;

                        case 1:
                            //For FoodTable
                            String strFood = jsonObject.getString(MyManage.column_food);
                            String strPrice = jsonObject.getString(MyManage.column_price);
                            String strSoucre = jsonObject.getString(MyManage.column_source);

                            myManage.addFood(strFood, strPrice, strSoucre);
                            break;

                    }
                }

            } catch ( Exception e) {
                Log.d("bencha", "Update ==>" + e.toString());
            }


            intTimes+=1;
        }
    } // synJSONtoSQLite

    private void deleteAllSQLite() {
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        sqLiteDatabase.delete(MyManage.food_table, null, null);
        sqLiteDatabase.delete(MyManage.user_table, null, null);

    }

    private void testAddValue() {
        myManage.addUser("testUser", "1234", "สุวิชัย");
        myManage.addFood("กระเพราไก่", "50", "urlFOod");
    }

} // Main class