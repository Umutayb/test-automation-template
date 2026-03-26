@VueTestApp
Feature: Vue Test App

  Tests for the Vue-based Pickleib Test app at http://127.0.0.1:7457/

  Background: Navigate to test page
    * Navigate to url: http://127.0.0.1:7457/

  @Web-UI @SCN-VUE-1
  Scenario: Homepage category navigation
    * Wait for element pageTitle on the HomePage to be visible
    * Verify the text of pageTitle on the HomePage contains: UI Components Test Suite
    * Click listed element Forms from categoryCards list on the HomePage
    * Verify the url contains with the text forms

  @Web-UI @SCN-VUE-2
  Scenario: Form submission and verification
    * Navigate to url: http://127.0.0.1:7457/forms
    * Wait for element nameInput on the FormsPage to be visible
    * Update context testName -> John Smith
    * Update context testEmail -> john@example.com
    * Fill form input on the FormsPage
      | Input Element | Input              |
      | nameInput     | CONTEXT-testName   |
      | emailInput    | CONTEXT-testEmail  |
      | mobileInput   | 0655500001         |
      | hobbiesInput  | Reading            |
      | addressInput  | 123 Main Street    |
      | cityInput     | Amsterdam          |
    * Click the submitButton on the FormsPage
    * Wait for element submissionTitle on the FormsPage to be visible
    * Verify the text of submissionTitle on the FormsPage contains: Submitted Information

  @Web-UI @SCN-VUE-3
  Scenario: Alert button click and dismiss
    * Navigate to url: http://127.0.0.1:7457/alerts
    * Wait for element clickMeButton on the AlertsPage to be visible
    * Verify presence of element clickMeButton on the AlertsPage
    * Verify presence of element rightClickMeButton on the AlertsPage
    * Verify presence of element doubleClickMeButton on the AlertsPage
    * Click the clickMeButton on the AlertsPage

  @Web-UI @SCN-VUE-4
  Scenario: Button click and result verification
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Verify the text of resultText on the ButtonsPage contains: Last clicked: none
    * Click the primaryButton on the ButtonsPage
    * Verify the text of resultText on the ButtonsPage contains: Primary
    * Click the secondaryButton on the ButtonsPage
    * Verify the text of resultText on the ButtonsPage contains: Secondary
    * Click the dangerButton on the ButtonsPage
    * Verify the text of resultText on the ButtonsPage contains: Danger

  @Web-UI @SCN-VUE-5
  Scenario: Button states verification
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Wait for element pageTitle on the ButtonsPage to be visible
    * Verify the text of pageTitle on the ButtonsPage contains: Buttons
    * Verify that element disabledButton on the ButtonsPage is in disabled state

  @Web-UI @SCN-VUE-6
  Scenario: Dropdown element verification
    * Navigate to url: http://127.0.0.1:7457/dropdown
    * Wait for element singleSelect on the DropdownPage to be visible
    * Verify presence of element singleSelect on the DropdownPage
    * Verify presence of element multiSelect on the DropdownPage
    * Verify presence of element customDropdownButton on the DropdownPage

  @Web-UI @SCN-VUE-7
  Scenario: Tall page scroll interactions
    * Navigate to url: http://127.0.0.1:7457/tall
    * Verify the text of pageTitle on the TallPage contains: Tall Page
    * Verify presence of element scrollInfo on the TallPage
    * Center element named Section 10 on the sectionHeadings from TallPage
    * Select listed element containing partial text Section 5 from the sectionHeadings on the TallPage and verify its text contains Section 5

  @Web-UI @SCN-VUE-8
  Scenario: Browser navigation
    * Navigate to url: http://127.0.0.1:7457/forms
    * Verify the url contains with the text forms
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Verify the url contains with the text buttons
    * Navigate browser backwards
    * Verify the url contains with the text forms

  @Web-UI @SCN-VUE-9
  Scenario: Context store operations
    * Update context myKey -> myValue
    * Update context anotherKey -> anotherValue
    * Save context value from myKey context key to copiedKey
    * Assert that value of CONTEXT-myKey is equal to CONTEXT-copiedKey

  @Web-UI @SCN-VUE-10
  Scenario: Element attribute verification
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Verify that element disabledButton on the ButtonsPage is in disabled state
    * Verify that disabled attribute of element disabledButton on the ButtonsPage contains true value

  @Web-UI @SCN-VUE-11
  Scenario: Wait operations
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Wait for element pageTitle on the ButtonsPage to be visible
    * Wait 1 seconds
    * Verify presence of element primaryButton on the ButtonsPage

  @Web-UI @SCN-VUE-12
  Scenario: Fill input and verify element text
    * Navigate to url: http://127.0.0.1:7457/forms
    * Fill input nameInput on the FormsPage with text: Pickleib Tester
    * Verify that value attribute of element nameInput on the FormsPage contains Pickleib value
