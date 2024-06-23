# Introducing SiSF

**SiSF** or **Simple String Formatted** is a simple but structured String data format. As the name suggests, "Simple String Formatted" means a series of simple *Strings* that have been formatted into an easy-to-read data form. See the following SiSF data snippet.
```SiSF
name: "Willy August"
class: "8"
school: "School ABC"
motto: "Keep going!"
work_history: null
father {
  name: "Domine"
  phone: "0800123456789"
}
mother {
  name: "Alice"
  phone: "0800123456789"
}
school_history{
  "School A"
  "School B"
}
```
SiSF can contain keys, values, group keys, child keys, null objects and others.

SiSF does not use commas (`,`) to separate data, see the following data format above. SiSF takes data by looking for key-value pairs.

To understand, see the following discussion.

**1. Key and Value**

**Key** in SiSF is a set of characters that are the key to getting a **value**. Meanwhile, **value** is a string that contains the contents of a key.
Formula:
```SiSF
key: "value"

NOTE
Characters that allowed for key are a-z A-Z 0-9 _
```
A **value** must be surrounded by two quotation marks at the start and end. As an example:
```SiSF
participant: "Frank John"
```

**2. Group Key**

**Group Key** is a key that has an array values. Arrays in **group key** can be *null objects*, key:value pairs, or *unkeyed values*.
Formula:
```SiSF
mother {
  nama: "Clair"
  age: "50"
  phone: null
  "It's an unkeyed value"
}
```
The `mother` key is a **group key**, and the `age` and `phone` keys are **child keys**. At the end of the group above, there is a value `"It's an unkeyed value"` which is a value without a key. That's called **unkeyed value**.

A group key cannot contain any more group keys in it. This is because SiSF does not support *nested structures*. Another reason is because SiSF is intended for simple data formatting.

**3. Null Group Key**

**Null Group Key** is a group key that has no content. This type of group is characterized by contents that only contain *null* as for example:
```SiSF
key {
  null
}
```

**4. Null Object**

**Null Object** indicates that a key or group of keys does not contain anything. Null objects are marked with the code `null`. **Null Object** indicates that a key or group key does not contain anything. Null objects are marked with the code `null`. Example as below.

• Null value in key:
```SiSF
name: null
age: null
```
• Null in group key:
```SiSF
data{
  null
}
```

**5. Unkeyed Value**

**Unkeyed value** is a value type that does not have a key. Like:
```SiSF
school {
  "School A"
  "School B"
}
```
Unkeyed values ​​can only be added to group keys.

**6. Notes**

**Notes** or a note is a character or sentence that you create to indicate data. Like:
```SiSF
#! This is a note !#
```
Notes are similar to comments.

At the time of conversion, the notes form is deleted or ignored. Therefore, do not put format notes in your keys and values. For example, you have the key `name` with the value `"Agus #!Setiawan!#`. This is dangerous because the system will ignore `Setiawan` so the value will only be `Agus `.

```SiSF
nama: "Agus #!Setiawan!#"
```

After convertion by system:
```SiSF
nama: "Agus "
```
---
**Converting to SiSF format**

We provide a special library for converting SiSF (Java) format. Download the library in this repository. Once downloaded, install the library in your project and import:
```java
import id.sisf.*;
```
Explanation:
• This is SiSF format
```SiSF
name: "William"
class: "8"
school {
  "School A"
  "School B"
}
mother {
  name: "Jennie"
  age: "45"
}
bio {
  participant_age: "17"
  "This is an unkeyed value in the bio"
}
```
**1. Change to SiSF**
```java
SiSF sisf = new SiSF("Enter the SiSF format code");
```
**2. Getting value from key**
```java
String his_name = sisf.key("name");
//his_name will contain "William"
```
Method `key()` used to get the value from the key. This does not apply to group keys. If this method is used for a group key, it will return `unmatched`. Instead, you can use the `group()` method.
```java
String motherName = sisf.group("mother/name");
//motherName will contain "Jennie"
```
Code `ibu/nama` refers to the `name` key in the `mother` key group.

If you want to get the *unkeyed value* in the group key, you can use the code `groupkey/#sequenceUnkeyedValue`. As an example:
```java
String school2 = sisf.group("school/#2");
//school2 will contain "School B"
```
Code `school/#2` refers to the 2nd unkeyed value in the `school` group.

How do you determine the order of the unkeyed values?
```SiSF
bio {
  name: "Afgan" -> This is not an unkeyed value
  class: "8" -> This is not an unkeyed value
  "1234567890" -> This is unkeyed value sequence #1
  mother: "Suci" -> This is not an unkeyed value
  "School 1" -> This is unkeyed value sequence #2
}
```
So, if you enter the code `bio/#2`, it will return the 2nd unkeyed in the `bio` group which contains "School 1".

If you only enter the group key name like:
```java
String mother = sisf.group("mother");
```
Then the method will return the contents of the group key in String type like:
Then the method will return the contents of the group key in String type like:
`"<name:Jennie, age:45>"`

That's a brief explanation of the `key()` and `group()` methods. Both methods will return `"unmatched"` if the key you are looking for does not exist in the data, and returns `"null"` if the key or group of keys is *null*.
- - -
**All methods in the library:**
1. `String key(String key)`
2. `String group(String group)`
3. `Map groupToMap(String group)`
   This method will convert all data into a Map (HashMap) and return the Map.
```java
Map<String,String> motherdata = sisf.group("ibu");
//Map motherdata will contain "{name=Jennie, age=45}"

/*
IMPORTANT:
The groupToMap() method returns a Map, not a SortedMap.
*/
```
4. `int lengthOfGroup(String group)`
   This method checks the *length* of a group key. If the group key contains *null* or the group key is not found, then this method will return `0`.
```java
SiSF sisf = new SiSF("school{\"SD Negeri 1\" \"SMP Negeri 1\"}");
System.out.println(sisf.lengthOfGroup("school"));
//Return: 2
```
5. `boolean isValued(String key)`
   This method checks the value of a key. This will return `true` if the key contains, but return `false` if the key does not contain (*null*) or the key is not found.
```java
boolean i = sisf.isValued("key");

//To check whether a group key contains or is null, use:
boolean j = sisf.isValued("groupkey");

//To check the child key in a group key, use:
boolean k = sisf.isValued("groupkey/childkey");

//To check for unkeyed values ​​in a group key, use:
boolean k = sisf.isValued("groupkey/#sequenceUnkeyedValues");
```
6. `void toNull(String key)`
   This method will convert a value, group key, etc. become *null*.
```java
//Converts the value of the key to null
sisf.toNull("key");

//Converts group key to null
sisf.toNull("groupkey")

//Converts child values ​​in a group key to null
sisf.toNull("groupkey/key");

//Converts unkeyed values ​​in a group key to null
sisf.toNull("groupkey/#urutanUnkeyedValue");

/*
IMPORTANT:
Especially for group keys, if you convert them to null then all child keys, child values ​​and unkeyed values ​​in them will be deleted.

Example:
data{
  name: "Domine"
  "0800123456789"
}

Will be:
data{
  null
}
*/
```
7. `String getSource()`
   This method will return the currently stored SiSF format data string. Example returns:
```SiSF
name: "ABCDE"
class: "10"
phone{
  null
}
```

---
Other tutorials:
- Duplicating SiSF [Click this](https://github.com/office-byteboost/SiSF-Formatter/blob/main/Duplicating%20SiSF.md).
- Make your SiSF highlighter [Click this](https://github.com/office-byteboost/SiSF-Formatter/blob/main/Colorize%20SiSF.md).
