package th.ac.kmutnb.tictactoe2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import th.ac.kmutnb.tictactoe2.Fragments.GameFragment;

public class SingleplayerActivity extends AppCompatActivity {

    public static int scoreX = 0 , scoreO = 0 , gameMode = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);

        SingleplayerActivity.scoreO = 0 ;
        SingleplayerActivity.scoreX = 0 ;
        SingleplayerActivity.gameMode = 0 ;
        GameFragment game = new GameFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.addToBackStack(GameFragment.Tag);
        transaction.add(R.id.fragmentContainerViewSinglePlayer,game,"On Single Player");
        transaction.commit();
    }
}