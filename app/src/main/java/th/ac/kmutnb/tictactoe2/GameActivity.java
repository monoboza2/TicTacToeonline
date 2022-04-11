package th.ac.kmutnb.tictactoe2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import th.ac.kmutnb.tictactoe2.Fragments.GameFragment;

public class GameActivity extends AppCompatActivity {

    public static int scoreX = 0 , scoreO = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Single play
        Button startBtn = findViewById(R.id.Test);
        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                GameFragment game = new GameFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                GameActivity.scoreO = 0 ;
                GameActivity.scoreX = 0 ;
                transaction.addToBackStack(GameFragment.Tag);
                transaction.add(R.id.fragmentContainerView,game,"Test game fragment");
                transaction.commit();
            }
        });

    }
}