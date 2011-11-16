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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.pulimento.wifi.dialogs.AboutDialog;

public class ShowPass extends Activity {

	private Context mContext;
	private ClipboardManager mClipboardManager;
	


	private Button showpass;
	private Button clipboard;
	private Button back;
	private TextView mESSID;
	private TextView mBSSID;
	static WirelessNetwork current = new WirelessNetwork("CURRENT", "CURRENT", 1, "wawawa");
	static List<String> claves = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showpass);

		mContext = this;
		mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

		procesarListaClaves(ShowPass.current.getPassword());		
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
		    
	        if(ShowPass.current.getPassword()==null){
	        	Toast t = Toast.makeText(mContext, getString(R.string.showpass_null_password), Toast.LENGTH_LONG);
	        	t.show();
	        	finish();
	        }
	        
	        if(ShowPass.current.getPassword()!=null && ShowPass.current.getPassword().equals("NOPASSNOPASSNOPASSNOPASS")){
				Toast.makeText(mContext, R.string.showpass_network_nopass_toast, Toast.LENGTH_LONG).show();
				Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				this.finish();
			}
	}
	
	public void showpass(){
		String clave = ShowPass.current.getPassword();
   		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(clave)
		       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
        if(ShowPass.current.getPassword().length() > 21) {
        	builder.setTitle(R.string.showpass_popup_title_many);
        } else {
			 builder.setTitle(getString(R.string.showpass_popup_title));
        }
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void clipboard(String clave){
		mClipboardManager.setText(clave);
    	Toast.makeText(mContext, getString(R.string.showpass_toclipboard)+"( "+clave+" )", Toast.LENGTH_LONG).show();
		Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
		this.finish();
	}
	
	public void back(){
		this.finish();
	}
	
	public void procesarListaClaves(String clave) {
		if (clave == null) {
			Toast.makeText(mContext, R.string.showpass_null_password , Toast.LENGTH_LONG).show();
			this.finish();
		}
		claves.clear();
		if(clave.length() < 21)
			ShowPass.claves.add(clave);
		else {
			String[] temp = clave.split("\n");
			for(String tmp : temp)
				if(!tmp.contains("null"))
					claves.add(tmp);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_generic, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ACERCA_DE:
			(new AboutDialog(mContext)).show();
			return true;
		case R.id.SALIR:
			this.finish();
			return true;
		}
		return false;
	}
}
