package th.ac.kmutnb.tictactoe2.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import th.ac.kmutnb.tictactoe2.GameActivity;
import th.ac.kmutnb.tictactoe2.GameboardAdapter;
import th.ac.kmutnb.tictactoe2.MainActivity;
import th.ac.kmutnb.tictactoe2.MultiplayerActivity;
import th.ac.kmutnb.tictactoe2.R;

public class GameFragment extends Fragment {

    private RecyclerView rv_boardgame;
    private GameboardAdapter gameboardAdapter;
    public static boolean turnO = true;
    public static TextView textTurn , text_win_x , text_win_o , text_win;
    private Button btn_again , btn_home ;
    public static ImageView img_stroke , img_win;
    public static RelativeLayout rl_win;
    public static String Tag = GameFragment.class.getName();
    public static TextView PlayerNameO ,PlayerNameX ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        rv_boardgame = view.findViewById(R.id.boardGame);
        textTurn = view.findViewById(R.id.textTurn);
        btn_again = view.findViewById(R.id.btn_again);
        btn_home = view.findViewById(R.id.btn_home);

        PlayerNameO = view.findViewById(R.id.textViewPlayer1);
        PlayerNameX = view.findViewById(R.id.textViewPlayer2);

        img_stroke = view.findViewById(R.id.img_stroke);
        rl_win = view.findViewById(R.id.rl_win);
        text_win_x = view.findViewById(R.id.textWin_X);
        text_win_o = view.findViewById(R.id.textWin_O);
        text_win = view.findViewById(R.id.textWin);
        img_win = view.findViewById(R.id.img_win);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        for(int i=0;i<9 ;i++){
            arrBms.add(null);
        }
        gameboardAdapter = new GameboardAdapter(getContext(),arrBms);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        rv_boardgame.setLayoutManager(layoutManager);
        rv_boardgame.setAdapter(gameboardAdapter);
        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_win.setVisibility(View.INVISIBLE);
                reset();
                if(GameActivity.gameMode == 2){
                    MultiplayerActivity.Playerturn = MultiplayerActivity.PlayerNameO ;
                    MultiplayerActivity.databaseReference.child("score").child(MultiplayerActivity.ConnectionID).child("winner").setValue("");
                    MultiplayerActivity.databaseReference.child("board").child(MultiplayerActivity.ConnectionID).setValue(-1);
                    MultiplayerActivity.databaseReference.child("turn").child(MultiplayerActivity.ConnectionID).setValue(MultiplayerActivity.Playerturn);
                }
            }
        });
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                getFragmentManager().popBackStack();
                //remove connection database
                MultiplayerActivity.databaseReference.child("connections").child(MultiplayerActivity.ConnectionID).removeValue();

                Intent itn = new Intent( getActivity(),MainActivity.class);
                startActivity(itn);
                getActivity().finish();
            }
        });
        return view ;
    }

    @Override
    public void onStart(){
        super.onStart();
        if(GameActivity.gameMode == 2 ){
            MultiplayerActivity.databaseReference.child("board").child(MultiplayerActivity.ConnectionID).setValue(-1);
            MultiplayerActivity.databaseReference.child("turn").child(MultiplayerActivity.ConnectionID).setValue(MultiplayerActivity.Playerturn);
            MultiplayerActivity.databaseReference.child("turn").child(MultiplayerActivity.ConnectionID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    MultiplayerActivity.Playerturn = snapshot.getValue(String.class);
                    //turn change
                    if(snapshot.getValue(String.class).equals(MultiplayerActivity.PlayerNameO)){
                        GameFragment.textTurn.setText("turn of O");
                    }
                    else{
                        GameFragment.textTurn.setText("turn of X");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            MultiplayerActivity.databaseReference.child("score").child(MultiplayerActivity.ConnectionID).child(MultiplayerActivity.PlayerNameO).setValue(GameActivity.scoreO);
            MultiplayerActivity.databaseReference.child("score").child(MultiplayerActivity.ConnectionID).child(MultiplayerActivity.PlayerNameX).setValue(GameActivity.scoreX);
            MultiplayerActivity.databaseReference.child("score").child(MultiplayerActivity.ConnectionID).child("winner").setValue("");

            MultiplayerActivity.databaseReference.child("score").child(MultiplayerActivity.ConnectionID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GameActivity.scoreO = snapshot.child(MultiplayerActivity.PlayerNameO).getValue(Integer.class);
                    GameActivity.scoreX = snapshot.child(MultiplayerActivity.PlayerNameX).getValue(Integer.class);
                    GameFragment.text_win_o.setText(": "+GameActivity.scoreO);
                    GameFragment.text_win_x.setText(": "+GameActivity.scoreX);

                    Log.i("OnlineLog",snapshot.child("winner").getValue(String.class));

                    if(snapshot.child("winner").getValue(String.class).equals(MultiplayerActivity.PlayerNameO)){
                        GameFragment.img_stroke.startAnimation(GameboardAdapter.anim_stroke);
                        GameFragment.rl_win.setAnimation(GameboardAdapter.anim_win);
                        GameFragment.rl_win.setVisibility(View.VISIBLE);
                        GameFragment.rl_win.startAnimation(GameboardAdapter.anim_win);
                        GameFragment.img_win.setImageBitmap(GameboardAdapter.bmO);
                        GameFragment.text_win.setText("win");
                    }
                    else if(snapshot.child("winner").getValue(String.class).equals(MultiplayerActivity.PlayerNameX)){
                        GameFragment.img_stroke.startAnimation(GameboardAdapter.anim_stroke);
                        GameFragment.rl_win.setAnimation(GameboardAdapter.anim_win);
                        GameFragment.rl_win.setVisibility(View.VISIBLE);
                        GameFragment.rl_win.startAnimation(GameboardAdapter.anim_win);
                        GameFragment.img_win.setImageBitmap(GameboardAdapter.bmX);
                        GameFragment.text_win.setText("win");

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            GameFragment.PlayerNameO.setText(MultiplayerActivity.PlayerNameO);
            GameFragment.PlayerNameX.setText(MultiplayerActivity.PlayerNameX);
        }

    }

    public void surrender(){
        if(turnO){
            gameboardAdapter.winCharacter = "x" ;
        }
        else {
            gameboardAdapter.winCharacter = "o" ;
        }
        gameboardAdapter.win();
    }
    public void surrenderOnline(){
        if(MultiplayerActivity.PlayerName.equals(MultiplayerActivity.PlayerNameO)){
            gameboardAdapter.winCharacter = "x" ;
        }
        else {
            gameboardAdapter.winCharacter = "o" ;
        }
        gameboardAdapter.win();
    }
    public void reset(){
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        for(int i=0;i<9;i++){
            arrBms.add(null);
        }
        gameboardAdapter.setArrBms(arrBms);
        gameboardAdapter.notifyDataSetChanged();
        turnO = true;
        textTurn.setText("Turn O");
        img_stroke.setImageBitmap(null);
    }
}
