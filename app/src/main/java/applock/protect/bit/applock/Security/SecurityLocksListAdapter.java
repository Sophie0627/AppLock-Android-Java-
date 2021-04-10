package applock.protect.bit.applock.Security;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

import applock.protect.bit.applock.R;

public class SecurityLocksListAdapter extends ArrayAdapter{
	
	private final Context con;
	private final ArrayList<SecurityLocksEnt> SecurityCredentialsEntList;
	LayoutInflater layoutInflater;
	Resources res;

	@SuppressWarnings("unchecked")
	public SecurityLocksListAdapter(Context context, int simpleListItem1, ArrayList<SecurityLocksEnt> list) {
		super(context, simpleListItem1, list);

		this.con = context;
		this.SecurityCredentialsEntList = list;
		res = context.getResources();
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View view, ViewGroup parent) {

		view = layoutInflater.inflate(R.layout.security_locks_activity_item, null);

		TextView lblLoginOption = (TextView) view.findViewById(R.id.lblloginoptionitem);
		lblLoginOption.setTextColor(con.getResources().getColor(R.color.transparent_Black));
		ImageView iv_login_option_icon = (ImageView)view.findViewById(R.id.icon);
		//Typeface lblLoginOptionfont = Typeface.createFromAsset(con.getAssets(), "ebrima.ttf");
		//lblLoginOption.setTypeface(lblLoginOptionfont);

		final CheckBox cbLoginOptionitem = (CheckBox)view.findViewById(R.id.cbLoginOptionitem);
		final RelativeLayout ll_securitycredentials = (RelativeLayout)view.findViewById(R.id.ll_securitycredentials);
		cbLoginOptionitem.setClickable(false);

		final SecurityLocksEnt SecurityCredentialsEnt = this.SecurityCredentialsEntList.get(position);
		cbLoginOptionitem.setClickable(false);
		
		if(SecurityCredentialsEnt.GetisCheck()){
			ll_securitycredentials.setBackgroundResource(R.drawable.list_login_option_click);
			cbLoginOptionitem.setChecked(true);
			//cbLoginOptionitem.setBackground(R.drawable.select_security_icon_click);colorPrimary
			lblLoginOption.setTextColor(con.getResources().getColor(R.color.colorPrimary));
				
		}else{
			ll_securitycredentials.setBackgroundResource(R.color.Coloractivity_bg);
			//cbLoginOptionitem.setBackgroundResource(R.drawable.select_security_icon);
		}
		
		lblLoginOption.setText(SecurityCredentialsEnt.GetLoginOption());
		cbLoginOptionitem.setChecked(SecurityCredentialsEnt.GetisCheck());
		iv_login_option_icon.setBackgroundResource(SecurityCredentialsEnt.GetDrawable());
		
		return view;
	}

}
