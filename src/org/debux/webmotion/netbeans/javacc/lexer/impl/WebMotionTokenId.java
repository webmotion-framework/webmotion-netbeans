package org.debux.webmotion.netbeans.javacc.lexer.impl;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

/**
 *
 * @author julien
 */
public class WebMotionTokenId implements TokenId {

    protected final String name;
    protected final String primaryCategory;
    protected final int id;

    public static Language<WebMotionTokenId> language = new WebMotionLanguageHierarchy().language();
    
    public static Language<WebMotionTokenId> getLanguage() {
        return language;
    }
    
    WebMotionTokenId(
            String name,
            String primaryCategory,
            int id) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

    @Override
    public int ordinal() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }
}
