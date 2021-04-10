package applock.protect.bit.applock.entities;

public class ImportAlbumEnt {


		private int _id;
		private String _arrPath;
		private String _albumName;
		private String _activity_type;
		private boolean _isCheck;
		
		public void SetId(int id){
			this._id = id;
		}
		
		public int GetId(){
			 return this._id;
		}
		
		public String Get_Activity_type(){
			return _activity_type;
		}
		
		public void Set_Activity_type(String activity_type){
			this._activity_type = activity_type;
		}
		
		public void SetPath(String arrPath){
			this._arrPath = arrPath;
		}
		
		public String GetPath(){
			 return this._arrPath;
		}
		
		public void SetAlbumName(String albumName){
			this._albumName = albumName;
		}
		
		public String GetAlbumName(){
			 return this._albumName;
		}
		
		public boolean GetAlbumFileCheck(){
			return this._isCheck;
		}
		public void SetAlbumFileCheck(boolean isCheck){
			this._isCheck = isCheck;
		}
}
