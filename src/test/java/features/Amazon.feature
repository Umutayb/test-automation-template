Feature: Amazon
  Desc: amazon test case feature

  @Web-UI
  Scenario: Burger menu items
    * Navigate to url: https://www.amazon.com/
    * Click the hamburgerMenuButton on the HomePage
    * Click listed element Electronics from categories list on the HomePage
    * Click listed attribute element that has /s?bbn=16225009011&rh=i%3Aspecialty-aps%2Cn%3A%2116225009011%2Cn%3A10048700011&ref_=nav_em__nav_desktop_sa_intl_wearable_technology_0_2_5_17 value for its href attribute from menuItems list on the HomePage