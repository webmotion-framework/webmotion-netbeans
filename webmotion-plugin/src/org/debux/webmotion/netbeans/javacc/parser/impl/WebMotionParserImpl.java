package org.debux.webmotion.netbeans.javacc.parser.impl;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import javax.swing.text.StyledDocument;
import org.debux.webmotion.netbeans.javacc.parser.ParseException;
import org.debux.webmotion.netbeans.javacc.parser.Token;
import org.debux.webmotion.netbeans.javacc.parser.WebMotionParser;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.spi.DefaultError;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.Parser.Result;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.Severity;
import org.openide.text.NbDocument;

/**
 *
 * @author julien
 */
public class WebMotionParserImpl extends Parser {

    private Snapshot snapshot;
    private WebMotionParser parser;

    @Override
    public void parse(Snapshot snapshot, Task task, SourceModificationEvent event) {
        this.snapshot = snapshot;
        Reader reader = new StringReader(snapshot.getText().toString());
        parser = new WebMotionParser(reader);
        try {
            parser.Mapping();
        } catch (ParseException ex) {
            Logger.getLogger(WebMotionParserImpl.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    @Override
    public Result getResult(Task task) {
        return new WebMotionParserResult(snapshot, parser);
    }

    @Override
    public void addChangeListener (ChangeListener changeListener) {
    }

    @Override
    public void removeChangeListener (ChangeListener changeListener) {
    }
    
    public static class WebMotionParserResult extends ParserResult {

        private WebMotionParser parser;
        private boolean valid = true;

        WebMotionParserResult(Snapshot snapshot, WebMotionParser parser) {
            super (snapshot);
            this.parser = parser;
        }

        public WebMotionParser getParser() throws org.netbeans.modules.parsing.spi.ParseException {
            if (!valid) throw new org.netbeans.modules.parsing.spi.ParseException();
            return parser;
        }

        @Override
        protected void invalidate() {
            valid = false;
        }

        @Override
        public List<? extends Error> getDiagnostics() {
            List<Error> errors = new ArrayList<Error>();
            return errors;
        }
    }
}
