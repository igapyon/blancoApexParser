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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import blanco.apex.parser.token.BlancoApexSpecialCharToken;
import blanco.apex.parser.token.BlancoApexToken;

/**
 * Simple Apex language Parser.
 * 
 * <p>
 * This is Lexical Parser of Apex written in Java.
 * </p>
 * 
 * <p>
 * Useage:
 * </p>
 * 
 * <code>
 * List&lt;BlancoApexToken&gt; result = new BlancoApexParser().parse(new File("./test/data/apex/MySimpleTest.cls"));
 * </code>
 * 
 * @author Toshiki Iga
 */
public class BlancoApexParser {
    /**
     * Special char with combine. Like "++", "--", "&lt;=", "&gt;=", "==", "!=",
     * "&amp;&amp;", "||", "+=", ...
     */
    public static final String[] COMBINED_SPECIAL_CHAR = new String[] { "++", "--", "<=", ">=", "==", "!=", "&&", "||", //
            "+=", "-=", "*=", "/=", //
            "|=", "&=", //
            "<<=", ">>=", ">>>=", //
            "===", "!==", "^=", "<<", ">>", ">>>", //
            // additional
            "=>", "<>", };

    /**
     * Entry point of Apex parser.
     * 
     * @param sourceString
     *            String of source code to parse.
     * @return List of token.
     * @throws IOException
     *             I/O Exception.
     */
    public List<BlancoApexToken> parse(final String sourceString) throws IOException {
        final BufferedReader reader = new BufferedReader(new StringReader(sourceString));
        try {
            return parse(reader);
        } finally {
            reader.close();
        }
    }

    /**
     * Entry point of Apex parser.
     * 
     * @param file
     *            File of source code to parse.
     * @return List of token.
     * @throws IOException
     *             I/O Exception.
     */
    public List<BlancoApexToken> parse(final File file) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        try {
            return parse(reader);
        } finally {
            reader.close();
        }
    }

    /**
     * Entry point of Apex parser.
     * 
     * @param reader
     *            Reader of source code to parse.
     * @return List ot token.
     * @throws IOException
     *             I/O Exception.
     */
    public List<BlancoApexToken> parse(final BufferedReader reader) throws IOException {
        final List<BlancoApexToken> tokenList = new BlancoApexLexicalParser().parse(reader);

        // 3 chars
        for (int index = 0; index < tokenList.size() - 2; index++) {
            final BlancoApexToken lookup = tokenList.get(index);
            if (lookup instanceof BlancoApexSpecialCharToken) {
                final BlancoApexSpecialCharToken specialChar = (BlancoApexSpecialCharToken) lookup;

                if (tokenList.get(index + 1) instanceof BlancoApexSpecialCharToken == false) {
                    // combine special char needs both special char
                    continue;
                }
                if (tokenList.get(index + 2) instanceof BlancoApexSpecialCharToken == false) {
                    // combine special char needs both special char
                    continue;
                }
                final BlancoApexSpecialCharToken specialCharNext = (BlancoApexSpecialCharToken) tokenList
                        .get(index + 1);
                final BlancoApexSpecialCharToken specialCharNextNext = (BlancoApexSpecialCharToken) tokenList
                        .get(index + 2);
                final String combinedSpecialCharCandidate = specialChar.getValue() + specialCharNext.getValue() + specialCharNextNext.getValue();
                for (String combined : COMBINED_SPECIAL_CHAR) {
                    if (combinedSpecialCharCandidate.equals(combined)) {
                        specialChar.setValue(combinedSpecialCharCandidate);
                        tokenList.remove(index + 2);
                        tokenList.remove(index + 1);
                    }
                }
            }
        }

        // 2 chars
        for (int index = 0; index < tokenList.size() - 1; index++) {
            final BlancoApexToken lookup = tokenList.get(index);
            if (lookup instanceof BlancoApexSpecialCharToken) {
                final BlancoApexSpecialCharToken specialChar = (BlancoApexSpecialCharToken) lookup;

                if (tokenList.get(index + 1) instanceof BlancoApexSpecialCharToken == false) {
                    // combine special char needs both special char
                    continue;
                }
                final BlancoApexSpecialCharToken specialCharNext = (BlancoApexSpecialCharToken) tokenList
                        .get(index + 1);
                final String combinedSpecialCharCandidate = specialChar.getValue() + specialCharNext.getValue();
                for (String combined : COMBINED_SPECIAL_CHAR) {
                    if (combinedSpecialCharCandidate.equals(combined)) {
                        specialChar.setValue(combinedSpecialCharCandidate);
                        tokenList.remove(index + 1);
                    }
                }
            }
        }

        return tokenList;
    }
}
