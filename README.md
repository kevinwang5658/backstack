# BackStack

More information for BackStack can be [found here](https://kevinwang5658.github.io/backstack/)

BackStack is a light weight back navigation library for view groups on Android. It provides an easy and effortless way of navigating between different screens by taking care of back navigation and view persistence. BackStack is designed to be used under a view group based navigation system, a movement began by square [in 2014](https://medium.com/square-corner-blog/advocating-against-android-fragments-81fd0b462c97).

Using view groups come with some inherent obstacles. They don't come with back navigation and view tree persistence built in. BackStack aims to overcome these limitations of view groups and make them a viable choice for use in-place of fragments or activities. BackStack is built around the concept of a view creator, a contextless object that does what it's name says, creates views. Each view creator is associated with a particular view group. View creators don't hold state about what they create or who told them to create what. By doing this, each view group can be inflated independent of each other regardless of the order or conditions they were first created in. BackStack stores a stack of view creators in chronological order. The view creator at the top of the stack will inflate the view that is actually shown to the user. Using this stack, BackStack can return to any previous views. View creators are contextless, and therefore they can also be persisted through activity restarts without leaking.

## Using BackStack

#### Adding BackStack

Using gradle:

build.gradle (Project)
~~~Gradle
allprojects{
    repositories{
        jcenter()
        maven{
            url 'https://jitpack.io'
        }
    }
}
~~~

build.gradle (Module)
~~~Gradle
dependencies{
    compile 'com.github.kevinwang5658:backstack:v2.3'
}
~~~

#### Initialization

To initialize BackStack add the following code to onCreate().

~~~Java
public class MainActivity extends AppCompatActivity {

    BackStackManager backStackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...

        BackStack.install(this);
        backStackManager = BackStack.getBackStackManager();
    }

    @Override
    public void onBackPressed() {
        if (!backStackManager.goBack()) {
            super.onBackPressed();
        }
    }
}
~~~

#### Creating a Simple Back Stack

```Java
public static final String TAG = "TAG";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    BackStack.install(this);
    backStackManager = BackStack.getBackStackManager();

    //This is the container you want to be the root of your back stack
    root = findViewById(R.id.root);

    LinearBackStack linearBackStack = backStackManager.createLinearBackStack("TAG", root, (layoutInflater, container) -> {
        //This is our first view group in the stack
        CustomViewGroup cvg = new CustomViewGroup(layoutInflater.getContext());

        //Make sure that the view is added to container by the end of this block
        container.addView(cvg);

        //Return the view group that was newly inflated
        return cvg;
    });
}
```

#### Adding Subsequent View Groups

```Java
setOnClickListener(v -> {
    //All back stacks can be retrieved anywhere using their tag
    LinearBackStack linearBackStack = (LinearBackStack) BackStack.getStack(MainActivity.TAG);
    linearBackStack.add((layoutInflater, container) -> {
        //if attach to root is true layoutInflater.inflate() returns the container instead
        ViewGroup viewGroup = (ViewGroup) layoutInflater
                .inflate(R.layout.added_view, container, false);
        container.addView(viewGroup);
        return viewGroup;
    });
});
```

For more information please refer to [here](https://kevinwang5658.github.io/backstack/)

## License
```
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
```
