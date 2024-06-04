$('document').ready(function(){
    $('.action-user-edit').on('click', function(e){
            e.preventDefault();
            var href=$(this).attr('href')
            console.log(href)
            $.get(href, function(user, status){
            console.log(user.userId)
                $('#idEdit').val(user.id);
                $('#userIdEdit').val(user.userId);
                $('#firstNameEdit').val(user.firstName);
                $('#lastNameEdit').val(user.lastName);
                $('#emailEdit').val(user.email);
                $('#departmentIdEdit').val(user.departmentId);
                $('#roleEdit').val(user.role);
                $('#passwordEdit').val(user.password);
            });
            $('#editModal').modal('show');
     });
});

