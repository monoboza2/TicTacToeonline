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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import th.ac.kmutnb.tictactoe2.GameboardAdapter;
import th.ac.kmutnb.tictactoe2.R;

public class GameFragment extends Fragment {

    private RecyclerView rv_boardgame;
    private GameboardAdapter gameboardAdapter;
    public static boolean turnO = true;
    public static TextView textTurn , text_win_x , text_win_o , text_win;
    private Button btn_reset , btn_again , btn_home ;
    public static ImageView img_stroke , img_win;
    public static RelativeLayout rl_win;
    public static String Tag = GameFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        rv_boardgame = view.findViewById(R.id.boardGame);
        textTurn = view.findViewById(R.id.textTurn);
        btn_reset = view.findViewById(R.id.resetBtn);
        btn_again = view.findViewById(R.id.btn_again);
        btn_home = view.findViewById(R.id.btn_home);

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
        btn_reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reset();
            }
        });
        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_win.setVisibility(View.INVISIBLE);
                reset();
            }
        });
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                getFragmentManager().popBackStack();
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
        img_stroke.setImageBitmap(null);
    }
}
