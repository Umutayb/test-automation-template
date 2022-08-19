@BBC-DEMO
Feature: BBC test automation scenarios
  A collection of test scenarios for BBC, demonstrating capabilities of Pickleib
  Background: Navigate to the page
    Navigates to the page
    * Navigate to url: bbc.com

  Scenario: Orbit Banner Interactions
    A Scenario where orbit banner interactions are tested
    * Click component element moreButton of OrbitBanner component on the LandingPage
    * Click listed component element News of OrbitBanner from moreDrawerItems list on the LandingPage
    * Wait 5 seconds