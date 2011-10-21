package es.pulimento.wifi;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class ActividadPestanias extends TabActivity {
	
	static Resources res;
	private TabHost mTabHost;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        
            mTabHost = (TabHost) findViewById(android.R.id.tabhost); /////////////
            mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider); //////////


//	        Debug.startAllocCounting();
//	        Debug.startMethodTracing("pulWifi");
//	        Debug.startNativeTracing();
	        res = getResources(); // Objeto Resource, para obtener los drawables 
	        android.widget.TabHost tabHost = getTabHost();  // La actividad TabHost
	        android.widget.TabHost.TabSpec spec;  // TabSpec reutilizable
	        Intent intent;  // Intent reutilizable para cada pestaña

	        // Crea un Intent para lanzar una actividad para la pestaña(para ser reutilizada)
	        intent = new Intent().setClass(this, SelectWirelessNetwork.class);
	        
	        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tabs_bg, null);
	        TextView title = (TextView) view.findViewById(R.id.tv_tabs);
	        title.setText(getString(R.string.scanMode));
	        //ImageView im = (ImageView) view.findViewById(R.id.imageview_tabs);
	        //im.setImageResource(R.drawable.scan_icon);
	        
	        // Inicializa un TabSpec por cada pestaña, y lo añade al TabHost
	        // Añadimos la primera pestaña
	        spec = tabHost.newTabSpec("ScanModeEnabled").setIndicator(view)
	                      .setContent(intent);
	        tabHost.addTab(spec);
	        
	        View view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tabs_bg, null);
	        TextView title2 = (TextView) view2.findViewById(R.id.tv_tabs);
	        title2.setText(getString(R.string.manualMode));
	        //ImageView im2 = (ImageView) view2.findViewById(R.id.imageview_tabs);
	        //im2.setImageResource(R.drawable.manual_icon);
	        
	     // Añadimos la segunda pestaña
	        intent = new Intent().setClass(this, ManualCrack.class);
	        spec = tabHost.newTabSpec("ManualModeEnabled").setIndicator(view2)
	                      .setContent(intent);
	        tabHost.addTab(spec);       
	        
	        // Por defecto, se abrirá en la pestaña de la derecha (modo manual)
	        tabHost.setCurrentTab(0);
	 }
}