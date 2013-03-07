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
    var completionDateYearSelect = intakeForm["completionDate_year"]
    var completionDateMonthSelect = intakeForm["completionDate_month"]
    var completionDateDaySelect = intakeForm["completionDate_day"]

    //alert(completionDateYearSelect)
    completionDateYearSelect.selectedIndex=0
    completionDateDaySelect.selectedIndex=0
    completionDateMonthSelect.selectedIndex=0
}

function setCompletionDate(intakeForm)
{
    var completionDateYearSelect = intakeForm["completionDate_year"]
    var completionDateMonthSelect = intakeForm["completionDate_month"]
    var completionDateDaySelect = intakeForm["completionDate_day"]

    var startDateYearSelect = intakeForm["startDate_year"]
    var startDateMonthSelect = intakeForm["startDate_month"]
    var startDateDaySelect = intakeForm["startDate_day"]

    //alert(completionDateYearSelect)
    completionDateYearSelect.selectedIndex=startDateYearSelect.selectedIndex
    completionDateDaySelect.selectedIndex=startDateDaySelect.selectedIndex
    completionDateMonthSelect.selectedIndex=startDateMonthSelect.selectedIndex
}
