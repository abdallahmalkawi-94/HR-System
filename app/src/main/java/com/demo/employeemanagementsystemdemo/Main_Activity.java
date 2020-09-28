package com.demo.employeemanagementsystemdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.demo.employeemanagementsystemdemo.user.User_Main_Activity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class Main_Activity extends AppCompatActivity {
    private Timer timer = new Timer();
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    startActivity(new Intent(Main_Activity.this , User_Main_Activity.class));
                    Animatoo.animateSwipeLeft(Main_Activity.this);
                    finish();
                } // end if()
                else
                {
                    startActivity(new Intent(Main_Activity.this , Login_Activity.class));
                    Animatoo.animateSwipeLeft(Main_Activity.this);
                    finish();
                } // end else

            } // end run()
        }, 2000);
    } // end onCreate()
} // end class