package com.abcs.huaqiaobang.ytbt.sortlistview;
import java.util.Comparator;

import com.abcs.huaqiaobang.ytbt.bean.User;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinFriendsComparator implements Comparator<User> {
	
//	public int compare(SortModel o1, SortModel o2) {
//		if (o1.getSortLetters().equals("@")
//				|| o2.getSortLetters().equals("#")) {
//			return -1;
//		} else if (o1.getSortLetters().equals("#")
//				|| o2.getSortLetters().equals("@")) {
//			return 1;
//		} else {
//			return o1.getSortLetters().compareTo(o2.getSortLetters());
//		}
//	}

	@Override
	public int compare(User lhs, User rhs) {
		// TODO Auto-generated method stub
		if (lhs.getSortLetters().equals("@")
				|| rhs.getSortLetters().equals("#")) {
			return -1;
		} else if (lhs.getSortLetters().equals("#")
				|| rhs.getSortLetters().equals("@")) {
			return 1;
		} else {
			return lhs.getSortLetters().compareTo(rhs.getSortLetters());
		}
	}


	

}
