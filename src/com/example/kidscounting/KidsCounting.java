package com.example.kidscounting;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class KidsCounting extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_kids_counting);
		
		String[] mString = new String[]{
				"Contar de 1 à 10 de 10 à 1",
				"Contagem de figura",
				"Aritimetica basica",
				"Sair"
		};
		this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mString));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kids_counting, menu);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
		switch(position){
		case 0:
			startActivity(new Intent(this, Numeros.class));
			break;
		case 1:
			startActivity(new Intent(this, Contagem.class));
			break;
		case 2:
			startActivity(new Intent(this, Aritimetica.class));
			break;
		default :
			finish();
		}
	}

}
