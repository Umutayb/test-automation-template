@Login
Feature: Login
  A feature where the login scenarios are tested.
  There are dedicated scenarios for acceptance and test environments
  Background: Context user
    * Navigate to the test page

  @Web-UI @SCN-DEMO-QA-1
  Scenario: DemoQA form interactions
    * Click listed element Forms from categoryCards list on the LandingPage
    * Click listed element Practice Form from tools list on the ToolsPage
    * Fill component form input of practiceForm component on the ToolsPage
      | Input Element  | Input                |
      | firstNameInput | Umut                 |
      | lastNameInput  | Bora                 |
      | userEmailInput | umutaybora@gmail.com |
      | userNumber     | 0655500001           |
    * Click listed component element Male of PracticeForm from genderRadioButtons list on the ToolsPage
    * Click component element dateOfBirthInput of PracticeForm component on the ToolsPage
    * Click listed component element September of DatePicker from months list on the ToolsPage
    * Click listed component element 1996 of DatePicker from years list on the ToolsPage
    * Click listed component element 2 of DatePicker from days list on the ToolsPage
    * Fill component input subjectsInput of PracticeForm component on the ToolsPage with text: Science
    * Click listed component element Computer Science of PracticeForm from dropdownOptions list on the ToolsPage
    * Click listed component element Music of PracticeForm from hobbiesCheckboxes list on the ToolsPage
    * Upload file on component input uploadPictureButton of PracticeForm component on the ToolsPage with file: UPLOAD-src/test/resources/files/profile-picture.jpg
    * Fill component input currentAddressInput of PracticeForm component on the ToolsPage with text: Den Haag - Netherlands
    * Click component element stateDropdown of PracticeForm component on the ToolsPage
    * Click listed component element Haryana of PracticeForm from dropdownOptions list on the ToolsPage
    * Click component element cityDropdown of PracticeForm component on the ToolsPage
    * Click listed component element Karnal of PracticeForm from dropdownOptions list on the ToolsPage
    * Click component element submitButton of PracticeForm component on the ToolsPage
    * Verify text of listed component element Student Name from the dataRows of resultModel on the ToolsPage contains Umut Bora
    * Verify text of listed component element Student Email from the dataRows of resultModel on the ToolsPage contains umutaybora@gmail.com
    * Verify text of listed component element Gender from the dataRows of resultModel on the ToolsPage contains Male
    * Verify text of listed component element Mobile from the dataRows of resultModel on the ToolsPage contains 0655500001
    * Verify text of listed component element Date of Birth from the dataRows of resultModel on the ToolsPage contains 02 September,1996
    * Verify text of listed component element Subjects from the dataRows of resultModel on the ToolsPage contains Computer Science
    * Verify text of listed component element Hobbies from the dataRows of resultModel on the ToolsPage contains Music
    * Verify text of listed component element Picture from the dataRows of resultModel on the ToolsPage contains profile-picture.jpg
    * Verify text of listed component element Mobile from the dataRows of resultModel on the ToolsPage contains 0655500001
    * Verify text of listed component element Address from the dataRows of resultModel on the ToolsPage contains Den Haag - Netherlands
    * Verify text of listed component element State and City from the dataRows of resultModel on the ToolsPage contains Haryana Karnal
    * Click component element closeButton of ResultModel component on the ToolsPage

  @Web-UI @SCN-DEMO-QA-2
  Scenario: DemoQA slider interactions
    * Click listed element Widgets from categoryCards list on the LandingPage
    * Click listed element Slider from tools list on the ToolsPage

  @Web-UI @SCN-DEMO-QA-3
  Scenario: DemoQA bookstore test
    * Click listed element Book Store Application from categoryCards list on the LandingPage
    * Print book attribute
