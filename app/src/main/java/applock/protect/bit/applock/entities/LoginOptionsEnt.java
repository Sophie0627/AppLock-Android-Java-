package applock.protect.bit.applock.entities;

public class LoginOptionsEnt {
	
		private int _LoginOption;
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
		
		
	}
