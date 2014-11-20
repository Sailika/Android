package com.paradigmcreatives.apspeak.registration.listeners;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ListView;

import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.discovery.adapters.UserNetworkAdapter;
import com.paradigmcreatives.apspeak.discovery.fragments.SettingsFragment;
import com.paradigmcreatives.apspeak.registration.adapters.GroupsAdapter;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectAnimationFragment;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectFragment;

/**
 * Listener for search text.
 * 
 * @author Dileep | neuv
 */
public class SearchTextWatcherImpl implements TextWatcher {

	private Fragment mFragment;
	private ListView mListView;

	public enum SEARCH_TYPE {
		GROUPS, FACEBOOK_FRIENDS
	};

	private SEARCH_TYPE mSearchType;

	public SearchTextWatcherImpl(final Fragment fragment,
			final ListView listView, SEARCH_TYPE searchType) {
		super();
		this.mFragment = fragment;
		this.mListView = listView;
		this.mSearchType = searchType;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (mFragment != null && mSearchType != null) {
			if (mSearchType == SEARCH_TYPE.GROUPS) {
				if (mFragment instanceof FacebookConnectAnimationFragment) {
					((FacebookConnectAnimationFragment) mFragment)
							.setGroupSearchText(s);
				} else if (mFragment instanceof SettingsFragment) {
					((SettingsFragment) mFragment).setGroupSearchText(s);
				}
				ArrayList<GroupBean> searchGroupsList = performGroupsListSearch(s
						.toString());
				if (searchGroupsList.size() == 0) {
					GroupBean requestBean = new GroupBean();
					requestBean.setGroupName("Request your college.");
					searchGroupsList.add(requestBean);
				}
				GroupsAdapter groupsAdapter = new GroupsAdapter(
						mFragment.getActivity(), searchGroupsList);
				if (mListView != null) {
					mListView.setAdapter(groupsAdapter);
				}
			} else if (mSearchType == SEARCH_TYPE.FACEBOOK_FRIENDS) {
				if (mListView != null) {
					if (mListView.getAdapter() != null
							&& mListView.getAdapter() instanceof UserNetworkAdapter) {
						((UserNetworkAdapter) mListView.getAdapter())
								.setSearchText(s);
						((UserNetworkAdapter) mListView.getAdapter())
								.getFilter().filter(s);
					}
				}
			}

		}
	}

	/**
	 * This method performs the search on the groups list bases on group name
	 * 
	 * @param context
	 *            : Context
	 * @param searchTerm
	 *            :The term to search
	 * @return :The list of groups which were matched with the given searchTerm
	 */
	public ArrayList<GroupBean> performGroupsListSearch(String searchTerm) {
		ArrayList<GroupBean> searchCollegeList = new ArrayList<GroupBean>();
		ArrayList<GroupBean> tempCollegeList = new ArrayList<GroupBean>();
		if (mFragment != null) {
			ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
			if (mFragment instanceof FacebookConnectFragment) {
				groupList = ((FacebookConnectFragment) mFragment)
						.getGroupsList();
			} else if (mFragment instanceof FacebookConnectAnimationFragment) {
				groupList = ((FacebookConnectAnimationFragment) mFragment)
						.getGroupsList();
			} else if (mFragment instanceof SettingsFragment) {
				groupList = ((SettingsFragment) mFragment).getGroupsList();
			}
			if (groupList != null) {
				tempCollegeList = new ArrayList<GroupBean>(groupList);
				for (int i = 0; i < groupList.size(); i++) {
					String collegeName = groupList.get(i).getGroupName();
					if ((collegeName.toLowerCase()).startsWith(searchTerm
							.toLowerCase())) {
						searchCollegeList.add(groupList.get(i));
						tempCollegeList.remove(groupList.get(i));
					}
				}

				try {
					Pattern pattern = Pattern.compile(searchTerm,
							Pattern.CASE_INSENSITIVE);
					for (int i = 0; i < tempCollegeList.size(); i++) {
						String groupName = tempCollegeList.get(i)
								.getGroupName();
						boolean matchName = !TextUtils.isEmpty(groupName)
								&& pattern.matcher(groupName).find();
						if (matchName) {
							searchCollegeList.add(tempCollegeList.get(i));
						} // else nothing to do
					}
				} catch (Exception e) {
				}
			}
		}
		return searchCollegeList;
	}

}