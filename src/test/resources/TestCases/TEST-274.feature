@smoke_regression
@TEST-274
@search_product

Feature: TEST-274
 Background:
  Given I Initialize the framework with "TEST-274"
  Given I am anonymous user on Amazon Home Page

  Scenario: As any user, I can search for products using search bar.
    # This tests that user can search product and can see searched result

  When I search "Coffee" product
  Then I can see searched result for the "Coffee"
