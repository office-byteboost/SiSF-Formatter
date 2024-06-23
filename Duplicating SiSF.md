In this tutorial, we will discuss how to duplicate one SiSF to another.

You can use the `getSource()` method to duplicate SiSF data. Pay attention to the following code.
```java
//Old
SiSF sisf1 = new SiSF("your data");

//New
SiSF sisf2 = new SiSF(sisf1.getSource());
```
