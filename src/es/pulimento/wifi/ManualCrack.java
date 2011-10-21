package es.pulimento.wifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class ManualCrack extends Activity {
	
	/////////////////////////////////////////////////////////////////////////
	//Atributos
	/////////////////////////////////////////////////////////////////////////
	
	private EditText e1;
	private EditText e2;
	private Button OK;
	private Context ctx;
	//private Activity activity = this;
    
	/////////////////////////////////////////////////////////////////////////
    /** Called when the activity is first created. */
	/////////////////////////////////////////////////////////////////////////        
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manualcrack);
        
        //Definimos elementos		
        ctx = getApplicationContext();        
        e1 = (EditText) findViewById(R.id.inputESSID);
        e1.setText("");
        //e1.setFocusable(true);
        //e1.setFocusableInTouchMode(true);
        e2 = (EditText) findViewById(R.id.inputMAC);
        e2.setText("");
        
        
        /*
         * AdMob
         */
         
         
         // Create the adView
         AdView adView = new AdView(this, AdSize.BANNER, "a14de2ce2207597");
         // Lookup your LinearLayout assuming it’s been given
         // the attribute android:id="@+id/mainLayout"
         LinearLayout layout = (LinearLayout)findViewById(R.id.ads);
         // Add the adView to it
         layout.addView(adView);
         // Initiate a generic request to load it with an ad
         AdRequest ar = new AdRequest();
         //ar.setTesting(false);/////////////////////////////////////////////////////////////////////////////////
         //ar.addTestDevice(AdRequest.TEST_EMULATOR);/////////////////////////////////////////////////////////////////////////////////
         adView.loadAd(ar);
        
        OK = (Button) findViewById(R.id.Button01);
        OK.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {			
				crack();
			}
		});
     }
    
    
    public String crack(){
    	String ESSID = e1.getText().toString();
    	String BSSID = e2.getText().toString();
    	boolean bssidOK = (BSSID.length() == 12 || BSSID.length() == 17);
    	if(ESSID == "" || !bssidOK){
    		Toast t = Toast.makeText(ctx,getString(R.string.manual_inputerror), Toast.LENGTH_SHORT);
    		t.show();    		
    		Log.e("pulWifi","Fallo en los inputs del usuario");
    		return null;
    	}
    	
    	//OJO!!
    	boolean crackeable = (new CrackNetwork(ESSID,BSSID,"WPA2")).isCrackeable();
		//wirelessNetList.add(new WirelessNetwork(wifi.SSID, wifi.BSSID,crackeable));
    	//WirelessNetwork w = new WirelessNetwork(ESSID,BSSID);
    	if(crackeable){
        	WirelessNetwork w = new WirelessNetwork(ESSID,BSSID,true,1,"wawawa");
    		String clave = 	(new CrackNetwork(w.getEssid(),w.getBssid(),"WPA2")).crackNetwork();
    		w.setClave(clave);
    		ShowPass.current = w;
    		Intent intent = new Intent(ctx, ShowPass.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
    		startActivity(intent);
    		return clave;
    	}else{
    		Toast t = Toast.makeText(ctx,getString(R.string.manual_inputerror), Toast.LENGTH_LONG);
    		t.show();
    		return null;
	}
}		
    
	/////////////////////////////////////////////////////////////////////////
    //Menú
	/////////////////////////////////////////////////////////////////////////    
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_manualmode, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.ACERCA_DE:
	    	sobre();
	        return true;
	    case R.id.NETWORKS:
	    	Builder aDialog = new AlertDialog.Builder(this);
			aDialog.setTitle(R.string.supported_networks_title);
			aDialog.setMessage(R.string.supported_networks);
			aDialog.setPositiveButton(R.string.splash_ask_dialog_ok_button, new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
				   dialog.cancel();
			   }
			});
			AlertDialog a = aDialog.create();
			a.show();
	        return true;    
	    case R.id.SALIR:
	    	this.finish();
	    	return true;
	    }
	    return false;
	}  
    
	/////////////////////////////////////////////////////////////////////////
    //Varios
	/////////////////////////////////////////////////////////////////////////    
    
	public void sobre(){
	    ScrollView svMessage = new ScrollView(this); 
	    TextView tvMessage = new TextView(this);
	    SpannableString spanText = new SpannableString(ctx.getText(R.string.acerca_de));
	    Linkify.addLinks(spanText, Linkify.ALL);
	    tvMessage.setText(spanText);
	    tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
	    tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
	    svMessage.setPadding(10, 0, 6, 0);
	    svMessage.addView(tvMessage);
				
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(svMessage)
			.setTitle(getString(R.string.about_title))
		    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       })
		       .setNegativeButton(getString(R.string.about_gpl), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					WebViewActivity.readme = false;
					abrirNavegador();
				}
			})
		       .setNeutralButton(getString(R.string.about_readme), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					WebViewActivity.readme = true;
					abrirNavegador();
				}
			});
		AlertDialog alert = builder.create();
		alert.show();    	
    }
    
   public void abrirNavegador(){
	   Intent i = new Intent(getApplicationContext(),WebViewActivity.class);
	   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
	   //Log.i("pulWifi", "Se va a proceder a abrir un navegador");
       // the results are called on widgetActivityCallback		
	   Context c = getApplicationContext();
       c.startActivity(i);
       //Log.i("pulWifi", "Navegador abierto satisfactoriamente ;)");
   }

}
