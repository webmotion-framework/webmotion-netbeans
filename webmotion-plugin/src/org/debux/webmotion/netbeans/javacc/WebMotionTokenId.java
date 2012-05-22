package org.debux.webmotion.netbeans.javacc;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

/**
 *
 * @author julien
 */
public class WebMotionTokenId implements TokenId {

    private final String name;
    private final String primaryCategory;
    private final int id;

    public static Language<WebMotionTokenId> getLanguage() {
        return new WebMotionLanguageHierarchy().language();
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
