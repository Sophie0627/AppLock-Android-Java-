package applock.protect.bit.applock.hackattepmts;

public class HackAttemptEntity {
	
	private String _LoginOption;
	private String _wrongPassword;
	private String _imagePath;
	private String _hackAttemptTime;
	private boolean _isCheck;
	
	public String GetLoginOption(){
		return this._LoginOption;
	}
	
	public void SetLoginOption(String loginOption){
		this._LoginOption = loginOption;
	}
	
	public String GetWrongPassword(){
		return this._wrongPassword;
	}
	
	public void SetWrongPassword(String wrongPassword){
		this._wrongPassword = wrongPassword;
	}
	
	public String GetImagePath(){
		return this._imagePath;
	}
	
	public void SetImagePath(String imagePath){
		this._imagePath = imagePath;
	}
	
	public String GetHackAttemptTime(){
		return this._hackAttemptTime;
	}
	
	public void SetHackAttemptTime(String hackAttemptTime){
		this._hackAttemptTime = hackAttemptTime;
	}
	
	public boolean GetIsCheck(){
		return this._isCheck;
	}
	
	public void SetIsCheck(Boolean isCheck){
		this._isCheck = isCheck;
	}
	
}
