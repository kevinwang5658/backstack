# BackStack

A lightweight Android BackStack library for view groups.

## Background
And hey you know what I called it by what it actually does! Unlike all those other libraries out there.
A little background on this library, I wasn't really satisfied with the other backstack libraries available.
They were all clunky, required their own containers for pages and were prone to odd memory leaks. So I decided to try
my hands at making one. This is my first library so let me know if there can be any improvements.

## What It Does
The first thing I should tell you is what this library does. When used properly, this library provides back navigation to your app. 
On rotation it will recreate the correct viewgroups on rotation. This library will only work with view based screens. It will not work with
fragments or activities. The type of back navigation this library provides is different from the one recommended
officially by google. For linear navigation it will (screen 1 -> screen 2 -> screen 3), it will remove the previous view and add the
new view which is recommended behaviour. However for cluster selection types (like View Pagers or Bottom Bars), it will maintain separate
backstacks for each tab and will not switch between tabs. 

## How To Use It
Usage is very simple

In the main Activity
~~~~Java
    @Override
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
            return (ViewGroup) inflater.inflate(R.layout.your_layout, container, false);
        }));
~~~~

This will create 

