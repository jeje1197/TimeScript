# TimeScript
A high-level, dynamically-typed programming language designed for embedded use 
in Java applications.

## How to Use
___

#### Requirements:
- Java installed on your Computer

### Standalone

1) Download the <b>TimeScript.jar</b> file from the [jars](https://github.com/jeje1197/TimeScript/tree/master/jars)
directory
2) Place it in your folder of choice
3) Open your terminal / command line and navigate to the folder
containing the TimeScript.jar file
4) Run one of the following commands:\
\
   To run the REPL (Read-Eval-Print-Loop):\
   &nbsp;&nbsp;&nbsp;&nbsp;```java -jar TimeScript.jar```
\
\
    To execute a TimeScript file:\
    &nbsp;&nbsp;&nbsp;&nbsp;```java -jar TimeScript.jar [filepath]```


### Embedded in a project
1) Download the **TimeScript.jar** file from the [jars](https://github.com/jeje1197/TimeScript/tree/master/jars)
   directory

2) Add the jar as a dependency in your project\
    [Tutorial for IntelliJ](https://www.geeksforgeeks.org/how-to-add-external-jar-file-to-an-intellij-idea-project/)\
    [Tutorial for Eclipse](https://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-(Java))

3) Use the ```TimeScript.run(String code)``` method to run a string of code\
\
   Ex:\
   ```TimeScript.run("println(5 + 2)");```


4) To extend TimeScript's functionality, use the\
```NativeApi.addData(String variableName, TSObject data)```
method which binds an object to an identifier at global scope.\
\
   For example, adding a custom function and calling it:
   ```
   NativeApi.addData("myFunc", new TSFunction(List.of()) {
       @Override
       public TSObject call(ExecutionEngine engine, Environment environment) {
           System.out.println("Custom function called!");
           return new TSNull();
       }
   });

   TimeScript.run("myFunc()");
   ```
\
\
Sample programs can be found in the [examples](https://github.com/jeje1197/TimeScript/tree/master/examples) folder.



## Documentation
___

### Version 1.0

Data Types:
  - Number (A single representation for integers & floating-point numbers)
  - String
  - Boolean
  - Null
  - Function
  - List
  - User-Defined Classes

 ### Future Features:
  - An import system
  - Inheritance
  - Interfaces
  - More Data Structures (Set, Map, Stack, etc.)
  - Static Resolution
  - Reflection
  - Program Reversion
