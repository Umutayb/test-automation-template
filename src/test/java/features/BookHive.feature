@BookHive
Feature: BookHive — Cross-functional API + UI Testing

  Demonstrates combining API and UI testing for a bookstore application.
  API calls set up state, UI tests verify the frontend, and API assertions
  cross-validate what the UI shows.

  Background: Reset database
    * Reset BookHive database

  # ==================== Auth ====================

  @Web-UI @SCN-BH-1
  Scenario: Sign up via UI and verify profile
    * Navigate to url: CONTEXT-bookhive-url
    * Navigate to url: http://localhost:3000/signup
    * Fill input usernameInput on the BookHiveSignupPage with text: newuser
    * Fill input emailInput on the BookHiveSignupPage with text: newuser@test.com
    * Fill input passwordInput on the BookHiveSignupPage with text: Password123!
    * Click the createAccountButton on the BookHiveSignupPage
    * Wait 2 seconds
    * Verify the url contains with the text /
    # Verify profile via API
    * Login to BookHive API as newuser@test.com with password Password123!
    * Verify BookHive API profile username is newuser

  @Web-UI @SCN-BH-2
  Scenario: Create user via API then login via UI
    * Create BookHive user apiuser with email apiuser@test.com and password Password123!
    * Navigate to url: http://localhost:3000/login
    * Fill input emailInput on the BookHiveLoginPage with text: apiuser@test.com
    * Fill input passwordInput on the BookHiveLoginPage with text: Password123!
    * Click the signInButton on the BookHiveLoginPage
    * Wait 3 seconds
    # Verify redirected to home after login
    * Verify the url contains with the text localhost:3000
    # Verify logged in via API
    * Login to BookHive API as apiuser@test.com with password Password123!
    * Verify BookHive API profile username is apiuser

  # ==================== Browse & Search ====================

  @Web-UI @SCN-BH-3
  Scenario: Browse books and verify catalog matches API
    * Verify BookHive API has books available
    * Navigate to url: http://localhost:3000
    * Wait for element searchInput on the BookHiveHomePage to be visible

  @Web-UI @SCN-BH-4
  Scenario: Book detail page shows correct information
    * Get BookHive book details for book-001 via API
    * Navigate to url: http://localhost:3000/books/book-001
    * Wait for element bookTitle on the BookHiveBookDetailPage to be visible
    * Verify the text of bookTitle on the BookHiveBookDetailPage contains: CONTEXT-bookTitle
    * Verify the text of bookAuthor on the BookHiveBookDetailPage contains: CONTEXT-bookAuthor
    * Verify the text of bookPrice on the BookHiveBookDetailPage contains: CONTEXT-bookPrice

  # ==================== Cart ====================

  @Web-UI @SCN-BH-5
  Scenario: Add to cart via UI and verify via API
    * Create BookHive user cartuser with email cartuser@test.com and password Password123!
    # Login via UI
    * Navigate to url: http://localhost:3000/login
    * Fill input emailInput on the BookHiveLoginPage with text: cartuser@test.com
    * Fill input passwordInput on the BookHiveLoginPage with text: Password123!
    * Click the signInButton on the BookHiveLoginPage
    * Wait 2 seconds
    # Add book to cart via UI
    * Navigate to url: http://localhost:3000/books/book-001
    * Wait for element addToCartButton on the BookHiveBookDetailPage to be visible
    * Click the addToCartButton on the BookHiveBookDetailPage
    * Wait 1 seconds
    # Verify cart via API
    * Login to BookHive API as cartuser@test.com with password Password123!
    * Verify BookHive API cart has 1 items

  @Web-UI @SCN-BH-6
  Scenario: Add to cart via API and verify via UI
    * Create BookHive user cartuser2 with email cartuser2@test.com and password Password123!
    * Login to BookHive API as cartuser2@test.com with password Password123!
    * Add book book-003 to BookHive cart via API with quantity 2
    # Login via UI and check cart
    * Navigate to url: http://localhost:3000/login
    * Fill input emailInput on the BookHiveLoginPage with text: cartuser2@test.com
    * Fill input passwordInput on the BookHiveLoginPage with text: Password123!
    * Click the signInButton on the BookHiveLoginPage
    * Wait 2 seconds
    * Navigate to url: http://localhost:3000/cart
    * Wait for element cartTotal on the BookHiveCartPage to be visible
    * Verify the text of cartTotal on the BookHiveCartPage contains: $

  # ==================== Checkout ====================

  @Web-UI @SCN-BH-7
  Scenario: Full cart flow — browse, add to cart, verify cart, clear cart
    * Create BookHive user buyer with email buyer@test.com and password Password123!
    # Login
    * Navigate to url: http://localhost:3000/login
    * Fill input emailInput on the BookHiveLoginPage with text: buyer@test.com
    * Fill input passwordInput on the BookHiveLoginPage with text: Password123!
    * Click the signInButton on the BookHiveLoginPage
    * Wait 2 seconds
    # Browse and add to cart
    * Navigate to url: http://localhost:3000/books/book-002
    * Wait for element addToCartButton on the BookHiveBookDetailPage to be visible
    * Click the addToCartButton on the BookHiveBookDetailPage
    * Wait 1 seconds
    # Verify cart via API
    * Login to BookHive API as buyer@test.com with password Password123!
    * Verify BookHive API cart has 1 items
    # Go to cart and verify UI
    * Navigate to url: http://localhost:3000/cart
    * Wait for element cartTotal on the BookHiveCartPage to be visible
    * Verify the text of cartTotal on the BookHiveCartPage contains: $
    # Clear cart via API and verify UI is empty
    * Clear BookHive cart via API
    * Refresh the page
    * Wait 2 seconds
    * Verify presence of element emptyCartMessage on the BookHiveCartPage

  # ==================== Marketplace ====================

  @Web-UI @SCN-BH-8
  Scenario: Create marketplace listing via API and verify on UI
    * Create BookHive user seller with email seller@test.com and password Password123!
    * Login to BookHive API as seller@test.com with password Password123!
    * Create BookHive marketplace listing for book book-005 in GOOD condition at price 7.99
    # Verify on UI
    # Verify listing exists via API
    * Verify BookHive API marketplace has listings

  @Web-UI @SCN-BH-9
  Scenario: Create marketplace listing via UI
    * Create BookHive user seller2 with email seller2@test.com and password Password123!
    # Login
    * Navigate to url: http://localhost:3000/login
    * Fill input emailInput on the BookHiveLoginPage with text: seller2@test.com
    * Fill input passwordInput on the BookHiveLoginPage with text: Password123!
    * Click the signInButton on the BookHiveLoginPage
    * Wait 2 seconds
    # Create listing
    * Navigate to url: http://localhost:3000/marketplace/sell
    * Wait for element bookSelect on the BookHiveSellPage to be visible
    * Select option To Kill a Mockingbird - Harper Lee from bookSelect on the BookHiveSellPage
    * Select option GOOD from conditionSelect on the BookHiveSellPage
    * Fill input priceInput on the BookHiveSellPage with text: 8.99
    * Click the createListingButton on the BookHiveSellPage
    * Wait 2 seconds
    # Verify listing exists via API
    * Login to BookHive API as seller2@test.com with password Password123!
    * Verify BookHive API marketplace has listings

  # ==================== Profile ====================

  @Web-UI @SCN-BH-10
  Scenario: Verify profile page shows correct user data
    * Create BookHive user profuser with email profuser@test.com and password Password123!
    * Navigate to url: http://localhost:3000/login
    * Fill input emailInput on the BookHiveLoginPage with text: profuser@test.com
    * Fill input passwordInput on the BookHiveLoginPage with text: Password123!
    * Click the signInButton on the BookHiveLoginPage
    * Wait 2 seconds
    * Navigate to url: http://localhost:3000/profile
    * Wait for element username on the BookHiveProfilePage to be visible
    * Verify the text of username on the BookHiveProfilePage contains: profuser
    * Verify the text of email on the BookHiveProfilePage contains: profuser@test.com
