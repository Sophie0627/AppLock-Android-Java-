package applock.protect.bit.applock.entities;

public class UserInfoEnt {

	private int _Id;
	private String _Name;
	private String _Password;
	private String _FakePassword;
	private String _Email;
	private String _MobileId;
	
	public void SetId(int id){
		this._Id = id;
	}
	
	public void SetName(String name){
		this._Name = name;
	}
	
	public void SetPassword(String password){
		this._Password = password;
	}
	
	public void SetFakePassword(String fakePassword){
		this._FakePassword = fakePassword;
	}
	
	public void SetEmail(String email){
		this._Email = email;
	}
	
	public void SetMobileId(String mobileId){
		this._MobileId = mobileId;
	}
	
	public int GetId(){
		 return this._Id;
	}
	
	public String GetName(){
		return this._Name;
	}
	
	public String GetPassword(){
		return this._Password;
	}
	
	public String GetFakePassword(){
		return this._FakePassword;
	}
	
	public String GetEmail(){
		return this._Email;
	}
	
	public String GetMobileId(){
		return this._MobileId;
	}
	
}
