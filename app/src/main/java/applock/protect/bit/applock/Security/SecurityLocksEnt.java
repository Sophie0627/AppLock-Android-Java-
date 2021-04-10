package applock.protect.bit.applock.Security;

public class SecurityLocksEnt {
	
	private int _LoginOption;
	private int _drawable;
	private boolean _isCheck;
	
	public void SetLoginOption(int LoginOption){
		this._LoginOption = LoginOption;
	}
	public int GetLoginOption(){
		return this._LoginOption;
	}
	
	public boolean GetisCheck(){
		return this._isCheck;
	}
	
	public void SetisCheck(boolean isCheck){
		this._isCheck = isCheck;
	}
	
	public void SetDrawable(int drawable){
		this._drawable = drawable;
	}
	
	public int GetDrawable(){
		return this._drawable;
	}

}
