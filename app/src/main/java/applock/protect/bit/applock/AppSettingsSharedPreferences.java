package applock.protect.bit.applock;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettingsSharedPreferences {
	
	private static String _fileName = "AppSettings";
	/*private static String _IsDontShowThumbLockMsg = "IsDontShowThumbLockMsg";
	private static String _IsDontShowMsgLock = "IsDontShowThumbMsg";*/
	private static String _sortByPhotosAlbums = "SortByPhotosAlbums";
	private static String _sortByVideosAlbums = "SortByVideosAlbums";
	private static String _sortByDocumentFolders = "SortByDocumentFolders";
	private static String _sortByMiscellaneousFolders = "SortByMiscellaneousFolders";

	private static String _GallerySortBy = "GallerySortBy";
	private static String _PhotoAlbumViewIsGrid = "PhotoAlbumViewIsGrid";
	private static String _VideoAlbumViewIsGrid = "VideoAlbumViewIsGrid";
	private static String _DocAlbumViewIsGrid = "DocAlbumViewIsGrid";
	private static String _MiscAlbumViewIsGrid = "MiscAlbumViewIsGrid";

	private static String _viewByPhotos = "ViewByPhotos";
	private static String  _viewByVideos= "ViewByVideos";
	private static String  _viewByDocument = "ViewByDocument";
	private static String _viewByMiscellaneous = "ViewByMiscellaneous";
	private static String _GalleryViewBy = "GalleryViewBy";
	private static String _viewByAudio = "ViewByAudio";

	private static String _IsDontShowPhotoHelp = "IsDontShowPhotoHelp";
	private static String _IsDontShowVideoHelp = "IsDontShowVideoHelp";
	private static String _IsDontShowDocumentHelp = "IsDontShowDocumentHelp";
	private static String _IsDontShowMiscallaneousHelp = "IsDontShowMiscallaneousHelp";
	private static String _IsDontShowAudioHelp = "IsDontShowAudioHelp";
	private static String _IsDontShowWalletHelp = "IsDontShowWalletHelp";
	private static String _IsDontShowContactHelp = "IsDontShowContactHelp";
	private static String _IsDontShowCloudHelp = "IsDontShowCloudHelp";
	private static String _IsDontShowThumbLockMsg = "IsDontShowThumbLockMsg";
	private static String _IsDontShowMsgLock = "IsDontShowThumbMsg";

	private static String _IstimeStampInserted = "_IstimeStampInserted";
	
	private static AppSettingsSharedPreferences appSettingsSharedPreferences;
	static SharedPreferences myPrefs;
	static Context context;
	
	private AppSettingsSharedPreferences(){
	
	}
	
	public static AppSettingsSharedPreferences GetObject(Context con){
		
		if(appSettingsSharedPreferences == null)
			appSettingsSharedPreferences = new AppSettingsSharedPreferences();
		
		context = con;
		myPrefs = context.getSharedPreferences(_fileName, context.MODE_PRIVATE);
		
		return appSettingsSharedPreferences;
	}
	
	public boolean GetPhotoAlbumViewIsGrid(){
		return myPrefs.getBoolean(_PhotoAlbumViewIsGrid, true);
	}
	
	
	public void SetPhotoAlbumViewIsGrid(Boolean PhotoAlbumViewIsGrid){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_PhotoAlbumViewIsGrid, PhotoAlbumViewIsGrid);
		prefsEditor.commit();
	}
	
	public boolean GetVideoAlbumViewIsGrid(){
		return myPrefs.getBoolean(_VideoAlbumViewIsGrid, true);
	}
	
	
	public void SetVideoAlbumViewIsGrid(Boolean VideoAlbumViewIsGrid){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_VideoAlbumViewIsGrid, VideoAlbumViewIsGrid);
		prefsEditor.commit();
	}
	
	public boolean GetDocAlbumViewIsGrid(){
		return myPrefs.getBoolean(_DocAlbumViewIsGrid, true);
	}
	
	
	public void SetDocAlbumViewIsGrid(Boolean DocAlbumViewIsGrid){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_DocAlbumViewIsGrid, DocAlbumViewIsGrid);
		prefsEditor.commit();
	}

	public boolean GetMiscAlbumViewIsGrid(){
		return myPrefs.getBoolean(_MiscAlbumViewIsGrid, true);
	}


	public void SetMiscAlbumViewIsGrid(Boolean MiscAlbumViewIsGrid){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_MiscAlbumViewIsGrid, MiscAlbumViewIsGrid);
		prefsEditor.commit();
	}
	
	
	public int GetPhotosViewBy(){
		
		return myPrefs.getInt(_viewByPhotos, 1);
	}
	
	public void SetPhotosViewBy(int ViewBy){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_viewByPhotos, ViewBy);
		prefsEditor.commit();
	}
	
	public int GetVideosViewBy(){
		
		return myPrefs.getInt(_viewByVideos, 0);
	}
	
	public void SetVideosViewBy(int ViewBy){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_viewByVideos, ViewBy);
		prefsEditor.commit();
	}

	public int GetDocumentViewBy(){

		return myPrefs.getInt(_viewByDocument, 0);
	}

	public void SetDocumenViewBy(int ViewBy){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_viewByDocument, ViewBy);
		prefsEditor.commit();
	}

	public int GetMiscellaneousViewBy(){

		return myPrefs.getInt(_viewByMiscellaneous, 0);
	}

	public void SetMiscellaneousViewBy(int ViewBy){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_viewByMiscellaneous, ViewBy);
		prefsEditor.commit();
	}

	public int GetAudioViewBy(){

		return myPrefs.getInt(_viewByAudio, 0);
	}

	public void SetAudioViewBy(int ViewBy){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_viewByAudio, ViewBy);
		prefsEditor.commit();
	}

	public int GetPhotosAlbumsSortBy(){
		
		return myPrefs.getInt(_sortByPhotosAlbums, 0);
	}
	
	public void SetPhotosAlbumsSortBy(int SortBy){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_sortByPhotosAlbums, SortBy);
		prefsEditor.commit();
	}
	
	public int GetVideosAlbumsSortBy(){
		
		return myPrefs.getInt(_sortByVideosAlbums, 0);
	}
	
	public void SetVideosAlbumsSortBy(int SortBy){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_sortByVideosAlbums, SortBy);
		prefsEditor.commit();
	}
	
	public int GetDocumentFoldersSortBy(){
		
		return myPrefs.getInt(_sortByDocumentFolders, 0);
	}
	
	public void SetDocumentFoldersSortBy(int SortBy){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_sortByDocumentFolders, SortBy);
		prefsEditor.commit();
	}

	public int GetMiscellaneousFoldersSortBy(){

		return myPrefs.getInt(_sortByMiscellaneousFolders, 0);
	}

	public void SetMiscellaneousFoldersSortBy(int SortBy){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_sortByMiscellaneousFolders, SortBy);
		prefsEditor.commit();
	}

	public void SetIsDontShowThumbLockMsg(Boolean IsDontShowThumbMsg){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_IsDontShowThumbLockMsg, IsDontShowThumbMsg);
		prefsEditor.commit();
	}

	public boolean GetIsDontShowThumbLockMsg(){
		return myPrefs.getBoolean(_IsDontShowThumbLockMsg, false);
	}


	public void SetIsDontShowMsgLock(Boolean IsDontShowMsgLock){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_IsDontShowMsgLock, IsDontShowMsgLock);
		prefsEditor.commit();
	}

	public boolean GetIsDontShowMsgLock(){
		return myPrefs.getBoolean(_IsDontShowMsgLock, false);
	}

	/*public void SetIsDontShowThumbLockMsg(Boolean IsDontShowThumbMsg){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_IsDontShowThumbLockMsg, IsDontShowThumbMsg);
		prefsEditor.commit();
	}
	
	public boolean GetIsDontShowThumbLockMsg(){
		return myPrefs.getBoolean(_IsDontShowThumbLockMsg, false);
	}
	
	
	public void SetIsDontShowMsgLock(Boolean IsDontShowMsgLock){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_IsDontShowMsgLock, IsDontShowMsgLock);
		prefsEditor.commit();
	}
	
	public boolean GetIsDontShowMsgLock(){
		return myPrefs.getBoolean(_IsDontShowMsgLock, false);
	}*/
	
	public int GetGallerySortBy(){
		
		return myPrefs.getInt(_GallerySortBy, 1);
	}
	
	public void SetGallerySortBy(int SortBy){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_GallerySortBy, SortBy);
		prefsEditor.commit();
	}
	
	
	public int GetGalleryViewBy(){
		
		return myPrefs.getInt(_GalleryViewBy, 1);
	}
	
	public void SetGalleryViewBy(int ViewBy){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_GalleryViewBy, ViewBy);
		prefsEditor.commit();
	}

	public void SetIsDontShowPhotoHelp(Boolean IsDontShowPhotoHelp){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_IsDontShowPhotoHelp, IsDontShowPhotoHelp);
		prefsEditor.commit();
	}

	public boolean GetIsDontShowPhotoHelp(){
		return myPrefs.getBoolean(_IsDontShowPhotoHelp, false);
	}

	public void SetIsDontShowVideoHelp(Boolean IsDontShowVideoHelp){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_IsDontShowVideoHelp, IsDontShowVideoHelp);
		prefsEditor.commit();
	}

	public boolean GetIsDontShowVideoHelp(){
		return myPrefs.getBoolean(_IsDontShowVideoHelp, false);
	}

	public void SetIsDontShowDocumentHelp(Boolean IsDontShowDocumentHelp){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_IsDontShowDocumentHelp, IsDontShowDocumentHelp);
		prefsEditor.commit();
	}

	public boolean GetIsDontShowDocumentHelp(){
		return myPrefs.getBoolean(_IsDontShowDocumentHelp, false);
	}

	public void SetIsDontShowMiscHelp(Boolean IsDontShowMiscHelp){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_IsDontShowMiscallaneousHelp, IsDontShowMiscHelp);
		prefsEditor.commit();
	}

	public boolean GetIsDontShowMiscHelp(){
		return myPrefs.getBoolean(_IsDontShowMiscallaneousHelp, false);
	}

	public void SetIsDontShowAudioHelp(Boolean IsDontShowAudioHelp){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_IsDontShowAudioHelp, IsDontShowAudioHelp);
		prefsEditor.commit();
	}

	public boolean GetIsDontShowAudioHelp(){
		return myPrefs.getBoolean(_IsDontShowAudioHelp, false);
	}

	public void SetIsPhotoAndVideoTimeStampinserted(Boolean IstimeStampInserted){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_IstimeStampInserted, IstimeStampInserted);
		prefsEditor.commit();
	}

	public boolean GetIsPhotoAndVideoTimeStampinserted(){
		return myPrefs.getBoolean(_IstimeStampInserted, false);
	}
}
