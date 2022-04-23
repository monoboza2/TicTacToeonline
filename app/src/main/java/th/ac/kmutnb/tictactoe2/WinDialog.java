package th.ac.kmutnb.tictactoe2;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

public class WinDialog extends Dialog {
    private String message;
    public WinDialog(@NonNull Context context, String message){
        super(context);
        this.message = message;
    }
}
