Feature: Registering & Cancelling an order

  Scenario: create an order
    Given A user 1 registers an SELL order for 3.5 kg for a price of 306£
    Then the user receives status code of 201
    And the user receives an order id

  Scenario: cancel an order
    Given A user 1 registers an SELL order for 3.5 kg for a price of 306£
    When the user 1 cancels the registered order
    Then the user receives status code of 200

  Scenario: cancel a order not belonging to user
    Given A user 1 registers an SELL order for 3.5 kg for a price of 306£
    When the user 2 cancels the registered order
    Then the user receives as NOT FOUND