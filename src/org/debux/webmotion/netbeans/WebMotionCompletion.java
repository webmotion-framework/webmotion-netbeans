package org.debux.webmotion.netbeans;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JToolTip;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionProvider;
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
@MimeRegistration(mimeType = "text/x-wm", service = CompletionProvider.class)
public class WebMotionCompletion implements CompletionProvider {

    public static final String[] keywords = {
                                                "[config]", "[properties]", "[actions]", "[filters]", "[errors]", "[extensions]",
                                                "*", "GET", "POST", "PUT", "DELETE", "HEAD",
                                                "action:", "async:", "sync:",
                                                "view:", "url:", "redirect:", "forward:",
                                                "package.views", "package.base", "package.filters", "package.actions",
                                                "package.errors", "javac.debug", "server.async", "server.encoding", "server.error.page",
                                                "server.controller.scope", "server.listener.class", "server.main.handler.class",
                                                "server.secret", "server.static.autodetect"
                                            };
    
    @Override
    public CompletionTask createTask(int queryType, JTextComponent component) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
           return null;
        }
        
        return new AsyncCompletionTask(new AsyncCompletionQuery() {
            @Override
            protected void query(CompletionResultSet completionResultSet, Document document, int caretOffset) {
                
                String filter = null;
                int startOffset = caretOffset - 1;

                try {
                    final StyledDocument bDoc = (StyledDocument) document;
                    final int lineStartOffset = getRowFirstNonWhite(bDoc, caretOffset);
                    final char[] line = bDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
                    final int whiteOffset = indexOfWhite(line);
                    filter = new String(line, whiteOffset + 1, line.length - whiteOffset - 1);
                    if (whiteOffset > 0) {
                        startOffset = lineStartOffset + whiteOffset + 1;
                    } else {
                        startOffset = lineStartOffset;
                    }
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
        
                for (String keyword : keywords) {
                    if (keyword.startsWith(filter)) {
                        completionResultSet.addItem(new KeywordCompletionItem(keyword, startOffset, caretOffset));
                    }
                }
                
                completionResultSet.finish();
            }
        }, component);
    }

    static int getRowFirstNonWhite(StyledDocument doc, int offset) throws BadLocationException {
        Element lineElement = doc.getParagraphElement(offset);
        int start = lineElement.getStartOffset();
        while (start + 1 < lineElement.getEndOffset()) {
            try {
                if (doc.getText(start, 1).charAt(0) != ' ') {
                    break;
                }
            } catch (BadLocationException ex) {
                throw (BadLocationException) new BadLocationException(
                        "calling getText(" + start + ", " + (start + 1)
                        + ") on doc of length: " + doc.getLength(), start).initCause(ex);
            }
            start++;
        }
        return start;
    }

    static int indexOfWhite(char[] line) {
        int i = line.length;
        while (--i > -1) {
            final char c = line[i];
            if (Character.isWhitespace(c)) {
                return i;
            }
        }
        return -1;
    }
                    
    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        return 0;
    }
    
    public static class KeywordCompletionItem implements CompletionItem {

        private String text;
        private static ImageIcon fieldIcon = new ImageIcon(ImageUtilities.loadImage("org/debux/webmotion/netbeans/icon.png"));
        private static Color fieldColor = Color.decode("0x0000B2");
        private int caretOffset;
        private int dotOffset;

        public KeywordCompletionItem(String text, int dotOffset, int caretOffset) {
            this.text = text;
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
        public void render(Graphics g, Font defaultFont, Color defaultColor,
                Color backgroundColor, int width, int height, boolean selected) {
            CompletionUtilities.renderHtml(fieldIcon, text, null, g, defaultFont,
                    (selected ? Color.white : fieldColor), width, height, selected);
        }

        @Override
        public CompletionTask createDocumentationTask() {
            return null;
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
}
