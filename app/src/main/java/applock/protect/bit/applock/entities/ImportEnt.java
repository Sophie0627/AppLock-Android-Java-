package applock.protect.bit.applock.entities;

import android.graphics.Bitmap;

public class ImportEnt {

	private int _id;
	private String _arrPath;
	private Bitmap _thumbnails;
	private Boolean _thumbnailsselection;
	private Boolean _ischeck;
	
	public void SetId(int id){
		this._id = id;
	}
	
	public void SetPath(String arrPath){
		this._arrPath = arrPath;
	}
	
	public void SetThumbnail(Bitmap thumbnails){
		this._thumbnails = thumbnails;
	}
	
	public void SetThumbnailSelection(Boolean thumbnailsselection){
		this._thumbnailsselection = thumbnailsselection;
	}
	
	public int GetId(){
		 return this._id;
	}
	
	public String GetPath(){
		 return this._arrPath;
	}
	
	public Bitmap GetThumbnail(){
		return this._thumbnails;
	}
	
	public Boolean GetThumbnailSelection(){
		return this._thumbnailsselection;
	}
	
}
