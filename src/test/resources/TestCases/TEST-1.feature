@smoke_regression
@TEST-1
@search_product

Feature: TEST-1
 Background:
  Given I Initialize the framework with "TEST-1"
  Given I am anonymous user on Amazon Home Page

  Scenario: As any user, I can search for products using search bar.
    # This tests that user can search product and can see searched result

  When I search "Coffee" product
  Then I can see searched result for the "Coffee"
