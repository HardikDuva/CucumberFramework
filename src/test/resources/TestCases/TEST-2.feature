@full_regression
@TEST-2
@login

Feature: TEST-2
 Background:
   Given I Initialize the framework with "TEST-2"

  Scenario: As any user, I can't login with Invalid username and password
    # This tests that user can login with valid username & password

   When I try to login with In-valid username and password
   Then The user should not successfully landed on the LMS home page
   #Then I should see validation error on login page