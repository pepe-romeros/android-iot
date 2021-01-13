showCreateModal = function() {
    $('#modalHeader').html('Add Person');
    $('#modalAddForm').modal('toggle');

    $('#SaveButton').off('click').on('click', createPerson);
}

function createPerson() {
    console.log("saved");
    $('#modalAddForm').modal('toggle');
    $.post( "/names", $("#nameForm").serialize())
    .done(function(result) {
        location.reload();
    })
    .fail(function(result) {
        console.log("Failed creating person");
    });
}

showEditModal = function(id) {
    var personJson = $.getJSON("/names/"+id, function(json) {
        $('#firstName').val(json.first_name);
        $('#lastName').val(json.last_name);
        $('#SaveButton').click(function () {
            editPerson(json);
        });
    });
    $('#modalHeader').html('Edit Person');
    $('#modalAddForm').modal('toggle');
};

function editPerson(personJson) {
    console.log("Edit Item called");
    console.log(personJson);
    // logic for updating an existing item
    personJson.first_name = $('#firstName').val();
    personJson.last_name = $('#lastName').val();
    $.ajax({
        type: 'PUT',
        url: '/names/'+personJson.id,
        contentType: 'application/json',
        data: personJson, // access in body
    }).done(function (result) {
        location.reload();
    }).fail(function (result) {
        console.log('Fail editing person');
    });
    $('#modalAddForm').modal('toggle');
}

function clearForm() {
    $('#firstName').val("");
    $('#lastName').val("");
}

function deletePerson(id) {
    $.ajax({
        type: 'DELETE',
        url: '/names/'+id,
    }).done(function (result) {
        location.reload();
    }).fail(function (result) {
        console.log('Fail deleting person');
    });
}

function populateTable() {
    var my_array = $.getJSON("/names", function(json) {
        var tableRef = document.getElementById('namesTable').getElementsByTagName('tbody')[0];
        for(i =0; i<json.length; i++ ) {
            var newRow = tableRef.insertRow();
            newRow.insertCell(0).appendChild(document.createTextNode(json[i].first_name));
            newRow.insertCell(1).appendChild(document.createTextNode(json[i].last_name));
            newRow.insertCell(2).insertAdjacentHTML('beforeend','<button type="button" class="btn btn-primary mr-1" onclick="showEditModal('+ json[i].id + ')">Edit</button><button type="button" class="btn btn-danger" onclick="deletePerson(' + json[i].id + ')">Delete</button>');
        }
    });
}

$(document).ready(function () {
    populateTable();
});
