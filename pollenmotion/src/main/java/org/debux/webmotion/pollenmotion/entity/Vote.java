package org.debux.webmotion.pollenmotion.entity;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.debux.webmotion.jpa.IdentifiableEntity;

/**
 *
 * @author julien
 */
@Entity
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
