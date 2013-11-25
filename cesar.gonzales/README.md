coding-challenge-seitenbau-cgonzalez
====================================
#Description
Implementation for the SFCPU - The Seitenbau Fantasy CPU.

More information:

<a href="http://seitenbau.github.io/coding-challenge/">Seitenbau Coding Challenge 2013</a>
#Compile
```
mvn clean compile assembly:single
```

#Execute
```
java -jar -Dconfig.file=application.conf coding-challenge-seitenbau-cgonzalez-jar-with-dependencies.jar
```

##application.conf

- set (-Dconfig.file=application.conf)
- it should be in the same folder that the coding-challenge-seitenbau-cgonzalez-jar-with-dependencies.jar is
- default (no application.conf is set)

```
java -jar coding-challenge-seitenbau-cgonzalez-jar-with-dependencies.jar
```

```
memory_size = 256
instructions: [
        "LOAD A, 1",
        "SETM 0, A",
        "LOADM A,0",
        "OR A, 8",
        "PRINT A",
        "XOR A, 42",
        "PRINT A",
        "XOR A, 42",
        "PRINT A",
        "LOAD C, 10",
        "PRINT C",
        "DEC C",
        "CMP C, 0",
        "JNE -3",
        "STOP"
]
```
