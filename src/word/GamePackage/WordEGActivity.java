package word.GamePackage;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class WordEGActivity extends FragmentActivity {
	/** Called when the activity is first created. */
	private LinearLayout l;
	private AdView adView;
	public wordGameView vi;
	public long start_time= 0;
	public static int score = 0;
	public static boolean isBestScore ;
	public int bestScore = 999;
	private dialoog diag ;
    public boolean sound;
    public boolean vib;
	public Vibrator vibrator;
	private Bundle saved ;
	int i =0;
    String mode = "Classic";
	WordEGActivity weg = this;
	int period = 30;
	int level = 0;
	SharedPreferences settings;
	FacebookFragment mainFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings =  getPreferences(0);
	    diag = new dialoog(this, this);
	    weg = this;
	    sound = true;
	    vib = true;
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		try{
			String data = settings.getString("score7", "999"+",Classic,0")+"";
			bestScore = Integer.parseInt(data.split(",")[0]);
			mode = data.split(",")[1];
			level = Integer.parseInt(data.split(",")[2]);
		}catch(Exception e){
			
		}
		saved = savedInstanceState;
		startGame();
//		start_time = System.currentTimeMillis();
//		endGame();
	}
	
	/*
	 * called in the start of game App
	 * and preparing the layer of playing area   
	 */
	public void startGame(){
		start_time = System.currentTimeMillis();
		vi = new wordGameView(this, this);	
		setContentView(R.layout.setting);
		l = new LinearLayout(this);
		l.setOrientation(1);
		View linearLayout =  findViewById(R.id.linearLayout1);	
		((ViewGroup)linearLayout.getParent()).removeView(linearLayout);
		l.addView(linearLayout,0);
		setContentView(l);	
		String ads_ID = getString(R.string.ads_ID);
        adView = new AdView(this, AdSize.BANNER, ads_ID);
        l.addView(adView,1);
        adView.loadAd(new AdRequest());
        l.addView(vi,2);
		buttin_lis();
		
	}
	
	/*
	 * called when the player decide to play again
	 */
	public void startNewGame(){
		start_time = System.currentTimeMillis();
		vi.reint();	
		setContentView(l);
		vi.invalidate();
		buttin_lis();
	}
	
	/*
	 *this method called when the player
	 *find all the words and the game ended
	 *it counts his score in animated way, check for high score and store it and display 
	 *play again and exit buttons
	 */
	public void endGame() {
		i=0;
		score = (int) ((System.currentTimeMillis()- start_time) /1000);
		setContentView(R.layout.win_main);
	    level++;
		    Handler handler = new Handler(); 
		    handler.postDelayed(new Runnable() { 
		         public void run() { 
		     		setContentView(R.layout.score);
		     		final RelativeLayout smileF = (RelativeLayout)findViewById(R.id.relativeLayout1);
		     		final LinearLayout social_layer = (LinearLayout)findViewById(R.id.social_layer);
		     		final ImageView score1 = (ImageView)findViewById(R.id.image0);
		    		final ImageView score2 = (ImageView)findViewById(R.id.Image1);
		    		final ImageView score3 = (ImageView)findViewById(R.id.Image2);
		    		smileF.setVisibility(100);
		    		social_layer.setVisibility(100);
		    		final Timer T=new Timer();
		    		T.scheduleAtFixedRate(new TimerTask() {         
		    		        @Override
		    		        public void run() {
		    		            runOnUiThread(new Runnable()
		    		            {
		    		                @Override
		    		                public void run()
		    		                {  
		    			    			score1.setImageResource(getNumberImage(i/100));
		    			    			score2.setImageResource(getNumberImage((i-(i/100)*100)/10));
		    			    			score3.setImageResource(getNumberImage(((i-(i/100)*100)- ((i-(i/100)*100)/10)*10)));
		    		                	           
		    		                	if(i>=score || i ==999){
		    		                		T.cancel();
	    		                		    social_layer.setVisibility(0);
	    		                		    isBestScore = false;
		    		                		if (score < bestScore ){
		    		                			smileF.setVisibility(0);
		    		                		    bestScore = score;
		    		                		    isBestScore = true;
		    		                		    playsound(2);
		    		                		    //start share intent automatically 
		    		            				Intent myIntent = new Intent(weg, FacebookActivity.class);
		    		            				myIntent.putExtra("score", weg.score);
		    		            				weg.startActivity(myIntent);		    		                		    
		    		                		}
	    		                		    shareLis();
		    		                		lis();
		    		                	}else
		    		                		i++;     	
		    		                }
		    		            });
		    		        }
		    		    }, score , period);
		         } 
		    }, 1500); 

	}
	
	
	public void shareLis(){

		ImageView fb = (ImageView) findViewById(R.id.imageView2); //fb
 		fb.setOnClickListener(new OnClickListener() {
 			public void onClick(View arg0) {
				Intent myIntent = new Intent(weg, FacebookActivity.class);
				myIntent.putExtra("score", weg.score);
				weg.startActivity(myIntent);
 			}
 		});
 		
		ImageView twi = (ImageView) findViewById(R.id.imageView3); //twitter
 		twi.setOnClickListener(new OnClickListener() {
 			public void onClick(View arg0) {
				Toast.makeText(weg, "Tweet", 1000).show();
				final Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		        shareIntent.setType("text/plain");
		        if (isBestScore)
		            shareIntent.putExtra(Intent.EXTRA_TEXT,  "I have got a new best score "+score+"s in #word_search_game http://bit.ly/1fhxe46");
		        else
		            shareIntent.putExtra(Intent.EXTRA_TEXT,  "I scored "+score+"s in #word_search_game http://bit.ly/1fhxe46 ");
		        PackageManager pm = getPackageManager();
		        final List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
			
				for (final ResolveInfo app : activityList) {
		            if ((app.activityInfo.name).contains("twitter")) {
		                final ActivityInfo activity = app.activityInfo;
		                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);

		                System.out.println("package name"+name);
		                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		                shareIntent.setComponent(name);
		                startActivity(shareIntent);
		            }
				 }	
 			}
 		});
	}
	
	/*
	 * this method listen to the user click on 
	 * the buttons on the score layer
	 */
	private void lis() {
		// TODO Auto-generated method stub
 		Button newGame = (Button) findViewById(R.id.newGame);
 		newGame.setOnClickListener(new OnClickListener() {
 			public void onClick(View arg0) {
				startNewGame();
 			}
 		});
 		Button exit = (Button) findViewById(R.id.exit);
 		exit.setOnClickListener(new OnClickListener() {
 			public void onClick(View arg0) {
 	 			SharedPreferences.Editor editor = settings.edit();
 			    editor.putString("score7", bestScore+","+mode+","+level );  
 			    editor.commit();
 				System.exit(0);
 			}
 		});	
	}

	public void playsound(int type ){
		if (vib) 
			vibrator.vibrate(200);
		if (sound && type == 2)
		{
			MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.highscore);
			mPlayer.start();
		}
	}
	
	/*
	 * this method map each number to its image
	 * to be displayed during the score counting 
	 */
	public int getNumberImage(int i ) {
		if (i==0)
			return R.drawable.n0;
		else if (i==1)
			return R.drawable.n1;
		else if (i==2)
			return R.drawable.n2;
		else if (i==3)
			return R.drawable.n3;
		else if (i==4)
			return R.drawable.n4;
		else if (i==5)
			return R.drawable.n5;
		else if (i==6)
			return R.drawable.n6;
		else if (i==7)
			return R.drawable.n7;
		else if (i==8)
			return R.drawable.n8;
		else 
			return R.drawable.n9;
	}

	   @Override
	    protected void onStop(){
	       super.onStop();
	    }
	   
	   /*
	    *save the level and score when the game closed or paused 
	    */
	   @Override
	   protected void onDestroy() {
		    super.onDestroy();
 			SharedPreferences.Editor editor = settings.edit();
		    editor.putString("score7", bestScore+","+mode+","+level );  
		    editor.commit();
	    }
	   
		@Override
		public void onPause() {
		    super.onPause();
 			SharedPreferences.Editor editor = settings.edit();
		    editor.putString("score7", bestScore+","+mode+","+level );  
		    editor.commit();
		}
	   
	   /*
	    * the method listen to user touch on the header buttons 
	    * and start displaying there dialog box
	    */
	   private void buttin_lis() {
			// TODO Auto-generated method stub
			ImageView new_game_button = (ImageView) findViewById(R.id.new_game_view);
			ImageView score_button = (ImageView) findViewById(R.id.score_icon_view2);
			ImageView mode_button = (ImageView) findViewById(R.id.mode_icon_View);
			final ImageView vib_button  = (ImageView) findViewById(R.id.imageView1);
			
			new_game_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					diag.msg =  "Do you want to start a new game ?";
					diag.show();
				}
			});
			
			score_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					diag.msg =  "Your best score is: "+bestScore;
					diag.score = score;
					diag.show();
				}
			});
			
			mode_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					String newMode= "Modern";
					if(mode.equalsIgnoreCase("Modern"))
						newMode= "classic";
					diag.msg =   "Do you want to change game theme to "+newMode;
					diag.newMode = newMode;
					diag.show();
				}
			});
			vib_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(vib){
						vib_button.setImageResource(R.drawable.devib_icon);
						vib = false;
					}else{
						vib_button.setImageResource(R.drawable.vib_icon);
						vib = true;
					}
						
						
				}
			});
		}
}
