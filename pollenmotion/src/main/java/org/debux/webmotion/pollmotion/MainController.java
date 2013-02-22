package org.debux.webmotion.pollmotion;

import org.debux.webmotion.jpa.GenericDAO;
import org.debux.webmotion.jpa.GenericDAO.Parameters;
import org.debux.webmotion.jpa.IdentifiableEntity;
import org.debux.webmotion.server.WebMotionController;
import org.debux.webmotion.server.render.Render;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController extends WebMotionController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    
    public Render getPoll(GenericDAO dao,
            String poll) {
        
        IdentifiableEntity entity = dao.find(poll);
        return renderView("view.jsp", "poll", entity);
    }
    
    public Render createPoll(GenericDAO dao,
            String email,
            String question,
            String[] choices) {
        
        Parameters parameters = Parameters.create()
            .add("email", email)
            .add("question", question)
            .add("choices", choices);
        IdentifiableEntity entity = dao.create(parameters);
        
        return renderRedirect("/page/complete", "pollId", entity.getId());
    }

    public Render createVote(GenericDAO dao,
            String poll,
            String email,
            String[] votes) {
        
        Parameters parameters = Parameters.create()
            .add("poll", poll)
            .add("email", email)
            .add("votes", votes);
        IdentifiableEntity entity = dao.create(parameters);
        
        return renderRedirect("/page/complete", "voteId", entity.getId());
    }
    
}
