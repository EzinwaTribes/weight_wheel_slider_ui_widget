# Weight Wheel Slider Widget Library

## How to Use

### Step 1: Add the JitPack repository to your build file

#### Gradle

Add the following repository URL to your root `build.gradle` file at the end of the `repositories` block:

```gradle
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
#### Maven

If you are using Maven, add the following repository to your project's pom.xml file:

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### Step 2: Add the dependency

In your app-level build.gradle file, add the following dependency:
```
dependencies {
    implementation ("com.github.EzinwaTribes:weight_wheel_slider_widget:{$Tag}")
}
```
Replace 'Tag' with the specific version or release tag you want to use.

#### Share this release
### Release

### Usage
You can now use the Weight Wheel Slider Widget in your Android application. Import the widget into your `Compose`.  Please refer to the library's documentation and examples for detailed usage instructions.

#### Example
Here's an example of how to use the Weight Wheel Slider Widget in your Android Compose Layout:

```
@Composable
private fun ScaleWidget() {
    Box( modifier = Modifier.fillMaxSize()){
        var weight by remember { mutableStateOf(0) }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "$weight kg",
            fontSize = 45.sp)

        Scale(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter),
            style = ScaleStyle()
        ) {
            weight = it
        }
    }
}
```

#### Issues
If you encounter any issues or have suggestions for improvements, please open an issue on the GitHub repository's issue tracker.

#### License
This library is distributed under the MIT License. See the LICENSE file for more details.


