package applock.protect.bit.applock.applock;

public class AppLockEnt {

	public int id;
	public String appName;
	public String packageName;
	public int lockType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getLockType() {
		return lockType;
	}

	public void setLockType(int lockType) {
		this.lockType = lockType;
	}


}
