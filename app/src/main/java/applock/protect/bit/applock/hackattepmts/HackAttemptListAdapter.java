package applock.protect.bit.applock.hackattepmts;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;

import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Security.SecurityLocksCommon;

public class HackAttemptListAdapter extends ArrayAdapter{

	private final Context con;
	private final ArrayList<HackAttemptEntity> hackAttemptEntitys;
	LayoutInflater layoutInflater;
	Resources res;
	boolean _isEdit = false;
	boolean _isAllCheck = false;
	
	public HackAttemptListAdapter(Context context, int simpleListItem1, ArrayList<HackAttemptEntity> list,boolean isEdit, Boolean isAllCheck) {
		super(context, simpleListItem1, list);
		
		this.con = context;
		this.hackAttemptEntitys = list;
		res = context.getResources();
		this._isEdit = isEdit;
		this._isAllCheck = isAllCheck;
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		
		 if (view == null) {
		 
			 view = layoutInflater.inflate(R.layout.hack_attempt_activity_item, null);
			
			viewHolder = new ViewHolder();
			
			//Typeface font = Typeface.createFromAsset(con.getAssets(), "ebrima.ttf");
			
			viewHolder.lbl_hackattempt_pass_item = (TextView) view.findViewById(R.id.lbl_hackattempt_pass_item);
			viewHolder.lbl_hackattempt_description_item = (TextView) view.findViewById(R.id.lbl_hackattempt_description_item);
			
			//viewHolder.lbl_hackattempt_pass_item.setTextColor(Color.parseColor(CommonAppTheme.WhiteColor));
			//viewHolder.lbl_hackattempt_pass_item.setTypeface(font);
			
			viewHolder.iv_hackattempt_item = (ImageView)view.findViewById(R.id.iv_hackattempt_item);
			
			viewHolder.cb_hackattempt_item = (CheckBox)view.findViewById(R.id.cb_hackattempt_item);
			final LinearLayout ll_hackattemptitem = (LinearLayout)view.findViewById(R.id.ll_hackattemptitem);
			
			final HackAttemptEntity hackAttemptEntity = this.hackAttemptEntitys.get(position);

			if(SecurityLocksCommon.LoginOptions.Password.toString().equals(hackAttemptEntity.GetLoginOption())){
				viewHolder.lbl_hackattempt_pass_item.setText("Wrong Password: " + hackAttemptEntity.GetWrongPassword());
			}
			else if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(hackAttemptEntity.GetLoginOption())){
				viewHolder.lbl_hackattempt_pass_item.setText("Wrong PIN: " + hackAttemptEntity.GetWrongPassword());
			}
			else if(SecurityLocksCommon.LoginOptions.Pattern.toString().equals(hackAttemptEntity.GetLoginOption())){
				viewHolder.lbl_hackattempt_pass_item.setText("Wrong Pattern: " + hackAttemptEntity.GetWrongPassword());
			}
			
			String DateTime = hackAttemptEntity.GetHackAttemptTime().replace("GMT+05:00", ""); // DateTime = DateTime.replace("GMT+05:00", "");
			
			viewHolder.lbl_hackattempt_description_item.setText(DateTime);
			viewHolder.cb_hackattempt_item.setChecked(hackAttemptEntity.GetIsCheck());
			//viewHolder.iv_hackattempt_item.setBackgroundResource(hackAttemptEntity.GetDrawable());
			
			if(hackAttemptEntity.GetImagePath().length() > 0){
				File f = new File(hackAttemptEntity.GetImagePath());
				viewHolder.iv_hackattempt_item.setImageBitmap(HackAttemptMethods.DecodeFile(f));
			}
			
			if(_isEdit){
				viewHolder.cb_hackattempt_item.setVisibility(View.VISIBLE);
					
			}else{			
				viewHolder.cb_hackattempt_item.setVisibility(View.INVISIBLE);
			}
			
			if(_isAllCheck){
				if(hackAttemptEntity.GetIsCheck())
					viewHolder.cb_hackattempt_item.setChecked(true);
			}
			
			viewHolder.cb_hackattempt_item.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					
					  int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
					  hackAttemptEntitys.get(getPosition).SetIsCheck(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
				}
			});
			
			view.setTag(viewHolder);
			view.setTag(R.id.lbl_hackattempt_pass_item, viewHolder.lbl_hackattempt_pass_item);
			view.setTag(R.id.lbl_hackattempt_description_item, viewHolder.lbl_hackattempt_description_item);
			view.setTag(R.id.iv_hackattempt_item, viewHolder.iv_hackattempt_item);
			view.setTag(R.id.cb_hackattempt_item, viewHolder.cb_hackattempt_item);
			
		}
		 else {
	        viewHolder = (ViewHolder) view.getTag();
	    }
		 
		 viewHolder.cb_hackattempt_item.setTag(position); // This line is important.
		 
		if(_isEdit){
			viewHolder.cb_hackattempt_item.setVisibility(View.VISIBLE);
			
		}else{
			viewHolder.cb_hackattempt_item.setVisibility(View.INVISIBLE);
			
		}
		 
		 	//viewHolder.lbl_hackattempt_pass_item.setText(hackAttemptEntitys.get(position).GetWrongPassword());
			if(SecurityLocksCommon.LoginOptions.Password.toString().equals(hackAttemptEntitys.get(position).GetLoginOption())){
				viewHolder.lbl_hackattempt_pass_item.setText("Wrong Password: " + hackAttemptEntitys.get(position).GetWrongPassword());
			}
			else if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(hackAttemptEntitys.get(position).GetLoginOption())){
				viewHolder.lbl_hackattempt_pass_item.setText("Wrong PIN: " + hackAttemptEntitys.get(position).GetWrongPassword());
			}
			else if(SecurityLocksCommon.LoginOptions.Pattern.toString().equals(hackAttemptEntitys.get(position).GetLoginOption())){
				viewHolder.lbl_hackattempt_pass_item.setText("Wrong Pattern: " + hackAttemptEntitys.get(position).GetWrongPassword());
			}
		 	
			String DateTime = hackAttemptEntitys.get(position).GetHackAttemptTime().replace("GMT+05:00", "");
			viewHolder.lbl_hackattempt_description_item.setText(DateTime);
			viewHolder.cb_hackattempt_item.setChecked(hackAttemptEntitys.get(position).GetIsCheck());
			//viewHolder.iv_hackattempt_item.setBackgroundResource(hackAttemptEntitys.get(position).GetDrawable());
			
			if(hackAttemptEntitys.get(position).GetImagePath().length() > 0){
				File f = new File(hackAttemptEntitys.get(position).GetImagePath());
				viewHolder.iv_hackattempt_item.setImageBitmap(HackAttemptMethods.DecodeFile(f));
			}
			
			Animation anim=AnimationUtils.loadAnimation(con, android.R.anim.slide_in_left);
			view.startAnimation(anim);
		
		return view;
	}
	
	public class ViewHolder {

		public TextView lbl_hackattempt_pass_item,lbl_hackattempt_description_item;
		public CheckBox cb_hackattempt_item;
		public ImageView iv_hackattempt_item;
		
	}

}
