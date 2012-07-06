package org.debux.webmotion.netbeans.javacc.parser.impl;

import java.util.Collection;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.ParserFactory;

/**
 *
 * @author julien
 */
public class WebMotionParserFactory extends ParserFactory {
    
    public static WebMotionParserImpl parser = new WebMotionParserImpl();

    @Override
    public Parser createParser(Collection<Snapshot> snapshots) {
        return parser;
    }

}
