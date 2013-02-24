package org.debux.webmotion.pollmotion;

import java.util.List;
import javax.persistence.EntityManager;
import org.debux.webmotion.jpa.GenericDAO;
import org.debux.webmotion.jpa.GenericDAO.Parameters;
import org.debux.webmotion.jpa.IdentifiableEntity;
import org.debux.webmotion.pollmotion.entity.Poll;
import org.debux.webmotion.pollmotion.entity.Vote;
import org.debux.webmotion.server.WebMotionController;
import org.debux.webmotion.server.render.Render;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController extends WebMotionController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    
    public Render createPoll(GenericDAO dao,
            String email,
            String question,
            String[] choices) {
        
        Parameters parameters = Parameters.create()
            .add("email", email)
            .add("question", question)
            .add("choices", choices);
        IdentifiableEntity entity = dao.create(parameters);
        
        return renderRedirect("/poll/completed", "poll", entity.getId());
    }

    public Render createVote(GenericDAO dao,
            String pollId,
            String email,
            String[] votes) {
        
        Parameters parameters = Parameters.create()
            .add("poll", pollId)
            .add("email", email)
            .add("votes", votes);
        IdentifiableEntity entity = dao.create(parameters);
        
        return renderRedirect("/vote/" + pollId);
    }
    
    public Render getVotes(EntityManager em,
            String pollId) {
        
        GenericDAO daoPoll = new GenericDAO(em, Poll.class);
        Poll poll = (Poll) daoPoll.find(pollId);
        
        GenericDAO daoVote = new GenericDAO(em, Vote.class);
        List<Vote> votes = daoVote.query("findByPollId", 
                                         Parameters.create().add("poll_id", pollId));
        
        int [] results = new int[poll.getChoices().size()];
        for (Vote vote : votes) {
            
            List<Boolean> values = vote.getVotes();
            int index = 0;
            for (Boolean value : values) {
                if (value) {
                    results[index]++;
                }
                index ++;
            }
        }
        
        return renderView("poll_vote.jsp",
                "poll", poll,
                "votes", votes,
                "results", results);
    }
}
