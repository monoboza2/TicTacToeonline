package th.ac.kmutnb.tictactoe2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
//import android.view.windowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import th.ac.kmutnb.tictactoe2.Fragments.GameFragment;

public class MainActivity extends AppCompatActivity {
    private static final String Shared_Name="mypref";
    private static final String KEY_NAME="name";
    private static final String KEY_PASS="pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences=getSharedPreferences(Shared_Name,MODE_PRIVATE);
        String name =sharedPreferences.getString(KEY_NAME,null);
        String pass =sharedPreferences.getString(KEY_PASS,null);

//        onlineBtn = findViewById()
//        offlineBtn = findViewById(R.id.BtnSingle);
//        this.getWindow().setFlags(windowManager.LayoutParams.FLAGS_FULLSCREEN,windowManager);

        //Logout button
        ImageView Logoutbtn=findViewById(R.id.imgview);
        Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplication(), "Logout", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(MainActivity.this,Login.class));
            }
        });
    }
    public void onGameActivity(View view){
        Toast.makeText(getApplication(), "on Single play", Toast.LENGTH_LONG).show();
        startActivity(new Intent(MainActivity.this,GameActivity.class));
    }
//
//    public void onSingle(View view){
//        FragmentTransaction transaction =
//    }

}