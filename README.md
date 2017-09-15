Compete rewrite of the library to make things easier. 

# BackStack

A lightweight Android BackStack library for view groups.
And hey look! I called it by what it actually does, unlike all those other libraries out there.

## Background

A little background on this library, I wasn't really satisfied with the other android backstack libraries available.
They were all clunky, required their own containers for pages or were prone to odd memory leaks. So I decided to try
my hands at making one. This is my first library so let me know if there can be any improvements. Backstack libraries don't
have to be complicated!

## What It Does
When used properly, this library provides back navigation to your app. On rotation it will recreate the correct viewgroups, in the correct order. This library will only work with view based navigation. It will not work with fragments or activities. The type of back navigation this library provides is slightly different from the one recommended
officially by google. For linear navigation (screen 1 -> screen 2 -> screen 3), it will remove the previous view and add the
new view (recommended behaviour). However for cluster selection types (like View Pagers or Bottom Bars) where there are multiple screens, it will maintain separate backstacks for each tab and will not switch between tabs, treating each tab as a linear backstack.

## How To Get It

To install:

In project build.gradle:
~~~~
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
~~~~

and in your module build.gradle:
~~~~
dependencies {
    compile 'com.github.kevinwang5658:backstack:v1.0-beta'
}
~~~~

Note: do not add the jitpack.io repository under buildscript

## How To Use It
Usage is very simple

In the main Activity:
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
        LinearBackStack.create(TAG, root, (inflater, container) -> {
            return new ViewGroup1(inflater.getContext());
        });
        //Set the root backstack after you create your first one. This will be the first to receive the back event
        BackStackManager.setRootBackStack(LinearBackStack.get(TAG));
~~~~

This will create a backstack with the container as the root and the view created in the lambda as the first screen. All screens added to this backstack will be swapped (removeView() will be called on the previous view and addView() will be called on the new view) inside the container. The third parameter, the creator, is stored so that the view can be recreated on rotation. Be careful not to capture any variables in the enclosing class, this will memory leak. Java 8 lambda's will not hold a reference to the enclosing class if nothing is captured. An anonymous inner function will not work here and will leak. A static class can also be used if there are parameters for the viewgroup that are needed for recreation. 

~~~~Java
public void createLinearBackStack(int id){
    LinearBackStack.create(TAG, container, new ViewGroupCreator(id));
}

public static class ViewGroupCreator implements LinearBackStack.ViewCreator{

    final int id

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
        LinearBackStack.get(TAG)
                    .replaceView((inflater, container) -> {
                        return new ViewGroup1(inflater.getContext());
                    }).done();
    }
~~~~

As you may have noticed by now this entire library is held together by String tags. Each backstack must contain it's own unique tag. This ensures that backstacks are accessable from any location in the app and that new screens can be added from anywhere. Make sure all screens which are added outside of initalization (added at a later time) are done through BackStack so that back navigation will work. Not all views have to be created through BackStack, only the ones that you want to be saved for back navigation. For views that are created together (xml inflation or viewgroups that create other views inside their constructor or pager adapters) only the root view needs to be created through backstack to get back navigation. Also, notice the done() at the end? Don't forget that if you do nothing will be created. And that's it for linear navigation. On back pressed, the previously added view will be swapped in and on rotation the correct screen will be displayed.

## Cluster BackStacks

Cluster Backstacks are not really back stacks. They don't hold view creators and don't create views. They are useful for bottom bars or view pagers where the view creation is handled elsewhere at startup. Cluster Backstacks are responsible for delegating the back event to the correct LinearBackStack. LinearBackStacks have to be manually added. Each one is given or assigned a position and using changeContext(int) you can change which LienarBackStack will receive the back event. For a ViewPager, you need to create a new LienarBackStack for each page and then use switchContext(int) each time the show page is changed. 

## Animations

Animations are easy. Before we get started, I highly recommend using android view animations [here](https://github.com/daimajia/AndroidViewAnimations). It is what I'm going to using in this tutorial. All that's need is this:

~~~~Java

    LinearBackStack.get(TAG)
            .replaceView((inflater, container) -> {
                return new ViewGroup1(inflater.getContext());
            })
            .addAnimation((view1, complete) -> {
                // Put your animation you want when view is added in here.
                //Call complete.complete() when it's done. Don't forget this!!!!
                complete.complete();
            })
            .removeAnimation((view1, complete) -> {
                //Put your remove animation here
                //Again don't forget!!!
                complete.complete();
            })
            //call done at the end
            .done();
~~~~

With Andriod View Animations:

~~~~Java
    LinearBackStack.get(MainActivity.TAG)
            .replaceView((inflater, container) -> {
                return new ViewGroup2(inflater.getContext());
            })
            .addAnimation((view1, complete) -> {
                YoYo.with(Techniques.SlideInRight)
                        .duration(500)
                        .onEnd((a)->complete.complete())
                        .playOn(view1);
            })
            .removeAnimation((view1, complete) -> {
                YoYo.with(Techniques.SlideOutRight)
                        .duration(500)
                        .onEnd((a)->complete.complete())
                        .playOn(view1);
            })
            .done();
~~~~

## Combining Cluster BackStacks and Linear Back Stacks

This two types of backstacks can be chained together. This might be useful when you have a viewpager on the third screen and you want multiple screens inside the view pager. To do this you must implement ClusterBackStackImpl inside the ViewGroup that contains the cluster backstack and return the created ClusterBackStack. Remenber all the ClusterBackStack does is delegate back events. So inside the ViewPager, create LinearBackStacks like you did before for a normal ClusterBackStack. Remenber to use switchContext(int) when the shown page is changed. And that's all. BackStack will now delegate the back events to the selected page of the ViewPager.  

## License

~~~~
Copyright 2017 Kevin Wang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

~~~~









