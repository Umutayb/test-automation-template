@BBC-DEMO
Feature: BBC test automation scenarios
  A collection of test scenarios for BBC, demonstrating capabilities of Pickleib
  Background: Navigate to the page
    Navigates to the page
    * Navigate to url: bbc.com
    * If present, click component element refuseButton of CookiePopup component on the LandingPage
    * If present, click component element agreeButton of CookieBanner component on the LandingPage

  @OrbitBanner
  Scenario: Orbit Banner Interactions
    A Scenario where orbit banner interactions are tested
    * Click listed component element News of OrbitBanner from navItems list on the LandingPage
    * Click component element moreButton of OrbitBanner component on the LandingPage
    * Click listed component element Weather of OrbitBanner from moreDrawerItems list on the LandingPage
    * Click component element moreButton of OrbitBanner component on the LandingPage
    * Click listed component element TV of OrbitBanner from moreDrawerItems list on the LandingPage
    * Click listed component element Home of OrbitBanner from navItems list on the LandingPage

  @News
  Scenario: News Interactions
    A Scenario where news media interactions are tested
    To run: mvn clean test -q -Dcucumber.filter.tags="@News" or mvn clean test -q -Dcucumber.filter.tags="@BBC-DEMO and @News"
    * Click the media container titled Russia
    * Click listed component element Home of OrbitBanner from navItems list on the LandingPage
    * Click listed component element News of OrbitBanner from navItems list on the LandingPage