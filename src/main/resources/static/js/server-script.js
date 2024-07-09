function loadServers() {
    $.ajax({
        url: '/server/allServers',
        type: 'GET',
        dataType: 'json',
        success: function (servers) {
            let tbody = $('#allServers');
            tbody.empty();

            servers.forEach(function (user) {
                let roles = user.roles.map(role => role.role).join(', ');

                let userRow = `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${roles}</td>
                            <td><button type="button" class="btn btn-info btn-edit" data-id="${user.id}" data-toggle="modal" data-target="#ModalEditUser">Edit</button></td>
                            <td><button class="btn btn-danger btn-delete" data-id="${user.id}" data-toggle="modal" data-target="#ModalDeleteUserCentral">Delete</button></td>
                        </tr>
                    `;
                tbody.append(userRow);
            });

            $('.btn-edit').click(function () {
                let userId = $(this).data('id');
                $.ajax({
                    url: '/admin/servers/' + userId,
                    type: 'GET',
                    dataType: 'json',
                    success: function (user) {
                        console.log(user);
                        let form = $('#modalEditUserForm');
                        form.find('#ModalInputId').val(user.id);
                        form.find('#ModalInputFirstName').val(user.name);
                        form.find('#ModalInputLastName').val(user.lastName);
                        form.find('#ModalInputAge').val(user.age);
                        form.find('#ModalInputEmail').val(user.email);
                        let roleSelect = form.find('#ModalInputRole');
                        roleSelect.empty();
                        ['ROLE_ADMIN', 'ROLE_USER'].forEach(role => {
                            let isSelected = user.roles.some(userRole => userRole.role === role);
                            roleSelect.append(new Option(role, role, isSelected, isSelected));
                        });

                        $('#ModalEditUser').modal('show');
                    },
                    error: function (error) {
                        console.error("error of loading user:", error);
                    }
                });
            });

            $('.btn-delete').click(function () {
                let userId = $(this).data('id');
                $.ajax({
                    url: '/admin/servers/' + userId,
                    type: 'GET',
                    dataType: 'json',
                    success: function (user) {
                        // модальное окно удаления пользователя
                        $('#ModalIdDelete').val(user.id);
                        $('#ModalNameDelete').val(user.name);
                        $('#ModalLastNameDelete').val(user.lastName);
                        $('#ModalAgeDelete').val(user.age);
                        $('#ModalEmailDelete').val(user.email);
                        $('#ModalRoleDelete').val(user.roles.map(r => r.role).join(', '));
                        $('#ModalDeleteUserCentral').modal('show');
                    }
                });
            });
        },
        error: function (error) {
            console.error("Error of getting servers:", error);
        }
    });
}

loadServers();

// форма добавления нового пользователя
$('#addUser').click(function (event) {
    event.preventDefault();

    let user = {};

    $('#newUserForm').find('input').each(function () {
        let attr = $(this).attr('name');
        user[attr] = $(this).val();
    });

    user['roles'] = $('#newUserForm').find('select').val().map(role => ({role}));

    $.ajax({
        url: "./admin",
        type: "POST",
        data: JSON.stringify(user),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function () {
            loadUsers();
            window.location.href = '/admin';
        },
        error: function (xhr, status, error) {
            console.error('Error adding new user:', status, error);
        }
    });
});

// обработчик кнопки сохранения редактирования пользователя
$('#saveEditUser').click(function (event) {
    let user = {};

    $('#modalEditUserForm').find('input').each(function () {
        let attr = $(this).attr('name');
        user[attr] = $(this).val();
    });

    user['roles'] = $('#modalEditUserForm').find('select').val().map(role => ({role}));

    $.ajax({
        url: "/admin/servers/" + user.id,
        type: "PATCH",
        data: JSON.stringify(user),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function () {
            loadUsers();
            $('#ModalEditUser').modal('hide');
        },
        error: function (xhr, status, error) {
            console.error('Error of adding new user:', status, error);
        }
    });
});

// Обработчик кнопки подтверждения удаления пользователя
$('#confirmDeleteUser').click(function () {
    let userId = $('#ModalIdDelete').val();
    $.ajax({
        url: '/server/servers/' + userId,
        type: 'DELETE',
        success: function () {
            $('#ModalDeleteUserCentral').modal('hide');
            loadUsers();
        },
        error: function (error) {
            console.error("Error of deleting user:", error);
        }
    });
});

function serverNavigationPanel(user) {
    let email = `<strong>${user.email}</strong>`;
    let roles = user.roles.map(role => role.role.replace('ROLE_', '')).join(', ');
    let content = `${email} with roles: ${roles}`;
    $("#serverNavPanel").html(content);
}

currentUser();
function currentUser() {
    $.ajax({
        url: '/user/me',
        method: 'GET',
        dataType: 'json',
        success: function(user) {
            serverNavigationPanel(server);
            TableOfCurrentUser(server);
        },
        error: function(error) {
            console.error('Error of loading current server:', error);
        }
    });
}

function TableOfCurrentUser(server) {
    let roles = server.roles.map(role => role.role).join(', ');
    let serverRow = `
                <tr>
                    <td>${server.id}</td>
                    <td>${server.name}</td>
                    <td>${server.lastName}</td>
                    <td>${server.age}</td>
                    <td>${server.email}</td>
                    <td>${roles}</td>
                </tr>
            `;
    $('#currentUser').html(serverRow);
}