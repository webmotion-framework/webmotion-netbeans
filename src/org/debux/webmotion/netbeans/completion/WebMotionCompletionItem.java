package org.debux.webmotion.netbeans.completion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.lang.model.element.Element;
import javax.swing.ImageIcon;
import javax.swing.JToolTip;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.spi.editor.completion.CompletionDocumentation;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 *
 * @author julien
 */
public class WebMotionCompletionItem implements CompletionItem {
    protected static ImageIcon fieldIcon = new ImageIcon(ImageUtilities.loadImage("org/debux/webmotion/netbeans/icon.png"));
    protected static Color fieldColor = Color.decode("0x0000B2");
    
    protected String text;
    protected CompilationController controller;
    protected Element element;
    protected int caretOffset;
    protected int dotOffset;

    public WebMotionCompletionItem(String text, int dotOffset, int caretOffset) {
        this.text = text;
        this.dotOffset = dotOffset;
        this.caretOffset = caretOffset;
    }

    public WebMotionCompletionItem(String text, CompilationController controller, Element element, int dotOffset, int caretOffset) {
        this.text = text;
        this.controller = controller;
        this.element = element;
        this.dotOffset = dotOffset;
        this.caretOffset = caretOffset;
    }
    
    @Override
    public void defaultAction(JTextComponent component) {
        StyledDocument doc = (StyledDocument) component.getDocument();
        try {
            doc.remove(dotOffset, caretOffset - dotOffset);
            doc.insertString(dotOffset, text, null);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        //This statement will close the code completion box:
        Completion.get().hideAll();
    }

    @Override
    public void processKeyEvent(KeyEvent ke) {
    }

    @Override
    public int getPreferredWidth(Graphics graphics, Font font) {
        return CompletionUtilities.getPreferredWidth(text, null, graphics, font);
    }

    @Override
    public void render(Graphics g, Font defaultFont, Color defaultColor, Color backgroundColor, int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(fieldIcon, text, null, g, defaultFont, selected ? Color.white : fieldColor, width, height, selected);
    }

    @Override
    public CompletionTask createDocumentationTask() {
        return new AsyncCompletionTask(new AsyncCompletionQuery() {

            @Override
            protected void query(CompletionResultSet completionResultSet, Document document, int i) {
                CompletionDocumentation documentation = null;
                if (controller != null && element != null) {
                    documentation = JavaCompletionDoc.createJavaCompletionDoc(controller, element);
                } else {
                    documentation = new KeywordCompletionDocumentation(WebMotionCompletionItem.this);
                }
                completionResultSet.setDocumentation(documentation);
                completionResultSet.finish();
            }
        });
    }

    @Override
    public CompletionTask createToolTipTask() {
        return new AsyncCompletionTask(new AsyncCompletionQuery() {

            @Override
            protected void query(CompletionResultSet completionResultSet, Document document, int i) {
                JToolTip toolTip = new JToolTip();
                toolTip.setTipText("Press Enter to insert \"" + text + "\"");
                completionResultSet.setToolTip(toolTip);
                completionResultSet.finish();
            }
        });
    }

    @Override
    public boolean instantSubstitution(JTextComponent jtc) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return 0;
    }

    @Override
    public CharSequence getSortText() {
        return text;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return text;
    }
    
}
