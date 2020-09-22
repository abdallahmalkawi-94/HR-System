package com.demo.employeemanagementsystemdemo.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.demo.employeemanagementsystemdemo.Login_Activity;
import com.demo.employeemanagementsystemdemo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Vacation_Departure_Request_Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView name_txt , phone_txt , designation_txt , vacation_date , departure_date , departure_time_from , departure_time_to;
    private Spinner request_type_spin;
    private ArrayAdapter<CharSequence> adapter;
    private Button submit_btn , cancel_btn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private static int vacation_id = 1;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private String DATE;
    private SimpleDateFormat sdf = new SimpleDateFormat("d / M / yyyy - HH:mm");
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation__departure__request);
        defineItems();
        checkType();
    } // end onCreate()

    private void checkType() {
        try {
            request_type_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (request_type_spin.getSelectedItem().toString().equals("Vacation"))
                    {
                        vacation_date.setEnabled(true);
                        vacation_date.setBackgroundResource(R.drawable.selected_item_style);

                        departure_date.setEnabled(false);
                        departure_date.setBackgroundResource(R.drawable.edit_text_style);
                        departure_date.setText("");

                        departure_time_from.setEnabled(false);
                        departure_time_from.setBackgroundResource(R.drawable.edit_text_style);
                        departure_time_from.setText("");

                        departure_time_to.setEnabled(false);
                        departure_time_to.setBackgroundResource(R.drawable.edit_text_style);
                        departure_time_to.setText("");

                    } // end if()
                    else if (request_type_spin.getSelectedItem().toString().equals("Departure"))
                    {
                        vacation_date.setEnabled(false);
                        vacation_date.setBackgroundResource(R.drawable.edit_text_style);
                        vacation_date.setText("");

                        departure_time_from.setEnabled(true);
                        departure_time_from.setBackgroundResource(R.drawable.selected_item_style);

                        departure_time_to.setEnabled(true);
                        departure_time_to.setBackgroundResource(R.drawable.selected_item_style);

                        departure_date.setEnabled(true);
                        departure_date.setBackgroundResource(R.drawable.selected_item_style);
                    } // end else if()
                    else
                    {
                        vacation_date.setEnabled(false);
                        vacation_date.setBackgroundResource(R.drawable.edit_text_style);
                        vacation_date.setText("");

                        departure_date.setEnabled(false);
                        departure_date.setBackgroundResource(R.drawable.edit_text_style);
                        departure_date.setText("");

                        departure_time_from.setEnabled(false);
                        departure_time_from.setBackgroundResource(R.drawable.edit_text_style);
                        departure_time_from.setText("");

                        departure_time_to.setEnabled(false);
                        departure_time_to.setBackgroundResource(R.drawable.edit_text_style);
                        departure_time_to.setText("");
                    } // end else
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                } // end onNothingSelected()
            }); // end setOnItemSelectedListener()
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext() , e.getMessage() , Toast.LENGTH_LONG).show();
        } // end catch()

    } // end checkType()

    private void defineItems() {
        sharedPreferences = getSharedPreferences("UserID" , MODE_PRIVATE);
        editor = sharedPreferences.edit();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        name_txt = findViewById(R.id.V_D_Name_Txt);
        name_txt.setText(getString(R.string.name)+":- " + sharedPreferences.getString("Full Name" , ""));

        phone_txt = findViewById(R.id.V_D_Phone_Txt);
        phone_txt.setText(getString(R.string.phone) + ":- " + sharedPreferences.getString("Phone" , ""));

        designation_txt = findViewById(R.id.V_D_Designation_Txt);
        designation_txt.setText(getString(R.string.designation) + ":- " + sharedPreferences.getString("Designation" , ""));

        vacation_date = findViewById(R.id.V_Date_Txt);
        vacation_date.setOnClickListener(this);

        departure_date = findViewById(R.id.D_Date_Txt);
        departure_date.setOnClickListener(this);

        departure_time_from = findViewById(R.id.D_Time_From_Txt);
        departure_time_from.setOnClickListener(this);

        departure_time_to = findViewById(R.id.D_Time_To_Txt);
        departure_time_to.setOnClickListener(this);

        request_type_spin = findViewById(R.id.Request_Type_Spin);
        adapter = ArrayAdapter.createFromResource(this , R.array.request_type_string , R.layout.spinner_item_design);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        request_type_spin.setAdapter(adapter);

        submit_btn = findViewById(R.id.V_D_Submit_Btn);
        submit_btn.setOnClickListener(this);

        cancel_btn = findViewById(R.id.V_D_Cancel_Btn);
        cancel_btn.setOnClickListener(this);
    } // end defineItems()

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
                startActivity(new Intent(Vacation_Departure_Request_Activity.this , User_Main_Activity.class));
                finish();
                return true;

            case R.id.Settings_Item:
                startActivity(new Intent(Vacation_Departure_Request_Activity.this , Settings_Activity.class));
                finish();
                return true;

            case R.id.Help_Item:
                return true;

            case R.id.Logout_Item:
                firebaseAuth.signOut();
                editor.clear();
                editor.apply();
                startActivity(new Intent(Vacation_Departure_Request_Activity.this , Login_Activity.class));
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
            case R.id.V_Date_Txt:
                getCalander(0);
                break;

            case R.id.D_Date_Txt:
                getCalander(1);
                break;

            case R.id.D_Time_From_Txt:
                getTime("from");
                break;

            case R.id.D_Time_To_Txt:
                getTime("to");
                break;

            case R.id.V_D_Submit_Btn:
                Submit(request_type_spin.getSelectedItem().toString());
                break;

            case R.id.V_D_Cancel_Btn:
               request_type_spin.setSelection(0);
                break;
        } // end switch()
    } // end onClick()

    private void Submit(String s) {
        if (s.equals("Vacation"))
        {
            DocumentReference documentReference = firestore.collection(s).document(String.valueOf(vacation_id) + " " + sharedPreferences.getString("Full Name" , ""));
            vacation_id++;
            Vacation_Request vacation_request = new Vacation_Request(sharedPreferences.getString("Full Name" , "")
            , sharedPreferences.getString("Phone" , "")
            , sharedPreferences.getString("Designation" , "")
            , vacation_date.getText().toString()
            , sdf.format(calendar.getTime()));

            documentReference.set(vacation_request).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext() , getString(R.string.under_review) , Toast.LENGTH_SHORT).show();
                    request_type_spin.setSelection(0);
                } // end onSuccess()
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext() , getString(R.string.error) + " " + e.getMessage() , Toast.LENGTH_LONG).show();
                } // end onFailure()
            });
        } // end if()
    } // end Submit()

    private void getCalander(final int Option){

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(Vacation_Departure_Request_Activity.this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (Option == 0)
                {
                    DATE = dayOfMonth + " / " + (monthOfYear + 1) + " / " + year;
                    vacation_date.setText(DATE);
                } // end if()
                else if (Option == 1)
                {
                    DATE = dayOfMonth + " / " + (monthOfYear + 1) + " / " + year;
                    departure_date.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / " + year);
                } // end else if()
            } // end onDateSet()
        }, year, month, day);
        datePickerDialog.show();
    } // end getCalander()

    private void getTime(final String period) {
        timePickerDialog = new TimePickerDialog(Vacation_Departure_Request_Activity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if (period.equals("from"))
                    departure_time_from.setText(String.format("%02d:%02d" , i , i1));

                else if (period.equals("to"))
                    departure_time_to.setText(String.format("%02d:%02d" , i , i1));
            }
        } , 0 , 0 , true);
        timePickerDialog.show();
    } // end getTime()

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Vacation_Departure_Request_Activity.this , User_Main_Activity.class));
        finish();
    } // end onBackPressed()

} // end class