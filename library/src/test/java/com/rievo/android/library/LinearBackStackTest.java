package com.rievo.android.library;

import android.app.Activity;
import android.widget.RelativeLayout;

import com.rievo.library.BackStackManager;
import com.rievo.library.LinearBackStack;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;

import static org.mockito.Mockito.mock;

/**
 * Created by kevin on 2017-04-25.
 */

@RunWith(RobolectricTestRunner.class)
public class LinearBackStackTest {

    @Before
    public void setup(){
        Activity activity = Robolectric.setupActivity(Activity.class);
        BackStackManager.install(activity);
    }

    String TAG = "TAG";

    @Mock RelativeLayout relativeLayout = mock(RelativeLayout.class);
    @Mock RelativeLayout relativeLayout2 = mock(RelativeLayout.class);

    @Test
    public void create() throws Exception {
        assertNotEquals(LinearBackStack.create(TAG, relativeLayout, (inflater, container) -> relativeLayout2), null);
        assertNotEquals(BackStackManager.getBackStack(TAG), null);
        assertEquals(BackStackManager.backStackSize(), 1);
    }

    @Test
    public void get() throws Exception {

    }

    @Test
    public void addIndependentView() throws Exception {

    }

    @Test
    public void replaceView() throws Exception {

    }

    @Test
    public void createView() throws Exception {

    }

    @Test
    public void globalGoBack() throws Exception {

    }

    @Test
    public void goBack() throws Exception {

    }

    @Test
    public void getRootViewGroup() throws Exception {

    }

    @Test
    public void getCurrentViewGroup() throws Exception {

    }

}