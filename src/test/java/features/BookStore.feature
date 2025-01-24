Feature: BookStore API
  @Authenticate @Web-UI
  Scenario: Get books
    * Get all the books from database
    * Get all books on bookstore and add by publisher named No Starch Press to user in context
    * Get the user in context
    * Navigate to the test page
    * Click listed element Book Store Application from categoryCards list on the LandingPage
    * Click the loginButton on the ToolsPage
    * Fill component form input of LoginWrapper component on the loginPage
      | Input Element | Input            |
      | userName      | CONTEXT-userName |
      | password      | CONTEXT-password |
    * Click component element loginButton of LoginWrapper component on the loginPage
    #* Verify the book info for the user in context
    # Crosscheck scenario, the website is full of ads therefore this doesn't work very smoothly
    # TODO: fix the final verification step