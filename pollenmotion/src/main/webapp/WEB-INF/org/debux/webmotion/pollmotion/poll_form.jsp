<!DOCTYPE html>
<html lang="en">
    <head>
        <script type="text/javascript">
            $(function() {
                var delChoice = function() {
                    $(this).parent().remove();
                };
                                
                $(".del").click(delChoice);

                var addChoice = function() {
                    var choice = $("#choices div:first-child").clone();
                    choice.children(".del").click(delChoice);
                    choice.appendTo('#choices');
                };
                addChoice();
                addChoice();
                
                $("#add").click(function() {
                    addChoice();
                });
            });
        </script>
    </head>

    <body>
        <h4>Create your vote.</h4>
        <hr>
            
        <div class="row-fluid">
            <form class="form-horizontal" method="POST" action="./poll">

                <div class="control-group">
                    <label class="control-label" for="inputEmail">Email :</label>
                    <div class="controls">
                        <input type="text" id="inputEmail" name="email" placeholder="Email">
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="inputQuestion">Question :</label>
                    <div class="controls">
                        <input type="text" id="inputQuestion" name="question" placeholder="Question">
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="inputChoices">Choices :</label>
                    <div id="choices">
                        <div class="controls">
                            <input type="text" name="choices" placeholder="Choice"><button type="button" class="btn del">Del</button>
                        </div>
                    </div>

                    <div class="controls">
                        <button id="add" type="button" class="btn">Add</button>
                    </div>
                </div>

                <div class="control-group">
                    <div class="controls">
                        <button type="submit" class="btn">Create</button>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
