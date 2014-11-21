package com.paradigmcreatives.apspeak.discovery.fragments;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.registration.adapters.GroupsAdapter;
import com.paradigmcreatives.apspeak.registration.handlers.AddUserToGroupHandler;
import com.paradigmcreatives.apspeak.registration.handlers.GetGroupsListHandler;
import com.paradigmcreatives.apspeak.registration.listeners.GroupItemClickListenerImpl;
import com.paradigmcreatives.apspeak.registration.listeners.GroupsSearchActionListenerImpl;
import com.paradigmcreatives.apspeak.registration.listeners.SearchTextWatcherImpl;
import com.paradigmcreatives.apspeak.registration.listeners.SearchTextWatcherImpl.SEARCH_TYPE;
import com.paradigmcreatives.apspeak.registration.tasks.AddUserToGroupThread;
import com.paradigmcreatives.apspeak.registration.tasks.GetGroupsListThread;
import com.paradigmcreatives.apspeak.settings.listeners.SettingsOnClickListener;

public class SettingsFragment extends Fragment {
	private String TAG = "SettingsFragment";

	private ProgressBar mProgressBar;

	private Dialog mDialog;
	private ListView mGroupsListView;
	private GroupsAdapter mGroupsAdapter;
	private ArrayList<GroupBean> mGroupsList = null;
	private GroupBean mSelectedGroup = null;
	private String mGroupSearchText = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.settings_fragment, container,
				false);
		initUI(view);
		return view;
	}

	/**
	 * Initializes Settings view
	 * 
	 * @param view
	 */
	private void initUI(View view) {
		TextView aboutUsText = (TextView) view.findViewById(R.id.about_us_text);
		TextView logoutText = (TextView) view.findViewById(R.id.app_logout);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		TextView helpText = (TextView) view.findViewById(R.id.help_text);
		TextView changeCollegeText = (TextView) view
				.findViewById(R.id.change_college_text);
		TextView privacyPolicyText = (TextView) view
				.findViewById(R.id.privacy_policy_text);
		TextView feedbackText = (TextView) view
				.findViewById(R.id.feedback_text);
		SettingsOnClickListener listener = new SettingsOnClickListener(this);

		aboutUsText.setOnClickListener(listener);
		logoutText.setOnClickListener(listener);
		helpText.setOnClickListener(listener);
		privacyPolicyText.setOnClickListener(listener);
		changeCollegeText.setOnClickListener(listener);
		feedbackText.setOnClickListener(listener);
	}

	public void removeGroupsListView() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	public void setGroup(GroupBean group) {
		if (group != null) {
			mSelectedGroup = group;
			// Save the group id in shared preferences

			String mGroupName = group.getGroupName();

			if (group != null && !TextUtils.isEmpty(mGroupName)) {
				if (mGroupName.equals(getResources().getString(
						R.string.request_college))) {
					Intent emailIntent = Util.getPreFormattedEmailIntent(this
							.getActivity());
					if (emailIntent != null) {
						try {
							startActivity(Intent.createChooser(emailIntent,
									("Choose")));
						} catch (ActivityNotFoundException anfe) {
							Logger.warn(
									TAG,
									"Activity not found : "
											+ anfe.getLocalizedMessage());
						} catch (Exception e) {
							Logger.warn(
									TAG,
									"Unknown Exception : "
											+ e.getLocalizedMessage());
						}
					} else {
						Logger.warn(TAG, "email intent is null");
					}
				} else {
					AppPropertiesUtil.setGroupID(getActivity(),
							mSelectedGroup.getGroupId());
					AppPropertiesUtil.setUserGroupName(getActivity(),
							mGroupName);
					AppPropertiesUtil.setUserAddedToGroup(getActivity(), false);
					Toast.makeText(
							getActivity(),
							getResources().getString(
									R.string.college_update_success)
									+ " " + mGroupName, Toast.LENGTH_SHORT)
							.show();
				}
				removeGroupsListView();
				makeAddUserToGroupRequest();
			}

		} else {
			mSelectedGroup = null;
			Logger.warn(TAG, " value is invalid");
		}
	}

	public void setGroupSearchText(CharSequence text) {
		if (text != null) {
			mGroupSearchText = text.toString();
		} else {
			mGroupSearchText = "";
		}
	}

	public void extractAndProvideCollegeListView(
			ArrayList<GroupBean> groupsArrayList) {

		setGroupsList(groupsArrayList);
		if (getActivity() != null) {
			View groupsListView = (View) LayoutInflater.from(getActivity())
					.inflate(R.layout.colleges_list, null);
			groupsListView.setBackgroundColor(getResources().getColor(
					R.color.white));
			mGroupsListView = (ListView) groupsListView.findViewById(R.id.colleges);
			EditText searchField = (EditText) groupsListView
					.findViewById(R.id.search_field);
			mGroupsListView.setCacheColorHint(Color.TRANSPARENT);

			if (groupsArrayList.size() == 0) {
				mProgressBar.setVisibility(View.VISIBLE);
			}
			mGroupsAdapter = new GroupsAdapter(getActivity(), groupsArrayList);
			mGroupsListView.setAdapter(mGroupsAdapter);
			searchField.setInputType(InputType.TYPE_CLASS_TEXT);
			searchField.setImeOptions(EditorInfo.IME_ACTION_DONE);
			searchField
					.setOnEditorActionListener(new GroupsSearchActionListenerImpl(
							this));
			searchField.addTextChangedListener(new SearchTextWatcherImpl(this,
					mGroupsListView, SEARCH_TYPE.GROUPS));
			mGroupsListView
					.setOnItemClickListener(new GroupItemClickListenerImpl(this));
			addGroupsListView(groupsListView);

		}
	}

	public void addGroupsListView(View groupsListView) {
		if (groupsListView != null) {
			mDialog = new Dialog(getActivity(),
					android.R.style.Theme_Light_NoTitleBar_Fullscreen);
			mDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.setContentView(groupsListView);
			mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					mGroupsListView = null;
					if (mSelectedGroup == null) {

					}
				}
			});
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(mDialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			lp.height = WindowManager.LayoutParams.MATCH_PARENT;
			mProgressBar.setVisibility(View.GONE);
			mDialog.show();
			mDialog.getWindow().setAttributes(lp);
		}
	}

	private void setGroupsList(ArrayList<GroupBean> groupsList) {
		this.mGroupsList = groupsList;
		if (mProgressBar != null
				&& mProgressBar.getVisibility() == View.VISIBLE) {
			mProgressBar.setVisibility(View.GONE);
		}
		if (mGroupsListView != null) {
			mGroupsAdapter = new GroupsAdapter(getActivity(),
					performGroupsListSearch(mGroupSearchText));
			mGroupsListView.setAdapter(mGroupsAdapter);
			mGroupsAdapter.notifyDataSetChanged();
		}
	}

	public ArrayList<GroupBean> performGroupsListSearch(String searchTerm) {
		ArrayList<GroupBean> searchGroupsList = new ArrayList<GroupBean>();
		ArrayList<GroupBean> groupsList = new ArrayList<GroupBean>();
		groupsList = getGroupsList();

		String groupName = "";
		try {
			Pattern pattern = Pattern.compile(searchTerm,
					Pattern.CASE_INSENSITIVE);
			if (groupsList != null) {
				for (int i = 0; i < groupsList.size(); i++) {
					groupName = groupsList.get(i).getGroupName();
					boolean matchName = !TextUtils.isEmpty(groupName)
							&& pattern.matcher(groupName).find();
					if (matchName) {
						searchGroupsList.add(groupsList.get(i));
					} // else nothing to do
				}
			}
		} catch (Exception e) {
		}
		return searchGroupsList;
	}

	public void makeAddUserToGroupRequest() {
		AddUserToGroupHandler handler = new AddUserToGroupHandler(this);
		AddUserToGroupThread thread = new AddUserToGroupThread(getActivity(),
				handler, AppPropertiesUtil.getGroupId(getActivity()),
				AppPropertiesUtil.getUserID(getActivity()));
		thread.start();
	}

	public void fetchGroupsListFromServer() {
		if (Util.isOnline(getActivity())) {
			mProgressBar.setVisibility(View.VISIBLE);
			GetGroupsListHandler handler = new GetGroupsListHandler(this);
			GetGroupsListThread thread = new GetGroupsListThread(handler);
			thread.start();
		} else {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.no_network),
					Toast.LENGTH_SHORT).show();
		}
	}

	public ArrayList<GroupBean> getGroupsList() {
		return this.mGroupsList;
	}
}