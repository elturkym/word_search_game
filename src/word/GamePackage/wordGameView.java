package word.GamePackage;

import java.util.ArrayList;
import java.util.Random;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class wordGameView extends View implements OnTouchListener {
	ArrayList<ArrayList<Point>> pos = new ArrayList<ArrayList<Point>>();
	ArrayList<Point> pos2 = new ArrayList<Point>();
	ArrayList<String> Words;		// all the words 
	ArrayList<String> finalWords;  // words rows 
	int widthStep;
	int heighStep;
	int si =10;
	boolean firstv = true;
	String[][] map = null;
	Bitmap[][] bitmap = null;
	Bitmap[][] bitmap_c = null;
	private WordEGActivity wordEGActivity;

	public wordGameView(Context context, WordEGActivity wordEGActivity) {
		super(context);
		bitmap = new Bitmap[si][si];
		bitmap_c = new Bitmap[si][si];
		this.wordEGActivity = wordEGActivity;
		setFocusable(true);
		setFocusableInTouchMode(true);
		pos.add(pos2);
		widthStep = 0;
		heighStep = 0;
		Words = new ArrayList<String>();
		finalWords = new ArrayList<String>();
		this.setOnTouchListener(this);
	}
	
	public void reint(){
		firstv = true;
		pos.clear();
		pos2.clear();
		pos.add(pos2);
		Words.clear();
		finalWords.clear();
	}

	/*
	 *this method responsible to drawing the playing area
	 *first it initialize the map array 
	 *then draw the map
	 *then draw the line 
	 *then draw the remaining words under the map  
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		Paint p = new Paint();
		p.setTextSize(35);
		if (firstv) {
			map = getMap();
			firstv = false;
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					if (map[i][j] == null || map[i][j] == "ZZ") {
						Random r = new Random();
						int dir = Math.abs(r.nextInt() % 26);
						map[i][j] = "" + (char) (dir + 'a');
					}
					bitmap[i][j] = getBitmap_modern(map[i][j]);
					bitmap_c[i][j] = getBitmap_classic(map[i][j]);
				}
			}
			heighStep = (70*this.getHeight()/100) / map.length;
			widthStep = this.getWidth() / map.length;
		}
		p.setStyle(Paint.Style.FILL);
		int y = heighStep*map.length;

		// =================================
		p = new Paint();
		p.setTextSize(50);
		p.setColor(Color.BLACK);
		if(wordEGActivity.mode.equalsIgnoreCase("Modern")){
			this.setBackgroundResource(R.drawable.background_modern33);
			for (int i = 0; i < bitmap.length; i++) {
				for (int j = 0; j < bitmap[i].length; j++) {
					Rect rec = new Rect(widthStep * j + widthStep / 8, heighStep
							* i + heighStep / 8, (widthStep * j + widthStep)
							- widthStep / 8, (heighStep * i + heighStep)
							- heighStep / 8);
					canvas.drawBitmap(bitmap[i][j], null, rec, p);
				}
			}
		}else{
			this.setBackgroundResource(R.drawable.background_classic33);
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					Rect rec = new Rect(widthStep * j + widthStep / 8, heighStep
							* i + heighStep / 8, (widthStep * j + widthStep)
							- widthStep / 8, (heighStep * i + heighStep)
							- heighStep / 8);
					canvas.drawBitmap(bitmap_c[i][j], null, rec, p);
				}
			}
		}
		// ==============================
		p.setColor(Color.DKGRAY);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(widthStep/4); //20
		for (int j = 0; j < pos.size(); j++) {
			ArrayList<Point> pos2 = pos.get(j);
			if (pos2.size() > 0) {
				Point pre = pos2.get(0);
				for (int i = 1; i < pos2.size(); i++) {
					canvas.drawLine(pre.x, pre.y, pos2.get(i).x, pos2.get(i).y,
							p);
					pre = pos2.get(i);
				}
			}
		}
		// ==================================
		p = new Paint();
		p.setTextSize(widthStep/2);   //35
		adjustText(p);
		canvas.drawLine(0, y, this.getWidth(), y, p);
		for (int i = 0; i < finalWords.size(); i++) {
			canvas.drawText(finalWords.get(i), 0, y + 30, p);
			y += 30;
		}
	}

	/*
	 * display the remaining words and adjust them to the screen size
	 */
	private void adjustText(Paint p) {
		String outt = "";
		finalWords = new ArrayList<String>();
		float[] tsize = null;
		for (int i = 0; i < Words.size(); i++) {
			outt += " " + Words.get(i);
			tsize = new float[outt.length()];
			p.getTextWidths(outt, tsize);
			int sum = 0;
			for (int j = 0; j < tsize.length; j++) {
				sum += tsize[j];
			}
			if (sum > this.getWidth()) {
				finalWords.add(outt.substring(0, outt.lastIndexOf(" ")));
				outt = outt.substring(outt.lastIndexOf(" "), outt.length());
			}
		}
		finalWords.add(outt);
	}

	String out = "";

	/*
	 * the following to methods 
	 * maps the map the characters to its character image according to game theme style
	 * modern or classic
	 * and return bitmap of these images to be displayed to the player 
	 */
	private Bitmap getBitmap_modern(String letter) {
		Bitmap b = null;
		if (letter.equalsIgnoreCase("A")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.a);
		} else if (letter.equalsIgnoreCase("B")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.b);
		} else if (letter.equalsIgnoreCase("C")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.c);
		} else if (letter.equalsIgnoreCase("D")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.d);
		} else if (letter.equalsIgnoreCase("E")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.e);
		} else if (letter.equalsIgnoreCase("F")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.f);
		} else if (letter.equalsIgnoreCase("G")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.g);
		} else if (letter.equalsIgnoreCase("H")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.h);
		} else if (letter.equalsIgnoreCase("I")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.i);
		} else if (letter.equalsIgnoreCase("J")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.j);
		} else if (letter.equalsIgnoreCase("K")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.k);
		} else if (letter.equalsIgnoreCase("L")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.l);
		} else if (letter.equalsIgnoreCase("M")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.m);
		} else if (letter.equalsIgnoreCase("N")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.n);
		} else if (letter.equalsIgnoreCase("O")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.o);
		} else if (letter.equalsIgnoreCase("P")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.p);
		} else if (letter.equalsIgnoreCase("Q")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.q);
		} else if (letter.equalsIgnoreCase("R")) {
			b = BitmapFactory
					.decodeResource(this.getResources(), R.drawable.rr);
		} else if (letter.equalsIgnoreCase("S")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.s);
		} else if (letter.equalsIgnoreCase("t")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.t);
		} else if (letter.equalsIgnoreCase("u")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.u);
		} else if (letter.equalsIgnoreCase("v")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.v);
		} else if (letter.equalsIgnoreCase("w")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.w);
		} else if (letter.equalsIgnoreCase("x")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.x);
		} else if (letter.equalsIgnoreCase("y")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.y);
		} else if (letter.equalsIgnoreCase("z")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.z);
		}
		return b;
	}

	private Bitmap getBitmap_classic(String letter) {
		Bitmap b = null;
		if (letter.equalsIgnoreCase("A")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.a_1);
		} else if (letter.equalsIgnoreCase("B")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.b_1);
		} else if (letter.equalsIgnoreCase("C")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.c_1);
		} else if (letter.equalsIgnoreCase("D")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.d_1);
		} else if (letter.equalsIgnoreCase("E")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.e_1);
		} else if (letter.equalsIgnoreCase("F")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.f_1);
		} else if (letter.equalsIgnoreCase("G")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.g_1);
		} else if (letter.equalsIgnoreCase("H")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.h_1);
		} else if (letter.equalsIgnoreCase("I")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.i_1);
		} else if (letter.equalsIgnoreCase("J")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.j_1);
		} else if (letter.equalsIgnoreCase("K")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.k_1);
		} else if (letter.equalsIgnoreCase("L")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.l_1);
		} else if (letter.equalsIgnoreCase("M")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.m_1);
		} else if (letter.equalsIgnoreCase("N")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.n_1);
		} else if (letter.equalsIgnoreCase("O")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.o_1);
		} else if (letter.equalsIgnoreCase("P")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.p_1);
		} else if (letter.equalsIgnoreCase("Q")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.q_1);
		} else if (letter.equalsIgnoreCase("R")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.r_1);
		} else if (letter.equalsIgnoreCase("S")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.s_1);
		} else if (letter.equalsIgnoreCase("t")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.t_1);
		} else if (letter.equalsIgnoreCase("u")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.u_1);
		} else if (letter.equalsIgnoreCase("v")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.v_1);
		} else if (letter.equalsIgnoreCase("w")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.w_1);
		} else if (letter.equalsIgnoreCase("x")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.x_1);
		} else if (letter.equalsIgnoreCase("y")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.y_1);
		} else if (letter.equalsIgnoreCase("z")) {
			b = BitmapFactory.decodeResource(this.getResources(), R.drawable.z_1);
		}
		return b;
	}

	/*
	 *this method considered as the game loop
	 *game be processed with every touch 
	 */
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		out = "";
		int action = arg1.getAction();
		/*
		 * this part responsible to list to motion action and draw straight line from the start point 
		 * to the end point 
		 */
		if (action != MotionEvent.ACTION_UP) { 
			int newx = (int) arg1.getX();
			int newy = (int) arg1.getY();
			if(newx>0 && newx<widthStep*map.length && newy>0 && newy<heighStep*map.length  ){ // point the map area 
				int j = newx / widthStep;
				int i = newy / heighStep;
				Point p = new Point(((widthStep) / 2 + (widthStep * j)), heighStep
						/ 2 + (heighStep * i));
				try {
					Point lastP = pos.get(pos.size() - 1).get(0);
					int xf = p.x - lastP.x;
					int yf = p.y - lastP.y;
					int iF = Math.abs(lastP.y / heighStep - i);
					int jF = Math.abs(lastP.x / widthStep - j);
					if (xf == 0 || yf == 0 || iF - jF == 0) { // row or col or diagonal the add new point p 
						if (pos.get(pos.size() - 1).size() > 1)
							pos.get(pos.size() - 1).remove(1);
						pos.get(pos.size() - 1).add(p);
					}
				} catch (Exception e) {
					pos.get(pos.size() - 1).add(p);
				}
				invalidate();
			}
		/*
		 * when the  player raise up his finger 
		 * this part is responsible to move from the start point to the end point 
		 * and during visiting each sell the characters combined together to calculate the selected 
		 * word then if the words data contains this word it will be removed 
		 * if the data now is empty the game ended   
		 * else the line points will be removed as the word is incorrect 
		 */
		} else {
			if (pos.get(pos.size() - 1).size() > 1) {
				Point p1 = pos.get(pos.size() - 1).get(0);
				Point p2 = pos.get(pos.size() - 1).get(1);
				int i1 = p1.y / heighStep;
				int j1 = p1.x / widthStep;
				int i2 = p2.y / heighStep;
				int j2 = p2.x / widthStep;
				int di = 1;     // row step
				int dj = 1;		//col step 	
				if (i2 < i1) {
					di = -1;
				} else if (i1 == i2)
					di = 0;      
				if (j2 < j1) {
					dj = -1;
				} else if (j1 == j2)
					dj = 0;
				while (i1 != i2 || j1 != j2) { // count the selected word 
					out += map[i1][j1];
					i1 += di;
					j1 += dj;
				}
				out += map[i1][j1];
				if (Words.contains(out)) {
					Words.remove(out);
					wordEGActivity.playsound(1);
					if (Words.size() == 0) 		// end of game 
						wordEGActivity.endGame();
				} else {
					pos.remove(pos.size() - 1);
					out = "";
				}
				pos.add(new ArrayList<Point>());  // emtpy array list for the new line points positions 
			}
			invalidate();
		}

		return true;

	}

	/*
	 * this method is responsible to create randomly the characters 
	 * map according to giving words list
	 */
	private String[][] getMap() {
		String[] arr = null;
		int dir1 = wordEGActivity.level %12;
		/* 
		 * 9 because the numbers of data arrays is 9
		 * it should be incremented when adding new words array
		 */
		if (dir1 == 0) {
			arr = getResources().getStringArray(R.array.data1s);
		} else if (dir1 == 1) {
			arr = getResources().getStringArray(R.array.data2s);
		} else if (dir1 == 2) {
			arr = getResources().getStringArray(R.array.data3s);
		} else if (dir1 == 3) {
			arr = getResources().getStringArray(R.array.data4s);
		} else if (dir1 == 4) {
			arr = getResources().getStringArray(R.array.data5s);
		} else if (dir1 == 5) {
			arr = getResources().getStringArray(R.array.data6s);
		} else if (dir1 == 6) {
			arr = getResources().getStringArray(R.array.data7s);
		} else if (dir1 == 7) {
			arr = getResources().getStringArray(R.array.data8s);
		} else if (dir1 == 8) {
			arr = getResources().getStringArray(R.array.data9s);
		}else if (dir1 == 9) {
			arr = getResources().getStringArray(R.array.data10s);
		} else if (dir1 == 10) {
			arr = getResources().getStringArray(R.array.data11s);
		} else if (dir1 == 11) {
			arr = getResources().getStringArray(R.array.data12s);
		}
		
		map = new String[si][si];
		for (int i = 0; i < arr.length; i++) {
			Words.add(arr[i]);
			boolean ok = false;
			while (!ok) {
				Random r = new Random();
				int dir = Math.abs(r.nextInt() % 8);
				int ii = Math.abs(r.nextInt() % si);
				int jj = Math.abs(r.nextInt() % si);
				int ii2 = ii;
				int jj2 = jj;

				if (dir == 0) {
					for (int j = 0; j < arr[i].length() && ii >= 0 && ii < si
							&& jj < si && jj >= 0; j++) {
						if (map[ii][jj] == null || map[ii][jj].equals("")) {
							jj++;
						} else {
							break;
						}
						if (j == arr[i].length() - 1) {
							ok = true;
							System.out.println(dir + " " + i);
							for (int ju = 0; ju < arr[i].length(); ju++) {
								map[ii2][jj2] = "" + arr[i].charAt(ju);
								jj2++;
							}
						}
					}
				} else if (dir == 1) {
					for (int j = 0; j < arr[i].length() && ii >= 0 && ii < si
							&& jj < si && jj >= 0; j++) {
						if (map[ii][jj] == null || map[ii][jj].equals("")) {
							jj--;
						} else {
							break;
						}
						if (j == arr[i].length() - 1) {
							ok = true;
							System.out.println(dir + " " + i);
							for (int ju = 0; ju < arr[i].length(); ju++) {
								map[ii2][jj2] = "" + arr[i].charAt(ju);
								jj2--;
							}
						}
					}
				} else if (dir == 2) {
					for (int j = 0; j < arr[i].length() && ii >= 0 && ii < si
							&& jj < si && jj >= 0; j++) {
						if (map[ii][jj] == null || map[ii][jj].equals("")) {
							jj++;
							ii++;
						} else {
							break;
						}
						if (j == arr[i].length() - 1) {
							ok = true;
							System.out.println(dir + " " + i);
							for (int ju = 0; ju < arr[i].length(); ju++) {
								map[ii2][jj2] = "" + arr[i].charAt(ju);
								jj2++;
								ii2++;
							}
						}
					}
				} else if (dir == 3) {
					for (int j = 0; j < arr[i].length() && ii >= 0 && ii < si
							&& jj < si && jj >= 0; j++) {
						if (map[ii][jj] == null || map[ii][jj].equals("")) {
							jj--;
							ii--;
						} else {
							break;
						}
						if (j == arr[i].length() - 1) {
							ok = true;
							System.out.println(dir + " " + i);
							for (int ju = 0; ju < arr[i].length(); ju++) {
								map[ii2][jj2] = "" + arr[i].charAt(ju);
								jj2--;
								ii2--;
							}
						}
					}
				} else if (dir == 4) {
					for (int j = 0; j < arr[i].length() && ii >= 0 && ii < si
							&& jj < si && jj >= 0; j++) {
						if (map[ii][jj] == null || map[ii][jj].equals("")) {
							ii++;
						} else {
							break;
						}
						if (j == arr[i].length() - 1) {
							ok = true;
							System.out.println(dir + " " + i);
							for (int ju = 0; ju < arr[i].length(); ju++) {
								map[ii2][jj2] = "" + arr[i].charAt(ju);
								ii2++;
							}
						}
					}
				} else if (dir == 5) {
					for (int j = 0; j < arr[i].length() && ii >= 0 && ii < si
							&& jj < si && jj >= 0; j++) {
						if (map[ii][jj] == null || map[ii][jj].equals("")) {
							ii--;
						} else {
							break;
						}
						if (j == arr[i].length() - 1) {
							ok = true;
							System.out.println(dir + " " + i);
							for (int ju = 0; ju < arr[i].length(); ju++) {
								map[ii2][jj2] = "" + arr[i].charAt(ju);
								ii2--;
							}
						}
					}
				} else if (dir == 6) {
					for (int j = 0; j < arr[i].length() && ii >= 0 && ii < si
							&& jj < si && jj >= 0; j++) {
						if (map[ii][jj] == null || map[ii][jj].equals("")) {
							jj++;
							ii--;
						} else {
							break;
						}
						if (j == arr[i].length() - 1) {
							ok = true;
							System.out.println(dir + " " + i);
							for (int ju = 0; ju < arr[i].length(); ju++) {
								map[ii2][jj2] = "" + arr[i].charAt(ju);
								jj2++;
								ii2--;
							}
						}
					}
				} else if (dir == 7) {
					for (int j = 0; j < arr[i].length() && ii >= 0 && ii < si
							&& jj < si && jj >= 0; j++) {
						if (map[ii][jj] == null || map[ii][jj].equals("")) {
							jj--;
							ii++;
						} else {
							break;
						}
						if (j == arr[i].length() - 1) {
							ok = true;
							System.out.println(dir + " " + i);
							for (int ju = 0; ju < arr[i].length(); ju++) {
								map[ii2][jj2] = "" + arr[i].charAt(ju);
								jj2--;
								ii2++;
							}
						}
					}
				}
			}
		}
		return map;
	}
}
