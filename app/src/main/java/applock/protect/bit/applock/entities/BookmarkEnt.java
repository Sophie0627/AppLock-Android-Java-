package applock.protect.bit.applock.entities;

public class BookmarkEnt {

	private int _Id;
	private String _Url;
	private String _CreateDate;
	
	public void SetId(int id){
		this._Id = id;
	}
	
	public void SetURL(String url){
		this._Url = url;
	}
	
	public void SetCreateDate(String createDate){
		this._CreateDate = createDate;
	}
	
	public int GetId(){
		return this._Id;
	}
	
	public String GetURL(){
		return this._Url;
	}
	
	public String GetCreateDate(){
		return this._CreateDate;
	}
}
