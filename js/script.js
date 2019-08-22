$(document).ready(function() {
  var customers = null;
  $.getJSON('customers.json', function(data) {
    customers = data;
    data.forEach((row) => {
      var tr = $('<tr />');
      Object.values(row).forEach(column => tr.append(`<td>${column}</td>`));
      $('tbody').append(tr);
    });
  });

  var filter = function(searchAttribute, searchValue) {
    if (searchValue === '') {
      $('tbody tr').filter(function() {
        $(this).toggle(true);
      });
      return;
    }
    console.log(searchAttribute, searchValue);
    var filteredCustomerIds = customers
      .filter(customer => customer[searchAttribute].toString().toLowerCase().includes(searchValue))
      .map(customer => customer.id.toString());
    $('tbody tr').filter(function() {
      $(this).toggle(filteredCustomerIds.includes($(this).children().first().text()));
    });
  }

  $('select').on('change', function() {
    var searchAttribute = $(this).val();
    var searchValue = $('input').val().toLowerCase();
    filter(searchAttribute, searchValue);
  });

  $('input').on('keyup', function() {
    var searchAttribute = $('select').val();
    var searchValue = $(this).val().toLowerCase();
    filter(searchAttribute, searchValue);
  });
});