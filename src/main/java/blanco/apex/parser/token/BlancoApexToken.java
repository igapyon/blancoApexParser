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
package blanco.apex.parser.token;

/**
 * Simple Apex language lexical parser.
 * 
 * @author Toshiki Iga
 */
public abstract class BlancoApexToken {
    /**
     * value of token.
     */
    protected String value;

    /**
     * original value of token.
     */
    protected String originalValue;

    /**
     * line number on original source code.
     */
    protected int lineNumber = -1;

    /**
     * note from programmer.
     */
    protected String note;

    public String getValue() {
        return value;
    }

    public void setValue(final String arg) {
        value = arg;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getNote() {
        return note;
    }

    public void addNote(final String arg) {
        note = note + arg;
    }

    /**
     * Getting String of token for display.
     */
    public abstract String getDisplayString();
}
