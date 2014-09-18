package com.pcs.adapters;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pcs.contactmanager.R;
import com.pcs.model.Contacts;

public class CustomAdapter extends BaseAdapter{

	private AlertDialog dialog;
	private Context context;
	private ArrayList<Contacts> contacts;
	private LayoutInflater layoutInflater;/** Initializing an inflATOR **/
	public CustomAdapter(Context context, ArrayList<Contacts> contacts) {
		super();
		this.context = context;
		this.contacts = contacts;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return contacts.size();
	}

	@Override
	public Object getItem(int position) {

		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		ViewHolder holder;
		
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.listview, null);

			holder=new ViewHolder();

			holder.name=(TextView) convertView.findViewById(R.id.name);
			holder.phone = (TextView)convertView.findViewById(R.id.phone);
			holder.email = (TextView)convertView.findViewById(R.id.email);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
		holder.call = (Button)convertView.findViewById(R.id.call_btn);

			convertView.setTag(holder);
		}
		
		else{

			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name.setText(contacts.get(position).getName());
		holder.phone.setText(contacts.get(position).getPhone());
		holder.email.setText(contacts.get(position).getEmail());
		holder.pic.setImageResource(R.drawable.contactimage);
			holder.call.setBackgroundResource(R.drawable.menu);
		
		holder.call.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
/**
 * Dialog to be displayed onclick of menu button of the Item in  list
 */
				 AlertDialog.Builder dialog_builder = new AlertDialog.Builder(context);
				/**Creating a dialog and setting title and action **/
				 
				dialog_builder.setTitle(R.string.action_title);
				
				dialog_builder.setItems(R.array.action, new android.content.DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
                          
                           /**
                            * Call and Message actions are set on click of items in dialog
                            */
                           switch(which){
                           case 0 :
                        	   String num = "tel:"+contacts.get(position).getPhone();
       						Intent intent = new Intent(Intent.ACTION_CALL);
       						intent.setData(Uri.parse(num));
       				         context.startActivity(intent);
       							break;
       							
                           case 1:
                        	  
          				         
          				       Intent msg_intent = new Intent(Intent.ACTION_VIEW);
          				     msg_intent.setType("vnd.android-dir/mms-sms");
          				     msg_intent.putExtra("address", "1234567890");
          				     msg_intent.putExtra("sms_body","Hello EveryBody!!!");
          				     context.startActivity(msg_intent);
          							break;
                           case 2:
                        	   Intent mail_intent = new Intent(Intent.ACTION_VIEW);
                        	   mail_intent.setType("vnd.android-dir/mms-sms");
                        	   mail_intent.putExtra("address", contacts.get(position).getPhone());
                        	   mail_intent.putExtra("sms_body","Hello EveryBody!!!");
                        	   context.startActivity(mail_intent);
                        	   
                        	   
                        	   default:
                        		   break;
                           }
						
						
					}
				});
				/**Creating and showing the dialog **/
				
				dialog= dialog_builder.create();
				dialog.show();	
				
						
					}
				});
				
		
			
		

		return convertView;
	}
	/**
	 * ViewHolder class to hold the view of items in the List
	 * @author pcs-05
	 *
	 */
	public class ViewHolder{
		public TextView name;
		public TextView email;
		public TextView phone;
		public ImageView pic;
		public Button call;
		
	}

	/**
	 * Creating a method to add users to listview
	 * @param contact- object for Contacts class
	 */
public void addUser(Contacts contact){
	if(contacts!=null){
		contacts.add(contact); 
	}
	else{
		throw new IllegalArgumentException("Field Should not be Null");
		
	}
}
}


