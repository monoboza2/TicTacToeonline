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

                    MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("score").child("winner").setValue("");
                    MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("board").setValue(-1);
                    MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("turn").setValue(MultiplayerActivity.Playerturn);
                }
            }
        });
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                getFragmentManager().popBackStack();
                //remove connection database
                if(GameActivity.gameMode == 2){
                    MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("turn").removeEventListener(MultiplayerActivity.turnEventListener);
                    MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("score").removeEventListener(MultiplayerActivity.scoreEventListener);
                    MultiplayerActivity.databaseReference.child("connections").child(MultiplayerActivity.ConnectionID).child(MultiplayerActivity.PlayeruniqueID).child("status").setValue("disconnect");
                    MultiplayerActivity.opponentfound = false;
                }
                //                try {
//                    MultiplayerActivity.databaseReference.child("connections").child(MultiplayerActivity.ConnectionID).getRef().removeValue();
//                }
//                catch (Error error){}

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
            MultiplayerActivity.turnEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(MultiplayerActivity.PlayerNameO != null){
                        MultiplayerActivity.Playerturn = snapshot.getValue(String.class);
                        //turn change
                        if(snapshot.getValue(String.class) != null){
                            if(snapshot.getValue(String.class).equals(MultiplayerActivity.PlayerNameO)){
                                GameFragment.textTurn.setText("turn of O");
                            }
                            else{
                                GameFragment.textTurn.setText("turn of X");
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            MultiplayerActivity.scoreEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if( MultiplayerActivity.PlayerNameO != null && MultiplayerActivity.PlayerNameX != null){
                        if(snapshot.child(MultiplayerActivity.PlayerNameO).getValue(Integer.class) != null){
                            GameActivity.scoreO = snapshot.child(MultiplayerActivity.PlayerNameO).getValue(Integer.class);
                        }
                        if(snapshot.child(MultiplayerActivity.PlayerNameX).getValue(Integer.class) != null){
                            GameActivity.scoreX = snapshot.child(MultiplayerActivity.PlayerNameX).getValue(Integer.class);
                        }
                    }
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
            };
            MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("board").setValue(-1);
            MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("turn").setValue(MultiplayerActivity.Playerturn);
            MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("turn").addValueEventListener(MultiplayerActivity.turnEventListener);
            if(MultiplayerActivity.ConnectionID != null && MultiplayerActivity.PlayerNameO != (null) && MultiplayerActivity.PlayerNameX != null){
                MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("score").child(MultiplayerActivity.PlayerNameO).setValue(GameActivity.scoreO);
                MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("score").child(MultiplayerActivity.PlayerNameX).setValue(GameActivity.scoreX);
                MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("score").child("winner").setValue("");
            }


            MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("score").addValueEventListener(MultiplayerActivity.scoreEventListener);
            MultiplayerActivity.databaseReference.child("connections").child(MultiplayerActivity.ConnectionID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i("Online","check?? : " +snapshot.child(MultiplayerActivity.OpponentuniqueID).child("status").getValue(String.class));
                    if(snapshot.child(MultiplayerActivity.OpponentuniqueID).child("status").getValue(String.class) != null && snapshot.child(MultiplayerActivity.PlayeruniqueID).child("status").getValue(String.class) != null ){
                        if(snapshot.child(MultiplayerActivity.OpponentuniqueID).child("status").getValue(String.class).equals("disconnect") && snapshot.child(MultiplayerActivity.PlayeruniqueID).child("status").getValue(String.class).equals("disconnect")){
                            Log.i("Online","disconnect all");
                            MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("turn").getRef().removeEventListener(MultiplayerActivity.turnEventListener);
                            MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("score").getRef().removeEventListener(MultiplayerActivity.scoreEventListener);
                            MultiplayerActivity.databaseReference.child("connections").getRef().removeEventListener(MultiplayerActivity.connectionEventListener);
                            MultiplayerActivity.databaseReference.child("connections").child(MultiplayerActivity.ConnectionID).getRef().removeValue();
                            Log.i("Online",MultiplayerActivity.databaseReference.get()+" <--data");
                            MultiplayerActivity.databaseReference.child("connections").removeEventListener(this);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            if(MultiplayerActivity.PlayerNameO == null){
                MultiplayerActivity.PlayerNameO = "Disconnected";
            }
            if(MultiplayerActivity.PlayerNameX == null){
                MultiplayerActivity.PlayerNameX = "Disconnected";
            }
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
