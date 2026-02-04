jQuery(document).ready(function() {
    jQuery('.datePicker').datepicker({
        dateFormat: 'mm/dd/yy',
        minDate: new Date(2005, 0, 1),
        maxDate: new Date(2050, 11, 31)
    });
});

function changeCompletionDateRowDisplay()
{
  return
  //alert('go')
  //alert(document.forms.length)
  var resolution = caseResult.options[caseResult.selectedIndex].value
  var displayValue = 'inline'
  //alert('resolution = '+resolution)
  if (resolution == '' || resolution == '-Choose-')
    displayValue='none'
  completionDateRow.style.display=displayValue
}

function changeCaseTypeRowDisplay(loadOrChange)
{
  var intakeForm = document.forms["intakeForm"]
  var intakeType = intakeForm["intakeType"].value
  var caseTypeRow = document.getElementById("caseTypeRow")
  var resultRow  = document.getElementById("resultRow")
  var intensitySelect = document.getElementById("intensity")
  var displayValue = ''

  if (intakeType == 'Staff Representation')
  {
    if ('onchange' == loadOrChange)
    {
      clearCompletionDate(intakeForm)
      intensitySelect.selectedIndex=0 // The value '-Choose-'
    }
  }
  else
  {
    if ('onchange' == loadOrChange)
    {
      setCompletionDate(intakeForm)
      intensitySelect.selectedIndex=1 // The value '1'
    }
    displayValue='none'
  }
  caseTypeRow.style.display=displayValue
  resultRow.style.display=displayValue
}

function clearCompletionDate(intakeForm)
{
    jQuery('#completionDate').val('')
}

function setCompletionDate(intakeForm)
{
    jQuery('#completionDate').val(jQuery('#startDate').val())
}
