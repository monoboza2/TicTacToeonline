package th.ac.kmutnb.tictactoe2.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import th.ac.kmutnb.tictactoe2.GameboardAdapter;
import th.ac.kmutnb.tictactoe2.R;

public class GameFragment extends Fragment {

    private RecyclerView rv_boardgame;
    private GameboardAdapter gameboardAdapter;
    public static boolean turnO = true;
    public static TextView textTurn ;
    private Button btn_reset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        rv_boardgame = view.findViewById(R.id.boardGame);
        textTurn = view.findViewById(R.id.textTurn);
        btn_reset = view.findViewById(R.id.resetBtn);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        for(int i=0;i<9 ;i++){
            arrBms.add(null);
        }
        gameboardAdapter = new GameboardAdapter(getContext(),arrBms);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        rv_boardgame.setLayoutManager(layoutManager);
        rv_boardgame.setAdapter(gameboardAdapter);
        btn_reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reset();
            }
        });
        return view ;
    }
    private void reset(){
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        for(int i=0;i<9;i++){
            arrBms.add(null);
        }
        gameboardAdapter.setArrBms(arrBms);
        gameboardAdapter.notifyDataSetChanged();
        turnO = true;
        textTurn.setText("Turn O");
    }
}
