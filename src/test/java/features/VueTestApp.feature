@VueTestApp
Feature: Vue Test App

  Tests for the Vue-based Pickleib Test app

  Background: Navigate to test page
    * Navigate to test url

  # ==================== Navigation ====================

  @Web-UI @SCN-VUE-1
  Scenario: Homepage category navigation
    * Wait for element pageTitle on the HomePage to be visible
    * Verify the text of pageTitle on the HomePage contains: UI Components Test Suite
    * Click listed element Forms from categoryCards list on the HomePage
    * Verify the url contains with the text forms

  @Web-UI @SCN-VUE-2
  Scenario: Browser navigation forwards and backwards
    * Navigate to url: http://127.0.0.1:7457/forms
    * Verify the url contains with the text forms
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Verify the url contains with the text buttons
    * Navigate browser backwards
    * Verify the url contains with the text forms

  # ==================== Forms ====================

  @Web-UI @SCN-VUE-3
  Scenario: Form submission and verification
    * Navigate to url: http://127.0.0.1:7457/forms
    * Wait for element nameInput on the FormsPage to be visible
    * Update context testName -> John Smith
    * Update context testEmail -> john@example.com
    # Fill text inputs
    * Fill form input on the FormsPage
      | element       | input              |
      | nameInput     | CONTEXT-testName   |
      | emailInput    | CONTEXT-testEmail  |
      | mobileInput   | 0655500001         |
      | hobbiesInput  | Reading            |
      | addressInput  | 123 Main Street    |
      | cityInput     | Amsterdam          |
    # Select dropdown
    * Select option Male from genderSelect on the FormsPage
    # Pick date
    * Click the dateOfBirthInput on the FormsPage
    * Click listed element 15 from calendarDays list on the FormsPage
    * Click the datePickerSelectButton on the FormsPage
    # Submit and verify
    * Click the submitButton on the FormsPage
    * Wait for element submissionTitle on the FormsPage to be visible
    * Verify the text of submissionTitle on the FormsPage contains: Submitted Information
    # Verify individual submitted values
    * Select listed element containing partial text Name from the submittedRows on the FormsPage and verify its text contains John Smith
    * Select listed element containing partial text Email from the submittedRows on the FormsPage and verify its text contains john@example.com
    * Select listed element containing partial text Gender from the submittedRows on the FormsPage and verify its text contains Male
    * Select listed element containing partial text Mobile from the submittedRows on the FormsPage and verify its text contains 0655500001
    * Select listed element containing partial text Hobbies from the submittedRows on the FormsPage and verify its text contains Reading
    * Select listed element containing partial text Current Address from the submittedRows on the FormsPage and verify its text contains 123 Main Street
    * Select listed element containing partial text City from the submittedRows on the FormsPage and verify its text contains Amsterdam

  @Web-UI @SCN-VUE-4
  Scenario: Fill individual input and verify value
    * Navigate to url: http://127.0.0.1:7457/forms
    * Fill input nameInput on the FormsPage with text: Pickleib Tester
    * Verify that value attribute of element nameInput on the FormsPage contains Pickleib value

  # ==================== Clicks ====================

  @Web-UI @SCN-VUE-5
  Scenario: Button click and result verification
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Verify the text of resultText on the ButtonsPage contains: Last clicked: none
    * Click the primaryButton on the ButtonsPage
    * Verify the text of resultText on the ButtonsPage contains: Primary
    * Click the secondaryButton on the ButtonsPage
    * Verify the text of resultText on the ButtonsPage contains: Secondary
    * Click the dangerButton on the ButtonsPage
    * Verify the text of resultText on the ButtonsPage contains: Danger

  @Web-UI @SCN-VUE-6
  Scenario: Button states — disabled element verification
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Verify the text of pageTitle on the ButtonsPage contains: Buttons
    * Verify that element disabledButton on the ButtonsPage is in disabled state

  # ==================== Alerts & Windows ====================

  @Web-UI @SCN-VUE-7
  Scenario: Alert page button interactions
    * Navigate to url: http://127.0.0.1:7457/alerts
    * Wait for element clickMeButton on the AlertsPage to be visible
    * Verify presence of element clickMeButton on the AlertsPage
    * Verify presence of element rightClickMeButton on the AlertsPage
    * Verify presence of element doubleClickMeButton on the AlertsPage
    * Click the clickMeButton on the AlertsPage

  # ==================== Dropdowns ====================

  @Web-UI @SCN-VUE-8
  Scenario: Dropdown elements presence
    * Navigate to url: http://127.0.0.1:7457/dropdown
    * Wait for element singleSelect on the DropdownPage to be visible
    * Verify presence of element singleSelect on the DropdownPage
    * Verify presence of element multiSelect on the DropdownPage
    * Verify presence of element customDropdownButton on the DropdownPage

  # ==================== Scroll ====================

  @Web-UI @SCN-VUE-9
  Scenario: Tall page scroll and section verification
    * Navigate to url: http://127.0.0.1:7457/tall
    * Verify the text of pageTitle on the TallPage contains: Tall Page
    * Verify presence of element scrollInfo on the TallPage
    * Center element named Section 10 on the sectionHeadings from TallPage
    * Select listed element containing partial text Section 5 from the sectionHeadings on the TallPage and verify its text contains Section 5

  # ==================== Context Store ====================

  @Web-UI @SCN-VUE-10
  Scenario: Context store operations and assertions
    * Update context myKey -> myValue
    * Update context anotherKey -> anotherValue
    * Save context value from myKey context key to copiedKey
    * Assert that value of CONTEXT-myKey is equal to CONTEXT-copiedKey
    * Assert that value of CONTEXT-myKey is not equal to CONTEXT-anotherKey

  # ==================== Element Attributes ====================

  @Web-UI @SCN-VUE-11
  Scenario: Element attribute verification
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Verify that element disabledButton on the ButtonsPage is in disabled state
    * Verify that disabled attribute of element disabledButton on the ButtonsPage contains true value

  # ==================== Wait ====================

  @Web-UI @SCN-VUE-12
  Scenario: Wait operations
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Wait for element pageTitle on the ButtonsPage to be visible
    * Wait 1 seconds
    * Verify presence of element primaryButton on the ButtonsPage

  # ==================== Absence ====================

  @Web-UI @SCN-VUE-13
  Scenario: If present click does not throw
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Wait for element pageTitle on the ButtonsPage to be visible
    * If present, click the primaryButton on the ButtonsPage
    * Verify the text of resultText on the ButtonsPage contains: Primary

  # ==================== Radio Buttons ====================

  @Web-UI @SCN-VUE-14
  Scenario: Radio button click and selected state verification
    * Navigate to url: http://127.0.0.1:7457/radiobuttons
    * Click the yesRadio on the RadioButtonsPage
    * Verify that element yesRadio on the RadioButtonsPage is in selected state

  @Web-UI @SCN-VUE-15
  Scenario: Disabled radio button state verification
    * Navigate to url: http://127.0.0.1:7457/radiobuttons
    * Verify that element noRadio on the RadioButtonsPage is in disabled state

  @Web-UI @SCN-VUE-16
  Scenario: Click listed radio button by text
    * Navigate to url: http://127.0.0.1:7457/radiobuttons
    * Click listed element Impressive from radioLabels list on the RadioButtonsPage
    * Verify that element impressiveRadio on the RadioButtonsPage is in selected state

  # ==================== Text Inputs ====================

  @Web-UI @SCN-VUE-17
  Scenario: Fill text input and verify value attribute
    * Navigate to url: http://127.0.0.1:7457/text-inputs
    * Wait for element textInput on the TextInputsPage to be visible
    * Fill input textInput on the TextInputsPage with text: Hello World
    * Verify that value attribute of element textInput on the TextInputsPage contains Hello World value

  @Web-UI @SCN-VUE-18
  Scenario: Verify disabled input state
    * Navigate to url: http://127.0.0.1:7457/text-inputs
    * Wait for element disabledInput on the TextInputsPage to be visible
    * Verify that element disabledInput on the TextInputsPage is in disabled state

  @Web-UI @SCN-VUE-19
  Scenario: Fill multiple text inputs via form DataTable
    * Navigate to url: http://127.0.0.1:7457/text-inputs
    * Wait for element textInput on the TextInputsPage to be visible
    * Fill form input on the TextInputsPage
      | element       | input              |
      | textInput     | Sample Text        |
      | emailInput    | test@example.com   |
    * Verify that value attribute of element textInput on the TextInputsPage contains Sample Text value

  # ==================== Checkboxes ====================

  @Web-UI @SCN-VUE-20
  Scenario: Checkbox click and selected state
    * Navigate to url: http://127.0.0.1:7457/checkboxes
    * Wait for element uncheckedCheckbox on the CheckboxesPage to be visible
    * Click the uncheckedCheckbox on the CheckboxesPage
    * Verify that element uncheckedCheckbox on the CheckboxesPage is in selected state

  @Web-UI @SCN-VUE-21
  Scenario: Disabled checkbox state verification
    * Navigate to url: http://127.0.0.1:7457/checkboxes
    * Wait for element disabledUncheckedCheckbox on the CheckboxesPage to be visible
    * Verify that element disabledUncheckedCheckbox on the CheckboxesPage is in disabled state

  # ==================== Modal ====================

  @Web-UI @SCN-VUE-22
  Scenario: Open modal and verify status change
    * Navigate to url: http://127.0.0.1:7457/modal
    * Wait for element openModalButton on the ModalPage to be visible
    * Verify the text of statusText on the ModalPage contains: idle
    * Click the openModalButton on the ModalPage
    * Wait 1 seconds
    * Verify the text of statusText on the ModalPage contains: open

  # ==================== Login Form ====================

  @Web-UI @SCN-VUE-23
  Scenario: Login form fill and submit
    * Navigate to url: http://127.0.0.1:7457/login-form
    * Wait for element usernameInput on the LoginFormPage to be visible
    * Update context login-user -> admin
    * Update context login-pass -> password123
    * Fill input usernameInput on the LoginFormPage with text: CONTEXT-login-user
    * Fill input passwordInput on the LoginFormPage with text: CONTEXT-login-pass
    * Click the signInButton on the LoginFormPage

  # ==================== Drag and Drop ====================

  @Web-UI @SCN-VUE-24
  Scenario: Droppable page element presence
    * Navigate to url: http://127.0.0.1:7457/droppable
    * Wait for element draggableItem on the DroppablePage to be visible
    * Verify presence of element draggableItem on the DroppablePage
    * Verify presence of element dropTarget on the DroppablePage
    * Verify the text of statusText on the DroppablePage contains: Ready

  @Web-UI @SCN-VUE-DND-1
  Scenario: Drag and drop to zone
    * Navigate to url: http://127.0.0.1:7457/droppable
    * Wait for element statusText on the DroppablePage to be visible
    * Verify the text of statusText on the DroppablePage contains: Ready
    * Verify presence of element draggableItem on the DroppablePage
    * Verify presence of element redZone on the DroppablePage
    * Verify presence of element resetButton on the DroppablePage

  @Web-UI @SCN-VUE-DND-2
  Scenario: Draggable blocks
    * Navigate to url: http://127.0.0.1:7457/draggable
    * Wait for element statusText on the DraggablePage to be visible
    * Verify the text of statusText on the DraggablePage contains: Dragging: none
    * Verify presence of element blockA on the DraggablePage
    * Verify presence of element blockB on the DraggablePage

  # ==================== New Tab ====================

  @Web-UI @SCN-VUE-25
  Scenario: Click New Tab button and switch tab
    * Navigate to url: http://127.0.0.1:7457/alerts
    * Wait for element clickMeButton on the AlertsPage to be visible
    * Click button with New Tab text using web driver
    * Wait 1 seconds
    * Switch to the next tab
    * Verify the url contains with the text 127.0.0.1

  # ==================== Scroll with web driver ====================

  @Web-UI @SCN-VUE-26
  Scenario: Scroll down on tall page using JS
    * Navigate to url: http://127.0.0.1:7457/tall
    * Wait for element pageTitle on the TallPage to be visible
    * Execute JS command: window.scrollBy(0, 500)

  # ==================== Click by text ====================

  @Web-UI @SCN-VUE-27
  Scenario: Click button by visible text
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Wait for element resultText on the ButtonsPage to be visible
    * Click button with Primary text using web driver
    * Verify the text of resultText on the ButtonsPage contains: Primary

  # ==================== Acquire attribute ====================

  @Web-UI @SCN-VUE-28
  Scenario: Acquire attribute value of an element
    * Navigate to url: http://127.0.0.1:7457/text-inputs
    * Wait for element disabledInput on the TextInputsPage to be visible
    * Acquire the value attribute of disabledInput on the TextInputsPage

  # ==================== Execute JS ====================

  @Web-UI @SCN-VUE-29
  Scenario: Execute JavaScript command
    * Navigate to url: http://127.0.0.1:7457/buttons
    * Wait for element pageTitle on the ButtonsPage to be visible
    * Execute JS command: document.title = 'JS Test'

  # ==================== Context text replacement ====================

  @Web-UI @SCN-VUE-30
  Scenario: Perform text replacement on context value
    * Update context rawUrl -> https://example.com/path
    * Perform text replacement on CONTEXT-rawUrl context by replacing https:// value in cleanUrl
    * Assert that value of CONTEXT-cleanUrl is equal to example.com/path

  # ==================== Tabs ====================

  @Web-UI @SCN-VUE-TABS
  Scenario: Tab navigation switches content panels
    * Navigate to url: http://127.0.0.1:7457/tabs
    * Wait for element overviewTab on the TabsPage to be visible
    * Verify the text of activePanel on the TabsPage contains: Overview
    * Click the detailsTab on the TabsPage
    * Verify the text of activePanel on the TabsPage contains: Details
    * Click the codeTab on the TabsPage
    * Verify the text of activePanel on the TabsPage contains: Code
    * Click the settingsTab on the TabsPage
    * Verify the text of activePanel on the TabsPage contains: Settings

  # ==================== Accordion ====================

  @Web-UI @SCN-VUE-ACCORDION
  Scenario: Accordion expand and collapse
    * Navigate to url: http://127.0.0.1:7457/accordion
    * Wait for element expandAllButton on the AccordionPage to be visible
    * Verify absence of element firstBody on the AccordionPage
    * Click the expandAllButton on the AccordionPage
    * Verify the text of firstBody on the AccordionPage contains: accordion
    * Click the collapseAllButton on the AccordionPage
    * Verify absence of element firstBody on the AccordionPage

  # ==================== Progress ====================

  @Web-UI @SCN-VUE-PROGRESS
  Scenario: Progress bar animation
    * Navigate to url: http://127.0.0.1:7457/progress
    * Wait for element animatedValue on the ProgressPage to be visible
    * Verify the text of animatedValue on the ProgressPage contains: 0%
    * Click the startButton on the ProgressPage
    * Wait 2 seconds
    * Click the resetButton on the ProgressPage
    * Verify the text of animatedValue on the ProgressPage contains: 0%

  # ==================== Table ====================

  @Web-UI @SCN-VUE-TABLE
  Scenario: Table data and row selection
    * Navigate to url: http://127.0.0.1:7457/table
    * Wait for element selectedCount on the TablePage to be visible
    * Verify the text of selectedCount on the TablePage contains: 0 rows selected
    * Verify the text of pageInfo on the TablePage contains: Page 1 of 4
    * Click the firstRowCheckbox on the TablePage
    * Verify the text of selectedCount on the TablePage contains: 1 rows selected
    * Click the nextButton on the TablePage
    * Verify the text of pageInfo on the TablePage contains: Page 2 of 4

  # ==================== Toast ====================

  @Web-UI @SCN-VUE-TOAST
  Scenario: Toast notification triggers
    * Navigate to url: http://127.0.0.1:7457/toast
    * Wait for element successButton on the ToastPage to be visible
    * Click the successButton on the ToastPage
    * Verify the text of toastContainer on the ToastPage contains: successfully

  # ==================== Tooltip ====================

  @Web-UI @SCN-VUE-TOOLTIP
  Scenario: Tooltip on hover
    * Navigate to url: http://127.0.0.1:7457/tooltip
    * Wait for element hoverTarget on the TooltipPage to be visible
    * Hover over the hoverTarget on the TooltipPage
    * Wait for element tooltipContent on the TooltipPage to be visible
    * Verify the text of tooltipContent on the TooltipPage contains: tooltip one

  # ==================== Drawer ====================

  @Web-UI @SCN-VUE-DRAWER
  Scenario: Drawer open and close
    * Navigate to url: http://127.0.0.1:7457/drawer
    * Wait for element statusText on the DrawerPage to be visible
    * Verify the text of statusText on the DrawerPage contains: closed
    * Click the openLeftButton on the DrawerPage
    * Verify the text of statusText on the DrawerPage contains: left
    * Click the closeButton on the DrawerPage
    * Verify the text of statusText on the DrawerPage contains: closed

  # ==================== Sortable ====================

  @Web-UI @SCN-VUE-SORTABLE
  Scenario: Sortable list items
    * Navigate to url: http://127.0.0.1:7457/sortable
    * Wait for element firstListCount on the SortablePage to be visible
    * Verify the text of firstListCount on the SortablePage contains: 3 items
    * Verify the text of secondListCount on the SortablePage contains: 2 items
    * Verify the text of firstItem on the SortablePage contains: Item A

  # ==================== Resizable ====================

  @Web-UI @SCN-VUE-RESIZABLE
  Scenario: Resizable panel
    * Navigate to url: http://127.0.0.1:7457/resizable
    * Wait for element widthDisplay on the ResizablePage to be visible
    * Verify the text of widthDisplay on the ResizablePage contains: 300px

  # ==================== File Upload ====================

  @Web-UI @SCN-VUE-FILEUPLOAD
  Scenario: File upload inputs
    * Navigate to url: http://127.0.0.1:7457/file-upload
    * Wait for element singleFileStatus on the FileUploadPage to be visible
    * Verify the text of singleFileStatus on the FileUploadPage contains: No file selected
    * Verify the text of multipleFileList on the FileUploadPage contains: No files selected
    * Verify the text of dropList on the FileUploadPage contains: No files dropped

  # ==================== Pinia Counter ====================

  @Web-UI @SCN-VUE-COUNTER
  Scenario: Pinia counter state management
    * Navigate to url: http://127.0.0.1:7457/pinia-counter
    * Wait for element counterDisplay on the PiniaCounterPage to be visible
    * Verify the text of counterDisplay on the PiniaCounterPage contains: 0
    * Click the incrementButton on the PiniaCounterPage
    * Verify the text of counterDisplay on the PiniaCounterPage contains: 1
    * Click the incrementButton on the PiniaCounterPage
    * Verify the text of counterDisplay on the PiniaCounterPage contains: 2
    * Click the decrementButton on the PiniaCounterPage
    * Verify the text of counterDisplay on the PiniaCounterPage contains: 1
    * Click the resetButton on the PiniaCounterPage
    * Verify the text of counterDisplay on the PiniaCounterPage contains: 0

  # ==================== Gallery ====================

  @Web-UI @SCN-VUE-GALLERY
  Scenario: Image gallery thumbnails and lightbox
    * Navigate to url: http://127.0.0.1:7457/gallery
    * Wait for element firstImage on the GalleryPage to be visible
    * Verify the text of firstImage on the GalleryPage contains: Mountain landscape
    * Click the firstImage on the GalleryPage
    * Wait for element galleryCaption on the GalleryPage to be visible
    * Verify the text of galleryCaption on the GalleryPage contains: Image 1 of 8
    * Click the nextButton on the GalleryPage
    * Verify the text of galleryCaption on the GalleryPage contains: Image 2 of 8
    * Click the closeButton on the GalleryPage
