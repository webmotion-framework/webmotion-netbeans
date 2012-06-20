package org.debux.webmotion.netbeans.hints;

import org.debux.webmotion.netbeans.WebMotionLanguage;
import org.netbeans.modules.csl.api.HintsProvider;
import org.netbeans.modules.csl.api.HintsProvider.HintsManager;
import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import static org.debux.webmotion.netbeans.WebMotionLanguage.MIME_TYPE;

/**
 *
 * @author julien
 */
public class WebMotionHintsAdvancedOption extends AdvancedOption {

    OptionsPanelController panelController;
    
    @Override
    public String getDisplayName() {
        return "Hints";
    }

    @Override
    public String getTooltip() {
        return "Hints And Warnings for WebMotion";
    }

    @Override
    public OptionsPanelController create() {
        if ( panelController == null ) {
            HintsManager manager = HintsProvider.HintsManager.getManagerForMimeType(MIME_TYPE);
            assert manager != null;
            panelController = manager.getOptionsController();
        }

        return panelController;
    }
    
    //TODO: temporary solution, this should be solved on GSF level
    public static  OptionsPanelController createStatic(){
        return new WebMotionHintsAdvancedOption().create();
    }
}
