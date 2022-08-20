@APPLE-DEMO
Feature: BBC test automation scenarios
  A collection of test scenarios for BBC, demonstrating capabilities of Pickleib
  Background: Navigate to the page
    Navigates to the page
    * Navigate to url: apple.com

  @Universal
  Scenario: Universal Step
    A Scenario where universal step interactions are tested
    * Click listed attribute element of navigationBar component that has accessories value for its class attribute from categories list on the LandingPage

  @Shop
  Scenario: Shopping Steps
    A Scenario where shopping scenario are tested
    * Click listed component element Store of NavigationBar from categories list on the LandingPage
    * Click listed element iPad from productCards list on the StorePage
    * Click listed component element Accessories of PageShortcuts from categories list on the BuyMacPage
    * Click listed component element 35W Dual USB-C Port Compact Power Adapter of CardsShelf from cards list on the BuyMacPage
    * Wait 2 seconds