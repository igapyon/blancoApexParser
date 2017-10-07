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
public class BlancoApexNewlineToken extends BlancoApexToken {
    public BlancoApexNewlineToken(final String value, final int lineNumber) {
        this.value = value;
        this.originalValue = value;
        this.lineNumber = lineNumber;
    }

    /**
     * Getting String of token for display.
     * 
     * @return String of value for display.
     */
    @Override
    public String getDisplayString() {
        String val = getValue();
        val = val.replace('\r', 'r');
        val = val.replace('\n', 'n');
        return "NEWLINE[" + val + "]";
    }
}
