$('document').ready(function(){
    $('.add-user').on('click', function(e){
            e.preventDefault();
            console.log("click")
            var href=$(this).attr('href')

            console.log(href)
            $.get(href, function(user, status){
                $('#userId').val(user.userId);
                $('#firstName').val(user.firstName);
                $('#lastName').val(user.lastName);
                $('#email').val(user.email);
                $('#password').val(user.password);
            });

            $('#addModalUser').modal('show');
     });
});


