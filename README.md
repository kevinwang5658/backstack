# BackStack

A lightweight Android BackStack library for view groups.

## Background
And hey you know what I called it by what it actually does! Unlike all those other libraries out there.
A little background on this library, I wasn't really satisfied with the other backstack libraries available.
They were all clunky, required their own containers for pages and were prone to odd memory leaks. So I decided to try
my hands at making one. This is my first library so let me know if there can be any improvements.

## What It Does
The first thing I should tell you is what this library does. When used properly, this library provides back navigation to your app. 
On rotation it will recreate the correct viewgroups on rotation. This library will only work with view based screens. It will not work with fragments or activities. The type of back navigation this library provides is different from the one recommended
officially by google. For linear navigation it will (screen 1 -> screen 2 -> screen 3), it will remove the previous view and add the
new view which is recommended behaviour. However for cluster selection types (like View Pagers or Bottom Bars), it will maintain separate
backstacks for each tab and will not switch between tabs. 

## How To Use It
Usage is very simple

In the main Activity:
~~~~Java
J    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

This will create a backstack with the container as the root. All pages added to this backstack will be swapped (removeView() will be called on the previous view and addView() will be called on the new view) on the container. The third parameter, the lambda is responsible for creating the viewgroup. This is stored so that the view can be recreated on rotation. Be careful not to capture any variables in the enclosing class, this will memory leak since the functional interface is stored across rotations. Java 8 lambda's will not hold a reference to the enclosing class if nothing is captured. An anonymous inner function will not work here and will leak. A static class can so be used if there are parameters for the viewgroup that are needed for recreation. 

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
ensures that backstacks are accessable from any location in the app and that new screens can be added from anywhere. Make sure all screens which are added outside of initalization (added at a later time) are done through BackStack so that Back Navigation will work.
And that's it for linear navigation. On rotation the correct screen will be displayed and back navigation will be saved through rotation.










