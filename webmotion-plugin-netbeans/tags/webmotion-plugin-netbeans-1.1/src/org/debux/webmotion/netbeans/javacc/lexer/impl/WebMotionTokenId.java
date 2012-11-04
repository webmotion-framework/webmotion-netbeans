/*
 * #%L
 * WebMotion plugin netbeans
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2012 Debux
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
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
