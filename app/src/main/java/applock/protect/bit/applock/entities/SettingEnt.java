package applock.protect.bit.applock.entities;

public class SettingEnt {

	private int _SettingHeading;
	private int _SettingDesc;
	private int _drawable;
	
	public void SetSettingHeading(int settingHeading){
		this._SettingHeading = settingHeading;
	}
	
	public void SetSettingDesc(int settingDesc){
		this._SettingDesc = settingDesc;
	}
	
	public int GetSettingHeading(){
		return this._SettingHeading;
	}
	
	public int GetSettingDesc(){
		return this._SettingDesc;
	}
	
	public void SetDrawable(int drawable){
		this._drawable = drawable;
	}
	
	public int GetDrawable(){
		return this._drawable;
	}
	
	
}
