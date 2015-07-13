package com.example.selectiongame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

	TextView tv;
	ViewGroup layout;
	ImageView iv;
	int selFruit;
	int fruitCount;
	HashSet < Integer > pickedRand;
	HashMap < Integer, Integer > basket;
	HashMap < Integer, String > basketItems;
	HashMap < Integer, Integer > basketCount;
	HashMap < Integer, Integer > basketCountNew;
	Random rd;
	int fruitLeftCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv = (TextView) findViewById(R.id.textView1);
		layout = (ViewGroup) findViewById(R.id.RelativeLayout1);

		rd = new Random();

		selFruit = rd.nextInt(6) + 1;
		fruitCount = rd.nextInt(8) + 1;

		pickedRand = new HashSet < Integer > ();

		while (pickedRand.size() != fruitCount) {
			int g = rd.nextInt(25);
			pickedRand.add(g);
		}

		fruitLeftCount = fruitCount;

		basket = new HashMap < Integer, Integer > ();
		basketItems = new HashMap < Integer, String > ();
		basketCount = new HashMap < Integer, Integer > ();
		basketCountNew = new HashMap < Integer, Integer > ();

		basket.put(1, R.drawable.apple);
		basket.put(2, R.drawable.lemon);
		basket.put(3, R.drawable.mango);
		basket.put(4, R.drawable.peach);
		basket.put(5, R.drawable.strawberry);
		basket.put(6, R.drawable.tomato);

		basketItems.put(1, "Apples");
		basketItems.put(2, "Lemons");
		basketItems.put(3, "Mangoes");
		basketItems.put(4, "Peaches");
		basketItems.put(5, "Strawberries");
		basketItems.put(6, "Tomatoes");

		tv.setText(String.format("Find All %s (%d)", basketItems.get(selFruit),
		fruitCount));

		int counter = -1;
		// loop through layout children
		for (int i = 0; i < layout.getChildCount(); i++) {
			if (layout.getChildAt(i) instanceof Button) {
				Button b = (Button) layout.getChildAt(i);
				b.setOnClickListener(this);
			} else if (layout.getChildAt(i) instanceof ImageView) {
				counter++;

				if (pickedRand.contains(counter)) {
					iv = (ImageView) layout.getChildAt(i);

					iv.setImageResource(basket.get(selFruit));
					iv.setTag(basket.get(selFruit));
					iv.setOnClickListener(this);

					if (basketCount.containsKey(basket.get(selFruit))) {
						basketCount.put(basket.get(selFruit),
						basketCount.get(basket.get(selFruit)) + 1);

					} else {
						basketCount.put(basket.get(selFruit), 1);
					}

				} else {
					iv = (ImageView) layout.getChildAt(i);

					int rdSel = rd.nextInt(6) + 1;
					while (rdSel == selFruit) {
						rdSel = rd.nextInt(6) + 1;
					}
					iv.setImageResource(basket.get(rdSel));
					iv.setOnClickListener(this);
					iv.setTag(basket.get(rdSel));

					if (basketCount.containsKey(basket.get(rdSel))) {
						basketCount.put(basket.get(rdSel),
						basketCount.get(basket.get(rdSel)) + 1);
					} else {
						basketCount.put(basket.get(rdSel), 1);
					}
				}
			}
		}
		basketCountNew = copyHashMap(basketCount);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.button1) { // reset button
			onCreate(new Bundle());
		} else if (v.getId() == R.id.button2) { // exit button

			finish();
		} else {
			iv = (ImageView) v;

			Log.d("imageView Tag", String.format("%s", iv.getTag().toString()));
			Log.d("drawable id",
			String.format("%s", basket.get(selFruit).toString()));
			if (iv.getTag().equals(basket.get(selFruit))) {
				int ImageAlphaSetCount = 0;
				if (iv.getAlpha() == 1f) {
					fruitLeftCount--;
					ImageAlphaSetCount++;
					ViewCompat.setAlpha(v, 0.5f);
					basketCount.put(basket.get(selFruit),
					basketCount.get(basket.get(selFruit)) - 1);

					tv.setText(String.format("Find All %s (%d)",
					basketItems.get(selFruit), fruitLeftCount));
					if (fruitLeftCount == 0) {

						AlertDialog.Builder builder = new AlertDialog.Builder(
						this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
						StringBuilder sb = new StringBuilder();

						builder.setMessage(getResources().getString(
						R.string.alert_dialog_text_2) + " " + basketItems.get(selFruit));

						builder.setCancelable(false);

						builder.setPositiveButton(R.string.button_text_ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
							int which) {
								// TODO Auto-generated method stub

							}
						});
						builder.setNegativeButton(
						R.string.button_text_new_game,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
							int which) {
								// TODO Auto-generated method stub

								onCreate(new Bundle());
							}
						});
						AlertDialog alert = builder.create();
						alert.setCanceledOnTouchOutside(false);
						alert.show();

					}
					ResetMap(basketCountNew);
					reShuffle();

				} else {
					// do nothing

				}

			} else {
				// dialog box with message game ends, with exit and reset button

				// TODO Auto-generated method stub

				AlertDialog.Builder builder = new AlertDialog.Builder(this,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
				builder.setMessage(R.string.alert_dialog_text_1);
				builder.setCancelable(false);

				builder.setPositiveButton(R.string.button_text_exit,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
					int which) {
						// TODO Auto-generated method stub

						finish();

					}
				});
				builder.setNegativeButton(R.string.button_text_reset,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
					int which) {
						// TODO Auto-generated method stub

						onCreate(new Bundle());
					}
				});
				AlertDialog alert = builder.create();
				alert.setCanceledOnTouchOutside(false);
				alert.show();
			}
		}

	}

	private void reShuffle() {
		// TODO Auto-generated method stub
		for (int i = 0; i < layout.getChildCount(); i++) {
			if (layout.getChildAt(i) instanceof ImageView) {
				iv = (ImageView) layout.getChildAt(i);
				if (iv.getAlpha() == 1f) {

					Boolean inBasketExist = false;
					while (inBasketExist == false) {
						int rdSel = rd.nextInt(6) + 1;
						if (basketCount.containsKey(basket.get(rdSel)) && basketCount.get(basket.get(rdSel)) >= 1) {
							iv.setImageResource(basket.get(rdSel));
							iv.setTag(basket.get(rdSel));
							iv.setOnClickListener(this);
							basketCount.put(basket.get(rdSel),
							basketCount.get(basket.get(rdSel)) - 1);
							basketCountNew.put(basket.get(rdSel),
							basketCountNew.get(basket.get(rdSel)) + 1);
							inBasketExist = true;
						}
					}

				} else {
					// do nothing
				}
			}

		}

		basketCount = copyHashMap(basketCountNew);
	}

	public HashMap < Integer, Integer > copyHashMap(
	HashMap < Integer, Integer > original) {
		HashMap < Integer, Integer > copy = new HashMap < Integer, Integer > ();
		int intKey, intValue;
		for (Integer key: original.keySet()) {
			intKey = key.intValue();
			intValue = original.get(key).intValue();
			copy.put(intKey, intValue);
		}
		return copy;
	}

	public void ResetMap(HashMap < Integer, Integer > bucketCountNew) {
		for (Integer key: bucketCountNew.keySet()) {
			bucketCountNew.put(key.intValue(), 0);
		}
	}



}