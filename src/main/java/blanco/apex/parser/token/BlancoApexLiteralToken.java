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
public class BlancoApexLiteralToken extends BlancoApexToken {
    /**
     * Type of literal.
     * 
     * <ul>
     * <li>UNDEFINED: N/A</li>
     * <li>STRING: string</li>
     * <li>NUMBER: number</li>
     * </ul>
     */
    public enum LiteralType {
        UNDEFINED, STRING, NUMBER
    }

    protected LiteralType literalType = LiteralType.UNDEFINED;

    /**
     * Constructor of token.
     * 
     * @param value
     *            Literal string. include comma if STRING.
     * @param lineNumber
     *            Number of line.
     * @param literalType
     *            Type of literal.
     */
    public BlancoApexLiteralToken(final String value, final int lineNumber, final LiteralType literalType) {
        this.value = value;
        this.originalValue = value;
        this.lineNumber = lineNumber;
        this.literalType = literalType;
    }

    public void setLiteralType(LiteralType arg) {
        literalType = arg;
    }

    public LiteralType getLiteralType() {
        return literalType;
    }

    /**
     * Getting String of token for display.
     */
    public String getDisplayString() {
        return "LITERAL(" + literalType + ")[" + getValue() + "]";
    }
}
