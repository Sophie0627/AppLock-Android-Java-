package applock.protect.bit.applock.applock;

import java.util.Comparator;

public class AppsComparator implements Comparator<AppInfo>{

		@Override
		public int compare(AppInfo item1, AppInfo item2) {
			// TODO Auto-generated method stub
			return item1.getAppName().compareTo(item2.getAppName());
		}

	}

