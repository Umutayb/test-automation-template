@VueTestApp
Feature: Vue Test App

  Tests for the Vue-based Pickleib Test app at http://127.0.0.1:7457/

  @Web-UI @SCN-VUE-1
  Scenario: Navigate to forms via homepage category card
    * Navigate to url: http://127.0.0.1:7457/
    * Wait for element pageTitle on the HomePage to be visible
    * Verify the text of pageTitle on the HomePage contains: UI Components Test Suite
    * Click listed element Forms from categoryCards list on the HomePage
    * Wait for element pageTitle on the FormsPage to be visible
    * Verify the text of pageTitle on the FormsPage contains: Submission Form

  @Web-UI @SCN-VUE-2
  Scenario: Form submission and verification
    * Navigate to url: http://127.0.0.1:7457/forms
    * Wait for element nameInput on the FormsPage to be visible
    * Fill form input on the FormsPage
      | Input Element | Input              |
      | nameInput     | John Smith         |
      | emailInput    | john@example.com   |
      | mobileInput   | 0655500001         |
      | hobbiesInput  | Reading            |
      | addressInput  | 123 Main Street    |
      | cityInput     | Amsterdam          |
    * Click the submitButton on the FormsPage
    * Wait for element submissionTitle on the FormsPage to be visible
    * Verify the text of submissionTitle on the FormsPage contains: Submitted Information

  @Web-UI @SCN-VUE-3
  Scenario: Alerts page button presence
    * Navigate to url: http://127.0.0.1:7457/alerts
    * Wait for element clickMeButton on the AlertsPage to be visible
    * Verify presence of element clickMeButton on the AlertsPage
    * Verify presence of element rightClickMeButton on the AlertsPage
    * Verify presence of element doubleClickMeButton on the AlertsPage

  @Web-UI @SCN-VUE-4
  Scenario: Buttons page click and verify result
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Wait for element resultText on the ButtonsPage to be visible
    * Verify the text of resultText on the ButtonsPage contains: Last clicked: none
    * Click the primaryButton on the ButtonsPage
    * Verify the text of resultText on the ButtonsPage contains: Primary

  @Web-UI @SCN-VUE-5
  Scenario: Navigate to buttons page directly
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Wait for element pageTitle on the ButtonsPage to be visible
    * Verify the text of pageTitle on the ButtonsPage contains: Buttons
    * Click the secondaryButton on the ButtonsPage
    * Verify the text of resultText on the ButtonsPage contains: Secondary

  @Web-UI @SCN-VUE-6
  Scenario: Dropdown page element presence
    * Navigate to url: http://127.0.0.1:7457/dropdown
    * Wait for element singleSelect on the DropdownPage to be visible
    * Verify presence of element singleSelect on the DropdownPage
    * Verify presence of element multiSelect on the DropdownPage
    * Verify presence of element customDropdownButton on the DropdownPage

  @Web-UI @SCN-VUE-7
  Scenario: Tall page scroll and section verification
    * Navigate to url: http://127.0.0.1:7457/tall
    * Wait for element pageTitle on the TallPage to be visible
    * Verify the text of pageTitle on the TallPage contains: Tall Page
    * Verify presence of element scrollInfo on the TallPage
    * Select listed element containing partial text Section 5 from the sectionHeadings on the TallPage and verify its text contains Section 5
    * Center element named Section 10 on the sectionHeadings from TallPage
