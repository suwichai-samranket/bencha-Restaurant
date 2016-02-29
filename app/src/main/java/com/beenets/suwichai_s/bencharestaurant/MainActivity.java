package com.beenets.suwichai_s.bencharestaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private MyManage myManage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Request Database
        myManage = new MyManage(this);

        //Test add Value
        testAddValue();

    } // Main method

    private void testAddValue() {
        myManage.addUser("testUser", "1234", "สุวิชัย");
        myManage.addFood("กระเพราไก่", "50", "urlFOod");
    }

} // Main class