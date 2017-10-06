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

import blanco.apex.parser.token.BlancoApexToken;

/**
 * Utils for Apex language lexical parser.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexParserUtil {
    /**
     * Is whitespace chars or not excluding new-lines.
     * 
     * @param cRead
     * @return
     */
    public static boolean isWhitespaceWithoutLines(final char cRead) {
        if (cRead == ' ' || cRead == '\t' || cRead == '　'/* FULL_WIDTH_SPACE */) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Is special char.
     * 
     * @param cRead
     * @return
     */
    public static boolean isSpecialChar(final char cRead) {
        switch (cRead) {
        case '.':
        case ',':
        case ';':
        case ':':
        case '=':
        case '+':
        case '-':
        case '*':
        case '/':
        case '#':
        case '!':
        case '?':
        case '|':
        case '&':
        case '%':
        case '{':
        case '}':
        case '(':
        case ')':
        case '<':
        case '>':
        case '[':
        case ']':
        case '@':// Annotation!!! FIXME
            return true;
        default:
            return false;
        }
    }

    /**
     * convert token list to string.
     * 
     * @param tokenList
     * @return
     */
    public static String tokenList2String(final List<BlancoApexToken> tokenList) {
        final StringBuffer strbuf = new StringBuffer();
        for (BlancoApexToken lookup : tokenList) {
            strbuf.append(lookup.getValue());
        }
        return strbuf.toString();
    }
}
