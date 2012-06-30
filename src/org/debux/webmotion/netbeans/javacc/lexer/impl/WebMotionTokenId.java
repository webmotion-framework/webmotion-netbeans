package org.debux.webmotion.netbeans.javacc.lexer.impl;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

/**
 *
 * @author julien
 */
public class WebMotionTokenId implements TokenId {

    public static enum Section {
        NONE,
        CONFIG,
        EXTENSIONS,
        ACTIONS,
        FILTERS,
        ERRORS,
        PROPERTIES
    }
    
    protected String name;
    protected String primaryCategory;
    protected int id;
    protected Section section;
    
    public static Language<WebMotionTokenId> getLanguage() {
        return new WebMotionLanguageHierarchy().language();
    }
    
    WebMotionTokenId(String name, String primaryCategory, int id, Section section) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
        this.section = section;
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

    public Section getSection() {
        return section;
    }
}
