package org.debux.webmotion.pollmotion.entity;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.debux.webmotion.jpa.IdentifiableEntity;

/**
 *
 * @author julien
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "findByPollId",
        query = "SELECT v FROM Vote v WHERE v.poll.id=:poll_id")
})
public class Vote extends IdentifiableEntity {
    
    @ManyToOne
    protected Poll poll;
    
    @Basic
    protected String email;
    
    @ElementCollection
    protected List<Boolean> votes;

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Boolean> getVotes() {
        return votes;
    }

    public void setVotes(List<Boolean> votes) {
        this.votes = votes;
    }
}
