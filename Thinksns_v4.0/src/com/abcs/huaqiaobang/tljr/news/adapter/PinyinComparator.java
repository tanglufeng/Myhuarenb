package com.abcs.huaqiaobang.tljr.news.adapter;

import com.abcs.huaqiaobang.tljr.news.bean.CountrySortModel;

import java.util.Comparator;

public class PinyinComparator implements Comparator<CountrySortModel> {

	public int compare(CountrySortModel o1, CountrySortModel o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
