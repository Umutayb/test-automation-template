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
    * Fill form input on the PracticeFormPage
      | Input Element  | Input                |
      | firstNameInput | Umut                 |
      | lastNameInput  | Bora                 |
      | userEmailInput | umutaybora@gmail.com |
      | userNumber     | 0655500001           |
    * Click listed element Male from genderRadioButtons list on the PracticeFormPage
    * Click the dateOfBirthInput on the PracticeFormPage
    * Click listed element September from Months list on the PracticeFormPage
    * Click listed element 1996 from Years list on the PracticeFormPage
    * Click listed element 2 from Days list on the PracticeFormPage
    * Fill input subjectsInput on the PracticeFormPage with text: Science
    * Click listed element Computer Science from subjectOptions list on the PracticeFormPage
    * Click listed element Music from hobbiesCheckboxes list on the PracticeFormPage
    * Upload file on input uploadPictureButton on the PracticeFormPage with file: UPLOAD-src/test/resources/files/profile-picture.jpg
    * Fill input currentAddressInput on the PracticeFormPage with text: Apeldoorn - Netherlands
    * Click the stateDropdown on the PracticeFormPage
    * Click listed element Haryana from dropdownOptions list on the PracticeFormPage
    * Click the cityDropdown on the PracticeFormPage
    * Click listed element Karnal from dropdownOptions list on the PracticeFormPage
    * Click the submitButton on the PracticeFormPage
    * Select listed element containing partial text Student Name from the resultModelDataRows on the PracticeFormPage and verify its text contains Umut Bora
    * Select listed element containing partial text Student Email from the resultModelDataRows on the PracticeFormPage and verify its text contains umutaybora@gmail.com
    * Select listed element containing partial text Gender from the resultModelDataRows on the PracticeFormPage and verify its text contains Male
    * Select listed element containing partial text Mobile from the resultModelDataRows on the PracticeFormPage and verify its text contains 0655500001
    * Select listed element containing partial text Date from the resultModelDataRows on the PracticeFormPage and verify its text contains 02 September,1996
    * Select listed element containing partial text Subjects from the resultModelDataRows on the PracticeFormPage and verify its text contains Computer Science
    * Select listed element containing partial text Hobbies from the resultModelDataRows on the PracticeFormPage and verify its text contains Music
    * Select listed element containing partial text Picture from the resultModelDataRows on the PracticeFormPage and verify its text contains profile-picture.jpg
    * Select listed element containing partial text Address from the resultModelDataRows on the PracticeFormPage and verify its text contains Apeldoorn - Netherlands
    * Select listed element containing partial text State and City from the resultModelDataRows on the PracticeFormPage and verify its text contains Haryana Karnal
    * Click the resultModelCloseButton on the PracticeFormPage

  @Web-UI @SCN-DEMO-QA-2
  Scenario: DemoQA slider interactions
    * Click listed element Widgets from categoryCards list on the LandingPage
    * Click listed element Slider from tools list on the ToolsPage

  @Web-UI @SCN-DEMO-QA-3
  Scenario: DemoQA bookstore test
    * Click listed element Book Store Application from categoryCards list on the LandingPage
    * Print book attribute
