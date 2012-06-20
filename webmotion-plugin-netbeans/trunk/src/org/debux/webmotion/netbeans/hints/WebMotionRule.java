package org.debux.webmotion.netbeans.hints;

import java.util.Collections;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import org.netbeans.modules.csl.api.HintSeverity;
import org.netbeans.modules.csl.api.Rule.AstRule;
import org.netbeans.modules.csl.api.RuleContext;

/**
 *
 * @author julien
 */
public class WebMotionRule implements AstRule {

    public enum Kinds {
        DEFAULT;
    }
    
    @Override
    public Set<?> getKinds() {
        return Collections.singleton(Kinds.DEFAULT);
    }

    @Override
    public String getId() {
        return getClass().getSimpleName();
    }

    @Override
    public String getDescription() {
        return "Test description";//NbBundle.getMessage(WebMotionHint.class, String.format("%s_Desc", getId()));
    }

    @Override
    public boolean getDefaultEnabled() {
        return true;
    }

    @Override
    public JComponent getCustomizer(Preferences node) {
        return null;
    }
    
    @Override
    public boolean appliesTo(RuleContext context) {
        return true; //always
    }

    @Override
    public String getDisplayName() {
        return "Test name";//NbBundle.getMessage(WebMotionHint.class, getId());
    }

    @Override
    public boolean showInTasklist() {
        return true;
    }

    @Override
    public HintSeverity getDefaultSeverity() {
        return HintSeverity.WARNING;
    }
    
}
