# BackStack

A lightweight back stack library for Android view based navigation

## Getting Started
Using gradle:

build.gradle (Project)
~~~Gradle
allprojects {
    repositories {
        google()
        jcenter()
        maven{
            url "https://jitpack.io"
        }
    }
}
~~~

build.gradle (Module)
~~~Gradle
dependencies {
    compile 'com.github.kevinwang5658:backstack:v2.2'
}
~~~

## Usage

See the documentation for more info.

To install:
~~~Java
BackStackManager backstackManager;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    BackStack.install(this);
    backStackManager = BackStack.getBackStackManager();
}

@Override
public void onBackPressed() {
    if (!backStackManager.goBack()) {
        super.onBackPressed();
    }
}
~~~

Creating a back stack:
~~~java
@Override
protected void onCreate(Bundle savedInstanceState) {
    ...
    root = findViewById(R.id.root);
    
    //The first view is created added automatically to the stack
    backStackManager.createLinearBackStack(TAG, root, (layoutInflater, container) -> {
        //This is the first view group
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.viewgroup_layout, container, false);
        container.addView(viewGroup);
        return viewGroup;
    });
}
~~~

To add a view to the stack:
~~~Java
LinearBackStack linearBackStack = (LinearBackStack) BackStack.getStack(TAG);
linearBackStack.add((layoutInflater, container) -> {
    ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.viewgroup2, container, false);
    container.addView(viewGroup);
    return viewGroup;
});
~~~



