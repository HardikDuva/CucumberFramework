@full_regression
@TEST-1
@login

Feature: TEST-1
 Background:
   Given I Initialize the framework with "TEST-1"

  Scenario: As any user, I can login with valid username and password
    # This tests that user can login with valid username & password

   When I try to login with valid username/email and password
   Then The user should successfully landed on the LMS home page