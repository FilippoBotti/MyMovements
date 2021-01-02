package com.mobdev.mymovements.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mobdev.mymovements.fragment.HistoryFragment;
import com.mobdev.mymovements.fragment.MapFragment;
import com.mobdev.mymovements.fragment.SettingsFragment;
import com.mobdev.mymovements.fragment.StatsGridFragment;

import java.util.Random;


public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

	public AppSectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	/**
	 * Returns the Fragment associated to the element in position 'i'
	 */
	@Override
	public Fragment getItem(int i) {

		Random rand = new Random();

		switch (i) {
		case 0:
			return new MapFragment();
		case 1:
			return new HistoryFragment();
		case 2:
			return new StatsGridFragment();
		case 3:
			return new SettingsFragment();

		default:
			return new MapFragment();
		}
	}

	/**
	 * Returns the number of element in the ViewPager
	 */
	@Override
	public int getCount() {
		return 4;
	}


}
