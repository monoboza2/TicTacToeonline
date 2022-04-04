package th.ac.kmutnb.tictactoe2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import th.ac.kmutnb.tictactoe2.Fragments.GameFragment;

public class GameboardAdapter extends RecyclerView.Adapter<GameboardAdapter.ViewHolder> {
    private Context context ;
    private ArrayList<Bitmap> arrBms;
    private Bitmap bmX, bmO;
    private Animation anim_x_o ;

    public GameboardAdapter(Context context, ArrayList<Bitmap> arrBms) {
        this.context = context;
        this.arrBms = arrBms;
        bmO = BitmapFactory.decodeResource(context.getResources(),R.drawable.o);
        bmX = BitmapFactory.decodeResource(context.getResources(),R.drawable.x);

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
        holder.img_cell_boardgame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(arrBms.get(position) == null ){
                    if(GameFragment.turnO){
                        arrBms.set(position,bmO);
                        GameFragment.turnO = false;
                        GameFragment.textTurn.setText("Turn X");
                    }
                    else{
                        arrBms.set(position,bmX);
                        GameFragment.turnO = true;
                        GameFragment.textTurn.setText("Turn O");
                    }
                    holder.img_cell_boardgame.setAnimation(anim_x_o);
                    notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrBms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_cell_boardgame;
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
