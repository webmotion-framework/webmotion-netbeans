package org.debux.webmotion.pollmotion.entity;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import org.debux.webmotion.jpa.IdentifiableEntity;

/**
 *
 * @author julien
 */
@Entity
public class Poll extends IdentifiableEntity {
    
    @Basic
    protected String email;
    
    @Basic
    protected String question;
    
    @ElementCollection
    protected List<String> choices;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }
}
