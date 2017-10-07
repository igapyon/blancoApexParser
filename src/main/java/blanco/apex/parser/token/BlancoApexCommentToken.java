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
public class BlancoApexCommentToken extends BlancoApexToken {
    /**
     * Content type of Comment.
     * 
     * <ul>
     * <li>SINGLE_LINE: single line comment.</li>
     * <li>MULTI_LINE: multi line comment.</li>
     * </ul>
     */
    public enum CommentType {
        UNDEFINED, SINGLE_LINE, MULTI_LINE
    }

    /**
     * Content type of Comment.
     */
    protected CommentType commentType = CommentType.UNDEFINED;

    /**
     * Constructor
     * 
     * @param value
     *            value of comment.
     * @param lineNumber
     *            line number of this token.
     * @param commentType
     *            type of comment.
     */
    public BlancoApexCommentToken(final String value, final int lineNumber, final CommentType commentType) {
        this.value = value;
        this.originalValue = value;
        this.lineNumber = lineNumber;
        this.commentType = commentType;
    }

    /**
     * Set comment type.
     * 
     * @param arg
     *            comment type.
     */
    public void setCommentType(final CommentType arg) {
        commentType = arg;
    }

    /**
     * Get comment type.
     * 
     * @return comment type.
     */
    public CommentType getCommentType() {
        return commentType;
    }

    /**
     * Getting String of token for display.
     * 
     * @return String of value for display.
     */
    @Override
    public String getDisplayString() {
        return "COMMENT(" + commentType + ")[" + getValue() + "]";
    }
}
