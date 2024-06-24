You can make your own SiSF highlighter using **SiSFColoring** class. You can call this class like:
```java
SiSFColoring scolor = new SiSFColoring(EditText edittext, Mode.MODE, Activity activity);
```
Explanation of parameter.

**1. EditText**

You must add your EditText that will be the code highlighter.

**2. Mode**

There are 2 available mode in SiSFColoring. Dark mode, and light mode. To set into dark mode, you can type: `Mode.DARK`. And, to set into light mode, you can type: `Mode.LIGHT`.

[image](https://github.com/office-byteboost/SiSF-Formatter/blob/main/assets/New%20Project%20394%20%5BA2F388D%5D.png)

To the best experience, you can use `#FFFFFF` in light mode, and `#141924` in dark mode as background color.

**3. Activity**

Type current activity. Like: `MainActivity.this`

Full example:
```java
SiSFColoring scol = new SiSFColoring(myedittext1, Mode.DARK, MainActivity.this);
```
And tadaaa! Your EditText being SiSF Highlighter.
- - -
**Modify color of highlighter**
To modify or customize color of highlighter, you can use method
```java
modify(String color, Target.TARGET, Mode.MODE)
```
Explanation of parameter.

**1. String color**

This string contains color that will changing previous color. Like `#4caf50`.

**2. Target**

It contains the target whose color will be changed. All targets are:
- `Target.NULL` change color of null
- `Target.COLON` change color of colon
- `Target.BRACKET` change color of bracket
- `Target.KEY` change color of *key*
- `Target.VALUE` change color of *value*
- `Target.UNKEYED_VALUE` change color of *unkeyed value*
- `Target.NOTES` change color of *notes*
- `Target.QUOTES` change color of quote mark of value
- `Target.QUOTE_UNKEYED` change color of quote mark of unkeyed value

**3. Mode**

If this is LIGHT, then the color changing displayed in light mode. If this is DARK, then the color changing displayed in dark mode.
- - -
To reset into default colors, call `reset()` method.

Full example:
```java
//Initialize (Example: in onCreate event)
SiSFColoring scol = new SiSFColoring(myedittext1, Mode.DARK, MyActivity.this);

//Change color of bracket in dark mode
scol.modify("#4caf50", Target.BRACKET, Mode.DARK);

//Reset color
scol.reset();
```
