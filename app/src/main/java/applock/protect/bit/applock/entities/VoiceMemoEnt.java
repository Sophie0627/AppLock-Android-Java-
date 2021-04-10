package applock.protect.bit.applock.entities;

import java.io.File;

public class VoiceMemoEnt {
	
	private int _id;
	private String _filename;
	private String _fileoriginalpath;
	private String _filelocalpath;
	private File _file;
	private Boolean _isCheck;
	private int _ISDCard;
	
	public void SetId(int id){
		this._id = id;
	}
	
	public void SetFileName(String filename){
		this._filename = filename;
	}
	
	public void SetFile(File file){
		this._file = file;
	}
	
	public void SetFileOriginalPath(String fileoriginalpath){
		this._fileoriginalpath = fileoriginalpath;
	}
	
	public void SetFileLocalPath(String filelocalpath){
		this._filelocalpath = filelocalpath;
	}
	
	public void SetFileCheck(Boolean isCheck){
		this._isCheck = isCheck;
	}
	
	public int GetId(){
		 return this._id;
	}
	
	public String GetFileName(){
		return this._filename;
	}
	
	public File GetFile(){
		return this._file;
	}
	
	public String GetFileOriginalPath(){
		return this._fileoriginalpath;
	}
	
	public String GetFileLocalPath(){
		return this._filelocalpath;
	}
	
	public Boolean GetFileCheck(){
		return this._isCheck;
	}
	
	public int getISDCard(){
		return _ISDCard;
	}
	public void setISDCard(int ISDCard){
		this._ISDCard = ISDCard;
	}
	

}
