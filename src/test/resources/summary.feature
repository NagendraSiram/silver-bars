Feature: Summary for the dash board

  Scenario: Fetch SELL summary for the dash board
    Given The following orders are registered
      | userId | orderType | quantity | price |
      | 1      | SELL      | 3.5      | 306   |
      | 2      | SELL      | 1.2      | 310   |
      | 3      | SELL      | 1.5      | 307   |
      | 4      | SELL      | 2.0      | 306   |
    When the summary information for SELL is requested
    Then the live dash board should display the following
      | quantity | price |
      | 5.5      | 306   |
      | 1.5      | 307   |
      | 1.2      | 310   |

  Scenario: Fetch BUY summary for the dash board
    Given The following orders are registered
      | userId | orderType | quantity | price |
      | 1      | BUY      | 3.5      | 306   |
      | 2      | BUY      | 1.2      | 310   |
      | 3      | BUY      | 1.5      | 307   |
      | 4      | BUY      | 2.0      | 306   |
    When the summary information for BUY is requested
    Then the live dash board should display the following
      | quantity | price |
      | 1.2      | 310   |
      | 1.5      | 307   |
      | 5.5      | 306   |

