package com.jp.test;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainTabActivity extends TabActivity {
	public void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab_main);

	    Resources res = getResources(); // referencia para obtener los Drawables para las pestañas
	    TabHost tabHost = getTabHost();  // el TabHost del Activity

	    // Se agregan los tabs deseados
	    tabHost.addTab(tabHost.newTabSpec("list1").setIndicator("Lista 1",
                res.getDrawable(R.drawable.ic_tab_example))
                .setContent(new Intent(this, ListadoActivity.class)));

	    tabHost.addTab(tabHost.newTabSpec("list2").setIndicator("Lista 3",
                res.getDrawable(R.drawable.ic_tab_example))
                .setContent(new Intent(this, ListadoActivity.class)));

	    tabHost.addTab(tabHost.newTabSpec("list3").setIndicator("Lista 2",
                res.getDrawable(R.drawable.ic_tab_example))
                .setContent(new Intent(this, ListadoActivity.class)));
	}
}
