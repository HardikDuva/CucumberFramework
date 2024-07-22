@full_regression
@TEST-1
@search_product

Feature: TEST-1
 Background:
   Given I Initialize the framework with "TEST-1"

  Scenario: As any user, I can search for products using search bar.
    # This tests that user can login with valid username & password

   When I try to login with valid email and password
   Then The user has successfully landed on the LMS home page