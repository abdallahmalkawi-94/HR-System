package com.demo.employeemanagementsystemdemo.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.employeemanagementsystemdemo.Login_Activity;
import com.demo.employeemanagementsystemdemo.R;
import com.google.firebase.auth.FirebaseAuth;

public class User_Main_Activity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout v_d_r_btn , v_l_d_btn, salary_slip_btn;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserID" , MODE_PRIVATE);
        editor = sharedPreferences.edit();

        v_d_r_btn = findViewById(R.id.V_D_R_Btn);
        v_d_r_btn.setOnClickListener(this);

        v_l_d_btn = findViewById(R.id.V_L_D_Btn);
        v_l_d_btn.setOnClickListener(this);

        salary_slip_btn = findViewById(R.id.Salary_Slip_Btn);
        salary_slip_btn.setOnClickListener(this);
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
        home_item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Settings_Item:
                startActivity(new Intent(User_Main_Activity.this , Settings_Activity.class));
                finish();
                return true;

            case R.id.Help_Item:
                return true;

            case R.id.Logout_Item:
                firebaseAuth.signOut();
                editor.clear();
                editor.apply();
                startActivity(new Intent(User_Main_Activity.this , Login_Activity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        } // end switch()
    } // end onOptionsItemSelected()

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.V_D_R_Btn:
                startActivity(new Intent(User_Main_Activity.this , Vacation_Departure_Request_Activity.class));
                finish();
                break;
            case R.id.V_L_D_Btn:
                break;
            case R.id.Salary_Slip_Btn:
                break;
        } // end switch()
    } // end onClick()
} // end class
