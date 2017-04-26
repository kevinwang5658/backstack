# BackStack

A lightweight Android BackStack library for view groups.
Hey look I called it by what it actually does! Unlike all those other libraries out there.

## Background

A little background on this library, I wasn't really satisfied with the other android backstack libraries available.
They were all clunky, required their own containers for pages or were prone to odd memory leaks. So I decided to try
my hands at making one. This is my first library so let me know if there can be any improvements.

## What It Does
When used properly, this library provides back navigation to your app. On rotation it will recreate the correct viewgroups, in the correct order. This library will only work with view based navigation. It will not work with fragments or activities. The type of back navigation this library provides is slightly different from the one recommended
officially by google. For linear navigation (screen 1 -> screen 2 -> screen 3), it will remove the previous view and add the
new view (recommended behaviour). However for cluster selection types (like View Pagers or Bottom Bars) where there are multiple screens, it will maintain separate backstacks for each tab and will not switch between tabs, treating each tab as a linear backstack.

## How To Use It
Usage is very simple

In the main Activity:
~~~~Java
J    @Override
    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        BackStackManager.install(this);
    }


    @Override

public void onBackPressed() {
        BackStackManager.getBackStackManager().goBack();
    }
~~~~

And that's all that's needed to configure this library.

Inside the root viewgroup:

~~~~Java
      LinearBackStack.create(TAG, container, ((inflater, container) -> {
            return (ViewGroup) inflater.inflate(R.layout.your_layout, container, false); //<-- do not attach view in here
        }));
~~~~

This will create a backstack with the container as the root and the view created in the lambda as the first screen. All screens added to this backstack will be swapped (removeView() will be called on the previous view and addView() will be called on the new view) inside the container. The third parameter, the creator, is stored so that the view can be recreated on rotation. Be careful not to capture any variables in the enclosing class, this will memory leak. Java 8 lambda's will not hold a reference to the enclosing class if nothing is captured. An anonymous inner function will not work here and will leak. A static class can so be used if there are parameters for the viewgroup that are needed for recreation. 

~~~~Java
    public void createLinearBackStack(int id){
        LinearBackStack.create(TAG, container, new ViewGroupCreator(id));
    }

    public static class ViewGroupCreator implements LinearBackStack.ViewCreator{

        final int id;

        ViewGroupCreator(int id){
            this.id = id;
        }

        @Override
        public ViewGroup create(LayoutInflater inflater, ViewGroup container) {
            return new ContactsViewGroup(inflater.getContext(), id);
        }
    }
~~~~

New screens can be added from anywhere:

~~~~Java
    public void newScreen(){
        LinearBackStack.get(TAG).replaceView((inflater, container) -> {
            return (ViewGroup) inflater.inflate(R.layout.your_layout, container, false);
        });
    }
~~~~

As you may have noticed by now this entire library is held together by String tags. Each backstack must contain it's own unique tag. This
ensures that backstacks are accessable from any location in the app and that new screens can be added from anywhere. Make sure all screens which are added outside of initalization (added at a later time) are done through BackStack so that back navigation will work. Not all views have to be created through BackStack, only the ones that you want to be saved for back navigation. For views that are created together (xml inflation or viewgroups that create other views inside their constructor or pager adapters) only the root view needs to be created through backstack to get back navigation. And that's it for linear navigation. On back pressed, the previously added view will be swapped in and on rotation the correct screen will be displayed.

To Be Continued...










