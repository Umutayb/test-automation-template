@BookStore @Web-UI
  Feature: BookStore API Tests

    Scenario: Create a User, Get list of all books and Post them to the user, then log in and check if all books are added
      Given Create a user
      And Generate a token
      And Get list of all books
      When Post all books to the user
      And Navigate to the url: https://demoqa.com/login
      And Log in to the page
      Then Assert the details of the books

