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
import java.util.ArrayList;
import java.util.List;

import blanco.apex.parser.token.BlancoApexCommentToken;
import blanco.apex.parser.token.BlancoApexLiteralToken;
import blanco.apex.parser.token.BlancoApexNewlineToken;
import blanco.apex.parser.token.BlancoApexSpecialCharToken;
import blanco.apex.parser.token.BlancoApexToken;
import blanco.apex.parser.token.BlancoApexWhitespaceToken;
import blanco.apex.parser.token.BlancoApexWordToken;

/**
 * Simple Apex language lexical parser.
 * 
 * You should use 'BlancoApexParser#parse' instead of
 * 'BlancoApexLexicalParser#parse' for entry point.
 * 
 * See {@link BlancoApexParser#parse(File)}.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexLexicalParser {
    /**
     * List of token.
     */
    protected List<BlancoApexToken> tokenList = new ArrayList<BlancoApexToken>();

    /**
     * Current line number of original source code.
     */
    protected int lineNumber = 1;

    /**
     * Parse and tokenize source code string.
     * 
     * You should use 'BlancoApexParser#parse' instead of
     * 'BlancoApexLexicalParser#parse' for entry point.
     * 
     * @param sourceString
     *            source code string.
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
     * Parse and tokenize source file.
     * 
     * You should use 'BlancoApexParser#parse' instead of
     * 'BlancoApexLexicalParser#parse' for entry point.
     * 
     * @param file
     *            source file to parse.
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

    protected StringBuffer strbufWord = new StringBuffer();

    /**
     * main parser method for lexical parsing.
     * 
     * You should use 'BlancoApexParser#parse' instead of
     * 'BlancoApexLexicalParser#parse' for entry point.
     * 
     * @param reader
     *            Reader of source code.
     * @return List of token.
     * @throws IOException
     *             I/O Exception.
     */
    public List<BlancoApexToken> parse(final BufferedReader reader) throws IOException {
        for (;;) {
            final int iRead = reader.read();
            if (iRead < 0) {
                // will be end of line

                // flush word if exists
                fireWord();

                // finish process.
                break;
            }

            final char cRead = (char) iRead;
            if (cRead == '/') {
                // check comments

                reader.mark(1);

                final int iRead2 = reader.read();
                if (iRead2 < 0) {
                    reader.reset();
                    continue;
                }

                final char cRead2 = (char) iRead2;
                if (cRead2 == '*') {
                    // start multi-line comment

                    // flush word if exists
                    fireWord();

                    parseMultilineComment(reader);
                } else if (cRead2 == '/') {
                    // start single-line comment

                    // flush word if exists
                    fireWord();

                    parseSinglelineComment(reader);
                } else {
                    // others
                    reader.reset();

                    strbufWord.append(cRead);
                }
            } else if (cRead == '\'') {
                // start of string literal

                // flush word if exists
                fireWord();

                parseStringLiteral(reader);
            } else if (cRead == '\"') {
                // start of string literal for SOQL

                // flush word if exists
                fireWord();

                parseStringLiteralForSOQL(reader);
            } else if (BlancoApexParserUtil.isSpecialChar(cRead)) {
                // special char

                // flush word if exists
                fireWord();

                parseSpecialChar(reader, cRead);
            } else if (BlancoApexParserUtil.isWhitespaceWithoutLines(cRead)) {
                // whitespace

                // flush word if exists
                fireWord();

                parseWhitespaceWithoutlines(reader, cRead);
            } else if (cRead == '\n' || cRead == '\r') {
                // end of line

                // flush word if exists
                fireWord();

                parseNewlines(reader, cRead);
            } else {
                strbufWord.append(cRead);
            }
        }

        return tokenList;
    }

    public void fireWord() throws IOException {
        if (strbufWord.length() > 0) {
            try {
                // We can't determine keywrods on lexical parsing. It will be
                // able to determine on syntax parsing.

                {
                    // check simply the word is number literal or not
                    try {
                        Double.parseDouble(strbufWord.toString());
                        // it seems number literal
                        final BlancoApexLiteralToken newToken = new BlancoApexLiteralToken(strbufWord.toString(),
                                lineNumber, BlancoApexLiteralToken.LiteralType.NUMBER);
                        tokenList.add(newToken);

                        // exit here!
                        return;
                    } catch (Exception ex) {
                        // do nothing.
                        // falling to normal word.
                    }
                }

                final BlancoApexWordToken newToken = new BlancoApexWordToken(strbufWord.toString(), lineNumber);
                tokenList.add(newToken);
            } finally {
                strbufWord = new StringBuffer();
            }
        }
    }

    public void parseMultilineComment(final BufferedReader reader) throws IOException {
        // remember start time line number.
        // because multi line number will be changed while processing
        // multi-lines.
        final int startTimeLineNumber = lineNumber;

        final StringBuffer strbuf = new StringBuffer();
        strbuf.append("/*");

        // flag to determine line number.
        boolean isPastCr = false;

        for (;;) {
            reader.mark(1);
            final int iRead = reader.read();
            if (iRead < 0) {
                break;
            }
            final char cRead = (char) iRead;
            if (cRead == '*') {
                reader.mark(1);
                final int iRead2 = reader.read();
                if (iRead2 < 0) {
                    break;
                }
                final char cRead2 = (char) iRead2;
                if (cRead2 == '/') {
                    // end multi-line comment

                    strbuf.append("*/");

                    // do process
                    final BlancoApexCommentToken newToken = new BlancoApexCommentToken(strbuf.toString(),
                            startTimeLineNumber, BlancoApexCommentToken.CommentType.MULTI_LINE);
                    tokenList.add(newToken);

                    // exit method
                    return;
                } else {
                    reader.reset();
                    // in commentS
                }
            } else if (cRead == '\r') {
                lineNumber++;
                isPastCr = true;
            } else if (cRead == '\n') {
                if (isPastCr) {
                    isPastCr = false;
                } else {
                    lineNumber++;
                }
            }

            strbuf.append(cRead);
        }
    }

    public void parseSinglelineComment(final BufferedReader reader) throws IOException {
        final StringBuffer strbuf = new StringBuffer();
        strbuf.append("//");
        for (;;) {
            reader.mark(1);
            final int iRead = reader.read();
            if (iRead < 0) {
                // exit method
                final BlancoApexCommentToken newToken = new BlancoApexCommentToken(strbuf.toString(), lineNumber,
                        BlancoApexCommentToken.CommentType.SINGLE_LINE);
                tokenList.add(newToken);

                return;
            }
            final char cRead = (char) iRead;
            if (cRead == '\n' || cRead == '\r') {
                // exit method
                reader.reset();

                final BlancoApexCommentToken newToken = new BlancoApexCommentToken(strbuf.toString(), lineNumber,
                        BlancoApexCommentToken.CommentType.SINGLE_LINE);
                tokenList.add(newToken);

                return;
            }

            strbuf.append(cRead);
        }
    }

    public void parseStringLiteral(final BufferedReader reader) throws IOException {
        final StringBuffer strbuf = new StringBuffer();
        strbuf.append("'");
        for (;;) {
            reader.mark(1);
            final int iRead = reader.read();
            if (iRead < 0) {
                break;
            }
            final char cRead = (char) iRead;
            if (cRead == '\\') {
                // entering escape mode

                // one point: adding escape char too!
                strbuf.append(cRead);

                final int iRead2 = reader.read();
                if (iRead2 < 0) {
                    break;
                }
                final char cRead2 = (char) iRead2;
                strbuf.append(cRead2);

                // avoid to do default.
                continue;
            } else if (cRead == '\'') {
                // exit method

                strbuf.append(cRead);

                // string literal token.
                final BlancoApexLiteralToken newToken = new BlancoApexLiteralToken(strbuf.toString(), lineNumber,
                        BlancoApexLiteralToken.LiteralType.STRING);
                tokenList.add(newToken);

                return;
            }

            strbuf.append(cRead);
        }
    }

    public void parseStringLiteralForSOQL(final BufferedReader reader) throws IOException {
        final StringBuffer strbuf = new StringBuffer();
        strbuf.append("\"");
        for (;;) {
            reader.mark(1);
            final int iRead = reader.read();
            if (iRead < 0) {
                break;
            }
            final char cRead = (char) iRead;
            if (cRead == '\\') {
                // entering escape mode

                // one point: adding escape char too!
                strbuf.append(cRead);

                final int iRead2 = reader.read();
                if (iRead2 < 0) {
                    break;
                }
                final char cRead2 = (char) iRead2;
                strbuf.append(cRead2);

                // avoid to do default.
                continue;
            } else if (cRead == '\"') {
                // exit method

                strbuf.append(cRead);

                // string literal token.
                final BlancoApexLiteralToken newToken = new BlancoApexLiteralToken(strbuf.toString(), lineNumber,
                        BlancoApexLiteralToken.LiteralType.STRING);
                tokenList.add(newToken);

                return;
            }

            strbuf.append(cRead);
        }
    }

    public void parseWhitespaceWithoutlines(final BufferedReader reader, final char cPastRead) throws IOException {
        final StringBuffer strbuf = new StringBuffer();
        strbuf.append(cPastRead);
        for (;;) {
            reader.mark(1);
            final int iRead = reader.read();
            if (iRead < 0) {
                break;
            }
            final char cRead = (char) iRead;
            if (BlancoApexParserUtil.isWhitespaceWithoutLines(cRead) == false) {
                final BlancoApexWhitespaceToken newToken = new BlancoApexWhitespaceToken(strbuf.toString(), lineNumber);
                tokenList.add(newToken);

                reader.reset();
                return;
            }
            strbuf.append(cRead);
        }
    }

    public void parseNewlines(final BufferedReader reader, final char cPastRead) throws IOException {
        final boolean isNewlinesStartedWithCR = (cPastRead == '\r');

        final StringBuffer strbuf = new StringBuffer();
        strbuf.append(cPastRead);

        if (isNewlinesStartedWithCR) {
            reader.mark(1);
            final int iRead = reader.read();
            if (iRead >= 0 && ((char) iRead) == '\n') {
                // \r\n occur.
                strbuf.append((char) iRead);
            } else {
                reader.reset();
            }
        }

        final BlancoApexNewlineToken newToken = new BlancoApexNewlineToken(strbuf.toString(), lineNumber);
        tokenList.add(newToken);

        lineNumber++;
    }

    public void parseSpecialChar(final BufferedReader reader, final char cRead) throws IOException {
        final BlancoApexSpecialCharToken newToken = new BlancoApexSpecialCharToken("" + cRead, lineNumber);
        tokenList.add(newToken);
    }
}
