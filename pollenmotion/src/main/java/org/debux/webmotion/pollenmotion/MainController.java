package org.debux.webmotion.pollenmotion;

import org.debux.webmotion.jpa.GenericDAO;
import org.debux.webmotion.jpa.GenericDAO.Parameters;
import org.debux.webmotion.jpa.IdentifiableEntity;
import org.debux.webmotion.server.WebMotionController;
import org.debux.webmotion.server.render.Render;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController extends WebMotionController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    
    public Render create(GenericDAO dao,
            String email,
            String question,
            String[] choices) {
        
        Parameters parameters = Parameters.create()
            .add("email", email)
            .add("question", question)
            .add("choices", choices);
        IdentifiableEntity entity = dao.create(parameters);
        log.info(">>>> " + entity);
        
        return renderRedirect("/page/complete", "pollId", entity.getId());
    }
    
}
