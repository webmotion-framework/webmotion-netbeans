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
package org.debux.webmotion.netbeans.completion;

import java.net.URL;
import java.util.concurrent.Future;
import javax.swing.Action;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.ui.ElementJavadoc;
import org.netbeans.spi.editor.completion.CompletionDocumentation;

/**
 *
 * @author julien
 */
public class JavaCompletionDoc implements CompletionDocumentation {
    
    private ElementJavadoc elementJavadoc;

    public static JavaCompletionDoc createJavaCompletionDoc(CompilationController controller, javax.lang.model.element.Element element) {
        return new JavaCompletionDoc(ElementJavadoc.create(controller, element));
    }
    
    public JavaCompletionDoc(ElementJavadoc elementJavadoc) {
        this.elementJavadoc = elementJavadoc;
    }

    @Override
    public JavaCompletionDoc resolveLink(String link) {
        ElementJavadoc doc = elementJavadoc.resolveLink(link);
        return doc != null ? new JavaCompletionDoc(doc) : null;
    }

    @Override
    public URL getURL() {
        return elementJavadoc.getURL();
    }

    @Override
    public String getText() {
        return elementJavadoc.getText();
    }

    public Future<String> getFutureText() {
        return elementJavadoc.getTextAsync();
    }

    @Override
    public Action getGotoSourceAction() {
        return elementJavadoc.getGotoSourceAction();
    }
    
}
