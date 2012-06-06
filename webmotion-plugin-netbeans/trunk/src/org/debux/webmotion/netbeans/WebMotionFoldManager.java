package org.debux.webmotion.netbeans;

import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.netbeans.api.editor.fold.Fold;
import org.netbeans.api.editor.fold.FoldHierarchy;
import org.netbeans.api.editor.fold.FoldType;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.spi.editor.fold.FoldHierarchyTransaction;
import org.netbeans.spi.editor.fold.FoldManager;
import org.netbeans.spi.editor.fold.FoldManagerFactory;
import org.netbeans.spi.editor.fold.FoldOperation;
import org.openide.util.Exceptions;

/**
 *
 * @author julien
 */
public class WebMotionFoldManager implements FoldManager {
    
    public static final FoldType CUSTOM_FOLD_TYPE = new FoldType("custom-fold"); // NOI18N

    private FoldOperation operation;
    
    public static class Factory implements FoldManagerFactory {
        @Override
        public FoldManager createFoldManager() {
            return new WebMotionFoldManager();
        }
    }
    
    @Override
    public void init(FoldOperation operation) {
        this.operation = operation;
    }

    public FoldOperation getOperation() {
        return operation;
    }
    
    @Override
    public void initFolds(FoldHierarchyTransaction transaction) {
//        ((AbstractDocument) getDocument()).readLock();
//
//        try {
//            FoldHierarchy fh = getOperation().getHierarchy();
//            fh.lock();
//
//            try {
//                FoldHierarchyTransaction fhTran =
//                        getOperation().openTransaction();
//                getOperation().addToHierarchy(CUSTOM_FOLD_TYPE, "MyFold", false, 0,
//                        63, 0, 0, null, fhTran);
//                fhTran.commit();
//
//            } catch (BadLocationException ble) {
//                ErrorManager.getDefault().notify(ble);
//            } finally {
//                fh.unlock();
//            }
//
//        } finally {
//            ((AbstractDocument) getDocument()).readUnlock();
//        }
        FoldHierarchy hierarchy = operation.getHierarchy();
        Document document = hierarchy.getComponent().getDocument();
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        
        boolean firstSection = true;
        int start = 0;
        int end = 0;
        int offset = 0;
        while (ts.moveNext()) {
            offset = ts.offset();
            
            Token<WebMotionTokenId> token = ts.token();
            String matcherText = token.text().toString();
            
            WebMotionTokenId id = token.id();
            String name = id.name();
            if (name.startsWith("SECTION")) {
                if (!firstSection) {
                    try {
                        operation.addToHierarchy(CUSTOM_FOLD_TYPE, "...", true, start, end, 0, 0, hierarchy, transaction);
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                
                start = offset + matcherText.length() - 1;
                firstSection = false;
                
            } else if (!"\n".equals(matcherText)) {
                end = offset;
            }
        }
        
        if (!firstSection) {
            try {
                operation.addToHierarchy(CUSTOM_FOLD_TYPE, "...", true, start, end, 0, 0, hierarchy, transaction);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

    }

    @Override
    public void insertUpdate(DocumentEvent evt, FoldHierarchyTransaction transaction) {
    }

    @Override
    public void removeUpdate(DocumentEvent evt, FoldHierarchyTransaction transaction) {
    }
    
    @Override
    public void changedUpdate(DocumentEvent evt, FoldHierarchyTransaction transaction) {
    }
    
    @Override
    public void removeEmptyNotify(Fold emptyFold) {
    }
    
    @Override
    public void removeDamagedNotify(Fold damagedFold) {
    }
    
    @Override
    public void expandNotify(Fold expandedFold) {
    }

    @Override
    public void release() {
    }
}
