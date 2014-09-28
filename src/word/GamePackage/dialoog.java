package word.GamePackage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class dialoog extends Dialog {

	private Context  context;
	public  String msg ;
	public  int score;
	public  String newMode="";
	WordEGActivity listActivity2;
	public dialoog(Context context, WordEGActivity wEgActivity) {
		super(context);		
		this.context = context;
		this.listActivity2 = wEgActivity;
	}

	

	@Override
	public void show() {
		this.onCreate(null);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        if(msg.contains("start")){
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               })
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
              		    listActivity2.level++;
              		    listActivity2.startNewGame();
                      }
                  });

        // Create the AlertDialog object and return it
        }else if(msg.contains("score")){
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
           		    //nothing
                   }
               });	
        }else{ // Mode
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
             	   // create Object
             	   
                }
            })
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
           		    listActivity2.mode = newMode;
           		    listActivity2.vi.invalidate();
                   }
                
               });
        }
        builder.show();        	
    }
}