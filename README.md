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

## LICENSE

```
/*
 * Copyright 2016 Toshiki Iga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```
