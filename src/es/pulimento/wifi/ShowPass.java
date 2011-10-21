package es.pulimento.wifi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class ShowPass extends Activity{
	
	//Atributos
	
	private Button showpass;
	private Button clipboard;
	private Button back;
	private Context ctx;
	private TextView mESSID;
	private TextView mBSSID;
	static WirelessNetwork current = new WirelessNetwork("CURRENT","CURRENT",false,1,"wawawa");
	static List<String> claves = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showpass);
		procesarListaClaves(ShowPass.current.getClave());		
		//le damos valor a los atributos
		ctx = getApplicationContext();
		mESSID = (TextView) findViewById(R.id.showpass_ESSID);
		mESSID.setText(ShowPass.current.getEssid());
		mBSSID = (TextView) findViewById(R.id.showpass_BSSID);
		mBSSID.setText(ShowPass.current.getBssid());
		showpass = (Button) findViewById(R.id.button_showpass);
		showpass.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View arg0) {
				showpass();
			}
		});
		clipboard = (Button) findViewById(R.id.button_clipboard);
		clipboard.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View arg0) {
				if(claves.size()==1)clipboard(claves.get(0));
				else{
					
	//				Log.e("pulWifi","Nuevo diálogo!!");
					
					//aquí hay que poner la paarafernalia del nuevo diálogo de escoger clave, sacado de RouterKeygen
					//.onCreateDialog, case 2 creo
					
					AlertDialog.Builder builder = new Builder(ShowPass.this);
					builder.setTitle(mESSID.getText());
				    LayoutInflater inflater = (LayoutInflater) ShowPass.this.getSystemService(LAYOUT_INFLATER_SERVICE);
				    View layout = inflater.inflate(R.layout.results,
				                                   (ViewGroup) findViewById(R.id.layout_root));
				    ListView list = (ListView) layout.findViewById(R.id.list_keys);
					list.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							String key = ((TextView)view).getText().toString();
							clipboard(key);
//							Toast.makeText(getApplicationContext(), key + " " 							
//									+ getString(R.string.msg_copied),
//									Toast.LENGTH_SHORT).show();
//							ClipboardManager clipboard = 
//								(ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
//		
//							clipboard.setText(key);
//							startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
						}
					});
					
					list.setAdapter(new ArrayAdapter<String>(ShowPass.this, android.R.layout.simple_list_item_1, claves));
					builder.setView(layout);
					Dialog d = builder.create();
					d.show();
					
				}
			}
		});
		back = (Button) findViewById(R.id.button_back);
		back.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View arg0) {
				back();
			}
		});
		
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
	        //ar.setTesting(false);
	        //ar.addTestDevice("04FED1DF0A6D24A7EF092E57617BED52"); //HTC Hero
	        //ar.addTestDevice(AdRequest.TEST_EMULATOR);/////////////////////////////////////////////////////////////////////////////////
	        adView.loadAd(ar);
	        
	        if(ShowPass.current.getClave()==null){
	        	Toast t = Toast.makeText(ctx, getString(R.string.showpass_null_password), Toast.LENGTH_LONG);
	        	t.show();
	        	finish();
	        }
	        
	        if(ShowPass.current.getClave()!=null && ShowPass.current.getClave().equals("NOPASSNOPASSNOPASSNOPASS")){
				Toast t = Toast.makeText(ctx,ctx.getString(R.string.showpass_network_nopass_toast), Toast.LENGTH_LONG);
				t.show();
				Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				this.finish();
			}
	        
//	        if(ShowPass.current.getClave()!=null && ShowPass.current.getClave().length() > 21){
//	        	clipboard.setEnabled(false);
//	        }
	}
	
	public void showpass(){
		String clave = ShowPass.current.getClave();
   		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(clave)
		       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
        if(ShowPass.current.getClave().length() > 21){
        	builder.setTitle(R.string.showpass_popup_title_many);
        }else{
			 builder.setTitle(getString(R.string.showpass_popup_title));
        }
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void clipboard(String clave){
		//String clave = ShowPass.current.getClave();
    	//Ya tenemos la clave, la copiamos al portapapeles
    	ClipboardManager c = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    	c.setText(clave);
    	Log.d("pulWifi","Copiado al portapapeles :"+clave);    	
    	//Mostramos aviso
    	String s = getString(R.string.showpass_toclipboard)+"( "+clave+" )";
    	Toast t = Toast.makeText(ctx,s, Toast.LENGTH_LONG);
		t.show();
		//Log.d("pulWifi","Ya se debería de haber mostrado el toast del portapapeles");    	
    	//Abrimos System Wireless Settings
		Context c2 = this.getApplicationContext();
		Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c2.startActivity(i);
		finish();
	}
	
	public void back(){
		finish();
	}
	
	public void procesarListaClaves(String clave){
		if (clave == null){
			Toast.makeText(ctx,ctx.getString(R.string.showpass_null_password) , Toast.LENGTH_LONG);
			finish();
		}
		claves.clear();
		if(clave.length()<21)ShowPass.claves.add(clave);
		else{
			//if(clave.contains("null"))clave.replace("null","");
			
			//else separar por \n e ir añadiendo claves
			//luego hay que quitar lo de inhabilitar el botón, y ver el tamaño de la lista
			//que si es uno, pues del tirón, si son más, que te muestre el results.xml de routerkeygen
			
			String[] temp = clave.split("\n");
			for(int i = 0;i<temp.length;i++){
				if(!temp[i].contains("null"))claves.add(temp[i]);
			}
		}
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////
	
	/////////////////////////////////////////////////////////////////////////
    //Menú
	/////////////////////////////////////////////////////////////////////////
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_generic, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.ACERCA_DE:
	    	sobre();
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
