package com.demo.employeemanagementsystemdemo.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.demo.employeemanagementsystemdemo.Login_Activity;
import com.demo.employeemanagementsystemdemo.R;
import com.google.firebase.auth.FirebaseAuth;

public class Details_Activity extends AppCompatActivity {

    private LinearLayout Vacation_Details_Btn , Departure_Details_Btn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        sharedPreferences = getSharedPreferences("User Date" , MODE_PRIVATE);
        editor = sharedPreferences.edit();
        firebaseAuth = FirebaseAuth.getInstance();

        Vacation_Details_Btn = findViewById(R.id.Vacation_Details_Btn);
        Vacation_Details_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Details_Activity.this , Vacation_Details_Activity.class));
                Animatoo.animateSwipeLeft(Details_Activity.this);
                finish();
            }
        });

        Departure_Details_Btn = findViewById(R.id.Departure_Details_Btn);
        Departure_Details_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Details_Activity.this , Departure_Details_Activity.class));
                Animatoo.animateSwipeLeft(Details_Activity.this);
                finish();
            }
        });
    } // end onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu , menu);
        return true;
    } // end onCreateOptionsMenu()

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem home_item = menu.findItem(R.id.Home_Item);
        home_item.setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Home_Item:
                startActivity(new Intent(Details_Activity.this , User_Main_Activity.class));
                Animatoo.animateSwipeRight(this);
                finish();
                return true;

            case R.id.Settings_Item:
                startActivity(new Intent(Details_Activity.this , Settings_Activity.class));
                Animatoo.animateSwipeLeft(this);
                finish();
                return true;

            case R.id.Help_Item:
                return true;

            case R.id.Logout_Item:
                firebaseAuth.signOut();
                editor.clear();
                editor.apply();
                startActivity(new Intent(Details_Activity.this , Login_Activity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        } // end switch()
    } // end onOptionsItemSelected()

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Details_Activity.this , User_Main_Activity.class));
        Animatoo.animateSwipeRight(this);
    }
} // end class