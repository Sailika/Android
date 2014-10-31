package com.example.android.expandingcells;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {
	 private ViewFlipper viewFlipper;
     private float lastX;
     private final int CELL_DEFAULT_HEIGHT = 200;
     private final int NUM_OF_CELLS = 3;
     private ExpandingListView mListView;
     private ExpandingListView ovenListView;

     private ExpandingListView fsettingsListview;

     private ExpandingListView settingsListView;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        viewFlipper = (ViewFlipper) findViewById(R.id.content);

        ExpandableListItem[] values = new ExpandableListItem[] {
                new ExpandableListItem("Items", R.drawable.fridge_icon, CELL_DEFAULT_HEIGHT,
                        getResources().getString(R.string.item_txt)),
                new ExpandableListItem("List", R.drawable.list_tab, CELL_DEFAULT_HEIGHT,
                        getResources().getString(R.string.list_item_txt)),
                new ExpandableListItem("Settings", R.drawable.settings_icon, CELL_DEFAULT_HEIGHT,
                        getResources().getString(R.string.settings_txt)),
                        
        };

        List<ExpandableListItem> mData = new ArrayList<ExpandableListItem>();

        for (int i = 0; i < NUM_OF_CELLS; i++) {
            ExpandableListItem obj = values[i % values.length];
            mData.add(new ExpandableListItem(obj.getTitle(), obj.getImgResource(),
                    obj.getCollapsedHeight(), obj.getText()));
        }

        CustomArrayAdapter adapter = new CustomArrayAdapter(this, R.layout.list_view_item, mData);

        mListView = (ExpandingListView)findViewById(R.id.fridge_list_view);
        ovenListView = (ExpandingListView)findViewById(R.id.oven_list_view);
        fsettingsListview = (ExpandingListView)findViewById(R.id.fast_settings_list_view);
        settingsListView = (ExpandingListView)findViewById(R.id.settings_list_view);
        
        mListView.setAdapter(adapter);
        ovenListView.setAdapter(adapter);
        fsettingsListview.setAdapter(adapter);
        settingsListView.setAdapter(adapter);
        mListView.setDivider(getResources().getDrawable(R.drawable.border));
       ovenListView.setDivider(getResources().getDrawable(R.drawable.border));
        fsettingsListview.setDivider(getResources().getDrawable(R.drawable.border));
        settingsListView.setDivider(getResources().getDrawable(R.drawable.border));
	}

	
    
   // Method to handle touch event like left to right swap and right to left swap
   public boolean onTouchEvent(MotionEvent touchevent)
   {
                switch (touchevent.getAction())
                {
                       // when user first touches the screen to swap
                        case MotionEvent.ACTION_DOWN:
                        {
                            lastX = touchevent.getX();
                            break;
                       }
                        case MotionEvent.ACTION_UP:
                        {
                            float currentX = touchevent.getX();
                           
                            // if left to right swipe on screen
                            if (lastX < currentX)
                            {
                                 // If no more View/Child to flip
                                if (viewFlipper.getDisplayedChild() == 0)
                                    break;
                               
                                // set the required Animation type to ViewFlipper
                                // The Next screen will come in form Left and current Screen will go OUT from Right
                                viewFlipper.setInAnimation(this, R.anim.in_from_left);
                                viewFlipper.setOutAnimation(this, R.anim.out_to_right);
                                // Show the next Screen
                                viewFlipper.showNext();
                            }
                           
                            // if right to left swipe on screen
                            if (lastX > currentX)
                            {
                                if (viewFlipper.getDisplayedChild() == 1)
                                    break;
                                // set the required Animation type to ViewFlipper
                                // The Next screen will come in form Right and current Screen will go OUT from Left
                                viewFlipper.setInAnimation(this, R.anim.in_from_right);
                                viewFlipper.setOutAnimation(this, R.anim.out_to_left);
                                // Show The Previous Screen
                                viewFlipper.showPrevious();
                            }
                            break;
                        }
                }
                return false;
   }

}

