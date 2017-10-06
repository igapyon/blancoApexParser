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
    public enum LiteralType {
        UNDEFINED, STRING, NUMBER
    }

    protected LiteralType literalType = LiteralType.UNDEFINED;

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

    public String getDisplayString() {
        return "LITERAL(" + literalType + ")[" + getValue() + "]";
    }
}
