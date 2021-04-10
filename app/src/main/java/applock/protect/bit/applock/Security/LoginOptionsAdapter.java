package applock.protect.bit.applock.Security;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import applock.protect.bit.applock.R;
import applock.protect.bit.applock.entities.LoginOptionsEnt;


public class LoginOptionsAdapter extends ArrayAdapter {

	private final Context con;
	private final ArrayList<LoginOptionsEnt> LoginOptionsEntList;
	LayoutInflater layoutInflater;
	Resources res;
	
	public LoginOptionsAdapter(Context context, int simpleListItem1, ArrayList<LoginOptionsEnt> list) {
		super(context, simpleListItem1, list);
		
		this.con = context;
		this.LoginOptionsEntList = list;
		res = context.getResources();
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
	
		view = layoutInflater.inflate(R.layout.login_options_item, null);
		
		TextView lblLoginOption = (TextView) view.findViewById(R.id.lblloginoptionitem);
		CheckBox cbLoginOptionitem = (CheckBox)view.findViewById(R.id.cbLoginOptionitem);
		
		final LoginOptionsEnt loginOptionsEnt = this.LoginOptionsEntList.get(position);
		cbLoginOptionitem.setClickable(false);
		
		lblLoginOption.setText(loginOptionsEnt.GetLoginOption());
		cbLoginOptionitem.setChecked(loginOptionsEnt.GetisCheck());
		
		return view;
	}
}
