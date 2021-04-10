/**
 * 
 */
package applock.protect.bit.applock.applock;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.Serializable;

public class AppInfo implements Serializable {

	private static final long serialVersionUID = -8500888136359113356L;
	public String appName;
	public String packageName;
	public String versionName;
	public int versionCode = 0;
	public Drawable appIcon = null;
	public boolean isSection;
	public boolean isthumb;
	public boolean isMsg;
	public boolean isLock;

	public void print() {
		Log.v("app", "Name:" + appName + " Package:" + packageName);
		Log.v("app", "Name:" + appName + " versionName:" + versionName);
		Log.v("app", "Name:" + appName + " versionCode:" + versionCode);
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	
	public boolean getIsSection() {
		return isSection;
	}

	public void setIsSection(boolean isSection) {
		this.isSection = isSection;
	}
	
	public boolean getIsthumb() {
		return isthumb;
	}

	public void setIsthumb(boolean isthumb) {
		this.isthumb = isthumb;
	}
	
	public boolean getIsMSg() {
		return isMsg;
	}

	public void setIsMsg(boolean isMsg) {
		this.isMsg = isMsg;
	}
	
	public boolean getIsLock() {
		return isLock;
	}

	public void setIsLock(boolean isLock) {
		this.isLock = isLock;
	}

}
