package applock.protect.bit.applock.entities;


public class DownloadFileEnt {

	private int _id;
	private String _fileName;
	private String _fileDownloadPath;
	private String _referenceId;
	private String _downloadFileUrl;
	private int _downloadType;
	private int _status;
	
	public void SetId(int id){
		this._id = id;
	}
	
	public void SetFileName(String filename){
		this._fileName = filename;
	}
	
	public void SetFileDownloadPath(String fileDownloadPath){
		this._fileDownloadPath = fileDownloadPath;
	}
	
	public void SetReferenceId(String referenceId){
		this._referenceId = referenceId;
	}
	
	public void SetDownloadFileUrl(String downloadFileUrl){
		this._downloadFileUrl = downloadFileUrl;
	}
	
	public void SetStatus(int status){
		this._status = status;
	}
	
	public void SetDownloadType(int downloadType){
		this._downloadType = downloadType;
	}
	
	public int GetId(){
		 return this._id;
	}
	
	public String GetFileName(){
		return this._fileName;
	}
	
	public String GetFileDownloadPath(){
		return this._fileDownloadPath;
	}
	
	public String GetReferenceId(){
		return this._referenceId;
	}
	
	public String GetDownloadFileUrl(){
		return this._downloadFileUrl;
	}
	
	public int GetStatus(){
		return this._status;
	}
	
	public int GetDownloadType(){
		return this._downloadType;
	}
	
}
