package applock.protect.bit.applock.entities;

public class NotesEnt {
	
	private int _id;
	private String _title;
	private String _content;
	private String _fl_note_location;
		
	public void SetId(int id){
		this._id = id;
	}
	
	public void SetTitle(String title){
		this._title = title;
	}
	
	public void SetContent(String content){
		this._content = content;
	}
	
	public int GetId(){
		 return this._id;
	}
	
	public String GetTitle(){
		return this._title;
	}
	
	public String GetContent(){
		return this._content;
	}
	
	public String getFLNoteLocation(){
		return _fl_note_location;
	}
	public void setFLNoteLocation(String fl_note_location){
		this._fl_note_location = fl_note_location;
	}

}
