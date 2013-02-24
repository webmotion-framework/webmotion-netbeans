<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head></head>

    <body>
        <h4>You can vote.</h4>
        <hr>
        
        <form class="form-horizontal" method="POST" action="../vote/${poll.id}">
            
            <div class="row-fluid">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Email</th>
                            <c:forEach var="choice" items="${poll.choices}">
                                <th>${choice}</th>
                            </c:forEach>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="vote" items="${votes}">
                            <tr>
                                <td>${vote.email}</td>
                                <c:forEach var="value" items="${vote.votes}">
                                    <c:if test="${value}">
                                        <td><img src="../img/glyphicons_ok.png"></td>
                                    </c:if>
                                    <c:if test="not ${value}">
                                        <td><img src="../img/glyphicons_ko.png"></td>
                                    </c:if>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <div>
                                    <input type="text" name="email" placeholder="Email">
                                </div>
                                <div>
                                    <button type="submit" class="btn">Vote</button>
                                </div>
                            </td>
                            <c:forEach var="choice" items="${poll.choices}">
                                <td><input name="votes" type="checkbox"></td>
                            </c:forEach>
                        </tr>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td>Result :</td>
                            <td>3</td>
                            <td>0</td>
                            <td>3</td>
                            <td>0</td>
                            <td>3</td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </form>
    </body>
</html>
