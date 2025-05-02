@BookStore
Feature: BookStore API
  Demonstrating API capabilities of api_assured

  @Authenticate @Web-UI @BookStore-01
  Scenario: Get books
    * Get all the books from database
    * Get all books on bookstore and add by publisher named No Starch Press to user in context
    * Get the user in context
    * Navigate to the test page
    * Click listed element Book Store Application from categoryCards list on the LandingPage
    * Click the loginButton on the ToolsPage
    * Fill form input on the LoginPage
      | Input Element | Input            |
      | userName      | CONTEXT-userName |
      | password      | CONTEXT-password |
    * Click the loginButton on the LoginPage
    #* Verify the book info for the user in context
    # Crosscheck scenario, the website is full of ads therefore this doesn't work very smoothly
    # TODO: fix the final verification step

  