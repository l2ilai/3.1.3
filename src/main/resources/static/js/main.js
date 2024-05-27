
$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "/allUsers",
        dataType: 'json',
        success: function (users) {
            let table = $('#allUsers');
            table.empty();
            users.forEach(function (user){
            let roles = user.role.map(role =>role.role).join(' ');
            let users =
                `<table><tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.role}</td>
                    <td>
                        <button className="btn btn-info btn-edit" data-id="${user.id}">Edit</button>
                    </td>
                    <td>
                        <button onclick="delete(${user.id})" className="btn btn-danger btn-delete" data-id="${user.id}">Delete</button>
                    </td>
                </tr>> </table> 
                `;
                table.append(users);

            });
            $('.btn-edit').click(function (){
                let userId = $(this).data('id');
                $.ajax({
                    url: "/edit",
                    type: "GET",
                    dataType: 'json',
                    success: function (user) {
                        $('#user_id').val(user.id);
                        $('#user_name').val(user.name);
                        $('#user_role').val(user.role);
                        $('#edit_user').model('user');
                    }
                });
            });
        }
    })
});
