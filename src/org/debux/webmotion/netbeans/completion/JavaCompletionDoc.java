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
