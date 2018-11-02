# Project 2

The below executions are done using jar files. 
Each executions require two jar files , 
Processing core jar file
The project 1 jar file

In order to take a look at the src, go to Project1/src 

**Run all the commands inside the ./lib folder **
For section 1,2,3:

Run the following command
```java
java -classpath core.jar:hw2.jar com.hw2.networks.GameServer
java -classpath core.jar:hw2.jar com.hw2.networks.GameClient
```

For windows run the same command as 
```java
java -classpath core.jar;hw2.jar com.hw2.networks.GameServer
java -classpath core.jar;hw2.jar com.hw2.networks.GameClient
```
Notice windows classpath values has a *semicolon* between jars.


For Section 4 :
Run the Server with parameters <Total Number of platforms > <Total number of moving platforms

Thus for rendering a screen with 10 platforms out of which 5 are moving
```java
java -classpath core.jar:hw2.jar com.hw2.performance.objectbased.GameServer 10 5
```
For Running String Based performance implementation(Runs smoothly):
```java
java -classpath core.jar:hw2.jar com.hw2.performance.stringbased.GameServer 10 5 
java -classpath core.jar:hw2.jar com.hw2.performance.stringbased.GameClient
```

For Running Object Based performance implementation:
```java
java -classpath core.jar:hw2.jar com.hw2.performance.objectbased.GameServer 10 5 
java -classpath core.jar:hw2.jar com.hw2.performance.objectbased.GameClient
```
