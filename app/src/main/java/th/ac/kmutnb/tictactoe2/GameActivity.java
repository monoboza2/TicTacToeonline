package th.ac.kmutnb.tictactoe2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import th.ac.kmutnb.tictactoe2.Fragments.GameFragment;

public class GameActivity extends AppCompatActivity {
    //----------------------------------------------
    // Online Mode Setting
    //----------------------------------------------


    public static int scoreX = 0 , scoreO = 0 , gameMode = 0 ;
    public static GameFragment game = null;
    public static String Tag = "Tag GameActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent itn = getIntent();
        GameActivity.scoreX = 0;
        GameActivity.scoreO = 0;
        GameActivity.gameMode = itn.getIntExtra("gameMode",0);
        switch (GameActivity.gameMode){
            case 0 :
                GameActivity.Tag = "GameActivity:SinglePlayer Mode";break;
            case 1 : GameActivity.Tag = "GameActivity:MultiPlayer Mode";break;
            case 2 : GameActivity.Tag = "GameActivity:Online Mode";break;
            default: GameActivity.Tag = "GameActivity:Default(Single Player) Mode";
        }
        GameActivity.game = new GameFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(GameFragment.Tag);
        transaction.add(R.id.fragmentContainerView,GameActivity.game,GameActivity.Tag);
        transaction.commit();
    }
    public void HomeBtn(View view) {
        GameActivity.game.reset();
        removeFragmentGame();
        startActivity(new Intent(GameActivity.this,MainActivity.class));
        finish();
    }
    public void SurrenderBtn(View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View Surrender = getLayoutInflater().inflate(R.layout.surrender,null);
        CardView SurrenderCardView = (CardView) Surrender.findViewById(R.id.CardViewSurrender);
        TextView textView1 = (TextView) Surrender.findViewById(R.id.textViewSurrender1),
                 textView2 = (TextView) Surrender.findViewById(R.id.textViewSurrender2);
        Button  SurrenderYesButton = (Button) Surrender.findViewById(R.id.SurrenderYesBtn),
                SurrenderNoButton = (Button) Surrender.findViewById(R.id.SurrenderNoBtn);
        dialogBuilder.setView(Surrender);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        SurrenderYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.game.surrender();
                dialog.dismiss();
            }
        });
        SurrenderNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    public void removeFragmentGame(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().remove(GameActivity.game);
        getFragmentManager().popBackStack();
        transaction.commit();
    }
}