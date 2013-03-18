<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <script type="text/javascript">
            $(function() {
                $(".checkable").click(function() {
                    var check = $(this);
                    var next = check.next();

                    if (next.val() == "false") {
                        check.attr('src', '../img/glyphicons_ok.png')
                        next.val("true");
                    } else {
                        check.attr('src', '../img/glyphicons_ko.png')
                        next.val("false");
                    }
                });
            });
        </script>
    </head>

    <body>
        <h4>You can vote.</h4>
        <hr>
        <h5><span class="label label-info">Question :</span> ${poll.question}</h5>
        
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
                                    <c:if test="${not value}">
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
                                <td><img class="checkable" src="../img/glyphicons_ko.png"><input name="votes" type="hidden" value="false"></td>
                            </c:forEach>
                        </tr>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td>Result :</td>
                            <c:forEach var="result" items="${results}">
                                <td>${result}</td>
                            </c:forEach>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </form>
    </body>
</html>
