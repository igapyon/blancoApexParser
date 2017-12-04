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
package blanco.apex.parser;

import java.util.List;

import org.junit.Test;

import blanco.apex.parser.token.BlancoApexToken;

/**
 * Simple Apex language lexical parser.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexParserCombineTest {
    @Test
    public void test001() throws Exception {
        final List<BlancoApexToken> result = new BlancoApexParser().parse(//
                "public class MyClass {\n"//
                        + "    public void myMethod() {\n"//
                        +"         String str1 = '文字列1';\n"//
                        +"         if (str1 === str1) {\n"
                        +"         }\n"//
                        + "    }\n"//
                        + "}");

        for (BlancoApexToken lookup : result) {
            System.out.println(lookup.getDisplayString());
        }
        for (BlancoApexToken lookup : result) {
            System.out.print(lookup.getValue());
        }
    }
}
