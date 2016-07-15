# blancoApexParser
Lexical Parser for Apex language written in Java.

## usage

Prepare *.cls file as input.

```java
@isTest
public without sharing class MySimpleTest {
    static testMethod void testMain001() {
        System.assert(false, 'First hello word written in Apex, as a error of test code.');
    }
}
```

Run Apex rexical parser.

```java
final List<BlancoApexToken> result = new BlancoApexParser()
    .parse(new File("./test/data/apex/MySimpleTest.cls"));

for (BlancoApexToken lookup : result) {
    System.out.println(lookup.getDisplayString());
}
```

Result is like below.

```
SPECIAL_CHAR[@]
WORD[isTest]
NEWLINE[n]
WORD[public]
WHITESPACE[ ]
WORD[without]
WHITESPACE[ ]
WORD[sharing]
WHITESPACE[ ]
WORD[class]
WHITESPACE[ ]
WORD[MySimpleTest]
WHITESPACE[ ]
SPECIAL_CHAR[{]
NEWLINE[n]
WHITESPACE[    ]
WORD[static]
WHITESPACE[ ]
WORD[testMethod]
WHITESPACE[ ]
WORD[void]
WHITESPACE[ ]
WORD[testMain001]
SPECIAL_CHAR[(]
SPECIAL_CHAR[)]
WHITESPACE[ ]
SPECIAL_CHAR[{]
NEWLINE[n]
WHITESPACE[        ]
WORD[System]
SPECIAL_CHAR[.]
WORD[assert]
SPECIAL_CHAR[(]
WORD[false]
SPECIAL_CHAR[,]
WHITESPACE[ ]
LITERAL(STRING)['First hello word written in Apex, as a error of test code.']
SPECIAL_CHAR[)]
SPECIAL_CHAR[;]
NEWLINE[n]
WHITESPACE[    ]
SPECIAL_CHAR[}]
NEWLINE[n]
SPECIAL_CHAR[}]
NEWLINE[n]
```
