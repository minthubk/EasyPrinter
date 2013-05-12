
package com.easyprinter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author victor
 */
public class SearchFile extends Activity {
	FileListAdapter filelistadapter;
	
	/**
	 * Called when the activity is first created.
	 * @param icicle	Bundle
	 */
	@Override
	protected void onCreate(Bundle icicle) {
		final ListView listView;
		final Context c = SearchFile.this;
		super.onCreate(icicle);
		setContentView(R.layout.search_main);
		
		listView = (ListView) findViewById(R.id.list_files);
		filelistadapter = new FileListAdapter();
		
		final ProgressDialog progressdialog = ProgressDialog.show(this, "Cargando", "Espere un momento...", true);
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					Thread.sleep(3000);
					progressdialog.dismiss();
				}catch(InterruptedException ex){
					Log.e("Thread", ex.getMessage());
				}
			}
		}).start();
		
		Search();
		
		if ( filelistadapter == null ) Log.e("Search file", "error");
		listView.setAdapter(filelistadapter);		
		
		listView.setOnItemClickListener(new OnItemClickListener(){
			
			/**
			 * Set a listener on an item override the method from the interface OnItemClickListener
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				TextView tx = (TextView)view.findViewById(R.id.txt_list_absolute_path);
				String s = tx.getText().toString();
				
				Intent data = new Intent();
				data.putExtra("fichero", s);
				setResult(Activity.RESULT_OK, data);
				finish();
			}
		});
	}
	
	/**
	 * onDestroy method
	 */
	@Override
	protected void onDestroy(){
		super.onDestroy();
		finish();
	}
	
	/**
	 * Search files in the sdcard
	 * Note: future versions set parameter for a custom search in a directory
	 */
	public void Search(){
		try{
			File f = new File("/mnt/sdcard");
			if ( f.isDirectory() ){
				File[] docfiles = f.listFiles(new FilenameFilter(){
					@Override
					public boolean accept(File dir, String name){
						return name.endsWith(".pdf") || name.endsWith(".txt");
					}
				});
				
				for(int i=0;i<docfiles.length;i++){
					filelistadapter.add(new FileList(f.getPath(), docfiles[i].getName()));
				}
			}
		}catch(Exception ex){
			Log.e("SearchFile", ex.getMessage());
		}
	}
	
	/**
	 * Cancel de search and return to the previous activity
	 * @param view		the view of the button
	 */
	public void onCancelSearch(View view){
		onDestroy();
	}
}
