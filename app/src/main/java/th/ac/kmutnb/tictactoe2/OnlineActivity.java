package th.ac.kmutnb.tictactoe2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnlineActivity extends AppCompatActivity {
    private static final String Shared_Name="mypref";
    private static final String KEY_NAME="name";
    private static final String TAG = "my_app";

    private boolean isCodeMaker =true;
    private String Code = null;
    public boolean codeFound= false;
    public boolean checkTemp= true;
    public String KeyValue= "null";

    Button Createbtn=findViewById(R.id.Create);
    EditText Codetxt=findViewById(R.id.CodeGerate);
    ProgressBar Loading=findViewById(R.id.progressBar);
    Button Joinbtn=findViewById(R.id.Join);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        SharedPreferences sharedPreferences=getSharedPreferences(Shared_Name,MODE_PRIVATE);
        String name =sharedPreferences.getString(KEY_NAME,null);
        Log.i(TAG, name);

        Createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void accepted(){
        startActivity(new Intent(this,OnlineActivity.class));
        Createbtn.setVisibility(View.GONE);
        Joinbtn.setVisibility(View.GONE);
        Codetxt.setVisibility(View.GONE);
        Loading.setVisibility(View.GONE);

    }

    public void isValueAvalible(String code){

    }

}

