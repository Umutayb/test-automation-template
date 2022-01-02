@ExchangeRateNotifier
Feature: Exchange Rate Notifier

  Scenario: Get the exchange rate
    * Navigate to google.com
    * Dismiss privacy notification
    * Perform google search for phrase: eur to try
    * Print exchange rate
    * Notify if the rate has changed