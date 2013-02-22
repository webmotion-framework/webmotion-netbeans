<!DOCTYPE html>
<html lang="en">
    <head></head>

    <body>
        <h4>You can vote.</h4>
        <hr>
        ${poll.choices}
        
        <form class="form-horizontal" method="POST" action="../../service/vote">
            <input type="hidden" name="poll" value="${poll.id}">
            
            <div class="row-fluid">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Email</th>
                            <th>Choice 1</th>
                            <th>Choice 2</th>
                            <th>Choice 3</th>
                            <th>Choice 4</th>
                            <th>Choice 5</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>julien</td>
                            <td><img src="../../img/glyphicons_ok.png"></td>
                            <td><img src="../../img/glyphicons_ok.png"></td>
                            <td><img src="../../img/glyphicons_ko.png"></td>
                            <td><img src="../../img/glyphicons_ko.png"></td>
                            <td><img src="../../img/glyphicons_ok.png"></td>
                        </tr>
                        <tr>
                            <td>
                                <div>
                                    <input type="text" name="email" placeholder="Email">
                                </div>
                                <div>
                                    <button type="submit" class="btn">Vote</button>
                                </div>
                            </td>
                            <td><input name="votes" type="checkbox" ></td>
                            <td><input name="votes" type="checkbox" ></td>
                            <td><input name="votes" type="checkbox" ></td>
                            <td><input name="votes" type="checkbox" ></td>
                            <td><input name="votes" type="checkbox" ></td>
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
