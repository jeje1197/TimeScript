# TimeScript
A high-level, dynamically-typed programming language designed for embedded use 
in Java applications.

## How to Use
___
Requirements:
- Java installed on your Computer

### Standalone

1) Download the <b>TimeScript.jar</b> file from the [jars](https://github.com/jeje1197/TimeScript/tree/master/jars)
directory
2) Place it within your folder of choice
3) Open your terminal/command line and navigate to the folder containing the TimeScript.jar file
4) Run one of the following commands:
   
To run the REPL (Read-Eval-Print-Loop):
   java -jar TimeScript.jar


    To read from a file:
    java -jar TimeScript.jar [filepath]



### Embedded in a project
1) Download the **TimeScript.jar** file from the [jars](https://github.com/jeje1197/TimeScript/tree/master/jars)
   directory

2) Add the jar as a dependency in your project\
    [Tutorial for IntelliJ](https://www.geeksforgeeks.org/how-to-add-external-jar-file-to-an-intellij-idea-project/)\
    [Tutorial for Eclipse](https://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-(Java))

3) Use the ```TimeScript.run(String code)``` method to run code.
4) If you need to extend the functionality of the language to declare
a new variable, function or class, use the 
```NativeApi.addData(String variableName, TSObject data)```
method.






Sample programs can be found in the [/examples](https://github.com/jeje1197/TimeScript/tree/master/examples) folder.

Language Features
Data Types:
  - Number (Encompasses both int & float representations)
  - String
  - Boolean (true/false)
  - Null
  - Function
  - List
  - User-Defined Classes
 
 Future Features:
  - An import system
  - Inheritance
  - Interfaces
  - More Data Structures (Set, Map, Stack, etc.)
  - Static Resolution
  - Reflection
  - Program Reversion
