package th.ac.kmutnb.tictactoe2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import th.ac.kmutnb.tictactoe2.Fragments.GameFragment;

public class GameboardAdapter extends RecyclerView.Adapter<GameboardAdapter.ViewHolder> {
    private Context context ;
    static public ArrayList<Bitmap> arrBms , arrStrokes, arrBmTest;
    public static Bitmap bmX, bmO, draw;
    public static Animation anim_x_o , anim_stroke , anim_win ;
    public static String winCharacter = "o";
    private boolean checkMax = true;
    private int depth = 0;
    public static int positionOnline = 78 ;

    public GameboardAdapter(Context context, ArrayList<Bitmap> arrBms) {
        this.context = context;
        this.arrBms = arrBms;
        bmO = BitmapFactory.decodeResource(context.getResources(),R.drawable.o);
        bmX = BitmapFactory.decodeResource(context.getResources(),R.drawable.x);
        draw = BitmapFactory.decodeResource(context.getResources(),R.drawable.draw);

        arrStrokes = new ArrayList<>();
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.stroke1));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.stroke2));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.stroke3));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.stroke4));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.stroke5));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.stroke6));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.stroke7));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.stroke8));
        anim_stroke = AnimationUtils.loadAnimation(context,R.anim.anim_stroke);
        GameFragment.img_stroke.setAnimation(anim_stroke);
        anim_win = AnimationUtils.loadAnimation(context,R.anim.anim_win);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cell,parent,false)) ;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.img_cell_boardgame.setImageBitmap(arrBms.get(position));
        anim_x_o = AnimationUtils.loadAnimation(context,R.anim.anim_x_o);
        holder.img_cell_boardgame.setAnimation(anim_x_o);
        switch (GameActivity.gameMode){
            case 0 : playWithComputer(holder, position); break;
            case 1 : playWith2Player(holder, position); break;
            case 2 : playWithOnline(holder,position); break;
            default:playWithComputer(holder,position);
        }
        if(!checkWin()){
            checkDraw();
        }
    }


    public void playWithOnline(ViewHolder holder,int position){
        MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("board").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(Integer.class) >= 0 && snapshot.getValue(Integer.class) <=8){
                    positionOnline = snapshot.getValue(Integer.class) ;
                    if(arrBms.get(positionOnline)==null&&!checkWin() && (positionOnline>=0 && positionOnline <= 8)    ){
                        if (MultiplayerActivity.Playerturn.equals(MultiplayerActivity.PlayerNameO)){
                            arrBms.set(positionOnline,bmO);
                            MultiplayerActivity.Playerturn = MultiplayerActivity.PlayerNameX;
                        }
                        else{
                            arrBms.set(positionOnline,bmX);
                            MultiplayerActivity.Playerturn = MultiplayerActivity.PlayerNameO;
                        }
                        notifyItemChanged(positionOnline);
                        if (checkWin()){
                            win();
                        }
                        MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("turn").setValue(MultiplayerActivity.Playerturn);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.img_cell_boardgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MultiplayerActivity.Playerturn.equals(MultiplayerActivity.PlayerName)){
                    MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("board").setValue(position);
                }
            }
        });

    }



    private void playWith2Player(ViewHolder holder, int position) {
        holder.img_cell_boardgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrBms.get(position)==null&&!checkWin()){
                    if(GameFragment.turnO){
                        arrBms.set(position, bmO);
                        GameFragment.turnO = false;
                        GameFragment.textTurn.setText("turn of X");
                    }else{
                        arrBms.set(position, bmX);
                        GameFragment.turnO = true;
                        GameFragment.textTurn.setText("turn of O");
                    }
                    holder.img_cell_boardgame.startAnimation(anim_x_o);
                    if (checkWin()){
                        win();
                    }
                    notifyItemChanged(position);
                }
            }
        });
    }

    private void playWithComputer(ViewHolder holder, int position) {
        holder.img_cell_boardgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrBms.get(position)==null&&!checkWin()&&GameFragment.turnO){
                    if(GameFragment.turnO){
                        arrBms.set(position, bmO);
                        GameFragment.turnO = false;
                        GameFragment.textTurn.setText("turn of X");
                    }
                    holder.img_cell_boardgame.startAnimation(anim_x_o);
                    if (checkWin()){
                        win();
                    }
                    notifyItemChanged(position);
                    Handler handler = new Handler();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            arrBmTest = arrBms;
                            ArrayList<Mark> arrMark = solver(bmX);
                            if (arrMark.size()>0){
                                int max = arrMark.get(0).getPoint();
                                int id = 0;
                                for (int i = 0; i < arrMark.size(); i++) {
                                    if (max < arrMark.get(i).getPoint()) {
                                        max = arrMark.get(i).getPoint();
                                        id = i;
                                    }
                                }
                                int p = id;
                                arrBms.set(arrMark.get(p).getId(), bmX);
                                if (checkWin()){
                                    win();
                                }else{
                                    GameFragment.turnO = true;
                                    GameFragment.textTurn.setText("turn of O");
                                }
                                notifyItemChanged(arrMark.get(p).getId());
                            }
                        }
                    };
                    if (!checkWin()){
                        handler.postDelayed(r, 1000);
                    }
                }
            }
        });
    }

    private ArrayList<Mark> solver(Bitmap bm) {
        ArrayList<Mark> arrPoints = new ArrayList<>();
        for (int i = 0; i < 9; i++){
            if(arrBmTest.get(i)==null){
                if (bm==bmX) {
                    arrBmTest.set(i, bmX);
                }else{
                    arrBmTest.set(i, bmO);
                }
                if (checkWinTmp(bm)==-100){
                    if (bm==bmX){
                        depth++;
                        ArrayList<Mark> arr = solver(bmO);
                        depth--;
                        int minimum = 50;
                        int id = 50;
                        for (int j = 0; j < arr.size(); j++){
                            if (minimum>arr.get(j).getPoint()){
                                minimum = arr.get(j).getPoint();
                                id = i;
                            }
                        }
                        if (minimum!=50&&id!=50){
                            arrPoints.add(new Mark(i, minimum));
                        }
                    }else{
                        depth++;
                        ArrayList<Mark> arr = solver(bmX);
                        depth--;
                        int maximum = -50;
                        int id = -50;
                        for (int j = 0; j < arr.size(); j++){
                            if (maximum < arr.get(j).getPoint()){
                                maximum = arr.get(j).getPoint();
                                id = i;
                            }
                        }
                        if (maximum!=-50&&id!=-50) {
                            arrPoints.add(new Mark(i, maximum));
                        }

                    }
                }else{
                    if (bm == bmX){
                        arrPoints.add(new Mark(i, checkWinTmp(bm) - depth));
                    }else{
                        arrPoints.add(new Mark(i, -(checkWinTmp(bm) - depth)));
                    }
                }
                arrBmTest.set(i,null);
            }
        }
        return arrPoints;
    }

    private void checkDraw() {
        int count = 0 ;
        for( int i=0 ; i<arrBms.size() ; i++ ){
            if(arrBms.get(i)!=null){
                count++;
            }
        }
        if(count==9){
            GameFragment.rl_win.setVisibility(View.VISIBLE);
            GameFragment.rl_win.setAnimation(anim_win);
            GameFragment.img_win.setImageBitmap(draw);
            GameFragment.text_win.setText("draw");
        }
    }


    private int checkWinTmp(Bitmap bm) {
        int countRow = 0;
        for (int i = 0; i < 9; i++){
            if(i%3==0){
                countRow = 0;
            }
            if(arrBmTest.get(i)==bm){
                countRow++;
            }
            if (countRow==3){
                return 10;
            }
        }
        if (arrBmTest.get(0)==arrBmTest.get(3)&&arrBmTest.get(3)==arrBmTest.get(6)&&arrBmTest.get(0)==bm
                ||arrBmTest.get(1)==arrBmTest.get(4)&&arrBmTest.get(4)==arrBmTest.get(7)&&arrBmTest.get(1)==bm
                ||arrBmTest.get(2)==arrBmTest.get(5)&&arrBmTest.get(5)==arrBmTest.get(8)&&arrBmTest.get(2)==bm){
            return 10;
        }
        if (arrBmTest.get(0)==arrBmTest.get(4)&&arrBmTest.get(4)==arrBmTest.get(8)&&arrBmTest.get(0)==bm) return 10;
        if (arrBmTest.get(2)==arrBmTest.get(4)&&arrBmTest.get(4)==arrBmTest.get(6)&&arrBmTest.get(2)==bm) return 10;
        int count = 0;
        for (int i = 0; i < 9; i++){
            if (arrBmTest.get(i)!=null){
                count++;
            }
        }
        if (count==9){
            return 0;
        }
        return -100;
    }


    public void win() {
        GameFragment.img_stroke.startAnimation(anim_stroke);
        GameFragment.rl_win.setAnimation(anim_win);
        GameFragment.rl_win.setVisibility(View.VISIBLE);
        GameFragment.rl_win.startAnimation(anim_win);
        if(winCharacter.equals("o")){
            GameFragment.img_win.setImageBitmap(bmO);
            GameActivity.scoreO++;
            GameFragment.text_win_o.setText(": "+GameActivity.scoreO);
        }
        else{
            GameFragment.img_win.setImageBitmap(bmX);
            GameActivity.scoreX++;
            GameFragment.text_win_x.setText(": "+GameActivity.scoreX);
        }
        GameFragment.text_win.setText("win");
        if(GameActivity.gameMode == 2){
            MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("score").child(MultiplayerActivity.PlayerNameO).setValue(GameActivity.scoreO);
            MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("score").child(MultiplayerActivity.PlayerNameX).setValue(GameActivity.scoreX);
            if(winCharacter.equals("o")){
                MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("score").child("winner").setValue(MultiplayerActivity.PlayerNameO);
            }
            else{
                MultiplayerActivity.databaseReference.child("game").child(MultiplayerActivity.ConnectionID).child("score").child("winner").setValue(MultiplayerActivity.PlayerNameX);
            }
        }
    }
    public boolean checkWin() {
        if(arrBms.get(0) == arrBms.get(3) && arrBms.get(3) == arrBms.get(6) && arrBms.get(0)!=null ){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(2));
            checkWinCharacter(0);
            return true;
        }
        else if(arrBms.get(1) == arrBms.get(4) && arrBms.get(4) == arrBms.get(7) && arrBms.get(1)!=null ){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(3));
            checkWinCharacter(1);
            return true;
        }
        else if(arrBms.get(2) == arrBms.get(5) && arrBms.get(5) == arrBms.get(8) && arrBms.get(2)!=null ){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(4));
            checkWinCharacter(2);
            return true;
        }
        else if(arrBms.get(0) == arrBms.get(1) && arrBms.get(1) == arrBms.get(2) && arrBms.get(0)!=null ){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(5));
            checkWinCharacter(0);
            return true;
        }
        else if(arrBms.get(3) == arrBms.get(4) && arrBms.get(4) == arrBms.get(5) && arrBms.get(3)!=null ){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(6));
            checkWinCharacter(3);
            return true;
        }
        else if(arrBms.get(6) == arrBms.get(7) && arrBms.get(7) == arrBms.get(8) && arrBms.get(6)!=null ){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(7));
            checkWinCharacter(6);
            return true;
        }
        else if(arrBms.get(0) == arrBms.get(4) && arrBms.get(4) == arrBms.get(8) && arrBms.get(0)!=null ){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(1));
            checkWinCharacter(0);
            return true;
        }
        else if(arrBms.get(2) == arrBms.get(4) && arrBms.get(4) == arrBms.get(6) && arrBms.get(2)!=null ){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(0));
            checkWinCharacter(2);
            return true;
        }
        return false;
    }

    private void checkWinCharacter(int i) {
        if(arrBms.get(i) == bmO ){
            winCharacter = "o" ;
        }
        else{
            winCharacter = "x" ;
        }
    }

    @Override
    public int getItemCount() {
        return arrBms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView img_cell_boardgame;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cell_boardgame = itemView.findViewById(R.id.img_cell_boardgame);
        }
    }

    public ArrayList<Bitmap> getArrBms() {
        return arrBms;
    }

    public void setArrBms(ArrayList<Bitmap> arrBms) {
        this.arrBms = arrBms;
    }

}
