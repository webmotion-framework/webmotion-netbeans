package org.debux.webmotion.netbeans.javacc.parser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import org.debux.webmotion.netbeans.javacc.parser.ParseException;
import org.debux.webmotion.netbeans.javacc.parser.Token;
import org.debux.webmotion.netbeans.javacc.parser.impl.WebMotionParserImpl.WebMotionParserResult;
import org.netbeans.modules.parsing.spi.ParserResultTask;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.HintsController;
import org.netbeans.spi.editor.hints.Severity;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;

/**
 *
 * @author julien
 */
public class SyntaxErrorHighlightingTask extends ParserResultTask<WebMotionParserResult> {

    @Override
    public void run(WebMotionParserResult result, SchedulerEvent event) {
        try {
            List<ParseException> syntaxErrors = result.getParser().syntaxErrors;
            Document document = result.getSnapshot().getSource().getDocument(false);
            
            List<ErrorDescription> errors = new ArrayList<ErrorDescription>();
            for (ParseException syntaxError : syntaxErrors) {
                Token token = syntaxError.currentToken;
                int start = NbDocument.findLineOffset((StyledDocument) document, token.beginLine - 1) + token.beginColumn - 1;
                int end = NbDocument.findLineOffset((StyledDocument) document, token.endLine - 1) + token.endColumn;
                
                ErrorDescription errorDescription = ErrorDescriptionFactory.createErrorDescription(
                    Severity.ERROR,
                    syntaxError.getMessage(),
                    document,
                    document.createPosition(start),
                    document.createPosition(end)
                );
                errors.add(errorDescription);
            }
            HintsController.setErrors(document, "webmotion", errors);
            
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
            Logger.getLogger(SyntaxErrorHighlightingTask.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (org.netbeans.modules.parsing.spi.ParseException ex) {
            Exceptions.printStackTrace(ex);
            Logger.getLogger(SyntaxErrorHighlightingTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel() {
    }

}