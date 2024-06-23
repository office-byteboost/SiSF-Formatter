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
