package com.todo.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.todo.common.Const;
import com.todo.fragment.CompletedFragment;
import com.todo.fragment.PendingFragment;
import com.todo.model.BaseModel;
import com.todo.model.ModelDelegates;
import com.todo.model.TaskInfo;
import com.todo.modellist.TaskList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {


  private SectionsPagerAdapter mSectionsPagerAdapter;

  @BindView(R.id.container)
  ViewPager mViewPager;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.tabs)
  TabLayout tabLayout;
  SharedPreferences sharedpreferences;
  SharedPreferences.Editor editor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    sharedpreferences = getSharedPreferences(Const.TASK_API_PREF, Context.MODE_PRIVATE);
    if (sharedpreferences.getBoolean(Const.IS_FIRST_TIME, true)) {
      getTaskDataFromServer();
    } else {
      setViewPager();
    }
  }

  /**
   * Set adapter into viewpager
   */
  private void setViewPager() {
    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager.setAdapter(mSectionsPagerAdapter);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    tabLayout.setupWithViewPager(mViewPager);
  }

  /**
   * Fetching data from server if not available
   */
  private void getTaskDataFromServer() {
    showProgress();
    TaskList.Instance().getAllTask(new ModelDelegates.ModelDelegate<TaskInfo>() {
      @Override
      public void ModelLoaded(ArrayList<TaskInfo> list) {
        hideProgress();
        editor = sharedpreferences.edit();
        editor.putBoolean(Const.IS_FIRST_TIME, false);
        editor.commit();
        setViewPager();
      }

      @Override
      public void ModelLoadFailedWithError(String error) {
        hideProgress();
        setViewPager();
      }
    });
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 1:
          return new PendingFragment();
        default:
          return new CompletedFragment();
      }
    }

    @Override
    public int getCount() {
      return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case 1:
          return getResources().getString(R.string.tab_pending);
        default:
          return getResources().getString(R.string.tab_completed);
      }
    }
  }
}
